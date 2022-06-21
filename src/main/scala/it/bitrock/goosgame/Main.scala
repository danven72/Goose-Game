package it.bitrock.goosgame

import it.bitrock.goosgame.input.CommandDecoder

object Main extends App {

  val commandDecoder  = CommandDecoder(new GooseGame)
  var executedMessage = ""
  println("************* THE GOOSE GAME ******************* ")
  println("   ===== Commands (input without '') ===== ")
  println(" - 'add player [playerName]' ---> Add new Player (ex.: 'add player Tom') ")
  println(" - 'move player [playerName]' ---> move a Player with auto-rolls dices (ex.: 'move Tom') ")
  println(" - 'move player [playerName] d1, d2' ---> move a Player with dice1 + dice 2 (ex.: 'move Tom 2, 4') ")
  println(" - 'exit' ---> Exit the game")
  println("************************************************ ")
  println()

  while (executedMessage != "exit") {
    val command         = commandDecoder.readUserInput()
    val resultExecution = command.execute().message
    println(resultExecution)
    executedMessage = resultExecution
  }

}
