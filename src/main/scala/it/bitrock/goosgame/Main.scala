package it.bitrock.goosgame
import scala.io.StdIn._
import it.bitrock.goosgame.input.{InputCommandDecoder, UnknownCommand}
import it.bitrock.goosgame.outcomes.OutcomeResultBuilder

class Main {
  val outcomeResultBuilder = new OutcomeResultBuilder
  val gooseGame            = GooseGame(outcomeResultBuilder)
  val commandDecoder       = InputCommandDecoder(gooseGame)

  def displayCommands(): Unit = {
    println("************* THE GOOSE GAME ******************* ")
    println("   ===== Commands (input without '') ===== ")
    println(" - 'add player [playerName]' ---> Add new Player (ex.: 'add player Tom') ")
    println(" - 'move [playerName]' ---> move a Player with auto-rolls dices (ex.: 'move Tom') ")
    println(" - 'move [playerName] d1, d2' ---> move a Player with dice1 + dice 2 (ex.: 'move Tom 2, 4') ")
    println(" - 'exit' ---> Exit the game")
    println("************************************************ ")
    println()

  }

  def play(): Unit = {
    val inputCommand    = commandDecoder.readUserInput(readLine())
    val resultExecution = inputCommand.execute().message
    if (resultExecution != "exit") {
      println(resultExecution)
      inputCommand match {
        case UnknownCommand(p) =>
          displayCommands()
          play()
        case _ => play()
      }
    }
  }
}

object Main extends App {
  val main = new Main()
  main.displayCommands()
  main.play()
}
