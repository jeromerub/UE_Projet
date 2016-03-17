package application;

import application.alarm.Alarm;
import application.alarm.AlarmView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


public class View {
	
	private static Stage pStage;
	private Controller controller;
	
	private ListView<AlarmView> scrollAlarm;
	
	public View(Stage primaryStage, Controller c) {
		try {
			/* Déclarations */
			
			Group root = new Group();
			Scene scene = new Scene(root, 1000, 750, Color.BEIGE);
			
			Group top = new Group();
			Group botLeft = new Group();
			Group botRight = new Group();
			
			Rectangle topRect = new Rectangle();
			Rectangle botLeftRect = new Rectangle();
			Rectangle botRightRect = new Rectangle();
			
			Button buttonAddAlarmRandom = new Button();
			Button buttonSaveAlarms = new Button();
			
			/* Ajout de la feuille de style css */
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			/* Mise à jour controller */
			
			this.controller = c;
			
			/* Chargement des alarmes */
			
			this.scrollAlarm = new ListView<AlarmView>();
			
			for(Alarm a : this.getController().getModel().getListAlarm()){
				this.scrollAlarm.getItems().add(new AlarmView(a));
			}

			
			/* Positionnement des groupes */
			
			botLeft.setLayoutX(0);
			botLeft.setLayoutY(200);
			
			botRight.setLayoutX(485);
			botRight.setLayoutY(200);
			
			/* Réglages des rectangles */
			
			topRect.setWidth(1000);
			topRect.setHeight(200);
			topRect.setFill(Color.DARKGREY);

			botLeftRect.setWidth(500);
			botLeftRect.setHeight(550);
			botLeftRect.setFill(Color.CORNSILK);

			botRightRect.setWidth(500);
			botRightRect.setHeight(550);
			botRightRect.setFill(Color.ALICEBLUE);
			
			/* Paramétrage du scrollList */
			
			scrollAlarm.setPrefWidth(515);
			scrollAlarm.setPrefHeight(550);
			
			/* Paramétrage des boutons */
			
			buttonAddAlarmRandom.setLayoutX(5);
			buttonAddAlarmRandom.setLayoutY(5);
			buttonAddAlarmRandom.setText("Add Random Alarm");
			buttonAddAlarmRandom.setOnAction(new EventHandler<ActionEvent>(){

				@Override
				public void handle(ActionEvent arg0) {			
					getController().putAlarm(scrollAlarm);		
				}
				
			});
			
			buttonSaveAlarms.setLayoutX(5);
			buttonSaveAlarms.setLayoutY(40);
			buttonSaveAlarms.setText("Save All");
			buttonSaveAlarms.setOnAction(new EventHandler<ActionEvent>(){

				@Override
				public void handle(ActionEvent arg0) {
					getController().saveAlarms();				
				}
				
			});	
			
			/* Ajout éléments à la scène */			
			
			top.getChildren().add(topRect);
			
			botLeft.getChildren().add(botLeftRect);
			botLeft.getChildren().add(buttonAddAlarmRandom);
			botLeft.getChildren().add(buttonSaveAlarms);
			
			botRight.getChildren().add(botRightRect);
			botRight.getChildren().add(scrollAlarm);
			
			root.getChildren().add(top);
			root.getChildren().add(botLeft);
			root.getChildren().add(botRight);
			
			this.setPrimaryStage(primaryStage);
			
			primaryStage.setScene(scene);
			primaryStage.show();
			
		/* Récupération des exceptions potentielles */
			
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
