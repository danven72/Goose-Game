package it.bitrock.goosgame

sealed abstract class Outcome {
  val player: String
  val dices: Tuple2[Int, Int]
  val oldPosition: Int
  val theoreticalPosition: Int
  val realPosition: Int
}

case class Ordinary(
    player: String,
    dices: Tuple2[Int, Int],
    oldPosition: Int,
    theoreticalPosition: Int,
    realPosition: Int
) extends Outcome {}

case class Win(
    player: String,
    dices: Tuple2[Int, Int],
    oldPosition: Int,
    theoreticalPosition: Int,
    realPosition: Int
) extends Outcome {}

case class Bounce(
    player: String,
    dices: Tuple2[Int, Int],
    oldPosition: Int,
    theoreticalPosition: Int,
    realPosition: Int
) extends Outcome {}

case class Bridge(
    player: String,
    dices: Tuple2[Int, Int],
    oldPosition: Int,
    theoreticalPosition: Int,
    realPosition: Int
) extends Outcome {}

object Outcome {

  val BRIDGE_POSITION = 6
  val WIN_POSITION = 63

  def apply(
      player: String,
      dices: Tuple2[Int, Int],
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

}
