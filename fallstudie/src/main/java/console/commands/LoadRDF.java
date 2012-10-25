package console.commands;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Scanner;

import luposdate.evaluators.P2PIndexQueryEvaluator;

import net.tomp2p.p2p.Peer;


/**
 * Läd eine RDF Datei ein und speichert die Triple in das Netzwerk
 * 
 */
public class LoadRDF implements Command {

	/** The filename. */
	private String	filename;

	/*
	 * (non-Javadoc)
	 * 
	 * @see P2P.TOM.COMMANDS.Command#execute(java.util.Scanner,
	 * net.tomp2p.p2p.Peer)
	 */
	public void execute(Scanner scanner, Peer peer,
			P2PIndexQueryEvaluator evaluator) {
//
//		try {
//
//			filename = scanner.next();
//			DistributionStrategy distributor = DistributionFactory
//					.create(scanner.nextInt());
//			distributor.setPeer(peer);
//
//			Scanner file;
//
//			if (filename.startsWith("http://")) {
//				file = parseHttp();
//			} else {
//				file = parseFile();
//			}
//
//			int tripleCounter = 0;
//
//			while (file.hasNextLine()) {
//				RDFTriple triple = new RDFTriple(file.nextLine());
//				distributor.distribute(triple);
//				System.out.println(triple);
//				tripleCounter++;
//			}
//
//			System.out.println(tripleCounter + " triples imported.");
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

	}


	/**
	 * parses a http file and decompresses it if required.
	 *
	 * @return the scanner object for walking through the data
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws MalformedURLException the malformed url exception
	 */
	private Scanner parseHttp() throws IOException,
			MalformedURLException {
//
//		URLConnection connection = new URL(filename).openConnection();
//		InputStream in = connection.getInputStream();
//
//		in = decompressIfRequired(in);
//
//		return new Scanner(in);
		return null;
	}

	/**
	 * parses a local file and decompresses it if required.
	 *
	 * @return the scanner object for walking through the data
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private Scanner parseFile() throws IOException {

//		InputStream in = new FileInputStream(new File(filename));
//
//		in = decompressIfRequired(in);
//
//		return new Scanner(new File(filename));
		return null;
	}

	/**
	 * decompresses a given input stream if required and is an bzip2 compression.
	 *
	 * @param in the in
	 * @return the decompressed input stream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private InputStream decompressIfRequired(InputStream in) 
			throws IOException {
//		if (filename.endsWith("bz") || filename.endsWith("bz2")) {
//			in = new BZip2CompressorInputStream(in);
//			System.out.println("bzip2 is used");
//		}
//		return in;
	return null;
	}

	/* (non-Javadoc)
	 * @see console.commands.Command#getDescription()
	 */
	public String getDescription() {
//		return "[filename / URL] [strategy] loads a rdf file from the given filename / url into the p2p network."
//				+ "the data format of the file has to be N-TRIPLE like from http://downloads.dbpedia.org/, "
//				+ "the file format can be .nt or nt.bz2 (automatic decompression is possible)";
	return null;
	}

}
