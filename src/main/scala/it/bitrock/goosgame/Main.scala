package it.bitrock.goosgame
import scala.io.StdIn._
import it.bitrock.goosgame.input.InputCommandDecoder

object Main extends App {

  println("************* THE GOOSE GAME ******************* ")
  println("   ===== Commands (input without '') ===== ")
  println(" - 'add player [playerName]' ---> Add new Player (ex.: 'add player Tom') ")
  println(" - 'move player [playerName]' ---> move a Player with auto-rolls dices (ex.: 'move Tom') ")
  println(" - 'move player [playerName] d1, d2' ---> move a Player with dice1 + dice 2 (ex.: 'move Tom 2, 4') ")
  println(" - 'exit' ---> Exit the game")
  println("************************************************ ")
  println()

  val commandDecoder  = InputCommandDecoder(new GooseGame)
  var executedMessage = ""
  while (executedMessage != "exit") {
    val inputCommand    = commandDecoder.readUserInput(readLine())
    val resultExecution = inputCommand.execute().message
    println(resultExecution)
    executedMessage = resultExecution
  }

}
