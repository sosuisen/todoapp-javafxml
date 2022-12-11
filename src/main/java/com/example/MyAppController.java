package com.example;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashMap;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MyAppController {
	final String dataPath = "./tododata.json";

	DAO dao = new MemoryDAO(dataPath);

	final String TODO_COMPLETED = "Completed";
	final String TODO_TITLE = "Title";
	final String TODO_DATE = "Date";
	final LinkedHashMap<String, Integer> MENU = new LinkedHashMap<String, Integer>() {{
		put(TODO_COMPLETED, 0);
		put(TODO_TITLE, 1);
		put(TODO_DATE, 2);
	}};
	final String SORT_ASCENDANT = "Ascendant";
	final String SORT_DESCENDANT = "Descendant";
	
	@FXML
	private Button addBtn;

	@FXML
	private DatePicker datePicker;

	@FXML
	private TextField titleField;

	@FXML
	private VBox todoListVBox;

	@FXML
	private MenuItem menuItemAbout;

	@FXML
	private MenuItem menuItemClose;

	@FXML
	private ChoiceBox<String> sortTypeMenu;

	@FXML
	private ChoiceBox<String> sortOrderMenu;

	@FXML
	void onItemMenuAbout(ActionEvent event) {
		showInfo("ToDo App");
	}

	@FXML
	void onMenuItemClose(ActionEvent event) {
		Platform.exit();
	}

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
		titleField.focusedProperty().addListener((observable, oldProperty, newProperty) -> {
			if (!newProperty) {
				dao.updateTitle(todo.getId(), titleField.getText());
			}
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

	private void showInfo(String txt) {
		Alert dialog = new Alert(AlertType.INFORMATION);
		dialog.setHeaderText(null);
		dialog.setContentText(txt);
		dialog.showAndWait();
	}

	private void showError(String txt) {
		Alert dialog = new Alert(AlertType.ERROR);
		dialog.setHeaderText(null);
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

	private void sort(String type, String order) {
		Comparator<Node> comp = null;
		switch(type){
		case TODO_COMPLETED:
			comp = Comparator.comparing(node -> ((CheckBox)((HBox)node).getChildren().get(MENU.get(TODO_COMPLETED))).isSelected());
			break;
		case TODO_TITLE:
			comp = Comparator.comparing(node -> ((TextField)((HBox)node).getChildren().get(MENU.get(TODO_TITLE))).getText());
			break;
		case TODO_DATE:
		default:
			comp = Comparator.comparing(node -> ((DatePicker)((HBox)node).getChildren().get(MENU.get(TODO_DATE))).getValue());
			break;
		}
		if (order.equals(SORT_DESCENDANT)) {
			comp = comp.reversed();
		}
		FXCollections.sort(todoListItems, comp);
	}

	public void initialize() {
		sortTypeMenu.getItems().addAll(MENU.keySet());
		sortTypeMenu.setValue(TODO_DATE);
		sortTypeMenu.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> sort(newValue, sortOrderMenu.getValue()));
		
		sortOrderMenu.getItems().addAll(SORT_ASCENDANT, SORT_DESCENDANT);
		sortOrderMenu.setValue(SORT_ASCENDANT);
		sortOrderMenu.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> sort(sortTypeMenu.getValue(), newValue));


		// Set today
		datePicker.setValue(LocalDate.now());

		todoListItems = todoListVBox.getChildren();

		dao.getAll().stream()
		.sorted(Comparator.comparing(ToDo::getDate))
		.forEach(todo -> {
			todoListItems.add(createToDoHBox(todo));
		});

		EventHandler<ActionEvent> handler = e -> {
			var title = titleField.getText();
			if (title.equals(""))
				return;
			LocalDate localDate = datePicker.getValue(); // 2022-12-01
			ToDo newToDo = dao.create(title, localDate.toString());
			todoListItems.add(createToDoHBox(newToDo));
			sort(sortTypeMenu.getValue(), sortOrderMenu.getValue());
			titleField.setText("");
		};
		titleField.setOnAction(handler);
		addBtn.setOnAction(handler);
	}
}
