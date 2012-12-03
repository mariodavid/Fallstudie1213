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
import lupos.engine.operators.singleinput.Result;
import luposdate.operators.P2PApplication;

import org.json.JSONException;
import org.json.JSONObject;

// TODO: Auto-generated Javadoc
/**
 * Implementiert die Formatierung für den Result Operator.
 */
public class ResultFormatter implements OperatorFormatter {

	/** The json. */
	private JSONObject json;

	/** The dest_ip. */
	private String dest_ip;

	/** The request_id. */
	private int request_id;

	private P2PApplication p2pApplication;

	/**
	 * Use: public ResultSerializer(String dest_ip, int request_id).
	 */
	public ResultFormatter(P2PApplication p2pApplication) {
		this.p2pApplication = p2pApplication;
		request_id = 0;
	}

	public ResultFormatter() {
	}

	/**
	 * Instantiates a new result formatter.
	 * 
	 * @param dest_ip
	 *            the dest_ip
	 * @param request_id
	 *            the request_id
	 */
	public ResultFormatter(P2PApplication p2pApplication, int request_id) {
		this.p2pApplication = p2pApplication;
		this.request_id = request_id;
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
		Result result = (Result) operator;
		try {
			json.put("type", result.getClass().getName());
			json.put("node_id", node_id);
			// json.put("dest_ip", this.getDestinationIp());
			json.put("request_id", this.request_id);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
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

		Result result = new Result();
		// String ip = json.getString("dest_ip");

		if (this.p2pApplication == null) {
			this.p2pApplication = new P2PApplication();
		}

		result.addApplication(this.p2pApplication);

		return result;

	}
}
