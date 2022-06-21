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

  test("playerNotFoundOutcomeResult") {
    val player          = "Tom"
    val messageExpected = s"Player $player not Found!"
    assert(messageExpected == outcomeResultBuilder.playerNotFoundOutcomeResult(player).message)
  }

}
