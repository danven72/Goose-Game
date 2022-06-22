package it.bitrock.goosgame.input

import it.bitrock.goosgame.GooseGame
import it.bitrock.goosgame.outcomes.OutcomeResult

sealed trait InputCommand {
  def execute(): OutcomeResult
}

case class AddNewPlayerCommand(gooseGame: GooseGame, player: String) extends InputCommand {
  override def execute(): OutcomeResult = {
    gooseGame.addPNewPlayer(player)
  }
}

case class MovePlayerExternalDicesCommand(gooseGame: GooseGame, player: String, dices: (Int, Int))
    extends InputCommand {
  override def execute(): OutcomeResult = {
    gooseGame.movePlayer(player, dices)
  }
}

case class MovePlayerAutoDicesCommand(gooseGame: GooseGame, player: String) extends InputCommand {
  override def execute(): OutcomeResult = {
    gooseGame.movePlayer(player)
  }
}

case class ExitCommand(gooseGame: GooseGame) extends InputCommand {
  override def execute(): OutcomeResult = {
    gooseGame.exit()
  }
}

case class UnknownCommand(gooseGame: GooseGame, inputCommand: String) extends InputCommand {
  override def execute(): OutcomeResult = {
    gooseGame.unknownInput(inputCommand)
  }
}
