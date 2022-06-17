package it.bitrock.goosgame

class GooseGame {

  var players: Map[String, Int] = Map.empty[String, Int]
  val commandResultBuilder = new CommandResultBuilder

  val WIN_POSITION = 63

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
    players.find(p => p._1 == player) match {
      case Some(p) =>
        val oldPosition = p._2
        val newPosition = sum(p._2, sum(dice1, dice2))
        val hasWon = checkWin(newPosition)
        val positionBounceChecked = newPositionIfBounce(newPosition)
        updatePosition(player, positionBounceChecked)
        commandResultBuilder.buildMoveCommandResult(
          player,
          dice1,
          dice2,
          oldPosition,
          hasWon,
          Tuple3(
            newPosition,
            positionBounceChecked._1,
            positionBounceChecked._2
          )
        )
      case (None) =>
        commandResultBuilder.playerNotFoundCommandResult(player)
    }
  }

  private val sum: (Int, Int) => Int = (d1: Int, d2: Int) => d1 + d2

  private def updatePosition(
      player: String,
      newPosition: Tuple2[Int, Boolean]
  ): Unit = {
    players = players - player
    players = players + (player -> newPosition._1)
  }

  private val checkWin: (Int) => Boolean = (newPosition: Int) =>
    newPosition == WIN_POSITION

  private val newPositionIfBounce: (Int) => Tuple2[Int, Boolean] =
    (newPosition: Int) => {
      val bounce = newPosition > WIN_POSITION
      if (bounce) {
        val recalculatedPosition = WIN_POSITION - (newPosition - WIN_POSITION)
        Tuple2(recalculatedPosition, true)
      } else
        Tuple2(newPosition, false)
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
