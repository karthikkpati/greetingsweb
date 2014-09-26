package com.solweaver.greetings.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * @author Karthik
 *
 * @description BaseEntity captures the functionalities that all the entities should have.
 * 	
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Version of the Entity as how many times Entity has been modified
	@Version
	private int version;
	
	/*
	 * Both creationTime and modifiedTime to be populated by DAO
	 * */ 
	private Date modifiedTime;
	
	private Date creationTime;
	
	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
//	public void setVersion(int version) {
//		this.version = version;
//	}
	/**
	 * @return the modifiedTime
	 */
	public Date getModifiedTime() {
		return modifiedTime;
	}
	/**
	 * @param modifiedTime the modifiedTime to set
	 */
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	/**
	 * @return the creationTime
	 */
	public Date getCreationTime() {
		return creationTime;
	}
	/**
	 * @param creationTime the creationTime to set
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
}
