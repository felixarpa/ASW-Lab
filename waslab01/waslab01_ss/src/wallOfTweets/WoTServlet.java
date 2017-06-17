package wallOfTweets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Locale;
import java.util.Vector;
import java.security.MessageDigest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;

/**
 * Servlet implementation class WoTServlet
 */
@WebServlet("/")
public class WoTServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Locale currentLocale = new Locale("en");
	String ENCODING = "ISO-8859-1";
	private static final String ACCEPT = "Accept";
	private static final String TEXT_PLAIN = "text/plain";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WoTServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Vector<Tweet> tweets = Database.getTweets();
			String acceptHeader = request.getHeader(ACCEPT);
			if (acceptHeader.equals(TEXT_PLAIN)) {
				printPLAINresult(tweets, request, response);
			} else {
				printHTMLresult(tweets, request, response);
			}
		}

		catch (SQLException ex ) {
			throw new ServletException(ex);
		}
	}

	private void printPLAINresult(Vector<Tweet> tweets, HttpServletRequest req, HttpServletResponse res) throws IOException {
		res.setContentType(TEXT_PLAIN);
		res.setCharacterEncoding(ENCODING);
		PrintWriter out = res.getWriter();
		for (Tweet tweet: tweets) {
			out.println(String.format("tweet #%d: %s: %s [%s]", tweet.getTwid(), tweet.getAuthor(),
					tweet.getText(), tweet.getDate().toString()));
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Delete should be a DELETE operation (not POST) but this works for this session
		String twid = request.getParameter("twid");
		Long id = 0L;
		
		if (twid == null) {
			id = postTweet(request);
			Cookie c = new Cookie("CookieId" + String.valueOf(id), sha256(id));
			response.addCookie(c);
		} else {
			deleteTweet(request);
		}

		String acceptHeader = request.getHeader(ACCEPT);
		if (acceptHeader.equals(TEXT_PLAIN)) {
			PrintWriter out = response.getWriter();
			out.print(String.valueOf(id));
		} else {
			response.sendRedirect(request.getContextPath());
		}
	}
	
	private Long postTweet(HttpServletRequest request) {
		String author = request.getParameter("author");
		String tweet = request.getParameter("tweet_text");
		try {
			return Database.insertTweet(author, tweet);
		} catch (SQLException e) {
			return 0L;
		}
	
	}
	
	private void deleteTweet(HttpServletRequest request) {
		Long twid = Long.valueOf(request.getParameter("twid"));
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getValue().equals(sha256(twid))) {
					Database.deleteTweet(twid);
				}
			}
		}
	}

	private void printHTMLresult (Vector<Tweet> tweets, HttpServletRequest req, HttpServletResponse res) throws IOException
	{
		DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.FULL, currentLocale);
		DateFormat timeFormatter = DateFormat.getTimeInstance(DateFormat.DEFAULT, currentLocale);
		String currentDate = dateFormatter.format(new java.util.Date());
		res.setContentType ("text/html");
		res.setCharacterEncoding(ENCODING);
		PrintWriter  out = res.getWriter ( );
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head><title>Wall of Tweets</title>");
		out.println("<link href=\"wallstyle.css\" rel=\"stylesheet\" type=\"text/css\" />");
		out.println("</head>");
		out.println("<body class=\"wallbody\">");
		out.println("<h1>Wall of Tweets</h1>");
		out.println("<div class=\"walltweet\">"); 
		out.println("<form method=\"post\">");
		out.println("<table border=0 cellpadding=2>");
		out.println("<tr><td>Your name:</td><td><input name=\"author\" type=\"text\" size=70></td><td></td></tr>");
		out.println("<tr><td>Your tweet:</td><td><textarea name=\"tweet_text\" rows=\"2\" cols=\"70\" wrap></textarea></td>"); 
		out.println("<td><input type=\"submit\" name=\"action\" value=\"Tweet!\"></td></tr>"); 
		out.println("</table></form></div>"); 
		for (Tweet tweet: tweets) {
			String messDate = dateFormatter.format(tweet.getDate());
			if (!currentDate.equals(messDate)) {
				out.println("<br><h3>...... " + messDate + "</h3>");
				currentDate = messDate;
			}
			out.println("<div class=\"wallitem\">");
			out.println("<h4><em>" + tweet.getAuthor() + "</em> @ "+ timeFormatter.format(tweet.getDate()) +"</h4>");
			out.println("<p>" + tweet.getText() + "</p>");
			
			/* Code for Task #5 */
			out.println("<form action=\"wot\" method=\"post\">");
			out.println("<table border=0 cellpadding=2>");
			out.println("<input type=\"submit\" name=\"action\" value=\"Remove tweet\">");
			// Hidden input with the Tweet Id to get it as a parameter in the doPost method
			out.println("<tr><td><input type=\"hidden\" name=\"twid\" value=" + tweet.getTwid() + "></td></tr>");
			out.println("</table></form>");
			
			out.println("</div>");
			
		}
		out.println ( "</body></html>" );
	}
	

	public static String sha256(Long base) {
		return sha256(String.valueOf(base));
	}
	
	// Stack Overflow :D
	public static String sha256(String base) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        byte[] hash = digest.digest(base.getBytes("UTF-8"));
	        StringBuffer hexString = new StringBuffer();

	        for (int i = 0; i < hash.length; i++) {
	            String hex = Integer.toHexString(0xff & hash[i]);
	            if (hex.length() == 1) {
	            	hexString.append('0');
	            }
	            hexString.append(hex);
	        }

	        return hexString.toString();
	    } catch(Exception e) {
	    	throw new RuntimeException(e);
	    }
	}
}
