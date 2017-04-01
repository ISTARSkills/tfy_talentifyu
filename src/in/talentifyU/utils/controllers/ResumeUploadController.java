package in.talentifyU.utils.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.viksitpro.core.utilities.DBUTILS;
import com.viksitpro.core.utilities.IStarBaseServelet;

/**
 * Servlet implementation class ResumeUploadController
 */
@WebServlet("/resume_upload")  
public class ResumeUploadController extends IStarBaseServelet {
	private static final long serialVersionUID = 1L;
	public static File uploadFolder;
	String fileUploadPath;
	static final int BUFFER_SIZE = 4096;
	String fileType = "";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ResumeUploadController() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(ServletConfig config) {
		fileUploadPath = "/root/Camera Roll/android_images";
		System.out.println(fileUploadPath);
		uploadFolder = new File(fileUploadPath);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("user_id--> " + request.getHeader("user_id"));
		int studentId = Integer.parseInt(request.getHeader("user_id"));

		DBUTILS util = new DBUTILS();
		String sql = "select student_id, resume_url from student_profile_data where student_id=" + studentId;
		List<HashMap<String, Object>> data = util.executeQuery(sql);

		if (data.size() == 0) {
			// Gets file name for HTTP header
			String fileName = UUID.randomUUID().toString();
			try {
				fileType = request.getHeader("fileType");
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("fileType------>" + fileType);
			fileName =  fileName + "." + fileType;
			File saveFile = new File(uploadFolder + "/resume/" + fileName + "." + fileType);

			InputStream inputStream = request.getInputStream();

			// opens an output stream for writing file
			FileOutputStream outputStream = new FileOutputStream(saveFile);

			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			System.out.println("Receiving data...");

			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}

			System.out.println("Data received.");
			outputStream.close();
			inputStream.close();
			System.out.println("File written to: " + saveFile.getAbsolutePath());

			String create_profile = "insert into student_profile_data (id, student_id,resume_url) "
					+ "values(cast ((select coalesce(max(id)+1, 0) from student_profile_data) as integer)," + studentId
					+ ", '" + "video/android_images/resume/" +fileName + "')";
			System.out.println(create_profile);
			util.executeUpdate(create_profile);
		} else {
			String old_resume_url = "none";

			for (HashMap<String, Object> row : data) {
				if (row.get("resume_url") != null) {
					old_resume_url = (String) row.get("resume_url");
				}
			}

			// Gets file name for HTTP header
			String fileName = UUID.randomUUID().toString();
			try {
				fileType = request.getHeader("fileType");
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("old_resume_url--------------"+old_resume_url+" new fileType------>" + fileType);
			fileName = fileName + "." + fileType;
			File saveFile = new File(uploadFolder + "/resume/" + fileName);


			// update resume
			if (!old_resume_url.equalsIgnoreCase("none")) {
				try {
					File file = new File(uploadFolder + "/resume/"
							+ (old_resume_url).replace("video/android_images/resume/", ""));
					if (file.delete()) {
						System.out.println(file.getName() + " Resume is deleted!");
					} else {
						System.out.println("Resume operation is failed.");
					}
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
			
			InputStream inputStream = request.getInputStream();

			// opens an output stream for writing file
			FileOutputStream outputStream = new FileOutputStream(saveFile);

			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			
			System.out.println("Receiving data...");

			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}

			System.out.println("Data received.");
			outputStream.close();
			inputStream.close();
			
			
			System.out.println("File written to: " + saveFile.getAbsolutePath());
			
			String update_resume_url = "update student_profile_data set resume_url='" +  "video/android_images/resume/" +fileName + "' where student_id="
					+ studentId;
			System.out.println(update_resume_url);
			util.executeUpdate(update_resume_url);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
