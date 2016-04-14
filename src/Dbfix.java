import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.alchemyapi.*;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.alchemyapi.test.*;

import com.alchemyapi.api.AlchemyAPI;
import com.alchemyapi.api.*;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
public class Dbfix {

	private static Statement stmt;
		public static void main(String[] args)throws SQLException, ClassNotFoundException, Exception
		{
			Dbfix db=new Dbfix();
			
			 
			System.out.println("Somdeep Dey");
			try
			{
			Class.forName("com.mysql.jdbc.Driver");
	    	Connection con = DriverManager.getConnection(
	    			"jdbc:mysql://tweets.cmhwhoq4ms5s.us-east-1.rds.amazonaws.com:3306/Tweet",
	    			"somdeep", "somdeep10"
	    			);
	    	ResultSet rs = con.getMetaData().getCatalogs();

	    	while (rs.next()) {
	    	    System.out.println("TABLE_CAT = " + rs.getString("TABLE_CAT") );
	    	}
	    	stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
	                   ResultSet.CONCUR_UPDATABLE);
	    	String sql = "select * from trend1";
	    	//stmt.executeUpdate(sql);
	    	
	    	 rs = stmt.executeQuery(sql);
	    	 while(rs.next()){
					db.processRow(rs);
					
				}
				rs.close();
		
			}
			
			catch(Exception e)
			{
				System.out.println(e);
			}
		}
		
		public void processRow(ResultSet rs) throws Exception{
			String s = "";
			String sentiment;
			double score;
			//if (session != null && session.isOpen()){
				//s += rs.getString("latitude")+","+rs.getString("longitude")+",";
				//s += rs.getString("place")+","+rs.getString("source");
				//session.getBasicRemote().sendText(s);
			//}
			//else {
			try
			{
			 AlchemyAPI alchemyObj = AlchemyAPI.GetInstanceFromFile("src/api_key.txt");
				s +=  rs.getString("id") + ") ";
				//s += "lat: "+rs.getString("latitude")+", ";
				//s += "lng: "+rs.getString("longitude")+", ";
				//s += "app: "+rs.getString("source")+", ";
				//s += "ctr: "+rs.getString("place")+", ";
				s += "text: "+rs.getString("text");
				score=rs.getDouble("score");
				System.out.println("Score is : " + score);
				Document doc = alchemyObj.TextGetTextSentiment(rs.getString("text"));
        		NodeList part=doc.getElementsByTagName("score");
       		 org.w3c.dom.Element el =(org.w3c.dom.Element)part.item(0);
       		 score=Double.parseDouble(el.getTextContent());
       		// System.out.println(getStringFromDocument(doc));
       		System.out.println("Sentiment from alchemy :  " + score);
       		part=doc.getElementsByTagName("type");
       		el=(org.w3c.dom.Element)part.item(0);
      		 sentiment=el.getTextContent();
       		
      		System.out.println(sentiment);
      		rs.updateDouble("score",score);
      			rs.updateString("sentiment",sentiment);
       		rs.updateRow();
			}
			catch(Exception e)
			{
				System.out.println(e);
				System.out.println("Error - neutral");
				rs.updateDouble("score",0);
	      		rs.updateString("sentiment","neutral");
	       		rs.updateRow();
				return;
			}
       		 
			}
		private static String getStringFromDocument(Document doc) 
		{
	        try {
	            DOMSource domSource = new DOMSource(doc);
	            StringWriter writer = new StringWriter();
	            StreamResult result = new StreamResult(writer);

	            TransformerFactory tf = TransformerFactory.newInstance();
	            Transformer transformer = tf.newTransformer();
	            transformer.transform(domSource, result);

	            return writer.toString();
	        } catch (TransformerException ex) {
	            ex.printStackTrace();
	            return null;
	        }
	    }	

		}
