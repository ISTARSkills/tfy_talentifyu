package in.talentifyU.utils.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

import com.viksitpro.core.utilities.DBUTILS;
import com.viksitpro.core.utilities.IStarBaseServelet;

/**
 * Servlet implementation class ImageUploadController
 */
@WebServlet("/image_upload")  
public class ImageUploadController extends IStarBaseServelet {
	private static final long serialVersionUID = 1L;
	public static File uploadFolder;

	String fileUploadPath;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ImageUploadController() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(ServletConfig config) {
		fileUploadPath = "/root/Camera Roll/android_images";
		System.out.println(fileUploadPath);
		uploadFolder = new File(fileUploadPath);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@SuppressWarnings("resource")
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// printParams(request);

			System.out.println("request.getParamete) " + request.getParameter("user_id"));
			int studentId = Integer.parseInt(request.getParameter("user_id"));
			FileOutputStream fos = null;
			
			DBUTILS util = new DBUTILS();
			String sql = "select student_id, image_url_10, image_url_12, profile_image from student_profile_data where student_id="
					+ studentId;
			List<HashMap<String, Object>> data = util.executeQuery(sql);
			

			
			if(data.size()==0)
			{
				//create a new row
				String imageStr = request.getParameter("url");
				byte[] decoded = Base64.decodeBase64(imageStr.getBytes());
				String image_name = "";
				String image_name_10 = "";
				String image_name_12 = "";
				
				
				
				
				if (request.getParameter("type") != null && !request.getParameter("type").toString().equalsIgnoreCase("")) {
					if (request.getParameter("type").toString().equalsIgnoreCase("marks_10")) {
						image_name_10 = UUID.randomUUID() + "";
						fos = new FileOutputStream(uploadFolder + "/marks_10/" + image_name_10);
						fos.write(decoded);
						image_name_10 = "video/android_images/marks_10/" + image_name_10;
					} else {
						image_name_12 = UUID.randomUUID() + "";
						fos = new FileOutputStream(uploadFolder + "/marks_12/" + image_name_12);
						fos.write(decoded);
						image_name_12 = "video/android_images/marks_12/" + image_name_12;
					}

				}
				else {
					image_name = UUID.randomUUID() + "";
					fos = new FileOutputStream(uploadFolder + "/" + image_name);
					fos.write(decoded);
					image_name = "video/android_images/" + image_name;
				}
				
				
				
				
				String create_profile="insert into student_profile_data (id, student_id,image_url_10,image_url_12,	profile_image) "
						+ "values(cast ((select coalesce(max(id)+1, 0) from student_profile_data) as integer),"+studentId+", '"+image_name_10+"','"+image_name_12+"','"+image_name+"')";
				System.out.println(create_profile);
				util.executeUpdate(create_profile);
				
			}
			else
			{
				String old_profile_image = "none";
				String old_image_10 = "none";
				String old_image_12 = "none";
				for (HashMap<String, Object> row : data) {
					if (row.get("image_url_10") != null) {
						old_image_10 = (String) row.get("image_url_10");
					}
					if (row.get("image_url_12") != null) {
						old_image_12 = (String) row.get("image_url_12");
					}
					if (row.get("profile_image") != null) {
						old_profile_image = (String) row.get("profile_image");
					}
				}
				
				
				
				//update existing row
				String imageStr = request.getParameter("url");
				byte[] decoded = Base64.decodeBase64(imageStr.getBytes());
				String image_name = "";
				String image_name_10 = "";
				String image_name_12 = "";
				image_name = UUID.randomUUID() + "";
				image_name_10 = UUID.randomUUID() + "";
				image_name_12 = UUID.randomUUID() + "";
				
				
				
				
				
				
				if (request.getParameter("type") != null
						&& !request.getParameter("type").toString().equalsIgnoreCase("")) 
				{
				
					if (request.getParameter("type").toString().equalsIgnoreCase("marks_10")) {
						//update 10 image
						if (!old_image_10.equalsIgnoreCase("none")) {
							try {
								File file = new File(uploadFolder + "/marks_10/"
										+ (old_image_10).replace("video/android_images/marks_10/", ""));
								if (file.delete()) {
									System.out.println(file.getName() + " marks_10 is deleted!");
								} else {
									System.out.println("marks_10 Delete operation is failed.");
								}
							} catch (Exception e) {
								// e.printStackTrace();
							}
						}
						
						fos = new FileOutputStream(uploadFolder + "/marks_10/" + image_name_10);
						fos.write(decoded);
						image_name_10 = "video/android_images/marks_10/" + image_name_10;
						String update_image_10 ="update student_profile_data set image_url_10='"+image_name_10+"' where student_id="+studentId;
						System.out.println(update_image_10);
						util.executeUpdate(update_image_10);
						
					}
					else
					{
						//update 12 image
						
						if (!old_image_12.equalsIgnoreCase("none")) {
							try {
								File file = new File(uploadFolder + "/marks_12/"
										+ (old_image_12).replace("video/android_images/marks_12/", ""));
								if (file.delete()) {
									System.out.println(file.getName() + " marks_12 is deleted!");
								} else {
									System.out.println("marks_12 Delete operation is failed.");
								}
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
						fos = new FileOutputStream(uploadFolder + "/marks_12/" + image_name_12);
						fos.write(decoded);
						image_name_12 = "video/android_images/marks_12/" + image_name_12;
						String update_image_12 ="update student_profile_data set image_url_12='"+image_name_12+"' where student_id="+studentId;
						System.out.println(update_image_12);
						util.executeUpdate(update_image_12);
						
					}	
					
				}
				else
				{
					//update profile image only
					
					if (!old_profile_image.equalsIgnoreCase("none")) {
						try {
							File file = new File(uploadFolder + "/"
									+ (old_profile_image).replace("video/android_images/", ""));
							if (file.delete()) {
								System.out.println(file.getName() + " is deleted!");
							} else {
								System.out.println("Delete operation is failed.");
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
					fos = new FileOutputStream(uploadFolder + "/" + image_name);
					fos.write(decoded);
					

					image_name = "video/android_images/" + image_name;
					String update_profile_image ="update student_profile_data set profile_image='"+image_name+"' where student_id="+studentId;
					System.out.println(update_profile_image);
					util.executeUpdate(update_profile_image);
					
					
				}	
				
			}
			
			
			
			
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
