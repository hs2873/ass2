import java.util.List;
import java.util.Map.Entry;

//import com.alchemyapi.*;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

//import com.alchemyapi.test.*;

import com.alchemyapi.api.AlchemyAPI;
//import com.alchemyapi.api.*;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
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

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.DeleteTopicRequest;
import com.amazonaws.services.sns.model.MessageAttributeValue;


public class Worker extends Thread{
	public static AmazonSQS sqs;
	public static String myQueueUrl; 
	public static AmazonSNSClient snsClient;
	public static Connection con;
	
	public static void connectSQS(){
		if (sqs != null) return;
		AWSCredentials credentials = null;
        try {
           // credentials = new ProfileCredentialsProvider().getCredentials();
        	credentials = new BasicAWSCredentials("AKIAI24RX73LAS7434PA", "wASIT76m4fBPpjBbbjkX/3ZQQ6Z/PCyzwXqd/qxx");
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (~/.aws/credentials), and is in valid format.",
                    e);
        }

        sqs = new AmazonSQSClient(credentials);
        Region usEast1 = Region.getRegion(Regions.US_EAST_1);
        sqs.setRegion(usEast1);

        System.out.println("===========================================");
        System.out.println("Getting Connected to Amazon SQS");
        System.out.println("===========================================\n");
        
		
	}
	
	public static void connectSNS(){
		if (snsClient != null) return;
		snsClient = new AmazonSNSClient(new ClasspathPropertiesFileCredentialsProvider());		                           
		snsClient.setRegion(Region.getRegion(Regions.US_EAST_1));
		//create a new SNS topic
		//CreateTopicRequest createTopicRequest = new CreateTopicRequest("MyNewTopic1");
		//CreateTopicResult createTopicResult = snsClient.createTopic(createTopicRequest);
		//print TopicArn and SNS meta-data
		//System.out.println(createTopicResult);		
		//System.out.println("CreateTopicRequest - " + snsClient.getCachedResponseMetadata(createTopicRequest));
		//String topicArn = (""+createTopicResult).replace("{TopicArn: ", "").replace("}", "");
		//System.out.println(topicArn);
		//subscribe to an SNS topic
		//SubscribeRequest subRequest = new SubscribeRequest(topicArn, "http", 
				//"http://7fea5817.ngrok.io/snstry/Transcoder");
		//snsClient.subscribe(subRequest);
		
		//publish to an SNS topic
		//int i=0;
		//while( i<20)
		//{
		
		//String msg = "My text published to SNS topic with Somdeep Dey";
		//PublishRequest publishRequest = new PublishRequest(topicArn, msg,"work");
		//MessageAttributeValue m  = new MessageAttributeValue();
		//m.setStringValue("Notification");
		//publishRequest.addMessageAttributesEntry("type",m );
	//	PublishResult publishResult = snsClient.publish(publishRequest);
		//i++;
		//}
	}
	
	
	
	public static void connectDb()
	{
		if (con != null) return;
		
		try
		{
		Class.forName("com.mysql.jdbc.Driver");
    		con = DriverManager.getConnection(
    			"jdbc:mysql://tweets.cmhwhoq4ms5s.us-east-1.rds.amazonaws.com:3306/Tweet",
    			"somdeep", "somdeep10"
    			);
    	
    
    	
		}
		
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	public void run(){
		//myQueueUrl = "https://sqs.us-west-2.amazonaws.com/286242719761/MyQueue";
		//String topicArn="arn:aws:sns:us-east-1:286242719761:MyTopic";
		myQueueUrl = "https://sqs.us-east-1.amazonaws.com/575052755988/lol";
		String topicArn="arn:aws:sns:us-east-1:575052755988:MyNewTopic1";
		
		connectSQS();
		connectSNS();
		connectDb();
		String msg;
		int index;
		String latitude;
		String longitude;
		String text;
		Double score = 0.0;
		int id;
		String val;
		while (true){
			try {
                
	            // Receive messages
	            System.out.println("Polling messages from MyQueue.\n");
	            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(myQueueUrl);
	            List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
	            if (messages.isEmpty()) {
	            	Thread.sleep(5000);
	            	continue;
	            }
	            for (Message message : messages) {
	            	System.out.println("processed by " + Thread.currentThread().getId());
	                System.out.println("  Message");
	                System.out.println("    MessageId:     " + message.getMessageId());
	              //  System.out.println("    ReceiptHandle: " + message.getReceiptHandle());
	                //System.out.println("    MD5OfBody:     " + message.getMD5OfBody());
	                msg=message.getBody().toString();
	                System.out.println("    Body:          " + msg);
	                for (Entry<String, String> entry : message.getAttributes().entrySet()) {
	                    System.out.println("  Attribute");
	                    System.out.println("    Name:  " + entry.getKey());
	                    System.out.println("    Value: " + entry.getValue());
	                }
	                index=msg.indexOf(',');
	                val=msg.substring(0, index);
	                id=Integer.parseInt(val);
	                System.out.println("Id is : " + id);
	                
	                msg=msg.substring(index+1);
	                index=msg.indexOf(',');
	                latitude=msg.substring(0, index);
	                System.out.println("Latitude  is : " + latitude);
	               
	                msg=msg.substring(index+1);
	                index=msg.indexOf(',');
	                longitude=msg.substring(0, index);
	                System.out.println("Longitude  is : " + longitude);
	                
	                msg=msg.substring(index+1);
	                //index=msg.indexOf(',');
	                text=msg;
	                System.out.println("Text  is : " + text);
	                ResultSet rs=null;
	                try
	                {
	                	AlchemyAPI alchemyObj =AlchemyAPI.GetInstanceFromString("85a6d521ec4d6aebaf004d6be93a5dfe5c80278b");
	                			
	                			//AlchemyAPI.GetInstanceFromFile("src/api_key.txt");
	                	
	                	Document doc = alchemyObj.TextGetTextSentiment(text);
            		NodeList part=doc.getElementsByTagName("score");
           		  org.w3c.dom.Element el =(org.w3c.dom.Element)part.item(0);
           		  //System.out.println(getStringFromDocument(doc));
           		 	score=Double.parseDouble(el.getTextContent());
           		 System.out.println("Sentiment score " + score);
           		 
           		 
           		 Statement stmt;
           		stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
 	                   ResultSet.CONCUR_UPDATABLE);
 	    	String sql = "select * from tweet1 where id = " + id;
 	    	
 	    	
 	    	rs = stmt.executeQuery(sql);
 	    	rs.next();
 	    	rs.updateDouble("score",score);
       		rs.updateRow();
	                }
	               catch(Exception e)
	                {
	            	   System.out.println(e);
	            	   score=0.0;
	                }           	
	                
	                msg=latitude.toString()+","+longitude.toString()+","+score.toString();
           		 
	                PublishRequest publishRequest = new PublishRequest(topicArn, msg);
	            	PublishResult publishResult = snsClient.publish(publishRequest);	
	                
	            }
	            

	            
	            
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
	        } catch (Exception e){
	        	e.printStackTrace();
	        }
			
		}
		
		
	}
}
