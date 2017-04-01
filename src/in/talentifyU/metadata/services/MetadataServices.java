package in.talentifyU.metadata.services;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


import com.viksitpro.core.dao.entities.IstarUser;
import com.viksitpro.core.dao.entities.MetaColumn;
import com.viksitpro.core.dao.entities.MetaColumnDAO;
import com.viksitpro.core.utilities.DBUTILS;

import in.talentifyU.utilities.MetaColumnTypes;

/**
 * @author ComplexObject
 *
 */
public class MetadataServices {
	


	public void saveStudentProfile(IstarUser student, String firstname, String lastname, String dob, Long mobileno, String gender, Integer pincode, Long aadharno, String email, Integer yop_10, Float marks_10, Integer yop_12, Float marks_12, Boolean has_under_graduation, String under_graduation_specialization_name, Float under_gradution_marks, Boolean has_post_graduation, String post_graduation_specialization_name, Float post_gradution_marks, Boolean is_studying_further_after_degree, String job_sector,
			String preferred_location, String company_name, String position, String duration, String description, String interested_in_type_of_course, String area_of_interest, String ug_degree, String pg_degree) {

		DBUTILS util = new DBUTILS();
		String sql = "select student_id, firstname, lastname, dob, mobileno,gender, pincode,aadharno,email, yop_10, marks_10, yop_12, marks_12, has_under_graduation,  under_graduation_specialization_name, under_gradution_marks, has_post_graduation, post_graduation_specialization_name, post_gradution_marks, is_studying_further_after_degree, job_sector, preferred_location, company_name, position, duration, description, interested_in_type_of_course, area_of_interest, image_url_10, image_url_12, profile_image,id, under_graduate_degree_name, pg_degree_name, resume_url from student_profile_data where student_id="
				+ student.getId();
		List<HashMap<String, Object>> data = util.executeQuery(sql);
		if (data.size() > 0) {
			// update
			String update = "UPDATE student_profile_data SET student_id='" + student.getId() + "', firstname='" + firstname + "', lastname='" + lastname + "', dob=" + dob + ", mobileno='" + mobileno + "', gender='" + gender + "'," + " pincode='" + pincode + "', aadharno='" + aadharno + "', email='" + email + "', yop_10='" + yop_10 + "',  marks_10='" + marks_10 + "', yop_12='" + yop_12 + "', marks_12='" + marks_12 + "', has_under_graduation='" + has_under_graduation.toString().charAt(0)

					+ "', under_graduation_specialization_name='" + under_graduation_specialization_name + "', under_gradution_marks='" + under_gradution_marks + "', has_post_graduation='" + has_post_graduation.toString().charAt(0) + "', post_graduation_specialization_name='" + post_graduation_specialization_name + "', post_gradution_marks='" + post_gradution_marks + "',  is_studying_further_after_degree='" + is_studying_further_after_degree.toString().toLowerCase().charAt(0) + "', job_sector='" + job_sector + "',"
					+ " preferred_location='" + preferred_location + "', company_name='" + company_name + "', position='" + position + "', duration='" + duration + "', description='" + description + "'," + " interested_in_type_of_course='" + interested_in_type_of_course + "', area_of_interest='" + area_of_interest + "',under_graduate_degree_name='" + ug_degree + "', pg_degree_name='" + pg_degree + "' WHERE student_id='" + student.getId() + "'; ";

			System.out.println(update);
			util.executeUpdate(update);
		} else {
			// create


			String create = "INSERT INTO student_profile_data (student_id, firstname, lastname, dob, mobileno, gender, pincode, aadharno, email, yop_10, marks_10, yop_12, marks_12," + " has_under_graduation, under_graduation_specialization_name, under_gradution_marks, has_post_graduation, post_graduation_specialization_name, post_gradution_marks," + " is_studying_further_after_degree, job_sector, preferred_location, company_name, position, duration, description, interested_in_type_of_course, area_of_interest,"
					+ " id, under_graduate_degree_name, pg_degree_name) " + "VALUES ('" + student.getId() + "', '" + firstname + "', '" + lastname + "', " + dob + ", '" + mobileno + "', '" + gender + "', '" + pincode + "', '" + aadharno + "', '" + email + "', '" + yop_10 + "', '" + marks_10 + "', '" + yop_12 + "', '" + marks_12 + "', '" + has_under_graduation + "', '" + under_graduation_specialization_name + "', '" + under_gradution_marks + "', '" + has_post_graduation + "', '" + post_graduation_specialization_name
					+ "', '" + post_gradution_marks + "'," + " '" + is_studying_further_after_degree.toString().toLowerCase().charAt(0) + "', '" + job_sector + "', '" + preferred_location + "', '" + company_name + "', '" + position + "', '" + duration + "', '" + description + "'," + " '" + interested_in_type_of_course + "', '" + area_of_interest + "',cast ((select coalesce(max(id)+1, 0) from student_profile_data) as integer), '" + ug_degree + "', '" + pg_degree + "');";

			util.executeUpdate(create);
			System.out.println(create);
		}
	}

