package in.talentifyU.auth.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.viksitpro.core.dao.entities.IstarUser;
import com.viksitpro.core.dao.entities.IstarUserDAO;
import com.viksitpro.core.utilities.IStarBaseServelet;

/**
 * Servlet implementation class ResetPasswordController
 */
@WebServlet("/ResetPassword")
public class ResetPasswordController extends IStarBaseServelet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ResetPasswordController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		printAttrs(request);
		printParams(request);
		
		if (request.getParameterMap().containsKey("email") && request.getParameterMap().containsKey("new_password")) {
			try {
				IstarUserDAO dao = new IstarUserDAO();
				IstarUser user = dao.findByEmail(request.getParameter("email")).get(0);
					String new_password = request.getParameter("new_password").toString();
					Session session = dao.getSession();
					Transaction tx = null;
					try {
						user.setPassword(new_password);
						tx = session.beginTransaction();
						dao.attachDirty(user);
						tx.commit();
					} catch (Exception e) {
						if (tx != null)
							tx.rollback();
						e.printStackTrace();
					} finally {
						session.close();
					}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
