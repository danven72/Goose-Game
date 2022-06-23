package it.bitrock.goosgame.input

import it.bitrock.goosgame.GooseGame

case class InputCommandDecoder(gooseGame: GooseGame) extends App {

  def readUserInput(input: String): InputCommand = {
    input match {
      case i if i.startsWith(InputCommandDecoder.ADD_PLAYER) =>
        val player = i.substring((InputCommandDecoder.ADD_PLAYER.size), i.size)
        //println("[" + player + "]")
        AddNewPlayerCommand(
          gooseGame,
          player
        )
      case i if i.startsWith(InputCommandDecoder.MOVE) =>
        val playerNameAnDices = extractPlayerNameAndDicesFromMove(i)
        //println(playerNameAnDices)
        if (playerNameAnDices._2 == 0 && playerNameAnDices._3 == 0)
          MovePlayerAutoDicesCommand(gooseGame, playerNameAnDices._1)
        else
          MovePlayerExternalDicesCommand(
            gooseGame,
            playerNameAnDices._1,
            (playerNameAnDices._2, playerNameAnDices._3)
          )
      case "exit" => ExitCommand(gooseGame)
      case _      => UnknownCommand(gooseGame, input)
    }
  }

  // public for Test porpoise
  def extractPlayerNameAndDicesFromMove(
      input: String
  ): (String, Int, Int) = {
    val nameAndDices = input.substring(InputCommandDecoder.MOVE.size, input.size)
    //println(s"nameAndDices: [$nameAndDices]")
    if (nameAndDices.last.isDigit) {
      val player = nameAndDices.substring(0, nameAndDices.indexOf(" "))
      //println(s"player: [$player]")
      val dice1 = nameAndDices.substring(player.length + 1, nameAndDices.indexOf(","))
      //println(s"dice1: [$dice1]")
      val dice2 = nameAndDices.substring(nameAndDices.indexOf(",") + 2)
      //println(s"dice2: [$dice2]")
      (player, dice1.toInt, dice2.toInt)
    } else
      (nameAndDices, 0, 0)
  }
}

object InputCommandDecoder {
  private val ADD_PLAYER = "add player "
  private val MOVE       = "move "

}
