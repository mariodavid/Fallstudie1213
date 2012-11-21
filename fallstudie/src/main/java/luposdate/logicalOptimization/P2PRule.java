package luposdate.logicalOptimization;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import lupos.datastructures.items.Variable;
import lupos.engine.operators.BasicOperator;
import lupos.engine.operators.OperatorIDTuple;
import lupos.engine.operators.singleinput.Filter;
import lupos.engine.operators.index.BasicIndexScan;
import lupos.engine.operators.index.Root;
import lupos.engine.operators.singleinput.Result;
import lupos.engine.operators.tripleoperator.TriplePattern;
import lupos.optimizations.logical.rules.generated.runtime.Rule;
import lupos.sparql1_1.ParseException;
import luposdate.index.P2PIndexCollection;
import luposdate.index.P2PIndexScan;
import luposdate.operators.SubGraphContainer;
import p2p.P2PAdapter;

/**
 * Genau wie die Klasse P2PRuleGlobalJoin ist dies hier die logische Optimierung
 * des Anfragegraphen. Bei P2PRuleGlobalJoin war alles noch zentralistisch. Hier
 * sollen nun Optimierungen eingeführt werden, bei denen dann Teilgraphen
 * verschickt werden.
 * 
 * Wir befinden uns in der Kommunikation an der Stelle (1)
 * 
 * Teil Anfragegraph -------- (1) ------------------> (2) ----------- |Sender|
 * |Empfänger| -------- (4) <------------------ (3) ----------- Anfrage Result
 * 
 * Idee von Sven: Man sollte nun eine Klasse erstellen, die von
 * IndexScanOperators erbt (wahrscheinlich die Klasse) und es wird nun eine
 * einzelne IndexScanOperator die hier auftriff durch diese geerbte Klasse
 * ersetzt. Diese Klasse hat jetzt mehrere Operatoren in sich, so dass neben der
 * IndexScanOperator auch ein Result Operator enthält. Dieser Result Operator
 * enthält die P2PApplication klasse als Application. Diese drei Operatoren
 * werden von der von IndexCollection erbenden Klasse verschickt und das
 * Ergebnis wird wieder entgegen genommen.
 */
public class P2PRule extends Rule {

	/** The Op3. */
	private lupos.engine.operators.BasicOperator[]	Op3		= null;
	
	/** The Op2. */
	private lupos.engine.operators.index.BasicIndexScan	Op2		= null;
	
	/** The Op1. */
	private lupos.engine.operators.BasicOperator	Op1		= null;
	
	/** The _dim_0. */
	private int										_dim_0	= -1;


	private final P2PAdapter p2pAdapter;

	/**
	 * _check private0.
	 * 
	 * @param _op
	 *            the _op
	 * @return true, if successful
	 */
	private boolean _checkPrivate0(BasicOperator _op) {
		if (!(_op instanceof lupos.engine.operators.index.BasicIndexScan)) {
			return false;
		}

		this.Op2 = (lupos.engine.operators.index.BasicIndexScan) _op;

		List<BasicOperator> _precedingOperators_1_0 = _op
				.getPrecedingOperators();

		for (BasicOperator _precOp_1_0 : _precedingOperators_1_0) {
			if (!(_precOp_1_0 instanceof lupos.engine.operators.BasicOperator)) {
				continue;
			}

			this.Op1 = _precOp_1_0;

			List<OperatorIDTuple> _succedingOperators_1_0 = _op
					.getSucceedingOperators();

			this._dim_0 = -1;
			this.Op3 = new lupos.engine.operators.BasicOperator[_succedingOperators_1_0
					.size()];

			for (OperatorIDTuple _sucOpIDTup_1_0 : _succedingOperators_1_0) {
				this._dim_0 += 1;

				if (!this._checkPrivate1(_sucOpIDTup_1_0.getOperator())) {
					return false;
				}
			}

			return true;
		}

		return false;
	}

	/**
	 * _check private1.
	 * 
	 * @param _op
	 *            the _op
	 * @return true, if successful
	 */
	private boolean _checkPrivate1(BasicOperator _op) {
		if (!(_op instanceof lupos.engine.operators.BasicOperator)) {
			return false;
		}

		this.Op3[this._dim_0] = _op;

		return true;
	}

