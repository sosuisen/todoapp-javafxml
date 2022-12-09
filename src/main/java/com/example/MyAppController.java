package com.example;

import java.io.IOException;
import java.time.LocalDate;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MyAppController {
	final String dataPath = "./tododata.json";

	DAO dao = new MemoryDAO(dataPath);

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

		var datePicker = new DatePicker(todo.getLocalDate());
		datePicker.getStyleClass().add("todo-date");
		datePicker.setPrefWidth(105);
		HBox.setHgrow(datePicker, Priority.NEVER);
		datePicker.setOnAction(e -> {
			dao.updateDate(todo.getId(), datePicker.getValue().toString());
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

	private void showError(String txt) {
		Alert dialog = new Alert(AlertType.ERROR);
		dialog.setHeaderText("Error");
		dialog.setContentText(txt);
		dialog.showAndWait();
	}

	/**
	 * Must be called in Application.start() after FXML is loaded
	 * to get stage.
	 */
	public void rendered(Stage stage) {
		// For set beforeunload,
		// use showingProperty instead of setOnCloseRequest
		// https://torutk.hatenablog.jp/entry/20170613/p1
		stage.showingProperty().addListener((observable, oldValue, newValue) -> {
			if (oldValue == true && newValue == false) {
				try {
					((MemoryDAO) dao).save(dataPath);
				} catch (IOException e1) {
					showError("Failed to write to " + dataPath);
					e1.printStackTrace();
				}
			}
		});
	}

	public void initialize() {
		// Set today
		datePicker.setValue(LocalDate.now());

		todoListItems = todoListVBox.getChildren();

		dao.getAll().forEach(todo -> {
			todoListItems.add(createToDoHBox(todo));
		});

		EventHandler<ActionEvent> handler = e -> {
			var title = titleField.getText();
			if (title.equals(""))
				return;
			LocalDate localDate = datePicker.getValue(); // 2022-12-01
			ToDo newToDo = dao.create(title, localDate.toString());
			todoListItems.add(createToDoHBox(newToDo));
			titleField.setText("");
		};
		titleField.setOnAction(handler);
		addBtn.setOnAction(handler);
	}
}
