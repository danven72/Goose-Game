package it.bitrock.goosgame.input

import it.bitrock.goosgame.GooseGame
import org.scalatest.funsuite.AnyFunSuite

class InputCommandDecoderSpec extends AnyFunSuite {

  val gooseGame           = new GooseGame
  val inputCommandDecoder = InputCommandDecoder(gooseGame)

  test("testExtractPlayerNameAndDicesFromMove when dices in input") {
    val result = inputCommandDecoder.extractPlayerNameAndDicesFromMove("move Tom 3, 5")
    assert("Tom" == result._1)
    assert(3 == result._2)
    assert(5 == result._3)
  }

  test("testExtractPlayerNameAndDicesFromMove when NO dices") {
    val result = inputCommandDecoder.extractPlayerNameAndDicesFromMove("move Tom")
    assert("Tom" == result._1)
    assert(0 == result._2)
    assert(0 == result._3)
  }

  test("testReadUserInput when add player input") {
    val addInputCommand = inputCommandDecoder.readUserInput("add player Tom")
    assert(addInputCommand.isInstanceOf[AddNewPlayerCommand])
    assert("Tom" == addInputCommand.asInstanceOf[AddNewPlayerCommand].player)
  }

  test("testReadUserInput when move player autoDices input") {
    val moveInputCommand = inputCommandDecoder.readUserInput("move Tom")
    assert(moveInputCommand.isInstanceOf[MovePlayerAutoDicesCommand])
    assert("Tom" == moveInputCommand.asInstanceOf[MovePlayerAutoDicesCommand].player)
  }

  test("testReadUserInput when move player external Dices input") {
    val moveInputCommand = inputCommandDecoder.readUserInput("move Tom 2, 4")
    assert(moveInputCommand.isInstanceOf[MovePlayerExternalDicesCommand])
    assert("Tom" == moveInputCommand.asInstanceOf[MovePlayerExternalDicesCommand].player)
    assert(2 == moveInputCommand.asInstanceOf[MovePlayerExternalDicesCommand].dices._1)
    assert(4 == moveInputCommand.asInstanceOf[MovePlayerExternalDicesCommand].dices._2)
  }

  test("testReadUserInput when exit input") {
    val addInputCommand = inputCommandDecoder.readUserInput("exit")
    assert(addInputCommand.isInstanceOf[ExitCommand])

  }

}
