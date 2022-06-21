package it.bitrock.goosgame.outcomes

import scala.annotation.tailrec

class OutcomeResultBuilder {

  def addPlayerCommandResult(
      present: Boolean,
      playersNames: List[String]
  ): OutcomeResult = {
    if (present)
      OutcomeResult(s"${playersNames.last}: already existing player")
    else {
      OutcomeResult(
        "players: " + playerListNames(playersNames.tail, playersNames.head)
      )
    }
  }

  def buildMoveCommandResult(outcomeList: List[Outcome]): OutcomeResult = {
    if (outcomeList.size == 1) {
      val outcome = outcomeList.head
      OutcomeResult(
        outcome.buildBaseMoveMessage + outcome.buildSpecificMoveMessage()
      )
    } else {
      OutcomeResult(
        outcomeList.head.buildBaseMoveMessage + buildGooseMovesMessage(
          outcomeList,
          ""
        )
      )
    }
  }

  def playerNotFoundCommandResult(player: String): OutcomeResult =
    OutcomeResult(s"Player $player not Found!")

  @tailrec
  private def playerListNames(
      playersNames: List[String],
      playerStr: String
  ): String =
    playersNames match {
      case Nil => playerStr
      case p => {
        val result = playerStr + s", ${p.head}"
        playerListNames(playersNames.tail, result)
      }
    }

  @tailrec
  private def buildGooseMovesMessage(
      outcomeList: List[Outcome],
      gooseMessage: String
  ): String = {
    //println("Outcomes: " + outcomeList)
    outcomeList match {
      case Nil => gooseMessage
      case e =>
        val allGooseMessages = gooseMessage + e.head.gooseMoveMessage()
        buildGooseMovesMessage(outcomeList.tail, allGooseMessages)
    }
  }

}
