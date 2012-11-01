package operators.formatter;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import lupos.engine.operators.BasicOperator;
import lupos.gui.Demo_Applet;
import lupos.gui.GUI;
import lupos.gui.operatorgraph.graphwrapper.GraphWrapperBasicOperator;
import lupos.gui.operatorgraph.viewer.Viewer;
import luposdate.operators.formatter.SubGraphContainerFormatter;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import xpref.XPref;

public class SubGraphFormatterTest {


	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void testDeserialize() throws XPathExpressionException,
			SecurityException, IllegalArgumentException, MalformedURLException,
			IOException, SAXException, ParserConfigurationException,
			ClassNotFoundException, NoSuchFieldException,
			NoSuchMethodException, IllegalClassFormatException,
			IllegalAccessException, InvocationTargetException {

		String serializedGraph = "{\"edges\":[{\"to\":2,\"from\":1},{\"to\":3,\"from\":2}],\"nodes\":[{\"root\":true,\"type\":\"luposdate.index.P2PIndexCollection\",\"node_id\":1},{\"type\":\"luposdate.index.P2PIndexScan\",\"triple_pattern\":[{\"items\":[{\"name\":\"s\",\"type\":\"variable\"},{\"value\":\"<p>\",\"type\":\"literal\"},{\"name\":\"o\",\"type\":\"variable\"}]}],\"node_id\":2},{\"request_id\":0,\"dest_ip\":\"0.0.0.0\",\"type\":\"lupos.engine.operators.singleinput.Result\",\"node_id\":3}]}";

		try {
			BasicOperator root = SubGraphContainerFormatter.deserialize(
serializedGraph);

			System.out.println(root);

			try {
				XPref.getInstance(Demo_Applet.class
						.getResource("/preferencesMenu.xml"));
			} catch (Exception e) {
				XPref.getInstance(new URL("file:"
						+ GUI.class.getResource("/preferencesMenu.xml")
								.getFile()));
			}
			new Viewer(new GraphWrapperBasicOperator(root), "test", true, false);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			fail();
		}

	}

}
