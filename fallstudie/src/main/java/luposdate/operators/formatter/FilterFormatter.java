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

import lupos.engine.operators.BasicOperator;
import lupos.engine.operators.singleinput.Filter;
import lupos.sparql1_1.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Implementiert die Formatierung für den IndexCollection Operator.
 */
public class FilterFormatter implements OperatorFormatter {

	/** The json. */
	private JSONObject	json;

	/**
	 * Instantiates a new index collection formatter.
	 */
	public FilterFormatter() {
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
		json.put("expression", operator.toString());

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

		Filter filter;
		String filtername =  json.getString("expression");
		filtername = filtername.substring(0,filtername.length()-3);
		try {
			filter = new Filter(filtername);
			return filter;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;

	}

}
