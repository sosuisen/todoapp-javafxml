package com.example;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MyAppController {

	@FXML
	private Button button;

	@FXML
	private Label label;

	public void initialize() {
		var javaVersion = System.getProperty("java.version");
		var javafxVersion = System.getProperty("javafx.version");
		
		label.setText("Hello, JavaFX " + javafxVersion + "\nRunning on Java " + javaVersion + ".");

		button.setOnAction(e -> Platform.exit());
	}
}
