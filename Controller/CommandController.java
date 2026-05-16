package Controller;

import Model.Command.CommandManager;

public class CommandController {
	private final CommandManager commandManager;
	
	public CommandController() {
		this.commandManager = new CommandManager();
	}
	
	public CommandManager getCommandManager() { return commandManager; }
}
