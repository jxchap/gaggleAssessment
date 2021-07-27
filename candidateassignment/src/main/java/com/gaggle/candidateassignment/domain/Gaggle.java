package com.gaggle.candidateassignment.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "gaggle")
public class Gaggle{

	@Id
	@GeneratedValue
	private long id;

	private String firstName;

	private String lastName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	@Override
	public String toString() {
		return "Gaggle [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + "]";
	}

	
	
}
