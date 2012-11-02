package luposdate.operators.formatter;

import lupos.engine.operators.BasicOperator;
import lupos.engine.operators.index.Dataset;
import luposdate.index.P2PIndexCollection;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Implementiert die Formatierung für den IndexCollection Operator.
 */
public class P2PIndexCollectionFormatter implements OperatorFormatter {

	/** The json. */
	private JSONObject	json;

	/** The dataset. */
	private Dataset		dataset;

	/**
	 * Gets the dataset.
	 * 
	 * @return the dataset
	 */
	public Dataset getDataset() {
		return dataset;
	}

	/**
	 * Instantiates a new index collection formatter.
	 * 
	 * @param dataset
	 *            the dataset
	 */
	public P2PIndexCollectionFormatter(Dataset dataset) {
		this.dataset = dataset;
	}

	/**
	 * Instantiates a new index collection formatter.
	 */
	public P2PIndexCollectionFormatter() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * luposdate.operators.formatter.OperatorFormatter#serialize(lupos.engine
	 * .operators.BasicOperator, int)
	 */
	public JSONObject serialize(BasicOperator operator, int node_id)
			throws JSONException {
		json = new JSONObject();

		json.put("type", operator.getClass().getName());
		json.put("node_id", node_id);
		json.put("root", true);

		return json;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * luposdate.operators.formatter.OperatorFormatter#deserialize(org.json.
	 * JSONObject)
	 */
	public BasicOperator deserialize(JSONObject serialiezedOperator)
			throws JSONException {
		json = serialiezedOperator;

		P2PIndexCollection indexCollection = new P2PIndexCollection(dataset);

		return indexCollection;

	}

}
