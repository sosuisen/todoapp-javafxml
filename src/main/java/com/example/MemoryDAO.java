package com.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class MemoryDAO implements DAO {
	private ArrayList<ToDo> todos = new ArrayList<ToDo>() {
		// 匿名クラスのインスタンスイニシャライザを使用
		{
			add(new ToDo(1, "ネーム", LocalDate.of(2022, 12, 1), true));
			add(new ToDo(2, "下描き", LocalDate.of(2022, 12, 2), false));
		}
	};
	// MemoryDAO のインスタンスイニシャライザを使う場合、
	// 毎回 add の前に storage. と書く必要があるので、少し長い。
	//	{
	//		storage.add(new ToDo(1, "ネーム", LocalDate.of(2022, 12, 1), false));
	//	}

	@Override
	public Optional<ToDo> get(int id) {
		Optional<ToDo> targetTodo = todos.stream().filter(todo -> todo.getId() == id).findFirst();
		return targetTodo;
	}

	@Override
	public ArrayList<ToDo> getAll() {
		return todos;
	}

	@Override
	public ToDo create(String title, LocalDate date) {
		int newId = todos.stream().max((todo1, todo2)->todo1.getId() - todo2.getId()).get().getId() + 1;
		var newToDo = new ToDo(newId, title, date, false);
		todos.add(newToDo);
		
		// For checking current todos 
		System.out.println(todos);
		
		return newToDo; 
	}

	@Override
	public Optional<ToDo> updateTitle(int id, String title) {
		Optional<ToDo> targetTodo = todos.stream().filter(todo -> todo.getId() == id).findFirst();
		if(targetTodo.isPresent()) targetTodo.get().setTitle(title);
		
		// For checking current todos 
		System.out.println(todos);

		return targetTodo;
	}

	@Override
	public Optional<ToDo> updateDate(int id, LocalDate date) {
		Optional<ToDo> targetTodo = todos.stream().filter(todo -> todo.getId() == id).findFirst();
		if(targetTodo.isPresent()) targetTodo.get().setDate(date);
				
		// For checking current todos 
		System.out.println(todos);

		return targetTodo;
	}

	@Override
	public Optional<ToDo> updateCompleted(int id, boolean completed) {
		Optional<ToDo> targetTodo = todos.stream().filter(todo -> todo.getId() == id).findFirst();		
		if(targetTodo.isPresent()) targetTodo.get().setCompleted(completed);
				
		// For checking current todos 
		System.out.println(todos);
		
		return targetTodo;
	}

	@Override
	public Optional<Integer> delete(int id) {
		boolean success = todos.removeIf(todo->todo.getId() == id);
				
		// For checking current todos 
		System.out.println(todos);
	
		return success ? Optional.of(id) : Optional.empty();
	}

}
