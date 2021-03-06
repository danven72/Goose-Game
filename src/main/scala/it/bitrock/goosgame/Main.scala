package it.bitrock.goosgame
import scala.io.StdIn._
import it.bitrock.goosgame.input.{InputCommandDecoder, UnknownCommand}
import it.bitrock.goosgame.outcomes.{OutcomeResultBuilder, Win}

import scala.annotation.tailrec

class Main {
  private val outcomeResultBuilder = new OutcomeResultBuilder
  private val gooseGame            = GooseGame(outcomeResultBuilder)
  private val commandDecoder       = InputCommandDecoder(gooseGame)

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

  @tailrec
  final def play(): Unit = {
    val inputCommand         = commandDecoder.readUserInput(readLine())
    val outcomeResult        = inputCommand.execute()
    val outcomeResultMessage = outcomeResult.message

    println(outcomeResultMessage)
    if (outcomeResult.isLastOutcome) {
      System.exit(0)
    } else {
      inputCommand match {
        case UnknownCommand(p1, p2) =>
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
