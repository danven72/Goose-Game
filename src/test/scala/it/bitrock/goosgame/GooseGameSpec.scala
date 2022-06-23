package it.bitrock.goosgame

import it.bitrock.goosgame.outcomes.{Outcome, OutcomeResultBuilder}
import org.mockito.Mockito._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar

class GooseGameSpec extends AnyFunSuite with MockitoSugar {
  val mockOutcomeResultBuilder = mock[OutcomeResultBuilder]
  val goose                    = GooseGame(mockOutcomeResultBuilder)

  test("testAddNewPlayer") {
    val newPlayer = "Tom"
    goose.addPNewPlayer(newPlayer)
    verify(mockOutcomeResultBuilder, times(1)).buildAddPlayerOutcomeResult(false, List(newPlayer))

    goose.addPNewPlayer(newPlayer)
    verify(mockOutcomeResultBuilder, times(1)).buildAddPlayerOutcomeResult(true, List(newPlayer))
  }

  test("testExit") {
    goose.exit()
    verify(mockOutcomeResultBuilder, times(1)).buildExitOutcomeResult()
  }

  test("testUnknownInput") {
    val input = "Unknown"
    goose.unknownInput(input)
    verify(mockOutcomeResultBuilder, times(1)).buildUnknownCommandOutcomeResult(input)
  }

  test("testMovePlayer externalDices and dice not valid") {
    val dices = (23, 2)
    goose.movePlayer("tom", dices)
    verify(mockOutcomeResultBuilder, times(1)).buildDicesNotValidOutcomeResult(dices)
  }

  test("testMovePlayer when player not exists") {
    val player = "Bill"
    goose.movePlayer(player)
    verify(mockOutcomeResultBuilder, times(1)).buildPlayerNotFoundOutcomeResult(player)
  }

  test("testMovePlayer ordinary Outcome") {
    val player = "Ben"
    goose.addPNewPlayer(player)
    val dices    = (2, 2)
    val ordinary = Outcome(player, dices, 0, 4, 4, None)
    goose.movePlayer(player, dices)
    verify(mockOutcomeResultBuilder, times(1)).buildMoveOutcomeResult(List(ordinary))
  }

  test("testMovePlayer single Goose") {
    val player = "Thor"
    goose.addPNewPlayer(player)
    val dices        = (2, 3)
    val gooseOutcome = Outcome(player, dices, 0, 5, 8, None)
    goose.movePlayer(player, dices)
    verify(mockOutcomeResultBuilder, times(1)).buildMoveOutcomeResult(List(gooseOutcome))
  }

  test("testMovePlayer double Goose") {
    goose.resetPlayers() //Why this happen?
    val player = "Thor"
    goose.addPNewPlayer(player)
    val dices1    = (5, 5)
    val ordinary1 = Outcome(player, dices1, 0, 10, 10, None)
    goose.movePlayer(player, dices1)
    verify(mockOutcomeResultBuilder, times(1)).buildMoveOutcomeResult(List(ordinary1))

    val dices2       = (2, 2)
    val gooseOutcome = Outcome(player, dices2, 10, 14, 18, None)
    val ordinary2    = Outcome(player, dices2, 18, 22, 22, None)
    goose.movePlayer(player, dices2)
    verify(mockOutcomeResultBuilder, times(1)).buildMoveOutcomeResult(List(gooseOutcome, ordinary2))
  }
}
