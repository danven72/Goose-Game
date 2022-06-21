package it.bitrock.goosgame.outcomes

import org.scalatest.funsuite.AnyFunSuite

class OutcomeSpec extends AnyFunSuite {

  test("test Outcome ordinary") {
    val player   = "Tom"
    val ordinary = Outcome(player, Tuple2(2, 4), 57, 55, 55, None)
    assert(ordinary.isInstanceOf[Ordinary])
    assert("" == ordinary.buildSpecificMoveMessage())
  }

  test("testBuildBaseMoveMessage") {
    val player   = "Tom"
    val ordinary = Outcome(player, Tuple2(2, 4), 57, 55, 55, None)
    val messageExpected =
      s"${player} rolls ${ordinary.dices._1}, ${ordinary.dices._2}. ${player} move from ${ordinary.oldPosition} " +
        s"to ${ordinary.theoreticalPosition}"

    assert(messageExpected == ordinary.buildBaseMoveMessage)
  }

  test("testBuildBaseMoveMessage When Initial position os 0") {
    val player   = "Tom"
    val ordinary = Outcome(player, Tuple2(1, 2), 0, 3, 3, None)
    val messageExpected =
      s"${player} rolls ${ordinary.dices._1}, ${ordinary.dices._2}. ${player} move from Start " +
        s"to ${ordinary.theoreticalPosition}"

    assert(messageExpected == ordinary.buildBaseMoveMessage)
  }

  test("testBuildBaseMoveMessage When The Bridge") {
    val player = "Tom"
    val bridge = Bridge(player, Tuple2(2, 2), 2, 6, 6, None)
    val messageExpected =
      s"${player} rolls ${bridge.dices._1}, ${bridge.dices._2}. ${player} move from ${bridge.oldPosition} " +
        s"to The Bridge."

    assert(messageExpected == bridge.buildBaseMoveMessage)
  }

  test("test gooseMoveMessage ") {
    val player   = "Tom"
    val ordinary = Outcome(player, Tuple2(2, 4), 57, 55, 55, None)

    val messageExpected = s", The Goose. ${player} moves again and goes to ${ordinary.realPosition}"
    assert(messageExpected == ordinary.gooseMoveMessage())
  }

  test("test Outcome Bridge") {
    val player = "Tom"
    val bridge = Outcome(player, Tuple2(1, 1), 4, 6, 6, None)
    assert(bridge.isInstanceOf[Bridge])
    val realPositionRecalculated = 12
    assert(realPositionRecalculated == bridge.realPosition)
    val messageExpected = s" ${player} jumps to ${realPositionRecalculated}"
    assert(messageExpected == bridge.buildSpecificMoveMessage())
  }

  test("test Outcome Win") {
    val player = "Tom"
    val win    = Outcome(player, Tuple2(2, 4), 57, 63, 63, None)
    assert(win.isInstanceOf[Win])
    val messageExpected = s". ${player} Wins!!"
    assert(messageExpected == win.buildSpecificMoveMessage())
  }

  test("test Outcome Bounce") {
    val player = "Tom"
    val bounce = Outcome(player, Tuple2(2, 4), 57, 65, 65, None)
    assert(bounce.isInstanceOf[Bounce])
    val realPositionRecalculated = 63 - (65 - 63)
    assert(realPositionRecalculated == bounce.realPosition)
    val messageExpected = s". ${player} bounces! ${player} return to ${realPositionRecalculated}"
    assert(messageExpected == bounce.buildSpecificMoveMessage())
  }

  test("test Outcome Goose") {
    val player = "Tom"
    val goose  = Outcome(player, Tuple2(1, 1), 3, 5, 5, None)
    assert(goose.isInstanceOf[Goose])
    val realPositionRecalculated = 7
    assert(realPositionRecalculated == goose.realPosition)
    val messageExpected = s", The Goose. ${player} moves again and goes to ${realPositionRecalculated}"
    assert(messageExpected == goose.buildSpecificMoveMessage())
  }

  test("test Outcome Prank") {
    val player      = "Tom"
    val prankPlayer = "Bill"
    val prank       = Outcome(player, Tuple2(2, 4), 57, 55, 55, Some((prankPlayer, 33)))
    assert(prank.isInstanceOf[Prank])
    val realPositionRecalculated = 33
    assert(realPositionRecalculated == prank.realPosition)
    val messageExpected = s". On 55 there is ${prankPlayer}, who returns to 57"
    assert(messageExpected == prank.buildSpecificMoveMessage())
  }
}
