package com.example;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class MyAppController {
	@FXML
	private Button addBtn;

	@FXML
	private DatePicker datePicker;

	@FXML
	private TextField titleField;

	@FXML
	private VBox todoList;

	public void initialize() {
		// デフォルトで datePicker に今日の日付をセット
		
		addBtn.setOnAction(e -> {
			var str = titleField.getText();
			LocalDate utcDate = datePicker.getValue();
			// e.g.) 2022-12-06
			// System.err.println("Selected date: " + utcDate);

		});
	}
}
