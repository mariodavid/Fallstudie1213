package luposdate.evaluators;

import java.util.Collection;
import java.util.Date;

import lupos.datastructures.dbmergesortedds.heap.Heap;
import lupos.datastructures.dbmergesortedds.tosort.ToSort;
import lupos.datastructures.items.literal.LiteralFactory;
import lupos.datastructures.items.literal.URILiteral;
import lupos.datastructures.queryresult.QueryResult;
import lupos.datastructures.trie.SuperTrie;
import lupos.engine.evaluators.BasicIndexQueryEvaluator;
import lupos.engine.operators.index.BasicIndexScan;
import lupos.engine.operators.index.Dataset;
import lupos.engine.operators.index.Indices;
import lupos.engine.operators.index.Root;
import lupos.misc.Tuple;
import luposdate.index.P2PIndexCollection;
import luposdate.index.P2PIndices;
import luposdate.logicalOptimization.P2PRulePackage;
import p2p.DataStoreAdapter;
import p2p.P2PAdapter;

/**
 * Der P2PIndexQueryEvaluator ist eine spezielle Form eines Evaluators aus
 * Luposdate. Zusätzlich hält er eine Verbindung zum P2P-Netzwerk über den
 * P2PAdapter.
 */
public class P2PIndexQueryEvaluator extends BasicIndexQueryEvaluator {

	/** The p2p adapter. */
	private P2PAdapter p2pAdapter;

	/**
	 * Gets the p2p adapter.
	 * 
	 * @return the p2p adapter
	 */
	public DataStoreAdapter getP2PAdapter() {
		return p2pAdapter;
	}

	/**
	 * Sets the p2p adapter.
	 * 
	 * @param adapter
	 *            the new p2p adapter
	 */
	public void setP2PAdapter(P2PAdapter adapter) {
		this.p2pAdapter = adapter;
	}

	/**
	 * The Enum Optimizations.
	 */
	protected enum Optimizations {

		/** The none. */
		NONE,
		/** The mostrestrictions. */
		MOSTRESTRICTIONS,
		/** The mostrestrictionsleastentries. */
		MOSTRESTRICTIONSLEASTENTRIES,
		/** The leastentries. */
		LEASTENTRIES,
		/** The binary. */
		BINARY;
	}

	/**
	 * Instantiates a new p2 p index query evaluator.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public P2PIndexQueryEvaluator() throws Exception {
		super();
	}

	/**
	 * Instantiates a new p2p index query evaluator.
	 * 
	 * @param arguments
	 *            the arguments
	 * @throws Exception
	 *             the exception
	 */
	public P2PIndexQueryEvaluator(final String[] arguments) throws Exception {
		super(arguments);
	}

	/**
	 * Instantiates a new p2p index query evaluator.
	 * 
	 * @param debug
	 *            the debug
	 * @param multiplequeries
	 *            the multiplequeries
	 * @param compare
	 *            the compare
	 * @param compareoptions
	 *            the compareoptions
	 * @param times
	 *            the times
	 * @param dataset
	 *            the dataset
	 * @param type
	 *            the type
	 * @param externalontology
	 *            the externalontology
	 * @param inmemoryexternalontologyinference
	 *            the inmemoryexternalontologyinference
	 * @param rdfs
	 *            the rdfs
	 * @param codemap
	 *            the codemap
	 * @param tmpDirs
	 *            the tmp dirs
	 * @param loadindexinfo
	 *            the loadindexinfo
	 * @param parallelOperands
	 *            the parallel operands
	 * @param blockwise
	 *            the blockwise
	 * @param limit
	 *            the limit
	 * @param jointhreads
	 *            the jointhreads
	 * @param joinbuffer
	 *            the joinbuffer
	 * @param heap
	 *            the heap
	 * @param tosort
	 *            the tosort
	 * @param indexheap
	 *            the indexheap
	 * @param mergeheapheight
	 *            the mergeheapheight
	 * @param mergeheaptype
	 *            the mergeheaptype
	 * @param chunk
	 *            the chunk
	 * @param mergethreads
	 *            the mergethreads
	 * @param yagomax
	 *            the yagomax
	 * @param stringsearch
	 *            the stringsearch
	 * @param resulttype
	 *            the resulttype
	 * @param storage
	 *            the storage
	 * @param join
	 *            the join
	 * @param optional
	 *            the optional
	 * @param sort
	 *            the sort
	 * @param distinct
	 *            the distinct
	 * @param merge_join_optional
	 *            the merge_join_optional
	 * @param encoding
	 *            the encoding
	 * @param datastructure
	 *            the datastructure
	 * @param datasetsort
	 *            the datasetsort
	 * @param optimization
	 *            the optimization
	 */
	public P2PIndexQueryEvaluator(
			DEBUG debug,
			boolean multiplequeries,
			compareEvaluator compare,
			String compareoptions,
			int times,
			String dataset,
			final String type,
			final String externalontology,
			final boolean inmemoryexternalontologyinference,
			final RDFS rdfs,
			final LiteralFactory.MapType codemap,
			final String[] tmpDirs,
			final boolean loadindexinfo,
			final PARALLELOPERANDS parallelOperands,
			final boolean blockwise,
			final int limit,
			final int jointhreads,
			final int joinbuffer,
			final Heap.HEAPTYPE heap,
			final ToSort.TOSORT tosort,
			final int indexheap,
			final int mergeheapheight,
			final Heap.HEAPTYPE mergeheaptype,
			final int chunk,
			final int mergethreads,
			final int yagomax,
			final SuperTrie.TRIETYPE stringsearch,
			final QueryResult.TYPE resulttype,
			final STORAGE storage,
			final JOIN join,
			final JOIN optional,
			final SORT sort,
			final DISTINCT distinct,
			final MERGE_JOIN_OPTIONAL merge_join_optional,
			final String encoding,
			final lupos.engine.operators.index.Indices.DATA_STRUCT datastructure,
			final Dataset.SORT datasetsort, final Optimizations optimization) {
		super(debug, multiplequeries, compare, compareoptions, times, dataset,
				type, externalontology, inmemoryexternalontologyinference,
				rdfs, codemap, tmpDirs, loadindexinfo, parallelOperands,
				blockwise, limit, jointhreads, joinbuffer, heap, tosort,
				indexheap, mergeheapheight, mergeheaptype, chunk, mergethreads,
				yagomax, stringsearch, resulttype, storage, join, optional,
				sort, distinct, merge_join_optional, encoding, datastructure,
				datasetsort);
		init(datastructure, optimization);
	}

