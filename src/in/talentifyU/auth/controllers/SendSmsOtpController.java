package in.talentifyU.auth.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.viksitpro.core.utilities.IStarBaseServelet;

/**
 * Servlet implementation class SendSmsOtpController
 */
@WebServlet("/send_sms_otp")
public class SendSmsOtpController extends IStarBaseServelet {
	private static final long serialVersionUID = 1L;

	public SendSmsOtpController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		printAttrs(request);
		printParams(request);

		if (request.getParameter("mobile_num") != null && request.getParameter("message") != null
				&& !request.getParameter("mobile_num").equalsIgnoreCase("")
				&& !request.getParameter("message").equalsIgnoreCase("")) {
			// url =
			// "https://mobtexting.com/app/index.php/api?method=sms.normal&api_key=0c9ee1130f2a27302bbef3f39360a9eba5f7e48a&sender=TLNTFY&to="
			// + URLEncoder.encode(phoneno, "UTF-8") + "&message=" +
			// URLEncoder.encode(message, "UTF-8");
			String url = "https://mobtexting.com/app/index.php/api?method=sms.normal&api_key=0c9ee1130f2a27302bbef3f39360a9eba5f7e48a&sender=TLNTFY&to="
					+ URLEncoder.encode(request.getParameter("mobile_num"), "UTF-8") + "&message="
					+ URLEncoder.encode(request.getParameter("message"), "UTF-8");
			System.out.println(url);
			URL url2 = new URL(url);
			InputStream is = url2.openConnection().getInputStream();
			BufferedReader reader = new BufferedReader( new InputStreamReader( is )  );

		    String line = null;
		    while( ( line = reader.readLine() ) != null )  {
		       System.out.println(line);
		    }
		    reader.close();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

}
