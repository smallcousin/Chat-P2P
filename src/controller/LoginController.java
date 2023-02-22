package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private Main app;
    @FXML private TextField username;
    @FXML private Button btn;
    @FXML
    public boolean Login() throws Exception {
        String userName=username.getText();
        System.out.print(username.getText());
        app.login(username.getText());
        return true;
    }
    public void setApp(Main app){
        this.app=app;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
