package luposdate.logicalOptimization;


import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import lupos.datastructures.items.Variable;
import lupos.engine.operators.BasicOperator;
import lupos.engine.operators.OperatorIDTuple;
import lupos.engine.operators.tripleoperator.TriplePattern;
import lupos.optimizations.logical.rules.generated.runtime.Rule;
import luposdate.index.P2PIndexCollection;
import luposdate.index.P2PIndexScan;



// TODO: Auto-generated Javadoc
/**
 * Diese Klasse ist jetzt die logische Optimierung, die durch den Ruleeditor
 * generiert wurde. Hier findet die Transformation der Anfragegraphen statt. Zu
 * diesem Zeitpunkt funktioniert alles zentralistisch, sprich, es werden keine
 * Teilgraphen verschickt.
 * 
 * Somit stellt diese Klasse die Basisimplementierung dar. Diese kann als
 * Referenz dienen für die verschiedenen Optimierungen bei denen Teilgraphen
 * verschickt werden und diese können dann in Benchmarks gegeneinander
 * "antreten"
 * 
 */
public class P2PRuleGlobalJoin extends Rule {

    /** The Op3. */
    private lupos.engine.operators.BasicOperator[] Op3 = null;
    
    /** The Op2. */
	private lupos.engine.operators.index.BasicIndexScan	Op2		= null;
    
    /** The Op1. */
    private lupos.engine.operators.BasicOperator Op1 = null;
    
    /** The _dim_0. */
    private int _dim_0 = -1;

    /**
     * _check private0.
     *
     * @param _op the _op
     * @return true, if successful
     */
    private boolean _checkPrivate0(BasicOperator _op) {
		if (!(_op instanceof lupos.engine.operators.index.BasicIndexScan)) {
            return false;
        }

		this.Op2 = (lupos.engine.operators.index.BasicIndexScan) _op;

        List<BasicOperator> _precedingOperators_1_0 = _op.getPrecedingOperators();


        for(BasicOperator _precOp_1_0 : _precedingOperators_1_0) {
            if(!(_precOp_1_0 instanceof lupos.engine.operators.BasicOperator)) {
                continue;
            }

            this.Op1 = _precOp_1_0;

            List<OperatorIDTuple> _succedingOperators_1_0 = _op.getSucceedingOperators();


            this._dim_0 = -1;
            this.Op3 = new lupos.engine.operators.BasicOperator[_succedingOperators_1_0.size()];

            for(OperatorIDTuple _sucOpIDTup_1_0 : _succedingOperators_1_0) {
                this._dim_0 += 1;

                if(!this._checkPrivate1(_sucOpIDTup_1_0.getOperator())) {
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
     * @param _op the _op
     * @return true, if successful
     */
    private boolean _checkPrivate1(BasicOperator _op) {
        if(!(_op instanceof lupos.engine.operators.BasicOperator)) {
            return false;
        }

        this.Op3[this._dim_0] = _op;

        return true;
    }


    /**
     * Instantiates a new p2 p rule global join.
     */
    public P2PRuleGlobalJoin() {
		this.startOpClass = lupos.engine.operators.index.BasicIndexScan.class;
        this.ruleName = "P2PRule";
    }

    /* (non-Javadoc)
     * @see lupos.optimizations.logical.rules.generated.runtime.Rule#check(lupos.engine.operators.BasicOperator)
     */
    @Override
	protected boolean check(BasicOperator _op) {
       if (this._checkPrivate0(_op)) {
    	   return this.Op2.getTriplePattern().size() > 1;
       }
       return false;
    }

    /* (non-Javadoc)
     * @see lupos.optimizations.logical.rules.generated.runtime.Rule#replace(java.util.HashMap)
     */
    @Override
	protected void replace(HashMap<Class<?>, HashSet<BasicOperator>> _startNodes) {
        // remove obsolete connections...
        int[] _label_a = null;

        int _label_a_count = 0;
        _label_a = new int[this.Op3.length];

        for(lupos.engine.operators.BasicOperator _child : this.Op3) {
            _label_a[_label_a_count] = this.Op2.getOperatorIDTuple(_child).getId();
            _label_a_count += 1;

            this.Op2.removeSucceedingOperator(_child);
            _child.removePrecedingOperator(this.Op2);
        }


        // add new operators...
        lupos.engine.operators.multiinput.join.Join Join1 = null;
        Join1 = new lupos.engine.operators.multiinput.join.Join();
		lupos.engine.operators.index.BasicIndexScan Index1 = null;
        Index1 = new P2PIndexScan((P2PIndexCollection)Op1);


        // add new connections...
        this.Op2.addSucceedingOperator(new OperatorIDTuple(Join1, 0));
        Join1.addPrecedingOperator(this.Op2);
        

        Index1.addSucceedingOperator(new OperatorIDTuple(Join1, 1));
        Join1.addPrecedingOperator(Index1);

        _label_a_count = 0;

        for(lupos.engine.operators.BasicOperator _child : this.Op3) {
            Join1.addSucceedingOperator(new OperatorIDTuple(_child, _label_a[_label_a_count]));
            _child.addPrecedingOperator(Join1);

            _label_a_count += 1;
        }


        this.Op1.addSucceedingOperator(Index1);
        Index1.addPrecedingOperator(this.Op1);


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
        
        
    }
}
