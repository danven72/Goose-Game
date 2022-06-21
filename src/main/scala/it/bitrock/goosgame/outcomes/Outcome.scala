package it.bitrock.goosgame.outcomes

import it.bitrock.goosgame.outcomes.Outcome.THE_GOOSE

sealed abstract class Outcome {
  val player: String
  val dices: (Int, Int)
  val oldPosition: Int
  val theoreticalPosition: Int
  val realPosition: Int
  val prankPlayer: Option[(String, Int)]

  def realPositionWillBeGoose(): Boolean = THE_GOOSE.contains(realPosition)

  def buildBaseMoveMessage: String = {
    def decodePositionForMessage(position: Int): String = {
      position match {
        case 0 => "Start"
        case 6 => "The Bridge."
        case _ => position.toString
      }
    }
    s"${player} rolls ${dices._1}, ${dices._2}. ${player} move from ${decodePositionForMessage(oldPosition)} " +
      s"to ${decodePositionForMessage(theoreticalPosition)}"
  }

  def gooseMoveMessage(): String =
    s", The Goose. ${player} moves again and goes to ${realPosition}"

  def buildSpecificMoveMessage(): String
}

case class Ordinary(
    player: String,
    dices: (Int, Int),
    oldPosition: Int,
    theoreticalPosition: Int,
    realPosition: Int,
    prankPlayer: Option[(String, Int)]
) extends Outcome {

  override def buildSpecificMoveMessage(): String =
    ""
}

case class Win(
    player: String,
    dices: (Int, Int),
    oldPosition: Int,
    theoreticalPosition: Int,
    realPosition: Int,
    prankPlayer: Option[(String, Int)]
) extends Outcome {

  override def buildSpecificMoveMessage(): String =
    s". ${player} Wins!!"
}

case class Bounce(
    player: String,
    dices: (Int, Int),
    oldPosition: Int,
    theoreticalPosition: Int,
    realPosition: Int,
    prankPlayer: Option[(String, Int)]
) extends Outcome {
  override def buildSpecificMoveMessage(): String =
    s". ${player} bounces! ${player} return to ${realPosition}"
}

case class Bridge(
    player: String,
    dices: (Int, Int),
    oldPosition: Int,
    theoreticalPosition: Int,
    realPosition: Int,
    prankPlayer: Option[(String, Int)]
) extends Outcome {
  override def buildSpecificMoveMessage(): String =
    s" ${player} jumps to ${realPosition}"
}

case class Goose(
    player: String,
    dices: (Int, Int),
    oldPosition: Int,
    theoreticalPosition: Int,
    realPosition: Int,
    prankPlayer: Option[(String, Int)]
) extends Outcome {
  override def buildSpecificMoveMessage(): String =
    gooseMoveMessage
}

case class Prank(
    player: String,
    dices: (Int, Int),
    oldPosition: Int,
    theoreticalPosition: Int,
    realPosition: Int,
    prankPlayer: Option[(String, Int)]
) extends Outcome {
  override def buildSpecificMoveMessage(): String = {
    val prankPlayerV =
      prankPlayer.map(ppOpt => ppOpt._1).getOrElse("Error!!!")
    val prankPlayerPosition =
      prankPlayer.map(ppOpt => ppOpt._2).getOrElse("Error!!!")

    s".  On ${theoreticalPosition} there is ${prankPlayerV}, who returns to ${prankPlayerPosition}"
  }
}

object Outcome {

  private val BRIDGE_POSITION = 6
  private val WIN_POSITION    = 63
  private val THE_GOOSE       = Set(5, 9, 14, 18, 23, 27)

  def apply(
      player: String,
      dices: (Int, Int),
      oldPosition: Int,
      theoreticalPosition: Int,
      realPosition: Int,
      prankPlayer: Option[(String, Int)]
  ): Outcome = {

    prankPlayer match {
      case Some(p) => Prank(player, dices, oldPosition, theoreticalPosition, p._2, Some((p._1, oldPosition)))
      case None =>
        theoreticalPosition match {
          case WIN_POSITION => Win(player, dices, oldPosition, theoreticalPosition, realPosition, prankPlayer)
          case tp if (tp > WIN_POSITION) =>
            val recalculatedPosition = WIN_POSITION - (theoreticalPosition - WIN_POSITION)
            Bounce(player, dices, oldPosition, theoreticalPosition, recalculatedPosition, prankPlayer)
          case BRIDGE_POSITION =>
            Bridge(player, dices, oldPosition, theoreticalPosition, 12, prankPlayer)
          case tp if THE_GOOSE.contains(tp) =>
            val newTheoreticalPosition = theoreticalPosition + dices._1 + dices._2
            Goose(player, dices, oldPosition, theoreticalPosition, newTheoreticalPosition, prankPlayer)
          case _ => Ordinary(player, dices, oldPosition, theoreticalPosition, realPosition, prankPlayer)
        }
    }

  }
}

object TestOutcome extends App {

  val ordinary = Outcome("gino", Tuple2(2, 4), 57, 55, 55, None)
  println("outcome is a Ordinary: " + ordinary.isInstanceOf[Ordinary])

  val win = Outcome("gino", Tuple2(2, 4), 57, 63, 63, None)
  println("outcome is a Win: " + win.isInstanceOf[Win])

  val bounce = Outcome("gino", Tuple2(2, 4), 57, 65, 65, None)
  println(
    "outcome is a Bounce: " + bounce
      .isInstanceOf[Bounce] + " - real position is: " + bounce.realPosition
  )

  val bridge = Outcome("gino", Tuple2(1, 1), 4, 6, 6, None)
  println(
    "outcome is a Bridge: " + bridge
      .isInstanceOf[Bridge] + " - real position is: " + bridge.realPosition
  )

  val goose1 = Outcome("gino", Tuple2(1, 1), 3, 5, 5, None)
  println(
    "outcome is a Goose: " + goose1
      .isInstanceOf[Goose] + " - real position is: " + goose1.realPosition
  )

}
