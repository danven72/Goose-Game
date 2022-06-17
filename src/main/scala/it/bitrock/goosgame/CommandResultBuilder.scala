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

  def buildMoveCommandResult(
      player: String,
      dice1: Int,
      dice2: Int,
      oldPosition: Int,
      win: Boolean,
      newPositionBounceChecked: Tuple3[Int, Int, Boolean]
  ): CommandResult = {
    var message =
      s"$player rolls $dice1, $dice2. $player move from ${decodePositionForMessage(oldPosition)} to ${newPositionBounceChecked._1}"
    if (win)
      message = message + s". $player Wins!!"
    if (newPositionBounceChecked._3)
      message =
        message + s". $player bounces! $player return to ${newPositionBounceChecked._2}"

    CommandResult(message)
  }

  def playerNotFoundCommandResult(player: String): CommandResult =
    CommandResult(s"Player $player not Found!")

  private def decodePositionForMessage(position: Int): String = {
    position match {
      case 0 => "Start"
      case _ => position.toString
    }
  }

}
