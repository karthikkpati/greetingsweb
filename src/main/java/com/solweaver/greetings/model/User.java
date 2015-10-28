package com.solweaver.greetings.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class User extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private UserStatus userStatus;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="user")
	private List<UserEvent> userEventList;
	
	private Date lastLoggedInTime;
	
	private String lastLoggedInDeviceId;
	
	private String registeredDeviceId;
	
	private Gender gender;
	
	private Date dateOfBirth;

	private String password;
	
	private Channel registeredChannel;

	private Channel lastLoggedInChannel;

	private boolean emailVerified;
	
	private SocialAuthProvider socialAuthProvider;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="loggedInUser")
	private List<LoginActivity> loginActivity;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	public List<UserEvent> getUserEventList() {
		return userEventList;
	}

	public void setUserEventList(List<UserEvent> userEventList) {
		this.userEventList = userEventList;
	}

	public Date getLastLoggedInTime() {
		return lastLoggedInTime;
	}

	public void setLastLoggedInTime(Date lastLoggedInTime) {
		this.lastLoggedInTime = lastLoggedInTime;
	}

	public String getLastLoggedInDeviceId() {
		return lastLoggedInDeviceId;
	}

	public void setLastLoggedInDeviceId(String lastLoggedInDeviceId) {
		this.lastLoggedInDeviceId = lastLoggedInDeviceId;
	}

	public String getRegisteredDeviceId() {
		return registeredDeviceId;
	}

	public void setRegisteredDeviceId(String registeredDeviceId) {
		this.registeredDeviceId = registeredDeviceId;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Channel getLastLoggedInChannel() {
		return lastLoggedInChannel;
	}

	public void setLastLoggedInChannel(Channel lastLoggedInChannel) {
		this.lastLoggedInChannel = lastLoggedInChannel;
	}

	public Channel getRegisteredChannel() {
		return registeredChannel;
	}

	public void setRegisteredChannel(Channel registeredChannel) {
		this.registeredChannel = registeredChannel;
	}

	public boolean isEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

	public SocialAuthProvider getSocialAuthProvider() {
		return socialAuthProvider;
	}

	public void setSocialAuthProvider(SocialAuthProvider socialAuthProvider) {
		this.socialAuthProvider = socialAuthProvider;
	}

	public List<LoginActivity> getLoginActivity() {
		return loginActivity;
	}

	public void setLoginActivity(List<LoginActivity> loginActivity) {
		this.loginActivity = loginActivity;
	}

}
