package console;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import luposdate.LuposServer;

import p2p.Connection;
import p2p.P2PAdapter;

import console.commands.*;

/**
 * In dieser Klasse wird die Konsolenfunktion implementiert. Gleichzeitig stellt
 * die Konsole den Einstiegspunkt der Software dar.
 */
public class Console {
	/** Verbindung. */
	private final Connection connection;
	/** Lupos-Server. */
	private final LuposServer server;

	/**
	 * Konstruktor.
	 */
	public Console(Connection connection, LuposServer server) {
		this.connection = connection;
		this.server = server;
	}

	/**
	 * Startet die Konsole wenn ebreits eine Verbindung besteht. Es werden hier
	 * die Befehle von der Standard Eingabe gelesen und verarbeitet.
	 */
	public void start() throws ClassNotFoundException, IOException {
		System.out
				.println("############ KONSOLENMODUS GESTARTET ############");
		System.out.println("Type \"help\" for possible commands");
		String input = null;
		Scanner scanner = new Scanner(System.in);

		HashMap<String, Command> commandList = generateCommands();

		while (true) {

			System.out.print("command: ");
			input = scanner.next().toLowerCase();

			Command command = commandList.get(input);
			if (command != null) {
				command.execute(scanner, this.connection.getPeer(),
						this.server.getEvaluator());
			}

		}
	}

	/**
	 * Diese Methode erzeugt Instanzen für alle Befehle und gibt diese ein eine
	 * Collection.
	 */
	private HashMap<String, Command> generateCommands() {
		HashMap<String, Command> commands = new HashMap<String, Command>();

		commands.put("add", new Add());
		commands.put("get", new Get());
		commands.put("put", new Put());
		commands.put("remove", new Remove());
		commands.put("quit", new Quit());
		commands.put("getallnodes", new GetAllNodes());
		commands.put("getlocalstorage", new GetLocalStorage());
		commands.put("query", new Query());
		commands.put("setstrategy", new SetStrategy());
		commands.put("getexamplequeries", new GetExampleQueries());
		commands.put("getpeerforcontent", new GetPeerForContent());
		commands.put("test", new DeserializerTest());
		commands.put("sendMessage", new SendMessage());
		commands.put("sm", new SendMessage());
		commands.put("getmyid", new GetMyID());
		// commands.put("loadrdf", new LoadRDF());

		commands.put("help", new Help(commands.values()));

		return commands;
	}

	/**
	 * Der Eingangspunkt zu der Applikation. Abhängig von den Parametern wird
	 * eine Verbindung per Broadcast oder zu einer bestimmten IP(Port)
	 * hergestellt.
	 */
	public static void main(String[] args) throws Exception {

		Connection connection = new Connection();
		LuposServer server = new LuposServer();

		Console console = new Console(connection, server);

		switch (args.length) {
		// Standardfall: Broadcast und Port ist 4001
		case 0:
			connection.connect();
			P2PAdapter config = new P2PAdapter(connection.getPeer(),
					server.getEvaluator());
			server.start(config);
			console.start();
			break;
		// IP + remote Port + local Port
		case 3:
			String ip = args[0];
			int remotePort = Integer.parseInt(args[1]);
			int localPort = Integer.parseInt(args[2]);

			connection.connect(ip, remotePort, localPort);
			console.start();
			break;

		default:
			System.out.println("usage: program [ip] [port]");
			break;
		}
	}
}
