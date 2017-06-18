package asw01cs;


import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
//This code uses the Fluent API

public class SimpleFluentClient {

	private static String URI = "http://localhost:8080/waslab01_ss/";
	private static final String ACCEPT = "Accept";
	private static final String TEXT_PLAIN = "text/plain";

	public final static void main(String[] args) throws Exception {
		
		/* Code for Task #4 */
		List<NameValuePair> form = Form.form().add("author", "Simple Flient Client").add("tweet_text", "This is a tweet").build();
		Response response = Request.Post(URI + "wot").addHeader(ACCEPT, TEXT_PLAIN).bodyForm(form).execute();
		// Print the new Tweet ID
		String twid = response.returnContent().asString();
    	System.out.println(twid);
    	
    	System.out.println(Request.Get(URI).addHeader(ACCEPT, TEXT_PLAIN).execute().returnContent());
    	
    	/* Code for Task #5 */
    	// Give a random header
		Request.Post(URI + "wot").addHeader(ACCEPT, TEXT_PLAIN).bodyForm(Form.form().add("twid", twid).build()).execute();
		System.out.println(String.format("Tweet #%s has been deleted", twid));

    	System.out.println(Request.Get(URI).addHeader(ACCEPT, TEXT_PLAIN).execute().returnContent());
    	
    }
}

