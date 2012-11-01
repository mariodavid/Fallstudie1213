package luposdate.index;

import java.util.Collection;

import lupos.datastructures.items.Item;
import lupos.engine.operators.OperatorIDTuple;
import lupos.engine.operators.index.BasicIndex;
import lupos.engine.operators.index.Dataset;
import lupos.engine.operators.index.IndexCollection;
import lupos.engine.operators.tripleoperator.TriplePattern;

//TODO: Klasse erklären
/**
 * The Class P2PIndexCollection.
 */
public class P2PIndexCollection extends IndexCollection {

	/**
	 * Instantiates a new p2 p index collection.
	 *
	 * @param dataset the dataset
	 */
	public P2PIndexCollection(Dataset dataset) {
		super();
		this.dataset = dataset;
	}

	/**
	 * Wird verwendet, wenn der Operatorgraph aufgebaut wird.
	 *
	 * @param succeedingOperator the succeeding operator
	 * @param triplePattern the triple pattern
	 * @param data the data
	 * @return the basic index
	 */
	@Override
	public BasicIndex newIndex(OperatorIDTuple succeedingOperator,
			Collection<TriplePattern> triplePattern, Item data) {
		return new P2PIndexScan(succeedingOperator, triplePattern, data, this);
	}

	/**
	 * Fabrikmethode zum erstellen einer neuen P2PIndexCollection.
	 *
	 * @param dataset the dataset
	 * @return the index collection
	 */
	@Override
	public IndexCollection newInstance(Dataset dataset) {
		return new P2PIndexCollection(dataset);
	}

}
