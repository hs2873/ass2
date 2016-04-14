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


public class Micro {
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
         myQueueUrl="https://sqs.us-east-1.amazonaws.com/575052755988/K12APIQueue";


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
    
	
	    		
	
	public static void insertRecord (String message) {	
		
		System.out.println(message);
  		//String msg = Integer.toString(counter) + "," +  Double.toString(lat )+ "," + Double.toString(lon) + "," + text;
  		try
  		{
  		sqs.sendMessage(new SendMessageRequest(myQueueUrl, message));
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
    	
		//connectDB();
    	//just fill this
		createSQS();
		insertRecord("Somdeep");
		
		
    }		
}
    