	public void saveVacancyProfile(int vacancy_id, String filtered_cities, String filtered_colleges_id, String filtered_ug_degrees, String filtered_pg_degrees, HashMap<Integer, String> skill_percentile_hashmap, String grade_cutoff, String vacancy_category, String vacancy_position_type, String vacancy_experience_level, String vacancy_min_salary, String vacancy_max_salary) {
		HashMap<String, String> data = new HashMap<>();
		data.put(MetaColumnTypes.vacancy_cities, filtered_cities);
		data.put(MetaColumnTypes.vacancy_colleges, filtered_colleges_id);
		data.put(MetaColumnTypes.vacancy_ug_degree, filtered_ug_degrees);
		data.put(MetaColumnTypes.vacancy_pg_degree, filtered_pg_degrees);
		data.put(MetaColumnTypes.vacancy_grade_cutoff_ug, grade_cutoff);
		data.put(MetaColumnTypes.vacancy_grade_cutoff_pg, grade_cutoff);
		data.put(MetaColumnTypes.vacancy_category, vacancy_category);
		data.put(MetaColumnTypes.vacancy_position_type, vacancy_position_type);
		data.put(MetaColumnTypes.vacancy_experience_level, vacancy_experience_level);
		data.put(MetaColumnTypes.vacancy_min_salary, vacancy_min_salary);
		data.put(MetaColumnTypes.vacancy_max_salary, vacancy_max_salary);
		String skill_percentile_filter = "";
		for (Integer i : skill_percentile_hashmap.keySet()) {
			skill_percentile_filter += i + "__" + skill_percentile_hashmap.get(i) + "!#";
		}
		if (skill_percentile_filter.endsWith("!#")) {
			skill_percentile_filter = skill_percentile_filter.substring(0, skill_percentile_filter.length() - 2);
		}
		data.put(MetaColumnTypes.vacancy_skills, skill_percentile_filter);
		MetaColumnDAO metadao = new MetaColumnDAO();
		List<MetaColumn> columns = metadao.findByEntityName("VACANCY");
		for (MetaColumn row : columns) {
			String key = row.getPropertyName().trim();
			System.out.println(row.getPropertyName() + "  value is " + data.get(key));
			DBUTILS util = new DBUTILS();
			String sql = "select * from metadata where entity_id=" + vacancy_id + " and entity_type='VACANCY' and meta_column_id=" + row.getId();
			if (util.executeQuery(sql).size() > 0) {
				/// update
				String update = "update metadata set value='" + data.get(key) + "' where entity_id=" + vacancy_id + " and entity_type='VACANCY' and meta_column_id=" + row.getId();
				util.executeUpdate(update);
			} else {
				// create
				String insert = "INSERT INTO metadata ( 	ID, 	meta_column_id, 	entity_id, 	entity_type,  VALUE  ) VALUES 	( 		( 	SELECT 		case (select 1 where max(id) is null)     when 1 then 1     else max(id)+1   end 	FROM 		metadata )," + row.getId() + "," + vacancy_id + ", 'VACANCY','" + data.get(key) + "' )";
				util.executeUpdate(insert);
			}
		}
	}

