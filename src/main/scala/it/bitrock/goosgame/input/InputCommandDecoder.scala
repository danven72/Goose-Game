package it.bitrock.goosgame.input

import it.bitrock.goosgame.GooseGame

import scala.io.StdIn._

case class CommandDecoder(gooseGame: GooseGame) extends App {

  def readUserInput(): InputCommand = {
    val input = readLine()
    input match {
      case i if i.startsWith(CommandDecoder.ADD_PLAYER) =>
        //println(s"Add Player Command")
        val player = i.substring((CommandDecoder.ADD_PLAYER.size), i.size)
        println("[" + player + "]")
        AddPNewPlayerCommand(
          gooseGame,
          player
        )
      case i if i.startsWith(CommandDecoder.MOVE) =>
        val playerNameAnDices = extractPlayerNameAndDicesFromMove(i)
        println(playerNameAnDices)
        if (playerNameAnDices._2 == 0 && playerNameAnDices._3 == 0)
          MovePlayerAutoDicesCommand(gooseGame, playerNameAnDices._1)
        else
          MovePlayerExternalDicesCommand(
            gooseGame,
            playerNameAnDices._1,
            (playerNameAnDices._2, playerNameAnDices._3)
          )
      case "exit" => ExitCommand()
      case _      => throw new RuntimeException("Input not found")
    }
  }

  private def extractPlayerNameAndDicesFromMove(
      input: String
  ): (String, Int, Int) = {
    val nameAndDices = input.substring(CommandDecoder.MOVE.size, input.size)
    if (nameAndDices.last.isDigit) {
      val player = nameAndDices.substring(0, nameAndDices.length - 5)
      val dice1 =
        nameAndDices.substring(player.length + 1, nameAndDices.length - 3).toInt
      val dice2 = nameAndDices.last.toString.toInt
      println(
        "player: [" + player + "] dice1: [" + dice1 + "], dice2: [" + dice2 + "]"
      )
      (player, dice1, dice2)
    } else
      (nameAndDices, 0, 0)
  }
}

object CommandDecoder {
  val ADD_PLAYER = "add player "
  val MOVE       = "move "

}

object TestCommandDecoder extends App {
  val commandDecoder = CommandDecoder(new GooseGame)
  val inputCommand   = commandDecoder.readUserInput()
  println(inputCommand.execute().message)
}

object TestExtractPlayerName extends App {
  val commandDecoder = CommandDecoder(new GooseGame)
  val inputCommand   = commandDecoder.readUserInput()
}

//write "exit" at prompt
object TestExit extends App {
  val commandDecoder = CommandDecoder(new GooseGame)
  val exitCommand    = commandDecoder.readUserInput()
  println(exitCommand.execute().message)
}

object TestGame extends App {
  val commandDecoder  = CommandDecoder(new GooseGame)
  var executedMessage = ""
  while (executedMessage != "exit") {
    val command         = commandDecoder.readUserInput()
    val resultExecution = command.execute().message
    println(resultExecution)
    executedMessage = resultExecution
  }
}
