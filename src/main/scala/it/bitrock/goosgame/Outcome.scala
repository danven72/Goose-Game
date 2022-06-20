package it.bitrock.goosgame

import it.bitrock.goosgame.Outcome.THE_GOOSE

sealed abstract class Outcome {
  val player: String
  val dices: (Int, Int)
  val oldPosition: Int
  val theoreticalPosition: Int
  val realPosition: Int

  def realPositionWillBeGoose(): Boolean = THE_GOOSE.contains(realPosition)
}

case class Ordinary(
    player: String,
    dices: (Int, Int),
    oldPosition: Int,
    theoreticalPosition: Int,
    realPosition: Int
) extends Outcome {}

case class Win(
    player: String,
    dices: (Int, Int),
    oldPosition: Int,
    theoreticalPosition: Int,
    realPosition: Int
) extends Outcome {}

case class Bounce(
    player: String,
    dices: (Int, Int),
    oldPosition: Int,
    theoreticalPosition: Int,
    realPosition: Int
) extends Outcome {}

case class Bridge(
    player: String,
    dices: (Int, Int),
    oldPosition: Int,
    theoreticalPosition: Int,
    realPosition: Int
) extends Outcome {}

case class Goose(
    player: String,
    dices: (Int, Int),
    oldPosition: Int,
    theoreticalPosition: Int,
    realPosition: Int
) extends Outcome {}

object Outcome {

  val BRIDGE_POSITION = 6
  val WIN_POSITION = 63
  val THE_GOOSE = Set(5, 9, 14, 18, 23, 27)

  def apply(
      player: String,
      dices: (Int, Int),
      oldPosition: Int,
      theoreticalPosition: Int,
      realPosition: Int
  ): Outcome = {
    theoreticalPosition match {
      case WIN_POSITION =>
        Win(player, dices, oldPosition, theoreticalPosition, realPosition)
      case tp if (tp > WIN_POSITION) =>
        val recalculatedPosition =
          WIN_POSITION - (theoreticalPosition - WIN_POSITION)
        Bounce(
          player,
          dices,
          oldPosition,
          theoreticalPosition,
          recalculatedPosition
        )
      case BRIDGE_POSITION =>
        Bridge(player, dices, oldPosition, theoreticalPosition, 12)
      case tp if THE_GOOSE.contains(tp) =>
        Goose(
          player,
          dices,
          oldPosition,
          theoreticalPosition,
          theoreticalPosition + dices._1 + dices._2
        )

      case _ =>
        Ordinary(
          player,
          dices,
          oldPosition,
          theoreticalPosition,
          realPosition
        )
    }
  }
}

object TestOutcome extends App {

  val ordinary = Outcome("gino", Tuple2(2, 4), 57, 55, 55)
  println("outcome is a Ordinary: " + ordinary.isInstanceOf[Ordinary])

  val win = Outcome("gino", Tuple2(2, 4), 57, 63, 63)
  println("outcome is a Win: " + win.isInstanceOf[Win])

  val bounce = Outcome("gino", Tuple2(2, 4), 57, 65, 65)
  println(
    "outcome is a Bounce: " + bounce
      .isInstanceOf[Bounce] + " - real position is: " + bounce.realPosition
  )

  val bridge = Outcome("gino", Tuple2(1, 1), 4, 6, 6)
  println(
    "outcome is a Bridge: " + bridge
      .isInstanceOf[Bridge] + " - real position is: " + bridge.realPosition
  )

  val goose1 = Outcome("gino", Tuple2(1, 1), 3, 5, 5)
  println(
    "outcome is a Goose: " + goose1
      .isInstanceOf[Goose] + " - real position is: " + goose1.realPosition
  )

}