	/*public VacancyProfile intializeVacancyProfile(Vacancy vacancy) {
		VacancyProfile profile = new VacancyProfile();
		Metadata data = new Metadata();
		data.setEntityId(vacancy.getId());
		data.setEntityType("VACANCY");
		List<Metadata> dataList = new MetadataDAO().findByExample(data);
		ArrayList<Integer> alreadyAdded = new ArrayList<>();
		for (Metadata data_row : dataList) {
			// System.out.println("meta col
			// id"+data_row.getMetaColumn().getId()+" data
			// type="+data_row.getMetaColumn().getDataType()+" dtaa
			// valuse"+data_row.getValue() );
			if (!alreadyAdded.contains(data_row.getMetaColumn().getId())) {
				alreadyAdded.add(data_row.getMetaColumn().getId());
				if (data_row.getMetaColumn().getDataType().trim().equalsIgnoreCase("int4")) {
					profile.setProperty(data_row.getMetaColumn().getPropertyName(), Integer.parseInt(data_row.getValue()));
				} else if (data_row.getMetaColumn().getDataType().trim().equalsIgnoreCase("int8")) {
					profile.setProperty(data_row.getMetaColumn().getPropertyName(), Long.parseLong(data_row.getValue()));
				} else if (data_row.getMetaColumn().getDataType().trim().equalsIgnoreCase("bool")) {
					profile.setProperty(data_row.getMetaColumn().getPropertyName(), new Boolean(data_row.getValue()));
				} else if (data_row.getMetaColumn().getDataType().trim().equalsIgnoreCase("varchar")) {
					profile.setProperty(data_row.getMetaColumn().getPropertyName(), data_row.getValue());
				} else if (data_row.getMetaColumn().getDataType().trim().equalsIgnoreCase("date")) {
					// 2016-09-27
					try {
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
						Date date = formatter.parse(data_row.getValue());
						profile.setProperty(data_row.getMetaColumn().getPropertyName(), date);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (data_row.getMetaColumn().getDataType().trim().equalsIgnoreCase("timestamp")) {
					try {
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
						Date parsedDate;
						parsedDate = dateFormat.parse(data_row.getValue());
						Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
						profile.setProperty(data_row.getMetaColumn().getPropertyName(), timestamp);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (data_row.getMetaColumn().getDataType().trim().equalsIgnoreCase("float")) {
					profile.setProperty(data_row.getMetaColumn().getPropertyName(), Float.parseFloat(data_row.getValue()));
				} else if (data_row.getMetaColumn().getDataType().trim().equalsIgnoreCase("multi_select")) {
					// System.out.println("yes"+data_row.getMetaColumn().getPropertyName());
					ArrayList<String> ddd = new ArrayList<>();
					String dataString = data_row.getValue();
					for (String str : dataString.split("!#")) {
						ddd.add(str);
					}
					profile.setProperty(data_row.getMetaColumn().getPropertyName(), ddd);
				} else if (data_row.getMetaColumn().getDataType().trim().equalsIgnoreCase("key_value")) {
					// System.out.println("hmap
					// yes"+data_row.getMetaColumn().getPropertyName());
					String dataString = data_row.getValue();
					HashMap<String, String> hmap = new HashMap<>();
					for (String str : dataString.split("!#")) {
						if (str.contains("__")) {
							String key = str.split("__")[0];
							String value = str.split("__")[1];
							hmap.put(key, value);
						}
					}
					profile.setProperty(data_row.getMetaColumn().getPropertyName(), hmap);
				} else {
					System.out.println("else" + data_row.getMetaColumn().getPropertyName());
					profile.setProperty(data_row.getMetaColumn().getPropertyName(), data_row.getValue());
				}
			}
		}
		return profile;
	}*/
}