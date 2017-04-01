/**
 * 
 */
package in.talentifyU.complexData.pojo;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Created by AJITH on 27-09-2016.
 */
//@Cacheable
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class XMLStudentProfile {
	Integer user_id;
	String firstname;
	String lastname;
	String dob;
	Long mobileno;
	String gender;
	Integer pincode;
	Long aadharno;
	String email;
	Integer yop_10;
	Float marks_10;
	Integer yop_12;
	Float marks_12;
	Boolean has_under_graduation;
	String under_graduation_specialization_name;
	Float under_gradution_marks;
	Boolean has_post_graduation;
	String post_graduation_specialization_name;
	Float post_gradution_marks;
	Boolean is_studying_further_after_degree;
	String job_sector;
	String preferred_location;
	String company_name;
	String position;
	String duration;
	String description;
	String interested_in_type_of_course;
	String area_of_interest;
	String image_url_10;
	String image_url_12;
	String profile_image;
	String under_graduate_degree_name;
	String pg_degree_name;
	String resume_url;
	String user_category;

	public XMLStudentProfile() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getUser_id() {
		return user_id;
	}

	@XmlAttribute
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public String getFirstname() {
		return firstname;
	}

	@XmlAttribute
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	@XmlAttribute
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getDob() {
		return dob;
	}

	@XmlAttribute
	public void setDob(String dob) {
		this.dob = dob;
	}

	public Long getMobileno() {
		return mobileno;
	}

	@XmlAttribute
	public void setMobileno(Long mobileno) {
		this.mobileno = mobileno;
	}

	public String getGender() {
		return gender;
	}

	@XmlAttribute
	public void setGender(String gender) {
		this.gender = gender;
	}

	public Integer getPincode() {
		return pincode;
	}

	@XmlAttribute
	public void setPincode(Integer pincode) {
		this.pincode = pincode;
	}

	public Long getAadharno() {
		return aadharno;
	}

	@XmlAttribute
	public void setAadharno(Long aadharno) {
		this.aadharno = aadharno;
	}

	public String getEmail() {
		return email;
	}

	@XmlAttribute
	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getYop_10() {
		return yop_10;
	}

	@XmlAttribute
	public void setYop_10(Integer yop_10) {
		this.yop_10 = yop_10;
	}

	public Float getMarks_10() {
		return marks_10;
	}

	@XmlAttribute
	public void setMarks_10(Float marks_10) {
		this.marks_10 = marks_10;
	}

	public Integer getYop_12() {
		return yop_12;
	}

	@XmlAttribute
	public void setYop_12(Integer yop_12) {
		this.yop_12 = yop_12;
	}

	public Float getMarks_12() {
		return marks_12;
	}

	@XmlAttribute
	public void setMarks_12(Float marks_12) {
		this.marks_12 = marks_12;
	}

	public Boolean getHas_under_graduation() {
		return has_under_graduation;
	}

	@XmlAttribute
	public void setHas_under_graduation(Boolean has_under_graduation) {
		this.has_under_graduation = has_under_graduation;
	}

	public String getUnder_graduation_specialization_name() {
		return under_graduation_specialization_name;
	}

	@XmlAttribute
	public void setUnder_graduation_specialization_name(String under_graduation_specialization_name) {
		this.under_graduation_specialization_name = under_graduation_specialization_name;
	}

	public Float getUnder_gradution_marks() {
		return under_gradution_marks;
	}

	@XmlAttribute
	public void setUnder_gradution_marks(Float under_gradution_marks) {
		this.under_gradution_marks = under_gradution_marks;
	}

	public Boolean getHas_post_graduation() {
		return has_post_graduation;
	}

	@XmlAttribute
	public void setHas_post_graduation(Boolean has_post_graduation) {
		this.has_post_graduation = has_post_graduation;
	}

	public String getPost_graduation_specialization_name() {
		return post_graduation_specialization_name;
	}

	@XmlAttribute
	public void setPost_graduation_specialization_name(String post_graduation_specialization_name) {
		this.post_graduation_specialization_name = post_graduation_specialization_name;
	}

	public Float getPost_gradution_marks() {
		return post_gradution_marks;
	}

	@XmlAttribute
	public void setPost_gradution_marks(Float post_gradution_marks) {
		this.post_gradution_marks = post_gradution_marks;
	}

	public Boolean getIs_studying_further_after_degree() {
		return is_studying_further_after_degree;
	}

	@XmlAttribute
	public void setIs_studying_further_after_degree(Boolean is_studying_further_after_degree) {
		this.is_studying_further_after_degree = is_studying_further_after_degree;
	}

	public String getJob_sector() {
		return job_sector;
	}

	@XmlAttribute
	public void setJob_sector(String job_sector) {
		this.job_sector = job_sector;
	}

	public String getPreferred_location() {
		return preferred_location;
	}

	@XmlAttribute
	public void setPreferred_location(String preferred_location) {
		this.preferred_location = preferred_location;
	}

	public String getCompany_name() {
		return company_name;
	}

	@XmlAttribute
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getPosition() {
		return position;
	}

	@XmlAttribute
	public void setPosition(String position) {
		this.position = position;
	}

	public String getDuration() {
		return duration;
	}

	@XmlAttribute
	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getDescription() {
		return description;
	}

	@XmlAttribute
	public void setDescription(String description) {
		this.description = description;
	}

	public String getInterested_in_type_of_course() {
		return interested_in_type_of_course;
	}

	@XmlAttribute
	public void setInterested_in_type_of_course(String interested_in_type_of_course) {
		this.interested_in_type_of_course = interested_in_type_of_course;
	}

	public String getArea_of_interest() {
		return area_of_interest;
	}

	@XmlAttribute
	public void setArea_of_interest(String area_of_interest) {
		this.area_of_interest = area_of_interest;
	}

	public String getImage_url_10() {
		return image_url_10;
	}

	@XmlAttribute
	public void setImage_url_10(String image_url_10) {
		this.image_url_10 = image_url_10;
	}

	public String getImage_url_12() {
		return image_url_12;
	}

	@XmlAttribute
	public void setImage_url_12(String image_url_12) {
		this.image_url_12 = image_url_12;
	}

	public String getProfile_image() {
		return profile_image;
	}

	@XmlAttribute
	public void setProfile_image(String profile_image) {
		this.profile_image = profile_image;
	}

	public String getUnder_graduate_degree_name() {
		return under_graduate_degree_name;
	}

	@XmlAttribute
	public void setUnder_graduate_degree_name(String under_graduate_degree_name) {
		this.under_graduate_degree_name = under_graduate_degree_name;
	}

	public String getPg_degree_name() {
		return pg_degree_name;
	}

	@XmlAttribute
	public void setPg_degree_name(String pg_degree_name) {
		this.pg_degree_name = pg_degree_name;
	}

	public String getResume_url() {
		return resume_url;
	}

	@XmlAttribute
	public void setResume_url(String resume_url) {
		this.resume_url = resume_url;
	}

	public String getUser_category() {
		return user_category;
	}

	@XmlAttribute
	public void setUser_category(String user_category) {
		this.user_category = user_category;
	}
	
	
	
	
}
