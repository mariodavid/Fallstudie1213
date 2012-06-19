package console.commands;

import java.util.Scanner;

import net.tomp2p.p2p.Peer;

public interface Command {
	public void execute(Scanner scanner, Peer peer);

	public String getDescription();

}
