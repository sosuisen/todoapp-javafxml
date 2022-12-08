// 'myapp': Your module name
// 'com.example': Your package name
module myapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
	requires com.google.gson;
    
    opens com.example to javafx.fxml;
    exports com.example;
}