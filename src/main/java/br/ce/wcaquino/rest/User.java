package br.ce.wcaquino.rest;

import javax.xml.bind.annotation.*;
//import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlAttribute;
//import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@XmlRootElement(name="user")
@XmlAccessorType(XmlAccessType.FIELD)
public class User {

	@XmlAttribute
	private Long id;
	private String name;
	private Integer age;
	private Double salary;
		
	public User(String name, Integer age, double salary) {
		super();
		this.name = name;
		this.age = age;
		this.salary = salary;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}


	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}


	@JsonPropertyOrder("field_name")
	public User() {
		super();
		// TODO Auto-generated constructor stub
		
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", age=" + age + ", salary=" + salary + "]";
	}
	
	
	
	

}
