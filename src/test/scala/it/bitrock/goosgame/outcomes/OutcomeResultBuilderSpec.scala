package it.bitrock.goosgame.outcomes

import org.scalatest.funsuite.AnyFunSuite

class OutcomeResultBuilderSpec extends AnyFunSuite {
  val outcomeResultBuilder = new OutcomeResultBuilder

  test("addPlayerOutcomeResult when first ") {
    val newPlayer       = "Tom"
    val messageExpected = "players: " + newPlayer
    val result          = outcomeResultBuilder.addPlayerOutcomeResult(false, List(newPlayer))
    assert(messageExpected == result.message)
  }

  test("addPlayerOutcomeResult when more player ") {
    val tom             = "Tom"
    val jack            = "Jack"
    val bill            = "Bill"
    val messageExpected = s"players: $tom, $jack, $bill"
    val result          = outcomeResultBuilder.addPlayerOutcomeResult(false, List(tom, jack, bill))
    assert(messageExpected == result.message)
  }

  test("addPlayerOutcomeResult when presented yet ") {
    val tom = "Tom"

    val messageExpected = s"${tom}: already existing player"
    val result          = outcomeResultBuilder.addPlayerOutcomeResult(true, List(tom))
    assert(messageExpected == result.message)
  }

  test("testBuildPlayerNotFoundOutcomeResult") {
    val player          = "Tom"
    val messageExpected = s"Player $player not Found!"
    val result          = outcomeResultBuilder.buildPlayerNotFoundOutcomeResult(player)
    assert(messageExpected == result.message)
    assert(result.isLastOutcome == false)
  }

  test("buildMoveOutcomeResult when one Outcome present") {
    val ordinary        = Ordinary("Tom", (2, 2), 7, 11, 11, None)
    val expectedMessage = ordinary.buildBaseMoveMessage + ordinary.buildSpecificMoveMessage()
    val result          = outcomeResultBuilder.buildMoveOutcomeResult(List(ordinary))
    assert(expectedMessage == result.message)
    assert(result.isLastOutcome == false)
  }

  test("buildMoveOutcomeResult when one Goose and one Outcome present") {
    val goose           = Goose("John", (2, 2), 10, 14, 18, None)
    val ordinary        = Ordinary("John", (2, 2), 18, 22, 22, None)
    val expectedMessage = goose.buildBaseMoveMessage + goose.gooseMoveMessage() + ordinary.gooseMoveMessage()
    val result          = outcomeResultBuilder.buildMoveOutcomeResult(List(goose, ordinary))
    //println(expectedMessage)
    //println(result.message)
    assert(expectedMessage == result.message)
    assert(result.isLastOutcome == false)
  }

  test("buildMoveOutcomeResult when one Outcome Win is the last") {
    val win             = Win("Tom", (2, 2), 59, 63, 63, None)
    val expectedMessage = win.buildBaseMoveMessage + win.buildSpecificMoveMessage()
    val result          = outcomeResultBuilder.buildMoveOutcomeResult(List(win))
    println(result.message)
    assert(result.isLastOutcome == true)
  }

  test("testBuildExitOutcomeResult") {
    val result = outcomeResultBuilder.buildExitOutcomeResult()
    assert("You force program exit" == result.message)
    assert(result.isLastOutcome)
  }

  test("testBuildUnknownCommandOutcomeResult") {
    val unknownCommand = "UNKNOWN"
    val result         = outcomeResultBuilder.buildUnknownCommandOutcomeResult(unknownCommand)
    assert(s"Command [${unknownCommand}] unknown!" == result.message)
    assert(!result.isLastOutcome)
  }

  test("testBuildDicesNotValidOutcomeResult") {
    val dicesNotVald = (7, 0)
    val result       = outcomeResultBuilder.buildDicesNotValidOutcomeResult(dicesNotVald)
    assert(
      s"Dices not valid! You asks: [${dicesNotVald._1}, ${dicesNotVald._2}] but only 1..6 values are ok" == result.message
    )
    assert(!result.isLastOutcome)
  }

}
