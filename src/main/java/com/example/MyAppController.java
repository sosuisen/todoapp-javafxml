package com.example;

import java.time.LocalDate;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class MyAppController {
	DAO dao = new MemoryDAO();

	@FXML
	private Button addBtn;

	@FXML
	private DatePicker datePicker;

	@FXML
	private TextField titleField;
	@FXML
	private VBox todoListVBox;

	private ObservableList<Node> todoListItems;

	private HBox createToDoHBox(ToDo todo) {
		var completedCheckBox = new CheckBox();
		completedCheckBox.setSelected(todo.isCompleted());
		completedCheckBox.getStyleClass().add("todo-completed");
		completedCheckBox.setOnAction(e -> {
			dao.updateCompleted(todo.getId(), completedCheckBox.isSelected());
		});
		
		var titleField = new TextField(todo.getTitle());
		titleField.getStyleClass().add("todo-title");
		HBox.setHgrow(titleField, Priority.ALWAYS);
		titleField.setOnAction(e -> {
			dao.updateTitle(todo.getId(), titleField.getText());			
		});
		
		var datePicker = new DatePicker(todo.getDate());
		datePicker.getStyleClass().add("todo-date");
		datePicker.setPrefWidth(105);
		HBox.setHgrow(datePicker, Priority.NEVER);
		datePicker.setOnAction(e -> {
			dao.updateDate(todo.getId(), datePicker.getValue());			
		});

		var deleteBtn = new Button("Delete");
		deleteBtn.getStyleClass().add("todo-delete");

		var todoItem = new HBox(completedCheckBox, titleField, datePicker, deleteBtn);
		todoItem.getStyleClass().add("todo-item");
		
		deleteBtn.setOnAction(e -> {
			dao.delete(todo.getId());
			todoListItems.remove(todoItem);
		});
		
		return todoItem;
	}

	public void initialize() {
		// Set today
		datePicker.setValue(LocalDate.now());

		todoListItems = todoListVBox.getChildren();

		dao.getAll().forEach(todo -> {
			todoListItems.add(createToDoHBox(todo));
		});

		addBtn.setOnAction(e -> {
			var title = titleField.getText();
			LocalDate utcDate = datePicker.getValue();
			// System.err.println("Selected date: " + utcDate); // 2022-12-06
			ToDo newToDo = dao.create(title, utcDate);
			// Use Method Reference
			// dao.getAll().stream().forEach(System.out::println);
			todoListItems.add(createToDoHBox(newToDo));
		});
	}
}
