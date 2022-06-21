package it.bitrock.goosgame.input

import it.bitrock.goosgame.GooseGame
import it.bitrock.goosgame.outcomes.OutcomeResult

sealed trait InputCommand {
  def execute(): OutcomeResult
}

abstract class AbstractInputCommand extends InputCommand {}

case class AddPNewPlayerCommand(gooseGame: GooseGame, player: String) extends AbstractInputCommand {
  override def execute(): OutcomeResult = {
    gooseGame.addPNewPlayer(player)
  }
}

case class MovePlayerExternalDicesCommand(
    gooseGame: GooseGame,
    player: String,
    dices: (Int, Int)
) extends AbstractInputCommand {
  override def execute(): OutcomeResult = {
    gooseGame.movePlayer(player, dices)
  }
}

case class MovePlayerAutoDicesCommand(
    gooseGame: GooseGame,
    player: String
) extends AbstractInputCommand {
  override def execute(): OutcomeResult = {
    gooseGame.movePlayer(player)
  }
}

case class ExitCommand(
) extends AbstractInputCommand {
  override def execute(): OutcomeResult = {
    OutcomeResult("exit")
  }
}