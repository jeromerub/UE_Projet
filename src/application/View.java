package application;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javax.sound.sampled.UnsupportedAudioFileException;
import javafx.scene.media.MediaPlayer;
import application.alarm.Alarm;
import application.alarm.AlarmView;
import application.priorite.Priorite;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.Group;
import javafx.scene.Scene;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.media.Media;
import javafx.scene.control.Alert.AlertType;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import javafx.stage.Stage;

/**
 * Classe représentant la vue du logiciel.
 * @author Floo'
 */
public class View {
	
	private static Stage pStage = null;
	private Controller controller;
	
	private ListView<AlarmView> scrollAlarm;
	private Text nomAlarm;
	private Label descAlarm;
	private Text prioriteAlarm;
	private Text treatedAlarm;
	
	/**
	 * Création de la vue.
	 * @param primaryStage 
	 * 			Notre fenêtre.
	 * @param c 
	 * 			Le controlleur.
	 */
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
			Button buttonResetAlarms = new Button();
			Button buttonDeleteAlarm = new Button();
			Button buttonTreatAlarm = new Button();
			Button buttonSortByTime = new Button();
			Button buttonSortByPriority = new Button();
			
			/* Paramètres de la fenêtre */
			
			primaryStage.setTitle("Gestionnaire d'alarmes multimodal 1.0");
			
			/* Init attributs */
			
			this.nomAlarm = new Text();
			this.descAlarm = new Label();
			this.prioriteAlarm = new Text();
			this.treatedAlarm = new Text();
			this.setPrimaryStage(primaryStage);
			
			/* Ajout de la feuille de style css */
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			/* Mise à jour controller */
			
			this.controller = c;
			
			/* Chargement des alarmes */
			
			this.scrollAlarm = new ListView<AlarmView>();
			
