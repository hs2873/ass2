import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import com.alchemyapi.api.AlchemyAPI;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

@ServerEndpoint("/Server")
public class Server {
	Twitter twitter;
	static int count;
	
	@OnOpen
    public void onOpen(Session session){
        System.out.println(session.getId() + " has opened a connection"); 
        ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		 .setOAuthConsumerKey("hqZmOvjx4smEjOFJyMoa5BJdY")
	     .setOAuthConsumerSecret("1PS08ZtqWBs5iseLjx7o6dyek2NZMxipsidsfSEf5mm9hOWcpS")
	     .setOAuthAccessToken("619422408-kgxwOm6OtViITnp4A9jr08WY9FIsZmak2ATlv5XU")
	     .setOAuthAccessTokenSecret("gQOHSNiagqkoeGkadtuONVSKwiJZPCWKdzxBd2SBtpGK1"); 
		 
		
		if(count<3)
		{
			System.out.println("Creating new workers");
			for(int i=0;i<3;i++)
			new Worker().start();
		
			count=3;
		}
		
	twitter = new TwitterFactory(cb.build()).getInstance();
        new DBReader(session).start();
    }
 
    
    @OnMessage
    public void onMessage(String message, Session session){
        System.out.println("Message from " + session.getId() + ": " + message);
        String place = message.trim().replace(" ", "%20");
        String response = "Trending topics \n";
        Document doc;
		String _appid = "WkarP8vV34Gw0TWqCQ3bGW_wkExS8yoVB9o2nelJJwOrUnMORtrVTNKSOj_rO0ojhAIX94UZwqu7ZwMKij7z.A--";
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
		try {
			int woeid;
			db = dbf.newDocumentBuilder();
			if(place.equals(""))
				woeid=1;
				else
				{
			doc = db.parse(new URL("http://where.yahooapis.com/v1/places.q('"+place+"')?appid="+_appid).openStream());
	        NodeList part=doc.getElementsByTagName("woeid");
	  		org.w3c.dom.Element el =(org.w3c.dom.Element)part.item(0);
	  		
				 woeid = Integer.parseInt(el.getTextContent());
				 System.out.println("Woeid  = " + woeid);
	  		}
				Trends trends = twitter.getPlaceTrends(woeid);	    
			for (int i = 1; i < trends.getTrends().length; i++) {
				 response += trends.getTrends()[i].getName() + "\n";
				 System.out.println(response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			
			response += "can't get trends";
		} 
		System.out.println(response);
        try {
            session.getBasicRemote().sendText(response);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
 
    
    @OnClose
    public void onClose(Session session){
        System.out.println("Session " +session.getId()+" has ended");
    }
}
