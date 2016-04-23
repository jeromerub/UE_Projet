package application.main;

import application.Controller;
import application.Model;
import application.View;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Classe Main, qui instancie notre MVC et lance notre gestionnaire
 * @author Floo'
 */
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			
			/* Instanciation de notre pattern MVC */

			Model model = new Model();
			Controller controller = new Controller(model);
			View view = new View(primaryStage, controller);
			model.setView(view);	
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
		}
	}
	
	public static void main(String[] args) {
		
		launch(args);
		
	}
}
