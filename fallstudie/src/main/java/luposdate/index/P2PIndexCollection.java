package luposdate.index;

import java.util.Collection;

import lupos.datastructures.items.Item;
import lupos.engine.operators.OperatorIDTuple;
import lupos.engine.operators.index.BasicIndexScan;
import lupos.engine.operators.index.Dataset;
import lupos.engine.operators.index.Root;
import lupos.engine.operators.tripleoperator.TriplePattern;

//TODO: Klasse erklären
/**
 * The Class P2PIndexCollection.
 */
public class P2PIndexCollection extends Root {

	/**
	 * Instantiates a new P2PIndexCollection.
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
	public BasicIndexScan newIndexScan(OperatorIDTuple succeedingOperator,
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
	public Root newInstance(Dataset dataset) {
		return new P2PIndexCollection(dataset);
	}


}
