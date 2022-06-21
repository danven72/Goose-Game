package it.bitrock.goosgame

import scala.annotation.tailrec

class CommandResultBuilder {

  def addPlayerCommandResult(
      present: Boolean,
      playersNames: List[String]
  ): CommandResult = {
    if (present)
      CommandResult(s"${playersNames.last}: already existing player")
    else {
      CommandResult(
        "players: " + playerListNames(playersNames.tail, playersNames.head)
      )
    }
  }

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

  def buildMoveCommandResult(outcomeList: List[Outcome]): CommandResult = {
    if (outcomeList.size == 1) {
      val outcome = outcomeList.head
      CommandResult(
        outcome.buildBaseMoveMessage + outcome.buildSpecificMoveMessage()
      )
    } else {
      CommandResult(
        outcomeList.head.buildBaseMoveMessage + buildGooseMovesMessage(
          outcomeList,
          ""
        )
      )
    }
  }

  @tailrec
  private def buildGooseMovesMessage(
      outcomeList: List[Outcome],
      gooseMessage: String
  ): String = {
    println("Outcomes: " + outcomeList)
    outcomeList match {
      case Nil => gooseMessage
      case e =>
        val allGooseMessages = gooseMessage + e.head.gooseMoveMessage()
        buildGooseMovesMessage(outcomeList.tail, allGooseMessages)
    }
  }

  def playerNotFoundCommandResult(player: String): CommandResult =
    CommandResult(s"Player $player not Found!")

}
