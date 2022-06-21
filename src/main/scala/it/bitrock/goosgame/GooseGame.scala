package it.bitrock.goosgame
import it.bitrock.goosgame.TestMovePlayer.goose

import scala.util.Random

class GooseGame {

  //TODO: must be private!!
  var players: Map[String, Int] = Map.empty[String, Int]

  val commandResultBuilder = new CommandResultBuilder
  val WIN_POSITION = 63
  val random = new Random

  def addPNewPlayer(newPlayer: String): CommandResult = {
    players.find(p => p._1 == newPlayer) match {
      case Some(p) =>
        commandResultBuilder.addPlayerCommandResult(
          true,
          List(newPlayer)
        )
      case (None) =>
        players = players + (newPlayer -> 0)
        commandResultBuilder.addPlayerCommandResult(
          false,
          players.keys.toList
        )
    }
  }

  def movePlayer(player: String, dices: (Int, Int)): CommandResult = {
    doMovePlayer(player, dices, List())
  }

  def movePlayer(player: String): CommandResult = {
    val dices = rollDices();
    doMovePlayer(player, dices, List())
  }

  private def doMovePlayer(
      player: String,
      dices: (Int, Int),
      previousOutcome: List[Outcome]
  ): CommandResult = {
    players.find(p => p._1 == player) match {
      case Some(p) =>
        val oldPosition = p._2
        val theoreticalPosition = sum(p._2, sum(dices._1, dices._2))
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
          commandResultBuilder.buildMoveCommandResult(outcomeListUpdate)
        }

      case (None) =>
        commandResultBuilder.playerNotFoundCommandResult(player)
    }
  }

  private val sum: (Int, Int) => Int = (d1: Int, d2: Int) => d1 + d2

  private def updatePosition(outcome: Outcome): Unit = {
    outcome.prankPlayer match {
      case None =>
        players = players - outcome.player
        players = players + (outcome.player -> outcome.realPosition)

      case Some(p) =>
        players = players - outcome.player
        players = players - p._1
        players = players + (outcome.player -> outcome.realPosition)
        players = players + (p._1 -> p._2)
    }
  }

  private def rollDices(): (Int, Int) = {
    val MIN_RESULT_DICE = 1
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
  val goose = new GooseGame()

  val resultJohn = goose.addPNewPlayer("John");
  println(resultJohn.message)

  val resultTom = goose.addPNewPlayer(("Tom"))
  println(resultTom.message)

  val resultMary = goose.addPNewPlayer(("Mary"))
  println(resultMary.message)

}

object TestAddNewPlayerWhenPlayerAlredyPresent extends App {
  val goose = new GooseGame()
  val resultJohn = goose.addPNewPlayer("John");
  println(resultJohn.message)
  val resultJohn2 = goose.addPNewPlayer("John");
  println(resultJohn2.message)
}
object TestMovePlayer extends App {
  val goose = new GooseGame()
  val resultJohn = goose.addPNewPlayer("John");

  val moveResult = goose.movePlayer("John", (4, 4))
  println(moveResult.message)

  val bounceResult = goose.movePlayer("John", (44, 13))
  println(bounceResult.message)

  val winResult = goose.movePlayer("John", (1, 1))
  println(winResult.message)
}

object TestMoveBridge extends App {
  val goose = new GooseGame()
  val resultJohn = goose.addPNewPlayer("John");

  val move1 = goose.movePlayer("John", (2, 2))
  println(move1.message)

  val moveResult = goose.movePlayer("John", (1, 1))
  println(moveResult.message)

}

object TestMoveSimpleGoose extends App {
  val goose = new GooseGame()
  val resultJohn = goose.addPNewPlayer("John");
  println(resultJohn.message)

  val move1 = goose.movePlayer("John", (1, 2))
  println(move1.message)

  val gooseResult = goose.movePlayer("John", (1, 1))
  println(gooseResult.message)

}

object TestMoveMultipleGoose extends App {
  val goose = new GooseGame()
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
  val goose = new GooseGame()
  goose.addPNewPlayer("Pippo")
  goose.addPNewPlayer("Pluto")

  println(goose.movePlayer("Pippo", (1, 14)))
  println(goose.movePlayer("Pluto", (1, 16)))
  println(goose.players)
  println(goose.movePlayer("Pippo", (1, 1)).message)
  println(goose.players)
}