	/**
	 * Instantiates a new p2 p rule.
	 */
	public P2PRule(P2PAdapter p2pAdapter) {
		this.startOpClass = lupos.engine.operators.index.BasicIndexScan.class;
		this.ruleName = "P2PRule";
		this.p2pAdapter = p2pAdapter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * lupos.optimizations.logical.rules.generated.runtime.Rule#check(lupos.
	 * engine.operators.BasicOperator)
	 */
	@Override
	protected boolean check(BasicOperator _op) {
		if (this._checkPrivate0(_op)) {
			return this.Op2.getTriplePattern().size() > 1;
			// return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * lupos.optimizations.logical.rules.generated.runtime.Rule#replace(java
	 * .util.HashMap)
	 */
	@Override
	protected void replace(HashMap<Class<?>, HashSet<BasicOperator>> _startNodes) {
		int _label_a_count;
		int[] _label_a = removeObsoleteConnections();

		// add new operators...
		lupos.engine.operators.multiinput.join.Join Join1 = null;
		Join1 = new lupos.engine.operators.multiinput.join.Join();
		lupos.engine.operators.index.BasicIndexScan Index1 = null;
		Index1 = new P2PIndexScan((P2PIndexCollection) Op1);

		addNewConnections(_label_a, Join1, Index1);

		// selbstgeschriebene teil
		// additional replace method code...
		Collection<TriplePattern> tp = Op2.getTriplePattern();
		LinkedList<TriplePattern> tp1 = new LinkedList<TriplePattern>();
		LinkedList<TriplePattern> tp2 = new LinkedList<TriplePattern>();

		boolean first = true;
		for (TriplePattern cur : tp) {
			if (first) {
				tp1.add(cur);
				first = false;
			} else {
				tp2.add(cur);
			}
		}

		this.Op2.setTriplePatterns(tp1);
		Index1.setTriplePatterns(tp2);
		Index1.setGraphConstraint(this.Op2.getGraphConstraint());
		Index1.recomputeVariables();
		this.Op2.recomputeVariables();

		HashSet<Variable> hs = new HashSet<Variable>();
		hs.addAll(this.Op2.getUnionVariables());
		hs.addAll(Index1.getUnionVariables());
		Join1.setUnionVariables(hs);
		HashSet<Variable> hs2 = new HashSet<Variable>();
		hs2.addAll(this.Op2.getUnionVariables());
		hs2.retainAll(Index1.getUnionVariables());
		Join1.setIntersectionVariables(hs2);

		// hier wird der normale index scan operator (p2pIndexScan) durch einen
		// SubGraphContainer ersetzt
		replaceIndexScanOperatorWithSubGraphContainer(this.Op2);
		// falls Index1 nur noch ein Triple Pattern enthaelt, muss dieser
		// bereits jetzt ersetzt werden
		if (tp2.size() == 1) {
			replaceIndexScanOperatorWithSubGraphContainer(Index1);
		}

	}

	/**
	 * hier wird der normale index scan operator (p2pIndexScan) durch einen
	 * SubGraphContainer ersetzt.
	 * 
	 * @param indexScan
	 *            the index scan
	 */
	private void replaceIndexScanOperatorWithSubGraphContainer(
			BasicIndexScan indexScan) {

		Root rootNodeOfOuterGraph = indexScan.getRoot();
		Root rootNodeOfSubGraph = rootNodeOfOuterGraph
				.newInstance(rootNodeOfOuterGraph.dataset);

		SubGraphContainer container = new SubGraphContainer(p2pAdapter,
				rootNodeOfOuterGraph, rootNodeOfSubGraph);

		HashSet<Variable> variables = new HashSet<Variable>(
				indexScan.getIntersectionVariables());

		container.setHashableTriplePatterns(indexScan.getTriplePattern());

		container.setUnionVariables(variables);
		container.setIntersectionVariables(variables);
		// container.setTriplePatterns(new LinkedList<TriplePattern>());

		// vorgaenger und nachfolger des urspruenglichen indexScan OPs merken
		// und am schluss wieder an den neuen graphen ranhaengen
		Collection<BasicOperator> preds = indexScan.getPrecedingOperators();
		List<OperatorIDTuple> succs = indexScan.getSucceedingOperators();

		// alle vorgaenger werden durchlaufen und der neue Nachfolger gesetzt
		for (BasicOperator pred : preds) {
			pred.getOperatorIDTuple(indexScan).setOperator(container);
		}

		// neue Verbindungen werden erzeugt

		// TODO: es wird vermutlich kein Filter OP gefunden (als nachfolger von
		// indeScan, weil durch die Methode addNewConnections der Join als
		// nachfolger des IndexScan definiert wurde
		// hier muss noch die naechste ebene betrachtet werden, damit der filter gefunden wird
		if (indexScan.getSucceedingOperators().size() == 1) {
			for (OperatorIDTuple opId : indexScan.getSucceedingOperators()) {
				if (opId.getOperator() instanceof Filter) {
					Filter filter = (Filter) opId.getOperator();
					if (indexScan.getUnionVariables().containsAll(
							filter.getUsedVariables())) {
						Filter newFilter;
						try {
							newFilter = new Filter(filter.toString());
							indexScan
									.setSucceedingOperator(new OperatorIDTuple(
											newFilter, 0));
							newFilter
									.setSucceedingOperator(new OperatorIDTuple(
											new Result(), 0));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} else {
						indexScan.setSucceedingOperator(new OperatorIDTuple(
								new Result(), 0));
					}
				}
			}
		} else {
			indexScan
					.setSucceedingOperator(new OperatorIDTuple(new Result(), 0));
		}

		indexScan.setSucceedingOperator(new OperatorIDTuple(new Result(), 0));
		rootNodeOfSubGraph.setSucceedingOperator(new OperatorIDTuple(indexScan,
				0));

		// rueckrichtung geschieht automatisch
		rootNodeOfSubGraph.setParents();

		// urspruengliche Nachfolger werden an neuen Graphen angehangen
		container.setSucceedingOperators(succs);

		// der neue Vorgaenger von den Nachfolgern von dem urspruenglichen
		// IndexScan werden durchlaufen und der neue container wird festgelegt
		for (OperatorIDTuple succ : succs) {
			succ.getOperator().removePrecedingOperator(indexScan);
			succ.getOperator().addPrecedingOperator(container);
		}

	}

	/**
	 * Adds the new connections.
	 * 
	 * @param _label_a
	 *            the _label_a
	 * @param Join1
	 *            the join1
	 * @param Index1
	 *            the index1
	 */
	private void addNewConnections(int[] _label_a,
			lupos.engine.operators.multiinput.join.Join Join1,
			lupos.engine.operators.index.BasicIndexScan Index1) {
		int _label_a_count;
		// add new connections...
		this.Op2.addSucceedingOperator(new OperatorIDTuple(Join1, 0));
		Join1.addPrecedingOperator(this.Op2);

		Index1.addSucceedingOperator(new OperatorIDTuple(Join1, 1));
		Join1.addPrecedingOperator(Index1);

		_label_a_count = 0;

		for (lupos.engine.operators.BasicOperator _child : this.Op3) {
			Join1.addSucceedingOperator(new OperatorIDTuple(_child,
					_label_a[_label_a_count]));
			_child.addPrecedingOperator(Join1);

			_label_a_count += 1;
		}

		this.Op1.addSucceedingOperator(Index1);
		Index1.addPrecedingOperator(this.Op1);
	}

	/**
	 * Removes the obsolete connections.
	 * 
	 * @return the int[]
	 */
	private int[] removeObsoleteConnections() {
		// remove obsolete connections...
		int[] _label_a = null;

		int _label_a_count = 0;
		_label_a = new int[this.Op3.length];

		for (lupos.engine.operators.BasicOperator _child : this.Op3) {
			_label_a[_label_a_count] = this.Op2.getOperatorIDTuple(_child)
					.getId();
			_label_a_count += 1;

			this.Op2.removeSucceedingOperator(_child);
			_child.removePrecedingOperator(this.Op2);
		}
		return _label_a;
	}
}
