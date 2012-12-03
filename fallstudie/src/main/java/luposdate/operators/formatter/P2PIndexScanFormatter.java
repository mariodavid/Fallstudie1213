/**
 * Copyright (c) 2012, Institute of Telematics, Institute of Information Systems (Dennis Pfisterer, Sven Groppe, Andreas Haller, Thomas Kiencke, Sebastian Walther, Mario David), University of Luebeck
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 	- Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * 	  disclaimer.
 * 	- Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * 	  following disclaimer in the documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the University of Luebeck nor the names of its contributors may be used to endorse or promote
 * 	  products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package luposdate.operators.formatter;

import java.util.Collection;
import java.util.LinkedList;

import lupos.datastructures.items.Item;
import lupos.datastructures.items.Variable;
import lupos.datastructures.items.literal.LazyLiteral;
import lupos.engine.operators.BasicOperator;
import lupos.engine.operators.index.BasicIndexScan;
import lupos.engine.operators.index.Root;
import lupos.engine.operators.tripleoperator.TriplePattern;
import luposdate.index.P2PIndexScan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// TODO: Auto-generated Javadoc
/**
 * Implementiert die Formatierung für den IndexScan Operator.
 */
public class P2PIndexScanFormatter implements OperatorFormatter {

	/** The json. */
	private JSONObject		json;

	/** The index collection. */
	private Root		root;


	/**
	 * Instantiates a new index scan formatter.
	 */
	public P2PIndexScanFormatter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new index scan formatter.
	 * 
	 * @param indexCollection
	 *            the index collection
	 */
	public P2PIndexScanFormatter(Root indexCollection) {
		this.setRoot(indexCollection);
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

		BasicIndexScan indexScan = (BasicIndexScan) operator;
		try {
			json.put("type", operator.getClass().getName());
			json.put("node_id", node_id);

			Collection<JSONObject> triplePatterns = new LinkedList<JSONObject>();

			for (TriplePattern triplePattern : indexScan.getTriplePattern()) {
				JSONObject tripleJson = new JSONObject();
				Collection<JSONObject> tripleItems = createTriplePatternItemsArray(triplePattern);

				tripleJson.put("items", tripleItems);

				triplePatterns.add(tripleJson);
			}

			json.put("triple_pattern", triplePatterns);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json;
	}

	/**
	 * Creates the triple pattern items array.
	 * 
	 * @param triplePattern
	 *            the triple pattern
	 * @return the collection
	 * @throws JSONException
	 *             the jSON exception
	 */
	private Collection<JSONObject> createTriplePatternItemsArray(
			TriplePattern triplePattern) throws JSONException {
		Collection<JSONObject> tripleItems = new LinkedList<JSONObject>();

		for (Item item : triplePattern.getItems()) {
			JSONObject itemJson = createTriplePatternItemAsJsonString(item);
			tripleItems.add(itemJson);
		}
		return tripleItems;
	}

	/**
	 * Creates the triple pattern item as json string.
	 * 
	 * @param item
	 *            the item
	 * @return the jSON object
	 * @throws JSONException
	 *             the jSON exception
	 */
	private JSONObject createTriplePatternItemAsJsonString(Item item)
			throws JSONException {
		JSONObject itemJson = new JSONObject();

		if (item.isVariable()) {
			itemJson.put("type", "variable");
			itemJson.put("name", item.getName());
		} else {
			itemJson.put("type", "literal");
			itemJson.put("value", item.getName());
			// item.getName().substring(1, item.getName().length() - 1));
		}
		return itemJson;
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

		P2PIndexScan indexScan = new P2PIndexScan(root);

		Collection<TriplePattern> triplePatterns = createTriplePatternsListFromJSON(json);
		indexScan.setTriplePatterns(triplePatterns);

		return indexScan;

	}

	/**
	 * Creates the triple patterns list from json.
	 * 
	 * @param json
	 *            the json
	 * @return the collection
	 */
	private Collection<TriplePattern> createTriplePatternsListFromJSON(
			JSONObject json) {

		Collection<TriplePattern> result = new LinkedList<TriplePattern>();

		try {
			JSONArray triplePatternsJson = (JSONArray) json
					.get("triple_pattern");

			for (int i = 0; i < triplePatternsJson.length(); i++) {

				JSONObject triplePatternJson = triplePatternsJson
						.getJSONObject(i);

				JSONArray itemsJson = (JSONArray) triplePatternJson
						.get("items");

				Item[] items = new Item[3];

				for (int h = 0; h < 3; h++) {
					JSONObject itemJson = itemsJson.getJSONObject(h);

					if (itemJson.getString("type").equals("variable")) {
						items[h] = new Variable(itemJson.getString("name"));
					} else {
						items[h] = LazyLiteral.getLiteral(itemJson
								.getString("value"));

					}
				}

				TriplePattern triplePattern = new TriplePattern(items[0],
						items[1], items[2]);
				result.add(triplePattern);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Gets the index collection.
	 * 
	 * @return the index collection
	 */
	private Root getRoot() {
		return root;
	}

	/**
	 * Sets the index collection.
	 * 
	 * @param root
	 *            the new index collection
	 */
	public void setRoot(Root root) {
		this.root = root;
	}

}
