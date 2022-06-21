package it.bitrock.goosgame

sealed trait InputCommand {
  def execute(): CommandResult
}

abstract class AbstractInputCommand extends InputCommand {}

case class AddPNewPlayerCommand(gooseGame: GooseGame, player: String) extends AbstractInputCommand {
  override def execute(): CommandResult = {
    gooseGame.addPNewPlayer(player)
  }
}

case class MovePlayerExternalDicesCommand(
    gooseGame: GooseGame,
    player: String,
    dices: (Int, Int)
) extends AbstractInputCommand {
  override def execute(): CommandResult = {
    gooseGame.movePlayer(player, dices)
  }
}

case class MovePlayerAutoDicesCommand(
    gooseGame: GooseGame,
    player: String
) extends AbstractInputCommand {
  override def execute(): CommandResult = {
    gooseGame.movePlayer(player)
  }
}

case class ExitCommand(
) extends AbstractInputCommand {
  override def execute(): CommandResult = {
    CommandResult("exit")
  }
}

/*
object InputCommand {
  def apply(goose: Goose): InputCommand = ???
}

 */
