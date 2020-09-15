package com.example.demo.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "User")
@Getter @Setter @NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="us_id")
	private Long id;
	
	@Column(name="us_name")
	private String name;
	
	@Column(name="us_surname")
	private String surname;
	
	@Column(name="us_age")
	private int age;
	
	@Column(name="us_birthday")
	private Date birthday;
	
	@Column(name="us_phoneNumber")
	private String phoneNumber;
	
	@Column(name="us_email")
	private String email;

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", surname=" + surname + ", age=" + age + ", birthday=" + birthday
				+ ", phoneNumber=" + phoneNumber + ", email=" + email + "]";
	}
 
}
