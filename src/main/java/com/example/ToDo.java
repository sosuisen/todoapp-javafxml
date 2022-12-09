package com.example;

import java.time.LocalDate;

public class ToDo {
	private int id;
	private String title;
	/**
	 * Local date string
	 * e.g. 2022-12-01
	 */
	private String date;
	private boolean completed;
	
	@Override
	public String toString() {
		return "ToDo [id=%s, title=%s, date=%s, completed=%s".formatted(id, title, date, completed);
	}
	
	public ToDo(int id, String title, String date, boolean completed) {
		super();
		this.id = id;
		this.title = title;
		this.date = date;
		this.completed = completed;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return date string e.g. 2022-12-01
	 */
	public String getDate() {
		return date;
	}
	/**
	 * @param date date string e.g. 2022-12-01
	 */
	public void setDate(String date) {
		this.date = date;
	}
	public LocalDate getLocalDate() {
		return LocalDate.parse(date);
	}
	public void setLocalDate(LocalDate localDate) {
		this.date = localDate.toString();
	}
	public boolean isCompleted() {
		return completed;
	}
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	
}
