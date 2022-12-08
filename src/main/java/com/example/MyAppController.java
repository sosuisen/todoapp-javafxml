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
		// var idLabel = new Label(String.valueOf(todo.getId()));
		var completedCheckBox = new CheckBox();
		completedCheckBox.setSelected(todo.isCompleted());
		completedCheckBox.getStyleClass().add("todo-completed");

		var titleField = new TextField(todo.getTitle());
		titleField.getStyleClass().add("todo-title");
		HBox.setHgrow(titleField, Priority.ALWAYS);
		
		var datePicker = new DatePicker(todo.getDate());
		datePicker.getStyleClass().add("todo-date");
		datePicker.prefWidth(105);
		datePicker.setMaxWidth(105);
		HBox.setHgrow(datePicker, Priority.NEVER);
		
		var todoItem = new HBox(completedCheckBox, titleField, datePicker);
		todoItem.getStyleClass().add("todo-item");
		return todoItem;
	}

	public void initialize() {
		// デフォルトで datePicker に今日の日付をセット
		datePicker.setValue(LocalDate.now());

		todoListItems = todoListVBox.getChildren();
		// ToDo読み込み
		dao.getAll().forEach(todo -> {
			todoListItems.add(createToDoHBox(todo));
		});

		// ToDo追加
		addBtn.setOnAction(e -> {
			var title = titleField.getText();
			LocalDate utcDate = datePicker.getValue();
			// System.err.println("Selected date: " + utcDate); // 2022-12-06
			ToDo newToDo = dao.create(title, utcDate);
			// メソッド参照
			// dao.getAll().stream().forEach(System.out::println);
			todoListItems.add(createToDoHBox(newToDo));
		});

		//TODO; ToDo更新

		//TODO: ToDo削除

	}
}
