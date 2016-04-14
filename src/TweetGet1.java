

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.List;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.alchemyapi.api.AlchemyAPI;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;

/**
 * <p>This is a code example of Twitter4J Streaming API - sample method support.<br>
 * Usage: java twitter4j.examples.PrintSampleStream<br>
 * </p>
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class TweetGet1 {
	static AmazonSQS sqs;
	static String myQueueUrl;
	public static void createSQS(){
		AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (~/.aws/credentials), and is in valid format.",
                    e);
        }

        sqs = new AmazonSQSClient(credentials);
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        sqs.setRegion(usWest2);

        System.out.println("===========================================");
        System.out.println("Getting Started with Amazon SQS");
        System.out.println("===========================================\n");

        try {
            // Create a queue
            System.out.println("Creating a new SQS queue called MyQueue.\n");
            CreateQueueRequest createQueueRequest = new CreateQueueRequest("MyQueue");
            myQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();
         
            // List queues
            System.out.println("Listing all queues in your account.\n");
            for (String queueUrl : sqs.listQueues().getQueueUrls()) {
                System.out.println("  QueueUrl: " + queueUrl);
            }
            System.out.println();
            
            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(myQueueUrl);
            List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
            //String messageRecieptHandle = messages.get(0).getReceiptHandle();
            //sqs.deleteMessage(new DeleteMessageRequest(myQueueUrl, messageRecieptHandle));
            //sqs.deleteQueue(new DeleteQueueRequest(myQueueUrl));
        	} catch (AmazonServiceException ase) {
        		System.out.println("Caught an AmazonServiceException, which means your request made it " +
                    "to Amazon SQS, but was rejected with an error response for some reason.");
        		System.out.println("Error Message:    " + ase.getMessage());
        		System.out.println("HTTP Status Code: " + ase.getStatusCode());
        		System.out.println("AWS Error Code:   " + ase.getErrorCode());
        		System.out.println("Error Type:       " + ase.getErrorType());
        		System.out.println("Request ID:       " + ase.getRequestId());
        	} catch (AmazonClientException ace) {
        		System.out.println("Caught an AmazonClientException, which means the client encountered " +
                    "a serious internal problem while trying to communicate with SQS, such as not " +
                    "being able to access the network.");
        		System.out.println("Error Message: " + ace.getMessage());
        	}

	}
    
	//private static Connection con;
	private static Statement stmt;
	
	public static void connectDB() throws SQLException, ClassNotFoundException {
		 
		//for connectiong to RDS
    	Class.forName("com.mysql.jdbc.Driver");
    	Connection con = DriverManager.getConnection(
    			"jdbc:mysql://tweets.cmhwhoq4ms5s.us-east-1.rds.amazonaws.com:3306/Tweet",
    			"somdeep", "somdeep10"
    			);
    	ResultSet rs = con.getMetaData().getCatalogs();

    	while (rs.next()) {
    	    System.out.println("TABLE_CAT = " + rs.getString("TABLE_CAT") );
    	}
    	
    	 System.out.println("Creating table in given database...");
         stmt = con.createStatement();

         

		String sql = "SHOW TABLES LIKE 'tweet1'";
		if (stmt.executeUpdate(sql)==0){

          sql = "CREATE TABLE tweet1" +
				  "(id	INTEGER NOT NULL auto_increment," +
				  "userName varchar(255) NOT NULL," +
				  "longitude DECIMAL(15,10)," +
				  "latitude DECIMAL(15,10)," +
				  "place varchar(255)," +
				  "source varchar(255)," + 
				  "text varchar(255)," +
				  "PRIMARY KEY (id))";
         
       
         
         
         
         
         stmt.executeUpdate(sql);
		}
         //sql="insert into tweet1(userName,longitude,latitude,place,source,text) values (\"first\",10,15,\"work\",\"Somdeep\",\"Somdeep\")";
         
         //stmt.executeUpdate(sql);
         //System.out.println("Created table in given database...");
      }
	
    		
	
	public static void insertRecord (String userName, Double lon, Double lat,String place,String source, String text) throws SQLException{	
		String sql = "INSERT INTO trend1 (userName, longitude, latitude, place,source,text) "+
  			  "VALUES (\"" + userName + "\", \"" + lon + "\", \"" + lat + "\", \""+ place + "\", \"" + source + "\", \"" + text + "\")";
		//System.out.println(sql);
  		stmt.executeUpdate(sql);
  		sqs.sendMessage(new SendMessageRequest(myQueueUrl, sql));
		
	}
	
	
    public static void main(String[] args) throws Exception {
    	

    	AlchemyAPI alchemyObj = AlchemyAPI.GetInstanceFromFile("src/api_key.txt");
    	Document doc = alchemyObj.TextGetTextSentiment("Just posted a photo @ Tandil, Argentina");
	NodeList part=doc.getElementsByTagName("score");
	  org.w3c.dom.Element el =(org.w3c.dom.Element)part.item(0);
	  //System.out.println(getStringFromDocument(doc));
	 Double	score=Double.parseDouble(el.getTextContent());
	 System.out.println("Sentiment score " + score.toString());
		
		
    }
    
}