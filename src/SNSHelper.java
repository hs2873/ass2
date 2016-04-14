

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.ConfirmSubscriptionRequest;
import com.amazonaws.services.sns.model.ConfirmSubscriptionResult;


public enum SNSHelper {
	INSTANCE;
	
	//private AWSCredentials credentials = new BasicAWSCredentials("AKIAJYBJLH44F6DPQOPA", "YlnA6C+kMIdkg94aUPZ31oHQozmEaSvdKMTbRqLN");
	//private AmazonSNSClient amazonSNSClient = new AmazonSNSClient(credentials);
	private AmazonSNSClient amazonSNSClient = new AmazonSNSClient(new ClasspathPropertiesFileCredentialsProvider());
	
	public void confirmTopicSubmission(SNSMessage message) {
		amazonSNSClient.setRegion(Region.getRegion(Regions.US_EAST_1));
		ConfirmSubscriptionRequest confirmSubscriptionRequest = new ConfirmSubscriptionRequest()
		 							.withTopicArn(message.getTopicArn())
									.withToken(message.getToken());
		System.out.println(message.getTopicArn());
		System.out.println(message.getToken());
		ConfirmSubscriptionResult resutlt = amazonSNSClient.confirmSubscription(confirmSubscriptionRequest);
		System.out.println("subscribed to " + resutlt.getSubscriptionArn());
		
	}
	
}