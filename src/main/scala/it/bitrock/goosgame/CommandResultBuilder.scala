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

  def buildMoveCommandResult(outcomeList: List[Outcome]): CommandResult = {

    if (outcomeList.size == 1) {
      val outcome = outcomeList.head
      CommandResult(
        outcome.buildBaseMoveMessage + outcome.buildSpecificMoveMessage()
      )
    } else {
      CommandResult(
        outcomeList.head.buildBaseMoveMessage + buildGooseMovesMessage(
          outcomeList,
          ""
        )
      )
    }
  }

  private def buildGooseMovesMessage(
      outcomeList: List[Outcome],
      gooseMessage: String
  ): String = {
    println("Outcomes: " + outcomeList)
    outcomeList match {
      case Nil => gooseMessage
      case e =>
        val allGooseMessages = gooseMessage + e.head.gooseMoveMessage()
        buildGooseMovesMessage(outcomeList.tail, allGooseMessages)
    }
  }

  def playerNotFoundCommandResult(player: String): CommandResult =
    CommandResult(s"Player $player not Found!")

}
