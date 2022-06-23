package it.bitrock.goosgame.outcomes

import scala.annotation.tailrec

class OutcomeResultBuilder {

  def buildAddPlayerOutcomeResult(
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

  def buildMoveOutcomeResult(outcomeList: List[Outcome]): OutcomeResult = {
    if (outcomeList.size == 1) {
      val outcome = outcomeList.head
      OutcomeResult(
        outcome.buildBaseMoveMessage + outcome.buildSpecificMoveMessage(),
        outcome.isInstanceOf[Win]
      )
    } else {
      OutcomeResult(
        outcomeList.head.buildBaseMoveMessage + buildGooseMovesMessage(
          outcomeList,
          ""
        ),
        outcomeList.last.isInstanceOf[Win]
      )
    }
  }

  def buildExitOutcomeResult(): OutcomeResult =
    OutcomeResult("You force program exit", true)

  def buildUnknownCommandOutcomeResult(inputCommand: String): OutcomeResult =
    OutcomeResult(s"Command [${inputCommand}] unknown!")

  def buildPlayerNotFoundOutcomeResult(player: String): OutcomeResult =
    OutcomeResult(s"Player $player not Found!")

  def buildDicesNotValidOutcomeResult(dices: (Int, Int)): OutcomeResult =
    OutcomeResult(s"Dices not valid! You asks: [${dices._1}, ${dices._2}] but only 1..6 values are ok")

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
