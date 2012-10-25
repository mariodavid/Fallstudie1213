package console;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import test.LuposServer;
import console.commands.*;


public class Console {

	private final Connection	connection;
	private final LuposServer	server;

	public Console(Connection connection, LuposServer server) {
		this.connection = connection;
		this.server = server;
	}

	/**
	 * starts the console when a connection is already established. reads
	 * commands from std in and executes them
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void start() throws ClassNotFoundException, IOException {
		System.out
				.println("\n############ KONSOLENMODUS GESTARTET ############");
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
	 * this method generates objects for all available commands and returns them
	 * in a collection
	 * 
	 * @return the available commands
	 */
	private HashMap<String, Command> generateCommands() {
		HashMap<String, Command> commands = new HashMap<String, Command>();

		commands.put("add", new Add());
		commands.put("get", new Get());
		commands.put("getall", new GetAll());
		commands.put("put", new Put());
		commands.put("remove", new Remove());
		commands.put("removeall", new RemoveAll());
		commands.put("quit", new Quit());
		commands.put("getallnodes", new GetAllNodes());
		commands.put("getlocalstorage", new GetLocalStorage());
		commands.put("query", new Query());
		commands.put("setstrategy", new SetStrategy());
		commands.put("getexamplequeries", new GetExampleQueries());
		commands.put("getpeerforcontent", new GetPeerForContent());
		commands.put("test", new DeserializerTest());
		commands.put("rpc", new RPC());
//		commands.put("loadrdf", new LoadRDF());
		
		commands.put("help", new Help(commands.values()));
		
		return commands;
	}

	/**
	 * the entry point of the application. depending on the command line
	 * arguments the connection is established via broadcast or a given ip:port
	 * combination to connect to the network
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		Connection connection = new Connection();
		LuposServer server = new LuposServer();

		Console console = new Console(connection, server);

		switch (args.length) {
			case 0:
				connection.connect();
				P2PAdapter config = new P2PAdapter(
						connection.getPeer(), server.getEvaluator());
				server.start(config);
				console.start();
				break;

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
