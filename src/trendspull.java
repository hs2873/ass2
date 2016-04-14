import java.io.*;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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

public class trendspull {


	public static void main(String[]  args)throws Exception
	{
		
		Document doc;
		String place = "new%20york";
		String response="";
		String _appid = "WkarP8vV34Gw0TWqCQ3bGW_wkExS8yoVB9o2nelJJwOrUnMORtrVTNKSOj_rO0ojhAIX94UZwqu7ZwMKij7z.A--";
		int woeid;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        doc = db.parse(new URL("http://where.yahooapis.com/v1/places.q('"+place+"')?appid="+_appid).openStream());
        NodeList part=doc.getElementsByTagName("woeid");
  		 org.w3c.dom.Element el =(org.w3c.dom.Element)part.item(0);
  		  //System.out.println(getStringFromDocument(doc));
  		 woeid = Integer.parseInt(el.getTextContent());
  		 System.out.println("woeid is : " + woeid);
        
        
            

        

        
        

        
        
        
		  ConfigurationBuilder cb = new ConfigurationBuilder();
		 cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("hqZmOvjx4smEjOFJyMoa5BJdY")
	         .setOAuthConsumerSecret("1PS08ZtqWBs5iseLjx7o6dyek2NZMxipsidsfSEf5mm9hOWcpS")
	         .setOAuthAccessToken("619422408-kgxwOm6OtViITnp4A9jr08WY9FIsZmak2ATlv5XU")
	         .setOAuthAccessTokenSecret("gQOHSNiagqkoeGkadtuONVSKwiJZPCWKdzxBd2SBtpGK1");
		 
		 
		 Twitter twitter = new TwitterFactory().getInstance();
		 
		 Trends trends = twitter.getPlaceTrends(2347589);	             //set the WOEID here, 1 is for global
		 
		 
		 for (int i = 0; i < trends.getTrends().length; i++) {
			    System.out.println(trends.getTrends()[i].getName());
			}
		 

		
	}
	
}