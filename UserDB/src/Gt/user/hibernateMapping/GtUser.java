package Gt.user.hibernateMapping;

import java.sql.Timestamp;

/**
 * GtUser entity. @author MyEclipse Persistence Tools
 */

public class GtUser implements java.io.Serializable {

	// Fields

	private int userId;
	private String userName;
	private String password;
	private String email;
	private String phoneNo;
	private String gender;
	private String sttaus;
	private Timestamp modifiedTime;

	// Constructors

	/** default constructor */
	public GtUser() {
	}

	/** minimal constructor */
	public GtUser(String userName, String password, String email, String phoneNo, String gender,
			Timestamp modifiedTime) {
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.phoneNo = phoneNo;
		this.gender = gender;
		this.modifiedTime = modifiedTime;
	}

	/** full constructor */
	public GtUser(String userName, String password, String email, String phoneNo, String gender, String sttaus,
			Timestamp modifiedTime) {
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.phoneNo = phoneNo;
		this.gender = gender;
		this.sttaus = sttaus;
		this.modifiedTime = modifiedTime;
	}

	// Property accessors

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNo() {
		return this.phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getSttaus() {
		return this.sttaus;
	}

	public void setSttaus(String sttaus) {
		this.sttaus = sttaus;
	}

	public Timestamp getModifiedTime() {
		return this.modifiedTime;
	}

	public void setModifiedTime(Timestamp modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	@Override
	public String toString() {
		if(this!=null){
		return "GtUser [userId=" + userId + ", userName=" + userName + ", password=" + password + ", email=" + email
				+ ", phoneNo=" + phoneNo + ", gender=" + gender + ", sttaus=" + sttaus + "]";
	
		}else{
			return null ;
		}
	}
	
}