package index;

import java.util.Collection;

import lupos.datastructures.items.Item;
import lupos.engine.operators.OperatorIDTuple;
import lupos.engine.operators.index.BasicIndex;
import lupos.engine.operators.index.Dataset;
import lupos.engine.operators.index.IndexCollection;
import lupos.engine.operators.tripleoperator.TriplePattern;

public class P2PIndexCollection extends IndexCollection {

	public P2PIndexCollection(Dataset dataset) {
		super();
		this.dataset = dataset;
	}

	/**
	 * wird verwendet, wenn der Operatorgraph aufgebaut wird
	 */
	@Override
	public BasicIndex newIndex(OperatorIDTuple succeedingOperator,
			Collection<TriplePattern> triplePattern, Item data) {
		return new P2PIndexScan(succeedingOperator, triplePattern, data, this);
	}

	/**
	 * Fabrikmethode zum erstellen einer neuen P2PIndexCollection
	 */
	@Override
	public IndexCollection newInstance(Dataset dataset) {
		return new P2PIndexCollection(dataset);
	}

}
