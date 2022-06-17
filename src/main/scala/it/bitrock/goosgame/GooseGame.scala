package it.bitrock.goosgame

class GooseGame {

  var players: Map[String, Int] = Map.empty[String, Int]

  def addPNewPlayer(newPlayer: String): CommandResult = {
    players.find(p => p._1 == newPlayer) match {
      case Some(p) => CommandResult(s"$newPlayer: already existing player")
      case (None) =>
        players = players + (newPlayer -> 0)
        CommandResult(s"add player $newPlayer")
    }
  }

  def movePlayer(player: String, dice1: Int, dice2: Int): CommandResult = {
    players.find(p => p._1 == player) match {
      case Some(p) =>
        val oldPosition = p._2
        val newPosition = sum(p._2, sum(dice1, dice2))
        updatePosition(player, newPosition)
        CommandResult(
          s"$player rolls $dice1, $dice2. $player move from ${decodePositionForMessage(oldPosition)} to $newPosition"
        )
      case (None) =>
        CommandResult(s"Player $player not Found!")
    }

  }

  private val sum: (Int, Int) => Int = (d1: Int, d2: Int) => d1 + d2

  private def updatePosition(player: String, newPosition: Int): Unit = {
    players = players - player
    players = players + (player -> newPosition)
  }

  private def decodePositionForMessage(position: Int): String = {
    position match {
      case 0 => "Start"
      case _ => position.toString
    }
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
}
