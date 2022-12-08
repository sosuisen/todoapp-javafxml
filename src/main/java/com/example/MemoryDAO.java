package com.example;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import com.google.gson.Gson;

public class MemoryDAO implements DAO, Serializable {
	private ToDoList todos = new ToDoList() {
		// Use instance initializer of anonymouse class
		{
			getList().add(new ToDo(1, "Design", LocalDate.of(2022, 12, 1), true));
			getList().add(new ToDo(2, "Implementation", LocalDate.of(2022, 12, 7), false));
		}
	};

	@Override
	public Optional<ToDo> get(int id) {
		Optional<ToDo> targetTodo = todos.getList().stream().filter(todo -> todo.getId() == id).findFirst();
		return targetTodo;
	}

	@Override
	public ArrayList<ToDo> getAll() {
		return todos.getList();
	}

	@Override
	public ToDo create(String title, LocalDate date) {
		int newId = todos.getList().stream().max((todo1, todo2)->todo1.getId() - todo2.getId()).get().getId() + 1;
		var newToDo = new ToDo(newId, title, date, false);
		todos.getList().add(newToDo);
		
		// For checking current todos 
		System.out.println(todos);
		
		return newToDo; 
	}

	@Override
	public Optional<ToDo> updateTitle(int id, String title) {
		Optional<ToDo> targetTodo = todos.getList().stream().filter(todo -> todo.getId() == id).findFirst();
		if(targetTodo.isPresent()) targetTodo.get().setTitle(title);
		
		// For checking current todos 
		System.out.println(todos);

		serialize();
		
		return targetTodo;
	}

	@Override
	public Optional<ToDo> updateDate(int id, LocalDate date) {
		Optional<ToDo> targetTodo = todos.getList().stream().filter(todo -> todo.getId() == id).findFirst();
		if(targetTodo.isPresent()) targetTodo.get().setDate(date);
				
		// For checking current todos 
		System.out.println(todos);

		return targetTodo;
	}

	@Override
	public Optional<ToDo> updateCompleted(int id, boolean completed) {
		Optional<ToDo> targetTodo = todos.getList().stream().filter(todo -> todo.getId() == id).findFirst();		
		if(targetTodo.isPresent()) targetTodo.get().setCompleted(completed);
				
		// For checking current todos 
		System.out.println(todos);
		
		return targetTodo;
	}

	@Override
	public Optional<Integer> delete(int id) {
		boolean success = todos.getList().removeIf(todo->todo.getId() == id);
				
		// For checking current todos 
		System.out.println(todos);
	
		return success ? Optional.of(id) : Optional.empty();
	}
	
	public void serialize() {
		var gson = new Gson();
		var json = gson.toJson(todos);
		System.out.println(json);
	}
}
