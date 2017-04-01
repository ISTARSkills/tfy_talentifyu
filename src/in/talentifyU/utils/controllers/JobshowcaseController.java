package in.talentifyU.utils.controllers;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.examples.HtmlToPlainText;
import org.jsoup.nodes.Document;
import org.quartz.JobDetail;

import com.istarindia.apps.dao.Company;
import com.istarindia.apps.dao.CompanyDAO;
import com.istarindia.apps.dao.JobRootClass;
import com.istarindia.apps.dao.JobShowCase;
import com.istarindia.apps.dao.Skill;
import com.istarindia.apps.dao.SkillDAO;
import com.istarindia.apps.dao.Vacancy;
import com.viksitpro.core.utilities.IStarBaseServelet;

/**
 * Servlet implementation class JobshowcaseController
 */
@WebServlet("/JobshowcaseController")
public class JobshowcaseController extends IStarBaseServelet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public JobshowcaseController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		CompanyDAO companyDAO = new CompanyDAO();
		List<Company> companies = companyDAO.findAll();
		ArrayList<JobShowCase> jobshowcaseArrayList = new ArrayList<>();
		for (Company c : companies) {
			Set<Vacancy> vacacny = c.getVacancies();
			ArrayList<JobDetail> jobdetailarrayList = new ArrayList<>();
			for (Vacancy vcc : vacacny) {
				JobDetail jobdetail = new JobDetail();
				if (vcc.getProfileTitle() != null) {
					jobdetail.setJob_name(vcc.getProfileTitle());
					if (vcc.getLocation() != null) {
						jobdetail.setLocation(vcc.getLocation());
					}
					if (vcc.getDescription() != null) {

						String vacancyDescription = vcc.getDescription().replaceAll("&nbsp;", "").replaceAll("&amp;", "and").replaceAll("&#174;", "");
						
						byte ptext[] = vacancyDescription.getBytes();
						vacancyDescription = new String(ptext, "UTF-8");
						
						try {
							vacancyDescription =  URLDecoder.decode(vacancyDescription.replaceAll("&nbsp;", ""), "UTF-8").replaceAll("&#150;", "-").replaceAll("&#146;", "'").replaceAll("&#174;", "");
						} catch (Exception e) {
							vacancyDescription =  vacancyDescription.replaceAll("&#150;", "-").replaceAll("&#146;", "'").replaceAll("&#174;", "");

						}
						
						Document doc = Jsoup.parse(StringEscapeUtils.unescapeHtml4(vacancyDescription));
						org.jsoup.examples.HtmlToPlainText text = new HtmlToPlainText();
						jobdetail.setJob_description(text.getPlainText(doc));
					}
					if (vcc.getTotalPositions() != null) {
						jobdetail.setNos_of_vaccancy(vcc.getTotalPositions());
					}
					if (vcc.getVacancyProfile().getVacancy_min_salary() != null
							&& vcc.getVacancyProfile().getVacancy_max_salary() != null) {
						jobdetail.setSalary(vcc.getVacancyProfile().getVacancy_min_salary() + " - "
								+ vcc.getVacancyProfile().getVacancy_max_salary());
					}

					if (vcc.getVacancyProfile().getVacancy_experience_level() != null) {
						jobdetail.setExperience(vcc.getVacancyProfile().getVacancy_experience_level());
					}

					if (vcc.getVacancyProfile().getVacancy_position_type() != null) {
						jobdetail.setPosition_type(vcc.getVacancyProfile().getVacancy_position_type());
					}

					if (vcc.getVacancyProfile().getVacancy_grade_cutoff_pg() != null) {
						jobdetail.setGrade_cutoff_pg(vcc.getVacancyProfile().getVacancy_grade_cutoff_pg());
					}
					if (vcc.getVacancyProfile().getVacancy_grade_cutoff_ug() != null) {
						jobdetail.setGrade_cutoff_ug(vcc.getVacancyProfile().getVacancy_grade_cutoff_ug());
					}

					if (vcc.getVacancyProfile().getVacancy_skills() != null) {
						StringBuffer sb = new StringBuffer();
						HashMap<String, String> skill_hashMap = vcc.getVacancyProfile().getVacancy_skills();
						for (String key : skill_hashMap.keySet()) {
							Skill sk = new SkillDAO().findById(Integer.parseInt(key));

							sb.append(sk.getSkillTitle() + ",");
						}
						jobdetail.setSkill_set(sb.toString());

					}
					jobdetailarrayList.add(jobdetail);
				}

			}

			if (jobdetailarrayList.size() > 0) {
				JobShowCase jobshowcase = new JobShowCase();
				if (c.getName() != null)
					jobshowcase.setCompany_name(c.getName());
				if (c.getCompany_profile() != null) {
					
					String companyProfile = c.getCompany_profile().replaceAll("&nbsp;", "").replaceAll("&amp;", "and").replaceAll("&#174;", "");
					
					byte ptext[] = companyProfile.getBytes();
					companyProfile = new String(ptext, "UTF-8");
					
					try {
						companyProfile =  URLDecoder.decode(companyProfile, "UTF-8").replaceAll("&#150;", "-").replaceAll("&#146;", "'").replaceAll("&#174;", "");
					} catch (Exception e) {
						companyProfile =  companyProfile.replaceAll("&#150;", "-").replaceAll("&#146;", "'").replaceAll("&#174;", "");
					}
					
					Document doc = Jsoup.parse(StringEscapeUtils.unescapeHtml4(companyProfile));
					org.jsoup.examples.HtmlToPlainText text = new HtmlToPlainText();				
/*					Document doc = Jsoup.parse(c.getCompany_profile().replaceAll("&nbsp;", "").trim());
					org.jsoup.examples.HtmlToPlainText text = new HtmlToPlainText();*/
					jobshowcase.setCompany_description(text.getPlainText(doc));
				}
				
				jobshowcase.setNos_of_jobs(jobdetailarrayList.size());
				jobshowcase.setJobDetailArrayList(jobdetailarrayList);
				if (c.getImage() != null) {
					jobshowcase.setImage(c.getImage());
				}
				jobshowcaseArrayList.add(jobshowcase);
			}
		}

		if (jobshowcaseArrayList.size() > 0) {
			JobRootClass jobroot = new JobRootClass();
			jobroot.setJobshowcaseArrayList(jobshowcaseArrayList);
			try {
				JAXBContext jaxbContext = JAXBContext.newInstance(JobRootClass.class);

				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				jaxbMarshaller.marshal(jobroot, response.getWriter());

			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
