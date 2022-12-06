package com.example;

import java.time.LocalDate;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
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

	private ObservableList<Node> todoListHBoxes; 

	private HBox createToDoHBox(ToDo todo) {
		var idLabel = new Label(String.valueOf(todo.getId()));
		var titleLabel = new Label(todo.getTitle());
		var dateLabel = new Label(todo.getDate().toString());
		var completedLabel = new Label(String.valueOf(todo.isCompleted()));
		return new HBox(idLabel, titleLabel, dateLabel, completedLabel);
	}
	
	public void initialize() {
		// デフォルトで datePicker に今日の日付をセット
		datePicker.setValue(LocalDate.now());
		
		todoListHBoxes = todoListVBox.getChildren();		
		// ToDo読み込み
		dao.getAll().forEach(todo->{
			todoListHBoxes.add(createToDoHBox(todo));
		});
		
		// ToDo追加
		addBtn.setOnAction(e -> {
			var title = titleField.getText();
			LocalDate utcDate = datePicker.getValue();
			// e.g.) 2022-12-06
			// System.err.println("Selected date: " + utcDate);
			ToDo newToDo = dao.create(title, utcDate);
			// dao.getAll().stream().forEach(todo->System.out.println(todo));
			todoListHBoxes.add(createToDoHBox(newToDo));
		});
		
		//TODO; ToDo更新
		
		//TODO: ToDo削除
		
	}
}
