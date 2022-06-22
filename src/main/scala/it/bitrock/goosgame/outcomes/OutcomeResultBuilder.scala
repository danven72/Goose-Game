package it.bitrock.goosgame.outcomes

import scala.annotation.tailrec

class OutcomeResultBuilder {

  def addPlayerOutcomeResult(
      present: Boolean,
      playersNames: List[String]
  ): OutcomeResult = {
    if (present)
      OutcomeResult(s"${playersNames.last}: already existing player", valuateLastOutcome(null))
    else {
      OutcomeResult(
        "players: " + playerListNames(playersNames.tail, playersNames.head),
        valuateLastOutcome(null)
      )
    }
  }

  def buildMoveOutcomeResult(outcomeList: List[Outcome]): OutcomeResult = {
    if (outcomeList.size == 1) {
      val outcome = outcomeList.head
      OutcomeResult(
        outcome.buildBaseMoveMessage + outcome.buildSpecificMoveMessage(),
        valuateLastOutcome(outcome)
      )
    } else {
      OutcomeResult(
        outcomeList.head.buildBaseMoveMessage + buildGooseMovesMessage(
          outcomeList,
          ""
        ),
        valuateLastOutcome(outcomeList.last)
      )
    }
  }

  def playerNotFoundOutcomeResult(player: String): OutcomeResult =
    OutcomeResult(s"Player $player not Found!", valuateLastOutcome(null))

  private val valuateLastOutcome: Outcome => Boolean = (outcome: Outcome) =>
    (outcome != null && outcome.isInstanceOf[Win])

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
