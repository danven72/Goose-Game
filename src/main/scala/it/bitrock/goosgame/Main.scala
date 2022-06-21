package it.bitrock.goosgame

object Main extends App {

  val commandDecoder = CommandDecoder(new GooseGame)
  var executedMessage = ""

  while (executedMessage != "exit") {
    val command = commandDecoder.readUserInput()
    val resultExecution = command.execute().message
    println(resultExecution)
    executedMessage = resultExecution
  }

}
