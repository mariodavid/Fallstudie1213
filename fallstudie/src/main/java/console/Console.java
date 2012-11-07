package console;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import luposdate.LuposServer;
import p2p.P2PAdapter;
import p2p.P2PConnection;
import console.commands.Add;
import console.commands.Command;
import console.commands.CreateChannel;
import console.commands.DeserializerTest;
import console.commands.Get;
import console.commands.GetAllNodes;
import console.commands.GetExampleQueries;
import console.commands.GetLocalStorage;
import console.commands.GetLocalStorageSize;
import console.commands.GetMyID;
import console.commands.GetPeerForContent;
import console.commands.GetStrategy;
import console.commands.GetSubGraphDistribution;
import console.commands.Help;
import console.commands.Put;
import console.commands.Query;
import console.commands.Quit;
import console.commands.Remove;
import console.commands.SendMessage;
import console.commands.SetStrategy;
import console.commands.SetSubGraphDistribution;
import console.commands.LoadN3;

/**
 * In dieser Klasse wird die Konsolenfunktion implementiert. Gleichzeitig stellt
 * die Konsole den Einstiegspunkt der Software dar.
 */
public class Console {
	/** Verbindung. */
	private final P2PConnection	connection;
	/** Lupos-Server. */
	private final LuposServer	server;

	/**
	 * Konstruktor.
	 */
	public Console(P2PConnection connection, LuposServer server) {
		this.connection = connection;
		this.server = server;
	}

	/**
	 * Startet die Konsole wenn ebreits eine Verbindung besteht. Es werden hier
	 * die Befehle von der Standard Eingabe gelesen und verarbeitet.
	 */
	public void start() throws ClassNotFoundException, IOException {
		System.out.println();
		System.out.println("############ Console ############");
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
		commands.put("getlocalstoragesize", new GetLocalStorageSize());
		commands.put("query", new Query());
		commands.put("setstrategy", new SetStrategy());
		commands.put("getstrategy", new GetStrategy());
		commands.put("getexamplequeries", new GetExampleQueries());
		commands.put("getpeerforcontent", new GetPeerForContent());
		commands.put("test", new DeserializerTest());
		commands.put("sendMessage", new SendMessage());
		commands.put("sm", new SendMessage());
		commands.put("getmyid", new GetMyID());
		commands.put("cc", new CreateChannel());
		commands.put("setsubgraphdistribution", new SetSubGraphDistribution());
		commands.put("getsubgraphdistribution", new GetSubGraphDistribution());
		commands.put("loadn3", new LoadN3());

		commands.put("help", new Help(commands.values()));

		return commands;
	}

	/**
	 * Der Eingangspunkt zu der Applikation. Abhängig von den Parametern wird
	 * eine Verbindung per Broadcast oder zu einer bestimmten IP(Port)
	 * hergestellt.
	 */
	public static void main(String[] args) throws Exception {

		// Properties props = new Properties();
		// props.load(Console.class.getResourceAsStream("log4j.properties"));
		// PropertyConfigurator.configure(props);

		P2PConnection connection = new P2PConnection();
		LuposServer server = new LuposServer();

		Console console = new Console(connection, server);
		P2PAdapter config = null;

		switch (args.length) {
		// Standardfall: Broadcast und Port ist 4001
			case 0:
				connection.connect();
				config = new P2PAdapter(connection.getPeer());
				server.start(config);

				config.setEvaluator(server.getEvaluator());

				console.start();
				break;
			// IP + remote Port + local Port
			case 3:
				String ip = args[0];
				int remotePort = Integer.parseInt(args[1]);
				int localPort = Integer.parseInt(args[2]);

				connection.connect(ip, remotePort, localPort);

				config = new P2PAdapter(connection.getPeer());
				server.start(config);

				config.setEvaluator(server.getEvaluator());

				console.start();
				break;

			default:
				System.out.println("usage: program [ip] [port]");
				break;
		}
	}
}
