package com.jdbc.demo;

import java.io.Serializable;

/**
 * entity 实体类
 * @author Cherry
 * 2020年4月18日
 */
public class Teacher implements Serializable{
	private static final long serialVersionUID = 1L;
	private String UUID;
	private String name;
	private int age;
	private String sex;
	private String major;
	private double salary;
	private String attachment;
	private String picture;
	public String getUUID() {
		return UUID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public double getSalary() {
		return salary;
	}
	public void setSalary(double salary) {
		this.salary = salary;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public Teacher() {
	}
	public Teacher(String uUID, String name, int age, String sex, String major, double salary) {
		super();
		UUID = uUID;
		this.name = name;
		this.age = age;
		this.sex = sex;
		this.major = major;
		this.salary = salary;
	}
	public Teacher(String name, int age, String sex, String major, double salary) {
		super();
		this.name = name;
		this.age = age;
		this.sex = sex;
		this.major = major;
		this.salary = salary;
	}
	public Teacher(String name, String major) {
		super();
		this.name = name;
		this.major = major;
	}
	@Override
	public String toString() {
		return "Teacher [UUID=" + UUID + ", name=" + name + ", age=" + age + ", sex=" + sex + ", major=" + major
				+ ", salary=" + salary + "]";
	}
}
