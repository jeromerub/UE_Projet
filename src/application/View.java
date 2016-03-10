package application;
	
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


public class View {
	
	private static Stage pStage;
	private Controller controller;
	
	public View(Stage primaryStage, Controller c) {
		try {
			/* D�clarations */
			
			Group root = new Group();
			Scene scene = new Scene(root, 1000, 750, Color.BEIGE);
			
			Group top = new Group();
			Group botLeft = new Group();
			Group botRight = new Group();
			
			Rectangle topRect = new Rectangle();
			Rectangle botLeftRect = new Rectangle();
			Rectangle botRightRect = new Rectangle();
			
			Button buttonAddAlarmRandom = new Button();
			
			/* Mise � jour controller */
			
			this.controller = c;
			
			/* Ajout de la feuille de style css */
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			/* Positionnement des groupes */
			
			botLeft.setLayoutX(0);
			botLeft.setLayoutY(200);
			
			botRight.setLayoutX(500);
			botRight.setLayoutY(200);
			
			/* R�glages des rectangles */
			
			topRect.setWidth(1000);
			topRect.setHeight(200);
			topRect.setFill(Color.DARKGREY);

			botLeftRect.setWidth(500);
			botLeftRect.setHeight(550);
			botLeftRect.setFill(Color.CORNSILK);

			botRightRect.setWidth(500);
			botRightRect.setHeight(550);
			botRightRect.setFill(Color.ALICEBLUE);
			
			/* Positionnement des boutons */
			
			buttonAddAlarmRandom.setLayoutX(5);
			buttonAddAlarmRandom.setLayoutY(5);
			buttonAddAlarmRandom.setText("Add Random Alarm");
			buttonAddAlarmRandom.setOnAction(new EventHandler<ActionEvent>(){

				@Override
				public void handle(ActionEvent arg0) {
					AlarmView a = new AlarmView(new Alarm());
					
					botRight.getChildren().add(a);
				}
				
			});
			
			/* Ajout �l�ments � la sc�ne */			
			
			top.getChildren().add(topRect);
			
			botLeft.getChildren().add(botLeftRect);
			botLeft.getChildren().add(buttonAddAlarmRandom);
			
			botRight.getChildren().add(botRightRect);
			
			root.getChildren().add(top);
			root.getChildren().add(botLeft);
			root.getChildren().add(botRight);
			
			this.setPrimaryStage(primaryStage);
			
			primaryStage.setScene(scene);
			primaryStage.show();
			
		/* R�cup�ration des exceptions potentielles */
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public Controller getController(){
		return this.controller;
	}
	
	public static Stage getPrimaryStage() {
        return pStage;
    }

    private void setPrimaryStage(Stage pStage) {
        View.pStage = pStage;
    }
}
