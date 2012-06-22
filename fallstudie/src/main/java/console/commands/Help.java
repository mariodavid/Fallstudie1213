package console.commands;

import java.util.Collection;
import java.util.Scanner;

import net.tomp2p.p2p.Peer;
import evaluators.P2PIndexQueryEvaluator;

public class Help implements Command {

	private final Collection<Command>	commands;

	public Help(Collection<Command> commands) {
		this.commands = commands;
	}

	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {

		for (Command c : commands) {
			printCommandDescription(c);
		}

	}

	private void printCommandDescription(Command c) {
		System.out.printf("%-20s", c.getClass().getSimpleName());
		System.out.println(c.getDescription());
	}

	public String getDescription() {
		return "this help dialog";
	}

}