	/**
	 * Inits the...
	 * 
	 * @param datastructure
	 *            the datastructure
	 * @param optimization
	 *            the optimization
	 */
	private void init(final Indices.DATA_STRUCT datastructure,
			final Optimizations optimization) {
		Indices.setUsedDatastructure(datastructure);
		switch (optimization) {
		case MOSTRESTRICTIONS:
			opt = BasicIndexScan.MOSTRESTRICTIONS;
			break;
		case MOSTRESTRICTIONSLEASTENTRIES:
			opt = BasicIndexScan.MOSTRESTRICTIONSLEASTENTRIES;
			break;
		case LEASTENTRIES:
			opt = BasicIndexScan.LEASTENTRIES;
			break;
		case BINARY:
			opt = BasicIndexScan.Binary;
			break;
		default:
			opt = BasicIndexScan.NONE;
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lupos.engine.evaluators.BasicIndexQueryEvaluator#init()
	 */
	@Override
	public void init() throws Exception {
		super.init();
		// IndexMaps.setUsedDatastructure((IndexMaps.DATA_STRUCT)args.getEnum(
		// "datastructure"));
		init((Indices.DATA_STRUCT) args.getEnum("datastructure"),
				(Optimizations) this.args.getEnum("optimization"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lupos.engine.evaluators.BasicIndexQueryEvaluator#setupArguments()
	 */
	@Override
	public void setupArguments() {
		defaultOptimization = Optimizations.MOSTRESTRICTIONSLEASTENTRIES;
		super.setupArguments();
	}

	// moved to lupos.rdf.Indices
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * lupos.engine.evaluators.BasicIndexQueryEvaluator#prepareInputData(java
	 * .util.Collection, java.util.Collection)
	 */
	@Override
	public long prepareInputData(final Collection<URILiteral> defaultGraphs,
			final Collection<URILiteral> namedGraphs) throws Exception {
		final Date a = new Date();
		super.prepareInputData(defaultGraphs, namedGraphs);
		dataset = new Dataset(defaultGraphs, namedGraphs, type,
				getMaterializeOntology(), opt, new P2PIndicesFactory(),
				debug != DEBUG.NONE, inmemoryexternalontologyinference);
		dataset.buildCompletelyAllIndices();
		final long prepareTime = new Date().getTime() - a.getTime();
		return prepareTime;
	}

	/**
	 * Gets the index collection.
	 * 
	 * @return the index collection
	 */
	public Root getRoot() {
		return root;
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(final String[] args) {
		_main(args, P2PIndexQueryEvaluator.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * lupos.engine.evaluators.BasicIndexQueryEvaluator#createIndexCollection()
	 */
	@Override
	public lupos.engine.operators.index.Root createRoot() {
		return new P2PIndexCollection(dataset);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lupos.engine.evaluators.BasicIndexQueryEvaluator#
	 * prepareInputDataWithSourcesOfNamedGraphs(java.util.Collection,
	 * java.util.Collection)
	 */
	@Override
	public long prepareInputDataWithSourcesOfNamedGraphs(
			Collection<URILiteral> defaultGraphs,
			Collection<Tuple<URILiteral, URILiteral>> namedGraphs)
			throws Exception {
		final Date a = new Date();
		super.prepareInputDataWithSourcesOfNamedGraphs(defaultGraphs,
				namedGraphs);
		dataset = new Dataset(defaultGraphs, namedGraphs,
				getMaterializeOntology(), type, opt, new P2PIndicesFactory(),
				debug != DEBUG.NONE, inmemoryexternalontologyinference);
		dataset.buildCompletelyAllIndices();
		final long prepareTime = new Date().getTime() - a.getTime();
		return prepareTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * lupos.engine.evaluators.BasicIndexQueryEvaluator#logicalOptimization()
	 */
	@Override
	public long logicalOptimization() {
		P2PRulePackage rp = new P2PRulePackage(this.p2pAdapter);
		rp.applyRules(this.getRootNode());
		return super.logicalOptimization();
	};

	/**
	 * A factory for creating P2PIndices objects.
	 */
	private class P2PIndicesFactory implements Dataset.IndicesFactory {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * lupos.engine.operators.index.Dataset.IndicesFactory#createIndices
		 * (lupos.datastructures.items.literal.URILiteral)
		 */
		public Indices createIndices(final URILiteral uriLiteral) {
			return new P2PIndices(uriLiteral, p2pAdapter);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see lupos.engine.operators.index.Dataset.IndicesFactory#createRoot
		 * ()
		 */
		public lupos.engine.operators.index.Root createRoot() {
			return new P2PIndexCollection(dataset);
		}
	}

}
