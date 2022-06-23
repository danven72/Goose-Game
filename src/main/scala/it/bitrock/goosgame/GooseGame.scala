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
        outcomeResultBuilder.addPlayerOutcomeResult(
          true,
          List(newPlayer)
        )
      case (None) =>
        players = players + (newPlayer -> 0)
        outcomeResultBuilder.addPlayerOutcomeResult(
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

//TODO: Move to TestCase class
object TestAddNewPlayer extends App {
  val outcomeResultBuilder = new OutcomeResultBuilder
  val goose                = GooseGame(outcomeResultBuilder)

  val resultJohn = goose.addPNewPlayer("John");
  println(resultJohn.message)

  val resultTom = goose.addPNewPlayer(("Tom"))
  println(resultTom.message)

  val resultMary = goose.addPNewPlayer(("Mary"))
  println(resultMary.message)

}

object TestAddNewPlayerWhenPlayerAlredyPresent extends App {
  val outcomeResultBuilder = new OutcomeResultBuilder
  val goose                = GooseGame(outcomeResultBuilder)

  val resultJohn = goose.addPNewPlayer("John");
  println(resultJohn.message)
  val resultJohn2 = goose.addPNewPlayer("John");
  println(resultJohn2.message)
}
object TestMovePlayer extends App {
  val outcomeResultBuilder = new OutcomeResultBuilder
  val goose                = GooseGame(outcomeResultBuilder)

  val resultJohn = goose.addPNewPlayer("John");

  val moveResult = goose.movePlayer("John", (4, 4))
  println(moveResult.message)

  //val bounceResult = goose.movePlayer("John", (44, 13))
  //println(bounceResult.message)

  //val winResult = goose.movePlayer("John", (1, 1))
  //println(winResult.message)
}

object TestBounceAndWin extends App {
  val outcomeResultBuilder = new OutcomeResultBuilder
  val goose                = GooseGame(outcomeResultBuilder)

  val resultJohn = goose.addPNewPlayer("John");
  goose.movePlayer("John", (0, 62))
  val bounceResult = goose.movePlayer("John", (2, 2))
  println(bounceResult.message)
  //println(goose.players)

  val winResult = goose.movePlayer("John", (1, 2))
  println(winResult.message)
  //println(goose.players)

}

object TestMoveBridge extends App {
  val outcomeResultBuilder = new OutcomeResultBuilder
  val goose                = GooseGame(outcomeResultBuilder)

  val resultJohn = goose.addPNewPlayer("John");

  val move1 = goose.movePlayer("John", (2, 2))
  println(move1.message)

  val moveResult = goose.movePlayer("John", (1, 1))
  println(moveResult.message)

}

object TestMoveSimpleGoose extends App {
  val outcomeResultBuilder = new OutcomeResultBuilder
  val goose                = GooseGame(outcomeResultBuilder)

  val resultJohn = goose.addPNewPlayer("John");
  println(resultJohn.message)

  val move1 = goose.movePlayer("John", (1, 2))
  println(move1.message)

  val gooseResult = goose.movePlayer("John", (1, 1))
  println(gooseResult.message)

}

object TestMoveMultipleGoose extends App {
  val outcomeResultBuilder = new OutcomeResultBuilder
  val goose                = GooseGame(outcomeResultBuilder)

  val resultJohn = goose.addPNewPlayer("John");

  val move1 = goose.movePlayer("John", (5, 5))
  println(move1.message)

  val moveResult = goose.movePlayer("John", (2, 2))
  println(moveResult.message)

}
/*
object TestFindPrankPlayerIfPresent extends App {
  val goose = new GooseGame()
  //val resultJohn = goose.addPNewPlayer("John");

  val resultTom = goose.addPNewPlayer("Tom");
  val tomMoves = goose.movePlayer("Tom", 4, 3)
  println(tomMoves.message)
  println(goose.players)
  val f = goose.findPrankPlayerIfPresent(7)
  println(f)
}
 */

object testPrankResult extends App {
  val outcomeResultBuilder = new OutcomeResultBuilder
  val goose                = GooseGame(outcomeResultBuilder)

  goose.addPNewPlayer("Pippo")
  goose.addPNewPlayer("Pluto")

  println(goose.movePlayer("Pippo", (1, 14)))
  println(goose.movePlayer("Pluto", (1, 16)))
  //println(goose.players)
  println(goose.movePlayer("Pippo", (1, 1)).message)
  //println(goose.players)
}
