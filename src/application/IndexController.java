package application;

import application.CurrentUser;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class IndexController {
	private CurrentUser user;
	
	@FXML
	private Button homeBtn;
	@FXML
	private Button loginBtn;
	@FXML
	private Button registerBtn;
	
	
	public IndexController setCurrentUser(CurrentUser second) {
		user = second;
		return this;
	}
	
	public void onRegisterBtnClick() {
		registerBtn.setText(user.getIme());
		return;
	}
}
