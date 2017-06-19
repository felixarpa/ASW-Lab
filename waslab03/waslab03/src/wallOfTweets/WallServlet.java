package wallOfTweets;

import java.io.IOException;
import java.util.List;
import java.security.MessageDigest;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.repackaged.org.json.JSONArray;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;



@SuppressWarnings("serial")
@WebServlet(urlPatterns = {"/tweets", "/tweets/*"})
public class WallServlet extends HttpServlet {

	private String TWEETS_URI = "/waslab03/tweets/";

	@Override
	// Implements GET http://localhost:8080/waslab03/tweets
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		resp.setContentType("application/json");
		resp.setHeader("Cache-control", "no-cache");
		List<Tweet> tweets= Database.getTweets();
		JSONArray job = new JSONArray();
		for (Tweet t: tweets) {
			JSONObject jt = new JSONObject(t);
			jt.remove("class");
			job.put(jt);
		}
		resp.getWriter().println(job.toString());
	}

	@Override
	// Implements POST http://localhost:8080/waslab03/tweets/:id/likes
	//        and POST http://localhost:8080/waslab03/tweets
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String uri = req.getRequestURI();
		int lastIndex = uri.lastIndexOf("/likes");
		if (lastIndex > -1) {  // uri ends with "/likes"
			// Implements POST http://localhost:8080/waslab03/tweets/:id/likes
			long id = Long.valueOf(uri.substring(TWEETS_URI.length(),lastIndex));
			resp.setContentType("text/plain");
			resp.getWriter().println(Database.likeTweet(id));
		}
		else {
			// Implements POST http://localhost:8080/waslab03/tweets
			int max_length_of_data = req.getContentLength();
			byte[] httpInData = new byte[max_length_of_data];
			ServletInputStream  httpIn  = req.getInputStream();
			httpIn.readLine(httpInData, 0, max_length_of_data);
			String body = new String(httpInData);
			try {
				JSONObject tweet = new JSONObject(body);
				String author = tweet.getString("author");
				String text = tweet.getString("text");
				Tweet nt = Database.insertTweet(author, text);
				JSONObject ntweet = new JSONObject(nt);
				ntweet.put("token", sha256(String.valueOf(nt.getId())));
				resp.getWriter().println(ntweet.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	// Implements DELETE http://localhost:8080/waslab03/tweets/:id
	public void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		String uri = req.getRequestURI();
		String token = req.getQueryString().substring(6, token.length());
		long id = 0;
		id = Long.valueOf(uri.substring(TWEETS_URI.length(),uri.length()));
		String tokenId = sha256(String.valueOf(id));
		boolean isDeleted = false;
		if(!token.isEmpty() && token.equals(tokenId)) isDeleted = Database.deleteTweet(id);

		if(!isDeleted || uri.isEmpty()) throw new ServletException("DELETE not yet implemented");
	}

	private static String sha256(String base) {
	    try{
	        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
	        byte[] hash = messageDigest.digest(base.getBytes("UTF-8"));
	        StringBuffer stringBuffer = new StringBuffer();

	        for (int i = 0; i < hash.length; i++) {
	            String hex = Integer.toHexString(0xff & hash[i]);
	            if(hex.length() == 1) stringBuffer.append('0');
	            stringBuffer.append(hex);
	        }

	        return stringBuffer.toString();
	    } catch(Exception ex){
	       throw new RuntimeException(ex);
	    }
	}

}







