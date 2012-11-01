package luposdate.operators.formatter;

import java.lang.reflect.InvocationTargetException;

import lupos.engine.operators.BasicOperator;
import lupos.engine.operators.index.Dataset;
import luposdate.index.P2PIndexCollection;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Implementiert die Formatierung für den IndexCollection Operator.
 */
public class IndexCollectionFormatter implements OperatorFormatter {

	/** The json. */
	private JSONObject json;

	/** The dataset. */
	private Dataset dataset;

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
	public IndexCollectionFormatter(Dataset dataset) {
		this.dataset = dataset;
	}

	/**
	 * Instantiates a new index collection formatter.
	 */
	public IndexCollectionFormatter() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * luposdate.operators.formatter.OperatorFormatter#serialize(lupos.engine
	 * .operators.BasicOperator, int)
	 */
	public JSONObject serialize(BasicOperator operator, int node_id) {
		json = new JSONObject();

		try {
			json.put("type", operator.getClass().getName());
			json.put("node_id", node_id);
			json.put("root", true);
		} catch (JSONException e) {
			e.printStackTrace();
		}

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
		try {
			String className = (String) json.get("type");

			P2PIndexCollection indexCollection = (P2PIndexCollection) Class
					.forName(className).getConstructor(Dataset.class)
					.newInstance(this.getDataset());

			return indexCollection;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
