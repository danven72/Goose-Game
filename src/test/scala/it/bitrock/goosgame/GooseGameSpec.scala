package it.bitrock.goosgame

import it.bitrock.goosgame.outcomes.OutcomeResultBuilder
import org.scalatest.funsuite.AnyFunSuite

// TODO try to Test using Mock OutcomeResultBuilder
class GooseGameSpec extends AnyFunSuite {
  val outcomeResultBuilder = new OutcomeResultBuilder
  val goose                = GooseGame(outcomeResultBuilder)

  test("testAddNewPlayer") {
    val resultJohn = goose.addPNewPlayer("John")
  }
}
