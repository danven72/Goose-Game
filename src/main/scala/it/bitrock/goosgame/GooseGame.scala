package it.bitrock.goosgame
import it.bitrock.goosgame.outcomes.{Outcome, OutcomeResult, OutcomeResultBuilder}

import scala.util.Random

case class GooseGame(outcomeResultBuilder: OutcomeResultBuilder) {

  val random                         = new Random
  private val sum: (Int, Int) => Int = (d1: Int, d2: Int) => d1 + d2

  private var players: Map[String, Int] = Map.empty[String, Int]

  def addPNewPlayer(newPlayer: String): OutcomeResult = {
    players.find(p => p._1 == newPlayer) match {
      case Some(p) =>
        outcomeResultBuilder.buildAddPlayerOutcomeResult(
          true,
          List(newPlayer)
        )
      case (None) =>
        players = players + (newPlayer -> 0)
        outcomeResultBuilder.buildAddPlayerOutcomeResult(
          false,
          players.keys.toList
        )
    }
  }

  def movePlayer(player: String, dices: (Int, Int)): OutcomeResult = {
    if (isNotDiceValid(dices._1) || isNotDiceValid(dices._2))
      outcomeResultBuilder.buildDicesNotValidOutcomeResult(dices)
    else
      doMovePlayer(player, dices, List())
  }

  private val isNotDiceValid: Int => Boolean = (dice: Int) => dice < 1 || dice > 6

  def movePlayer(player: String): OutcomeResult = {
    val dices = rollDices();
    doMovePlayer(player, dices, List())
  }

  def exit(): OutcomeResult = {
    outcomeResultBuilder.buildExitOutcomeResult()
  }

  def unknownInput(input: String): OutcomeResult =
    outcomeResultBuilder.buildUnknownCommandOutcomeResult(input)

  private def doMovePlayer(player: String, dices: (Int, Int), previousOutcome: List[Outcome]): OutcomeResult = {
    players.get(player) match {
      case Some(oldPosition) =>
        val theoreticalPosition = sum(oldPosition, sum(dices._1, dices._2))
        val outcome = Outcome(
          player,
          dices,
          oldPosition,
          theoreticalPosition,
          theoreticalPosition,
          findPrankPlayerIfPresent(theoreticalPosition)
        )
        val outcomeListUpdate: List[Outcome] = previousOutcome :+ outcome
        //println("outcomeList: " + outcomeListUpdate)
        if (outcome.realPositionWillBeGoose()) {
          updatePosition(outcome)
          doMovePlayer(player, dices, outcomeListUpdate)
        } else {
          updatePosition(outcomeListUpdate.head)
          outcomeResultBuilder.buildMoveOutcomeResult(outcomeListUpdate)
        }
      case None =>
        outcomeResultBuilder.buildPlayerNotFoundOutcomeResult(player)
    }
  }

  private def updatePosition(outcome: Outcome): Unit = {
    outcome.prankPlayer match {
      case None =>
        players = players.updated(outcome.player, outcome.realPosition)

      case Some(p) =>
        players = players.updated(outcome.player, outcome.realPosition)
        players = players.updated(p._1, p._2)
    }
  }

  private def rollDices(): (Int, Int) = {
    val MIN_RESULT_DICE           = 1
    val MAX_RESULT_EXCLUSIVE_DICE = 7
    val result = Tuple2(
      random.between(MIN_RESULT_DICE, MAX_RESULT_EXCLUSIVE_DICE),
      random.between(MIN_RESULT_DICE, MAX_RESULT_EXCLUSIVE_DICE)
    )
    result
  }

  private def findPrankPlayerIfPresent(
      theoreticalPosition: Int
  ): Option[(String, Int)] = {
    players.find(p => p._2 == theoreticalPosition)
  }
}
