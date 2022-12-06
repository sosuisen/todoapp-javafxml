package com.example;

import java.time.LocalDate;

public class ToDo {
	private int id;
	private String title;
	private LocalDate date;
	private boolean completed;
	
	@Override
	public String toString() {
		return "ToDo [id=" + id + ", title=" + title + ", date=" + date + ", completed=" + completed + "]";
	}
	
	public ToDo(int id, String title, LocalDate date, boolean completed) {
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
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public boolean isCompleted() {
		return completed;
	}
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	
}
