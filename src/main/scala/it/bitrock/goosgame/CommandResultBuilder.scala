package it.bitrock.goosgame

class CommandResultBuilder {

  def addPlayerCommandResult(
      newPlayer: String,
      present: Boolean
  ): CommandResult = {
    if (present)
      CommandResult(s"$newPlayer: already existing player")
    else
      CommandResult(s"add player $newPlayer")
  }

  //Maybe this logic could be inside Outcome...
  def buildMoveCommandResult(outcome: Outcome): CommandResult = {
    outcome match {
      case Win(p1, p2, p3, p4, p5) => {
        val theMessage =
          buildMoveMessage(outcome, s". ${outcome.player} Wins!!")
        CommandResult(theMessage)
      }
      case Bounce(p1, p2, p3, p4, p5) => {
        val theMessage = buildMoveMessage(
          outcome,
          s". ${outcome.player} bounces! ${outcome.player} return to ${outcome.realPosition}"
        )
        CommandResult(theMessage)
      }
      case Ordinary(p1, p2, p3, p4, p5) =>
        CommandResult(buildMoveMessage(outcome, ""))
      case Bridge(p1, p2, p3, p4, p5) =>
        CommandResult(
          buildMoveMessage(
            outcome,
            s" ${outcome.player} jumps to ${outcome.realPosition}"
          )
        )
      case Goose(p1, p2, p3, p4, p5) =>
        CommandResult(
          buildMoveMessage(
            outcome,
            s", The Goose. ${outcome.player} moves again and goes to ${outcome.realPosition}"
          )
        )
    }
  }

  private def buildMoveMessage(outcome: Outcome, fromResult: String): String = {
    val message =
      s"${outcome.player} rolls ${outcome.dices._1}, ${outcome.dices._2}. ${outcome.player} move from ${decodePositionForMessage(
        outcome.oldPosition
      )} to ${decodePositionForMessage(outcome.theoreticalPosition)}"

    message + fromResult
  }

  def playerNotFoundCommandResult(player: String): CommandResult =
    CommandResult(s"Player $player not Found!")

  private def decodePositionForMessage(position: Int): String = {
    position match {
      case 0 => "Start"
      case 6 => "The Bridge."
      case _ => position.toString
    }
  }

}
