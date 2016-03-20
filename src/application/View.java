package application;

import application.alarm.Alarm;
import application.alarm.AlarmView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class View {
	
	private static Stage pStage = null;
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
			
			Text nomAlarm = new Text();
			Label descAlarm = new Label();
			Text prioriteAlarm = new Text();
			Text treatedAlarm = new Text();
			
			Button buttonAddAlarmRandom = new Button();
			Button buttonSaveAlarms = new Button();
			Button buttonDeleteAlarm = new Button();
			Button buttonTreatAlarm = new Button();
			Button buttonSortByTime = new Button();
			Button buttonSortByPriority = new Button();
			
			/* Ajout de la feuille de style css */
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			/* Mise à jour controller */
			
			this.controller = c;
			
			/* Chargement des alarmes */
			
			this.scrollAlarm = new ListView<AlarmView>();
			
			for(Alarm a : this.getController().getModel().getListAlarm()){
				this.scrollAlarm.getItems().add(new AlarmView(a, scene.getWidth() - 210));
			}
			
			/* Permet de ne jamais avoir 2 alarmes identiques (provisoire) */
			
			if((this.scrollAlarm.getItems().size() - 1) != -1)
				Alarm.cptRandom = Integer.parseInt(this.scrollAlarm.getItems().get(this.scrollAlarm.getItems().size() - 1).getAlarm().getNom().substring(7));
			
			/* Réglage de la scène */
			
			primaryStage.setMinHeight(425);
			primaryStage.setMinWidth(718);
			
			/* Définition style des labels */
			
			nomAlarm.setLayoutX(20);
			nomAlarm.setLayoutY(40);
			nomAlarm.setId("nom-infoalarm");
			
			descAlarm.setLayoutX(20);
			descAlarm.setLayoutY(50);
			descAlarm.setPrefWidth(960);
			descAlarm.setWrapText(true);
			descAlarm.setId("desc-infoalarm");
			
			prioriteAlarm.setLayoutX(20);
			prioriteAlarm.setLayoutY(180);
			prioriteAlarm.setId("priorite-infoalarm");
			
			treatedAlarm.setLayoutX(250);
			treatedAlarm.setLayoutY(180);
			treatedAlarm.setId("treated-infoalarm");
			
			/* Positionnement des groupes */
			
			botLeft.setLayoutX(0);
			botLeft.setLayoutY(200);
			
			botRight.setLayoutX(200);
			botRight.setLayoutY(200);
			
			/* Réglages des rectangles */
			
			topRect.setWidth(1000);
			topRect.setHeight(200);
			topRect.setFill(Color.DARKGREY);

			botLeftRect.setWidth(200);
			botLeftRect.setHeight(550);
			botLeftRect.setFill(Color.CORNSILK);

			botRightRect.setWidth(800);
			botRightRect.setHeight(550);
			botRightRect.setFill(Color.ALICEBLUE);
			
			/* Paramétrage du scrollList */

			scrollAlarm.setPrefWidth(800);
			scrollAlarm.setPrefHeight(550);
			
			for(AlarmView av : scrollAlarm.getItems()){
				av.setFondWidth(scene.getWidth() - 210);
			}

			scrollAlarm.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AlarmView>() {

			    @Override
			    public void changed(ObservableValue<? extends AlarmView> observable, AlarmView oldValue, AlarmView newValue) {
			        getController().majInfoAlarm(newValue, nomAlarm, descAlarm, prioriteAlarm, treatedAlarm);
			    }
			});
			
			/* Responsive Design */
			
			scene.widthProperty().addListener(new ChangeListener<Number>() {
			    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
			    	topRect.setWidth(newSceneWidth.floatValue());
					botRightRect.setWidth(newSceneWidth.floatValue() - 200);
					descAlarm.setPrefWidth(newSceneWidth.floatValue() - 40);
					scrollAlarm.setPrefWidth(newSceneWidth.floatValue());
					
					for(AlarmView av : scrollAlarm.getItems()){
						av.setFondWidth(newSceneWidth.floatValue() - 210);
					}
			    }
			});
			
			scene.heightProperty().addListener(new ChangeListener<Number>() {
			    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
			    	botLeftRect.setHeight(newSceneHeight.floatValue() - 200);
			    	botRightRect.setHeight(newSceneHeight.floatValue() - 200);
			    	scrollAlarm.setPrefHeight(newSceneHeight.floatValue() - 200);
			    }
			});
			
			/* Paramétrage des boutons */
			
			buttonAddAlarmRandom.setLayoutX(5);
			buttonAddAlarmRandom.setLayoutY(5);
			buttonAddAlarmRandom.setPrefWidth(190);
			buttonAddAlarmRandom.setText("Add Random Alarm");
			buttonAddAlarmRandom.setOnAction(new EventHandler<ActionEvent>(){
				
				/* Ajout d'une alarme aléatoire */
				
				@Override
				public void handle(ActionEvent arg0) {			
					getController().putAlarm(scrollAlarm);		
				}
				
			});
			
			buttonSaveAlarms.setLayoutX(5);
			buttonSaveAlarms.setLayoutY(40);
			buttonSaveAlarms.setPrefWidth(190);
			buttonSaveAlarms.setText("Save All");
			buttonSaveAlarms.setOnAction(new EventHandler<ActionEvent>(){

				/* Sauvegarde des alarmes */
				
				@Override
				public void handle(ActionEvent arg0) {
					getController().saveAlarms();				
				}
				
			});	
			
			buttonDeleteAlarm.setLayoutX(5);
			buttonDeleteAlarm.setLayoutY(75);
			buttonDeleteAlarm.setPrefWidth(190);
			buttonDeleteAlarm.setText("Delete Alarm");
			buttonDeleteAlarm.setOnAction(new EventHandler<ActionEvent>(){

				/* Suppresion d'une alarme aléatoire */
				
				@Override
				public void handle(ActionEvent arg0) {
					getController().deleteAlarm(scrollAlarm);				
				}
				
			});	
			
			buttonTreatAlarm.setLayoutX(5);
			buttonTreatAlarm.setLayoutY(110);
			buttonTreatAlarm.setPrefWidth(190);
			buttonTreatAlarm.setText("Treat Alarm");
			buttonTreatAlarm.setOnAction(new EventHandler<ActionEvent>(){

				/* Traitement d'une alarme aléatoire */
				
				@Override
				public void handle(ActionEvent arg0) {
					getController().treatAlarm(scrollAlarm, treatedAlarm);				
				}
				
			});	
			
			buttonSortByTime.setLayoutX(5);
			buttonSortByTime.setLayoutY(145);
			buttonSortByTime.setPrefWidth(90);
			buttonSortByTime.setText("Sort Time");
			buttonSortByTime.setOnAction(new EventHandler<ActionEvent>(){

				/* Tri part date de création */
				
				@Override
				public void handle(ActionEvent arg0) {
					getController().sortAlarmsByTime(scrollAlarm);				
				}
				
			});	
			
			buttonSortByPriority.setLayoutX(105);
			buttonSortByPriority.setLayoutY(145);
			buttonSortByPriority.setPrefWidth(90);
			buttonSortByPriority.setText("Sort Prio");
			buttonSortByPriority.setOnAction(new EventHandler<ActionEvent>(){

				//* Tri part priorité */
				
				@Override
				public void handle(ActionEvent arg0) {
					getController().sortAlarmsByPriority(scrollAlarm);				
				}
				
			});	
			
			/* Ajout éléments à la scène */			
			
			top.getChildren().add(topRect);
			top.getChildren().add(nomAlarm);
			top.getChildren().add(descAlarm);
			top.getChildren().add(prioriteAlarm);
			top.getChildren().add(treatedAlarm);
			
			botLeft.getChildren().add(botLeftRect);
			botLeft.getChildren().add(buttonAddAlarmRandom);
			botLeft.getChildren().add(buttonSaveAlarms);
			botLeft.getChildren().add(buttonDeleteAlarm);
			botLeft.getChildren().add(buttonTreatAlarm);
			botLeft.getChildren().add(buttonSortByTime);
			botLeft.getChildren().add(buttonSortByPriority);
			
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
