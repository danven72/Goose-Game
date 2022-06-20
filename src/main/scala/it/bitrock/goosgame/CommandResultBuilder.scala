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
  def buildMoveCommandResult(outcomeList: List[Outcome]): CommandResult = {
    outcomeList.head match {
      case Win(p1, p2, p3, p4, p5, p6) => {
        val theMessage =
          buildMoveMessage(
            outcomeList.head,
            s". ${outcomeList.head.player} Wins!!"
          )
        CommandResult(theMessage)
      }
      case Bounce(p1, p2, p3, p4, p5, p6) => {
        val theMessage = buildMoveMessage(
          outcomeList.head,
          s". ${outcomeList.head.player} bounces! ${outcomeList.head.player} return to ${outcomeList.head.realPosition}"
        )
        CommandResult(theMessage)
      }
      case Ordinary(p1, p2, p3, p4, p5, p6) =>
        CommandResult(buildMoveMessage(outcomeList.head, ""))
      case Bridge(p1, p2, p3, p4, p5, p6) =>
        CommandResult(
          buildMoveMessage(
            outcomeList.head,
            s" ${outcomeList.head.player} jumps to ${outcomeList.head.realPosition}"
          )
        )
      case Goose(p1, p2, p3, p4, p5, p6) =>
        CommandResult(
          buildMoveMessage(
            outcomeList.head,
            buildGooseMovesMessage(outcomeList, "")
          )
        )
      case Prank(p1, p2, p3, theoreticalPosition, p5, p6) =>
        val prankPlayer: String = p6.map(pn => pn._1).getOrElse("Error!!!")
        val prankPlayerPosition =
          outcomeList.head.prankPlayer.map(pn => pn._2).getOrElse("Error!!!")
        CommandResult(
          buildMoveMessage(
            outcomeList.head,
            s".  On ${theoreticalPosition} there is ${prankPlayer}, who returns to ${prankPlayerPosition}"
          )
        )

    }
  }

  private def buildMoveMessage(outcome: Outcome, fromResult: String): String = {
    val message = buildBaseMessage(outcome);
    message + fromResult
  }

  private val buildBaseMessage: Outcome => String = (outcome) =>
    s"${outcome.player} rolls ${outcome.dices._1}, ${outcome.dices._2}. ${outcome.player} move from ${decodePositionForMessage(
      outcome.oldPosition
    )} to ${decodePositionForMessage(outcome.theoreticalPosition)}"

  private def buildGooseMovesMessage(
      outcomeList: List[Outcome],
      gooseMessage: String
  ): String = {
    outcomeList match {
      case Nil => gooseMessage
      case e => {
        val allGooseMessages = gooseMessage + buildGooseMessage(e.head)
        buildGooseMovesMessage(outcomeList.tail, allGooseMessages)
      }
    }
  }

  val buildGooseMessage: Outcome => String = (outcome: Outcome) =>
    s", The Goose. ${outcome.player} moves again and goes to ${outcome.realPosition}"

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