			for(Alarm a : this.getController().getModel().getListAlarm()){
				this.scrollAlarm.getItems().add(new AlarmView(a, scene.getWidth() - 210, this));
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
			
			/* Réglage des rectangles */
			
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
			        getController().majInfoAlarm();
			    }
			});
			
			/* Responsive Design */
			
			scene.widthProperty().addListener(new ChangeListener<Number>() {
				
			    @Override 
			    public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
			    	topRect.setWidth(newSceneWidth.floatValue());
					botRightRect.setWidth(newSceneWidth.floatValue() - 200);
					descAlarm.setPrefWidth(newSceneWidth.floatValue() - 40);
					scrollAlarm.setPrefWidth(newSceneWidth.floatValue());
					
					for(AlarmView av : scrollAlarm.getItems()){
						av.setFondWidth(newSceneWidth.floatValue() - 210);
						av.setDeleteLayoutX(newSceneWidth.floatValue() - 270);
					}
			    }
			});
			
			scene.heightProperty().addListener(new ChangeListener<Number>() {
				
			    @Override 
			    public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
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
					getController().putAlarm();		
					
				}
				
			});
			
			
			buttonResetAlarms.setLayoutX(5);
			buttonResetAlarms.setLayoutY(75);
			buttonResetAlarms.setPrefWidth(190);
			buttonResetAlarms.setText("Reset All");
			buttonResetAlarms.setOnAction(new EventHandler<ActionEvent>(){

				/* Réinitialisation des alarmes */
				
				@Override
				public void handle(ActionEvent arg0) {
					getController().resetAlarms();				
				}
				
			});
			
			buttonDeleteAlarm.setLayoutX(5);
			buttonDeleteAlarm.setLayoutY(110);
			buttonDeleteAlarm.setPrefWidth(190);
			buttonDeleteAlarm.setText("Delete Alarm");
			buttonDeleteAlarm.setOnAction(new EventHandler<ActionEvent>(){

				/* Suppresion d'une alarme */
				
				@Override
				public void handle(ActionEvent arg0) {
					getController().deleteAlarm(getSelectedAlarm());				
				}
				
			});	
			
			buttonTreatAlarm.setLayoutX(5);
			buttonTreatAlarm.setLayoutY(145);
			buttonTreatAlarm.setPrefWidth(190);
			buttonTreatAlarm.setText("Treat Alarm");
			buttonTreatAlarm.setOnAction(new EventHandler<ActionEvent>(){

				/* Traitement d'une alarme */
				
				@Override
				public void handle(ActionEvent arg0) {
					getController().treatAlarm(getSelectedAlarm());				
				}
				
			});	
			
			buttonSortByTime.setLayoutX(5);
			buttonSortByTime.setLayoutY(180);
			buttonSortByTime.setPrefWidth(90);
			buttonSortByTime.setText("Sort Time");
			buttonSortByTime.setOnAction(new EventHandler<ActionEvent>(){

				/* Tri par date de création */
				
				@Override
				public void handle(ActionEvent arg0) {
					getController().sortAlarmsByTime();				
				}
				
			});	
			
			buttonSortByPriority.setLayoutX(105);
			buttonSortByPriority.setLayoutY(180);
			buttonSortByPriority.setPrefWidth(90);
			buttonSortByPriority.setText("Sort Prio");
			buttonSortByPriority.setOnAction(new EventHandler<ActionEvent>(){

				/* Tri par priorité */
				
				@Override
				public void handle(ActionEvent arg0) {
					getController().sortAlarmsByPriority();				
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
			botLeft.getChildren().add(buttonResetAlarms);
			botLeft.getChildren().add(buttonDeleteAlarm);
			botLeft.getChildren().add(buttonTreatAlarm);
			botLeft.getChildren().add(buttonSortByTime);
			botLeft.getChildren().add(buttonSortByPriority);
			
			botRight.getChildren().add(botRightRect);
			botRight.getChildren().add(scrollAlarm);
			
			root.getChildren().add(top);
			root.getChildren().add(botLeft);
			root.getChildren().add(botRight);
			
			primaryStage.setScene(scene);
			primaryStage.show();
			
		/* Catch exceptions */
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Rafraichissement des infos sur l'alarme selectionnée.
	 */
	public void refreshTopDesc(){
		AlarmView alarm = this.scrollAlarm.getSelectionModel().getSelectedItem();
		
		if(alarm != null){
			this.nomAlarm.setText(alarm.getAlarm().getNom());
			this.descAlarm.setText(alarm.getAlarm().getDesc());
			this.prioriteAlarm.setText("Priorité : " + alarm.getAlarm().getPriorite());
			
			if(alarm.getAlarm().isTreated()) this.treatedAlarm.setText("Alarme traitée");
			else this.treatedAlarm.setText("Alarme non-traitée");
		} else {
			this.nomAlarm.setText("");
			this.descAlarm.setText("");
			this.prioriteAlarm.setText("");
			this.treatedAlarm.setText("");
		}
	}
	
	/**
	 * Rafraichissement de la liste d'alarme.
	 */
	public void refreshList(){
		ListView<AlarmView> list = this.getController().getAlarmsAsListView();	
		this.setListView(list);
	}
	
	/**
	 * Affiche un popUp de validation de suppression et attend la réponse.
	 * @param a 
	 * 			Alarme à supprimer.
	 * @return La réponse de l'utilisateur (OK ou Annuler).
	 */
	public ButtonType popDeleteDialog(Alarm a){
		Alert dialog = new Alert(AlertType.CONFIRMATION);
		Optional<ButtonType> result;
		
		dialog.setTitle("Confirmation");
		dialog.setHeaderText("Supprimer une alarme.");
		dialog.setContentText("Etes-vous sur de vouloir supprimer cette alarme : " + a.getNom());
		
		result = dialog.showAndWait();
		
		return result.get();
	}
	
	/**
	 * Affiche un popUp de validation de réinitialisation et attend la réponse.
	 * @return La réponse de l'utilisateur (OK ou Annuler)
	 */
	public ButtonType popResetDialog(){
		Alert dialog = new Alert(AlertType.CONFIRMATION);
		Optional<ButtonType> result;
		
		dialog.setTitle("Confirmation");
		dialog.setHeaderText("Reinitialiser les alarme.");
		dialog.setContentText("Etes-vous sur de vouloir réinitialiser les alarmes ?");
		
		result = dialog.showAndWait();
		
		return result.get();
	}
	
	/**
	 * Emet un son selon la priorité.
	 * @param Priorité de l'alarm qui doit etre émise 
	 * @throws IOException 
	 * @throws UnsupportedAudioFileException 
	 */
	public void emettreSon(Priorite p){
		File f = new File("src/application/son/alarm.mp3");
		Media media = new Media(f.toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		
		
		if (p.equals(Priorite.Max)){
			mediaPlayer.setVolume(1);
		}
		if (p.equals(Priorite.Haute)){
			mediaPlayer.setVolume(0.75);
		}
		if (p.equals(Priorite.Moyenne)){
			mediaPlayer.setVolume(0.5);
		}
		if (p.equals(Priorite.Basse)){
			mediaPlayer.setVolume(0.25);
		}
		
		mediaPlayer.play();
	}
	
	public Alarm getSelectedAlarm(){
		if(this.scrollAlarm.getSelectionModel().getSelectedItem() != null){
			return this.scrollAlarm.getSelectionModel().getSelectedItem().getAlarm();
		}
		
		return null;
	}
	
	/**
	 * @return Liste des alarmes graphiques.
	 */
	public ListView<AlarmView> getListView(){
		return this.scrollAlarm;
	}
	
	/**	
	 * @param list
	 * 			Nouvelle liste d'alarmes graphique.
	 */
	public void setListView(ListView<AlarmView> list){
		this.scrollAlarm.setItems(null);
		this.scrollAlarm.setItems(list.getItems());
	}
	
	/**
	 * @return Référence vers l'objet Stage.
	 */
	public static Stage getPrimaryStage() {
        return pStage;
    }

    /**
     * @param pStage
     */
    private void setPrimaryStage(Stage pStage) {
        View.pStage = pStage;
    }
	
	/**
	 * @return Le controlleur
	 */
	public Controller getController(){
		return this.controller;
	}
}
