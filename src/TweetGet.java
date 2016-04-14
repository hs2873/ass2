

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
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;


public class TweetGet {
	static AmazonSQS sqs;
	static String myQueueUrl;
	static int counter;
	
	public static void createSQS(){
		AWSCredentials credentials = null;
        try {
            //credentials = new ProfileCredentialsProvider("default").getCredentials();
        	credentials = new BasicAWSCredentials("AKIAI24RX73LAS7434PA", "wASIT76m4fBPpjBbbjkX/3ZQQ6Z/PCyzwXqd/qxx");
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (~/.aws/credentials), and is in valid format.",
                    e);
        }

        sqs = new AmazonSQSClient(credentials);
        Region useast1 = Region.getRegion(Regions.US_EAST_1);
        sqs.setRegion(useast1);

        System.out.println("===========================================");
        System.out.println("Getting Started with Amazon SQS");
        System.out.println("===========================================\n");

        try {
            // Create a queue
           // System.out.println("Creating a new SQS queue called MyQueue.\n");
          //  CreateQueueRequest createQueueRequest = new CreateQueueRequest("MyQueue");
     //      myQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();
         myQueueUrl="https://sqs.us-east-1.amazonaws.com/575052755988/lol";


            // List queues
            System.out.println("Listing all queues in your account.\n");
            for (String queueUrl : sqs.listQueues().getQueueUrls()) {
                System.out.println("  QueueUrl: " + queueUrl);
            }
            System.out.println();
            
     
            
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
         counter = 1;
		}
		else {
			sql = "SELECT MAX(id) AS id FROM tweet1";
			ResultSet result = stmt.executeQuery(sql);
			while (result.next()){
				counter = Integer.parseInt(result.getString("id"))+1;
				System.out.println(counter);
			}
			
		}
         //sql="insert into tweet1(userName,longitude,latitude,place,source,text) values (\"first\",10,15,\"work\",\"Somdeep\",\"Somdeep\")";
         
         //stmt.executeUpdate(sql);
         //System.out.println("Created table in given database...");
      }
	
    		
	
	public static void insertRecord (String userName, Double lon, Double lat,String place,String source, String text) throws SQLException{	
		String sql = "INSERT INTO tweet1 (id, userName, longitude, latitude, place,source,text) "+
  			  "VALUES (\"" + counter + "\", \"" + userName + "\", \"" + lon + "\", \"" + lat + "\", \""+ place + "\", \"" + source + "\", \"" + text + "\")";
		//System.out.println(sql);
  		stmt.executeUpdate(sql);
  		String msg = Integer.toString(counter) + "," +  Double.toString(lat )+ "," + Double.toString(lon) + "," + text;
  		try
  		{
  		sqs.sendMessage(new SendMessageRequest(myQueueUrl, msg));
  		}
  		catch (AmazonServiceException ase) {
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
  		
  		counter++;
		
	}
	
	
    public static void main(String[] args) throws Exception {
    	
		connectDB();
    	//just fill this
		createSQS();
		
		
		 ConfigurationBuilder cb = new ConfigurationBuilder();
		 cb.setDebugEnabled(true)
         .setOAuthConsumerKey("2A3lA0bP72ssMQSDZejfpH4Gy")
         .setOAuthConsumerSecret("0dt5UPsF5xHnAaDY0gkC67QmteyebGb8o9dL8lpzMnUZK4jQSr")
         .setOAuthAccessToken("3905837358-50GjNeIEXGrx0jqDSb0nNkvhJmRRm5sXjub6bTO")
         .setOAuthAccessTokenSecret("GywxpLoWV7d5JcmDnXfbZdmE9lrcL3g4LDxjdFePhutDK");
       
         
        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
            	if (status.getGeoLocation()!=null  ){
            		System.out.println("@" + status.getUser().getScreenName() + " - "+ status.getPlace().getCountry()+ " - " + status.getText());
            		System.out.println(status.getGeoLocation());
            		
            		//System.out.println(status.getPlace());
            		String source,place;
            		double lon = status.getGeoLocation().getLongitude();
            		double lat = status.getGeoLocation().getLatitude();
            		
            		
            		if(status.getSource()==null)
            			source="Twitter";
            		else
            		{
            			int first=status.getSource().indexOf('>');
                		int last=status.getSource().indexOf("</");
            			source=status.getSource().substring(first+1,last);
            			
            		}
            		
            		if(status.getPlace()==null)
            			place="none";
            		else
            		{
            			//int first=status.getPlace().toString().indexOf('>');
                		//int last=status.getPlace().toString().indexOf('<');
                		
            			place=status.getPlace().getCountry();
            		}
            		
            		
            		
					try {
						insertRecord(status.getUser().getScreenName(), lon, lat,place,source,status.getText());				
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
            		
            	}
            }
            
            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                //System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
         //       System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
     //           System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
       //         System.out.println("Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };
       FilterQuery fq = new FilterQuery();
       // fq.language("English");
        //String keywords[] = {"trump","donald","refugee","terror","republican","election","gop"};

        //fq.track(keywords);
        twitterStream.addListener(listener);
   //   twitterStream.filter(fq);
        
      twitterStream.sample();
    }
}
    