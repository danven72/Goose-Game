package it.bitrock.goosgame
import it.bitrock.goosgame.TestMovePlayer.goose

import scala.util.Random

class GooseGame {

  var players: Map[String, Int] = Map.empty[String, Int]

  val commandResultBuilder = new CommandResultBuilder
  val WIN_POSITION = 63
  val random = new Random

  def addPNewPlayer(newPlayer: String): CommandResult = {
    players.find(p => p._1 == newPlayer) match {
      case Some(p) =>
        commandResultBuilder.addPlayerCommandResult(newPlayer, true)
      case (None) =>
        players = players + (newPlayer -> 0)
        commandResultBuilder.addPlayerCommandResult(newPlayer, false)
    }
  }

  def movePlayer(player: String, dice1: Int, dice2: Int): CommandResult = {
    doMovePlayer(player, Tuple2(dice1, dice2), List())
  }

  def movePlayer(player: String): CommandResult = {
    val dices = rollDices();
    doMovePlayer(player, dices, List())
  }

  private def doMovePlayer(
      player: String,
      dices: Tuple2[Int, Int],
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
          theoreticalPosition
        )
        val outcomeListUpdate: List[Outcome] = previousOutcome :+ outcome
        println("outcomeList.size: " + outcomeListUpdate.size)
        if (outcome.isInstanceOf[Goose]) {
          println("GOOSE")
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
    players = players - outcome.player
    players = players + (outcome.player -> outcome.realPosition)
  }

  private def rollDices(): Tuple2[Int, Int] = {
    val MIN_RESULT_DICE = 1
    val MAX_RESULT_EXCLUSIVE_DICE = 7
    val result = Tuple2(
      random.between(MIN_RESULT_DICE, MAX_RESULT_EXCLUSIVE_DICE),
      random.between(MIN_RESULT_DICE, MAX_RESULT_EXCLUSIVE_DICE)
    )
    result
  }
}

//TODO: Move to TestCase class
object TestAddNewPlayer extends App {
  val goose = new GooseGame()
  val resultJohn = goose.addPNewPlayer("John");
  println(resultJohn.message)
  val resultJohn2 = goose.addPNewPlayer("John");
  println(resultJohn2.message)

  println(goose.players)
}

object TestMovePlayer extends App {
  val goose = new GooseGame()
  val resultJohn = goose.addPNewPlayer("John");

  val moveResult = goose.movePlayer("John", 4, 4)
  println(moveResult.message)

  val bounceResult = goose.movePlayer("John", 44, 13)
  println(bounceResult.message)

  val winResult = goose.movePlayer("John", 1, 1)
  println(winResult.message)
}

object TestMoveBridge extends App {
  val goose = new GooseGame()
  val resultJohn = goose.addPNewPlayer("John");

  val move1 = goose.movePlayer("John", 2, 2)
  println(move1.message)

  val moveResult = goose.movePlayer("John", 1, 1)
  println(moveResult.message)

}

object TestMoveSimpleGoose extends App {
  val goose = new GooseGame()
  val resultJohn = goose.addPNewPlayer("John");
  println(resultJohn.message)

  val move1 = goose.movePlayer("John", 1, 2)
  println(move1.message)

  val moveResult = goose.movePlayer("John", 1, 1)
  println(moveResult.message)

}

object TestMoveMultipleGoose extends App {
  val goose = new GooseGame()
  val resultJohn = goose.addPNewPlayer("John");

  val move1 = goose.movePlayer("John", 5, 5)
  println(move1.message)

  val moveResult = goose.movePlayer("John", 2, 2)
  println(moveResult.message)

}
