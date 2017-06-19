package twitter;

import java.util.Date;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

public class SimpleClient {

	public static void main(String[] args) throws Exception {
		
		final Twitter twitter = new TwitterFactory().getInstance();
		
		/*
		Date now = new Date();
		String latestStatus = "I want to increase the Klout score of @cfarre [task #4 completed "+now+"]";
		Status status = twitter.updateStatus(latestStatus);
		System.out.println("Successfully updated the status to: " + status.getText());
		*/
		
		Query query1 = new Query();
		query1.setQuery("@fib_was");
		query1.setResultType(Query.RECENT);
		query1.setCount(1);
		QueryResult result1 = twitter.search(query1);
		
		Status aswTweet = result1.getTweets().get(0);
		
		System.out.println(aswTweet.getText());
		
		twitter.retweetStatus(aswTweet.getId());
		
		
		
	}
}
