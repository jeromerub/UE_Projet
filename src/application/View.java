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
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import javafx.stage.Stage;
import javafx.util.Duration;

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
	
	
	Button buttonAddAlarmRandom;
	Button buttonResetAlarms;
	Button buttonDeleteAlarm;
	Button buttonTreatAlarm;
	Button buttonSortByTime ;
	Button buttonSortByPriority;
	
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
			
			/* Paramètres de la fenêtre */
			
			primaryStage.setTitle("Gestionnaire d'alarmes multimodal 1.0");
			
			/* Init attributs */
			
			this.nomAlarm = new Text();
			this.descAlarm = new Label();
			this.prioriteAlarm = new Text();
			this.treatedAlarm = new Text();
			
			this.buttonAddAlarmRandom = new Button();
			this.buttonResetAlarms = new Button();
			this.buttonDeleteAlarm = new Button();
			this.buttonTreatAlarm = new Button();
			this.buttonSortByTime = new Button();
			this.buttonSortByPriority = new Button();
		
			primaryStage.setScene(scene);
			this.setPrimaryStage(primaryStage);
			
			/* Ajout de la feuille de style css */
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			/* Mise à jour controller */
			
			this.controller = c;
			
			/* Mise à jour du model */
			
			this.getController().getModel().setView(this);
			
			/* Chargement des alarmes */
			
			this.scrollAlarm = this.getController().getAlarmsAsListView();
			
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
			
			/* Activations et désactivation boutons */
			
			switch (getController().getModel().getVisualListAlarm().size()){
				case 0:
					disableButtonResetSortDeleteAndTreat();
					break;
				case 1:
					disableButtonSortDeleteAndTreat();
					break;
				default:
					disableButtonDeleteAndTreat();
					break;
			}
			
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
					
					switch (getController().getModel().getVisualListAlarm().size()){
						case 0:
							//Interdit
						case 1:
							disableButtonSortDeleteAndTreat();
							break;
						default:
							disableButtonDeleteAndTreat();
							break;
					}
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
					
					switch (getController().getModel().getVisualListAlarm().size()){
						case 0:
							disableButtonResetSortDeleteAndTreat();
							break;
						case 1:
							disableButtonSortDeleteAndTreat();
							break;
						default:
							disableButtonDeleteAndTreat();
							break;
					}
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
					
					switch (getController().getModel().getVisualListAlarm().size()){
						case 0:
							disableButtonResetSortDeleteAndTreat();
							break;
						case 1:
							disableButtonSortDeleteAndTreat();
							break;
						default:
							disableButtonDeleteAndTreat();
							break;
					}
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
					
					switch (getController().getModel().getVisualListAlarm().size()){
						case 0:
							//Interdit
						case 1:
							disableButtonSortDeleteAndTreat();
							break;
						default:
							disableButtonDeleteAndTreat();
							break;
					}
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
					
					switch (getController().getModel().getVisualListAlarm().size()){
						case 0:
							//Interdit
						case 1:
							//Interdit
						default:
							disableButtonDeleteAndTreat();
							break;
					}
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
					switch (getController().getModel().getVisualListAlarm().size()){
						case 0:
							//Interdit
						case 1:
							//Interdit
						default:
							disableButtonDeleteAndTreat();
							break;
					}
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
			if (alarm.getAlarm().isTreated()){
				switch (getController().getModel().getVisualListAlarm().size()){
					case 0:
						//Interdit
					case 1:
						disableButtonSortAndTreat();
						break;
					default:
						disableButtonTreat();
						break;
				}
			} else {
				switch (getController().getModel().getVisualListAlarm().size()){
					case 0:
						//Interdit
					case 1:
						disableButtonSort();
						break;
					default:
						noDisableButton();
						break;
				}
			}
			
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
	 * @return Alarme selectionnée.
	 */
	/**
	 * Emet un son 
	 */
	public void emettreSon(){
		File f1 = new File("src/application/son/alarm.mp3");
		Media media1 = new Media(f1.toURI().toString());
		MediaPlayer mediaPlayer1 = new MediaPlayer(media1);
		
		File f2 = new File("src/application/son/alarm.mp3");
		Media media2 = new Media(f2.toURI().toString());
		MediaPlayer mediaPlayer2 = new MediaPlayer(media2);
		
		mediaPlayer1.setBalance(-1);
		mediaPlayer2.setBalance(1);
		
		mediaPlayer1.play();
		mediaPlayer2.setStartTime(Duration.seconds(0.5));
		mediaPlayer2.play();
	}
	
	/**
	 * Affiche une nouvelle fenêtre avec les infos de la dernière alarme
	 * @param a
	 * 			Alarme a afficher
	 */
	public void newWindow(Alarm a){
		Group root = new Group();
        Scene scene = new Scene(root);

        Stage stage = new Stage();
        
        Group groupNom = new Group();
        Group groupDesc = new Group();
        Group groupPriorite = new Group();
		
        Text nom = new Text(a.getNom());
        Text desc = new Text(a.getDesc());
        Text priorite = new Text("Priorité : " + a.getPriorite().toString());

        stage.setScene(scene);
        stage.setTitle(a.getNom());
        stage.setMinWidth(800);
        stage.setMinHeight(200);
        stage.setX(50);
        stage.setY(50);
        
        groupNom.setLayoutX(10);
        groupNom.setLayoutY(80);
        
        groupDesc.setLayoutX(10);
        groupDesc.setLayoutY(130);
        
        groupPriorite.setLayoutX(10);
        groupPriorite.setLayoutY(180);
        
        groupNom.getChildren().add(nom);
        groupDesc.getChildren().add(desc);
        groupPriorite.getChildren().add(priorite);
        
        nom.setFont(new Font(80));
        desc.setFont(new Font(58));
        priorite.setFont(new Font(58));
        
        root.getChildren().add(groupNom);
        root.getChildren().add(groupDesc);
        root.getChildren().add(groupPriorite);
        
        stage.show();
        //View.pStage.toFront();
	}
	
	/**
	 * @return Alarme selectionnée.
	 */
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
	
	public void disableButtonDeleteAndTreat(){
		buttonAddAlarmRandom.setDisable(false);
		buttonResetAlarms.setDisable(false);
		buttonDeleteAlarm.setDisable(true);
		buttonTreatAlarm.setDisable(true);
		buttonSortByTime.setDisable(false);
		buttonSortByPriority.setDisable(false);
	}
	
	public void disableButtonSortDeleteAndTreat(){
		buttonAddAlarmRandom.setDisable(false);
		buttonResetAlarms.setDisable(false);
		buttonDeleteAlarm.setDisable(true);
		buttonTreatAlarm.setDisable(true);
		buttonSortByTime.setDisable(true);
		buttonSortByPriority.setDisable(true);
	}
	
	public void disableButtonResetSortDeleteAndTreat(){
		buttonAddAlarmRandom.setDisable(false);
		buttonResetAlarms.setDisable(true);
		buttonDeleteAlarm.setDisable(true);
		buttonTreatAlarm.setDisable(true);
		buttonSortByTime.setDisable(true);
		buttonSortByPriority.setDisable(true);
	}
	
	public void noDisableButton(){
		buttonAddAlarmRandom.setDisable(false);
		buttonResetAlarms.setDisable(false);
		buttonDeleteAlarm.setDisable(false);
		buttonTreatAlarm.setDisable(false);
		buttonSortByTime.setDisable(false);
		buttonSortByPriority.setDisable(false);
	}
	
	public void disableButtonTreat(){
		buttonAddAlarmRandom.setDisable(false);
		buttonResetAlarms.setDisable(false);
		buttonDeleteAlarm.setDisable(false);
		buttonTreatAlarm.setDisable(true);
		buttonSortByTime.setDisable(false);
		buttonSortByPriority.setDisable(false);
	}
	
	public void disableButtonSortAndTreat(){
		buttonAddAlarmRandom.setDisable(false);
		buttonResetAlarms.setDisable(false);
		buttonDeleteAlarm.setDisable(false);
		buttonTreatAlarm.setDisable(true);
		buttonSortByTime.setDisable(true);
		buttonSortByPriority.setDisable(true);
	}
	
	public void disableButtonSort(){
		buttonAddAlarmRandom.setDisable(false);
		buttonResetAlarms.setDisable(false);
		buttonDeleteAlarm.setDisable(false);
		buttonTreatAlarm.setDisable(false);
		buttonSortByTime.setDisable(true);
		buttonSortByPriority.setDisable(true);
	}
}
