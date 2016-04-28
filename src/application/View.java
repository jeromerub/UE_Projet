package application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import application.alarm.Alarm;
import application.alarm.AlarmView;
import application.audiovisuel.AudioVisuel;
import application.figures.OngletListAlarms;
import application.priorite.Priorite;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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
/**
 * @author Floo'
 *
 */
public class View {
	private Priorite plusHautePriorite = Priorite.Basse;
	
	private static Stage pStage = null;
	private Controller controller;
	
	private List<MediaPlayer> soundsAlarms;
	
	private ListView<AlarmView> scrollAlarm;
	private Text nomAlarm;
	private Label descAlarm;
	private Text prioriteAlarm;
	private Text treatedAlarm;
	
	private Button buttonAddAlarmRandom;
	private Button buttonResetAlarms;
	private Button buttonDeleteAlarm;
	private Button buttonTreatAlarm;
	private Button buttonSortByTime ;
	private Button buttonSortByPriority;
	
	private Slider masterVolume;
	private Text volumeText;
	
	private OngletListAlarms ongletVisual;
	private OngletListAlarms ongletAll;
	private boolean visualOnly;
	
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
			primaryStage.setMinHeight(520);
			primaryStage.setMinWidth(718);
			primaryStage.setScene(scene);
			primaryStage.getScene().getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			this.setPrimaryStage(primaryStage);
			
			/* Init attributs */
			
			this.controller = c;
			this.getController().getModel().setView(this);
			
			initSoundsAlarms();
			initLabels();
			initButtons();
			initAlarmList();
			initOnglets();
			initSlider();

			/* Permet de ne jamais avoir 2 alarmes identiques (provisoire) */
			
			if((this.scrollAlarm.getItems().size() - 1) != -1)
				Alarm.cptRandom = Integer.parseInt(this.scrollAlarm.getItems().get(this.scrollAlarm.getItems().size() - 1).getAlarm().getNom().substring(7));
			
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
			botLeft.getChildren().add(masterVolume);
			botLeft.getChildren().add(volumeText);
			
			botRight.getChildren().add(botRightRect);
			botRight.getChildren().add(scrollAlarm);
			
			root.getChildren().add(top);
			root.getChildren().add(botLeft);
			root.getChildren().add(botRight);
			root.getChildren().add(ongletVisual);
			root.getChildren().add(ongletAll);
			
			primaryStage.show();
			
		/* Catch exceptions */
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void refreshVolume(){
		for(MediaPlayer mp : soundsAlarms){
			double vol = Model.getVolume();
			mp.setVolume(vol/100);
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
		ListView<AlarmView> list;
		Alarm a;
		
		if(visualOnly)
			list = this.getController().getVisualAlarmsAsListView();
		else
			list = this.getController().getAlarmsAsListView();
		
		this.setListView(list);
		
		a=findAlarmMaxPrio();
		if (this.plusHautePriorite.equals(Priorite.Max)){
			emettreSon(a);
		}
		else{
			stopSonAlarmMax();
		}
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
	 * Emet un son pour une alarme donnée.
	 * @param a
	 * 			Alarme à déclencher.
	 */
	public void emettreSon(Alarm a){
		
		/* Les alarmes visuelles uniquement ne produisent pas de son */
		
		if(a.getTypeAudioVisuel() != AudioVisuel.VISUEL){
			stopSon();
			
			switch(a.getPriorite()){
				case Basse:
					this.soundsAlarms.get(0).play();
					this.soundsAlarms.get(1).setStartTime(Duration.seconds(0.5));
					this.soundsAlarms.get(1).play();
					break;
				
				case Moyenne:
					this.soundsAlarms.get(2).play();
					this.soundsAlarms.get(3).setStartTime(Duration.seconds(0.5));
					this.soundsAlarms.get(3).play();
					break;
				
				case Haute:
					this.soundsAlarms.get(4).play();
					this.soundsAlarms.get(5).setStartTime(Duration.seconds(0.5));
					this.soundsAlarms.get(5).play();
					break;
					
				case Max:
					this.soundsAlarms.get(6).play();
					this.soundsAlarms.get(7).setStartTime(Duration.seconds(0.5));
					this.soundsAlarms.get(7).play();
					break;
			}
		}
	}
	
	/**
	 * Arrete tous les sons qui ne sont pas l'alarme max
	 */
	public void stopSon(){
		//for (int i = 0; i < this.soundsAlarms.size(); ++i){
		for (int i = 0; i < 6; ++i){
			this.soundsAlarms.get(i).stop();
		}
	}
	
	/**
	 * Arrete les sons de l'alarme max
	 */
	public void stopSonAlarmMax(){
		for (int i = 6; i < this.soundsAlarms.size(); ++i){
			this.soundsAlarms.get(i).stop();
		}
	}
	
	/**
	 * Met a jour la variable plus haute priorite et renvoie l'alarme avec cette plus haute priorite
	 * @return l'alarme avec la plus grande priorite
	 */
	public Alarm findAlarmMaxPrio(){
		this.plusHautePriorite=Priorite.Basse;
		Alarm a=null;
		for(AlarmView av : scrollAlarm.getItems()){
			if ((av.getAlarm().getPriorite().compareTo(this.plusHautePriorite) >= 0) && (av.getAlarm().isTreated()==false)){
				this.plusHautePriorite = av.getAlarm().getPriorite();
				a=av.getAlarm();
			}
		}
		return a;
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
        stage.toFront();
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
	 * @return Le controlleur
	 */
	public Controller getController(){
		return this.controller;
	}
	
	/**
	 * Désactive les boutons de suppression et traitement.
	 */
	public void disableButtonDeleteAndTreat(){
		buttonAddAlarmRandom.setDisable(false);
		buttonResetAlarms.setDisable(false);
		buttonDeleteAlarm.setDisable(true);
		buttonTreatAlarm.setDisable(true);
		buttonSortByTime.setDisable(false);
		buttonSortByPriority.setDisable(false);
	}
	
	/**
	 * Désactive les boutons de tri, suppression et traitement.
	 */
	public void disableButtonSortDeleteAndTreat(){
		buttonAddAlarmRandom.setDisable(false);
		buttonResetAlarms.setDisable(false);
		buttonDeleteAlarm.setDisable(true);
		buttonTreatAlarm.setDisable(true);
		buttonSortByTime.setDisable(true);
		buttonSortByPriority.setDisable(true);
	}
	
	/**
	 * Désactive les boutons reset, tri, suppression et traitement.
	 */
	public void disableButtonResetSortDeleteAndTreat(){
		buttonAddAlarmRandom.setDisable(false);
		buttonResetAlarms.setDisable(true);
		buttonDeleteAlarm.setDisable(true);
		buttonTreatAlarm.setDisable(true);
		buttonSortByTime.setDisable(true);
		buttonSortByPriority.setDisable(true);
	}
	
	/**
	 * Désactive le bouton traitement.
	 */
	public void disableButtonTreat(){
		buttonAddAlarmRandom.setDisable(false);
		buttonResetAlarms.setDisable(false);
		buttonDeleteAlarm.setDisable(false);
		buttonTreatAlarm.setDisable(true);
		buttonSortByTime.setDisable(false);
		buttonSortByPriority.setDisable(false);
	}
	
	/**
	 * Désactive les boutons de tri et de traitement.
	 */
	public void disableButtonSortAndTreat(){
		buttonAddAlarmRandom.setDisable(false);
		buttonResetAlarms.setDisable(false);
		buttonDeleteAlarm.setDisable(false);
		buttonTreatAlarm.setDisable(true);
		buttonSortByTime.setDisable(true);
		buttonSortByPriority.setDisable(true);
	}
	
	/**
	 * Désactive les boutons de tri.
	 */
	public void disableButtonSort(){
		buttonAddAlarmRandom.setDisable(false);
		buttonResetAlarms.setDisable(false);
		buttonDeleteAlarm.setDisable(false);
		buttonTreatAlarm.setDisable(false);
		buttonSortByTime.setDisable(true);
		buttonSortByPriority.setDisable(true);
	}
	
	/**
	 * Active tout les boutons.
	 */
	public void noDisableButton(){
		buttonAddAlarmRandom.setDisable(false);
		buttonResetAlarms.setDisable(false);
		buttonDeleteAlarm.setDisable(false);
		buttonTreatAlarm.setDisable(false);
		buttonSortByTime.setDisable(false);
		buttonSortByPriority.setDisable(false);
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
	 * Initialise les sons.
	 */
	private void initSoundsAlarms(){
		soundsAlarms = new ArrayList<MediaPlayer>(8);
		
		soundsAlarms.add(new MediaPlayer(new Media(new File("src/application/son/alarmBas.mp3").toURI().toString())));
		soundsAlarms.add(new MediaPlayer(new Media(new File("src/application/son/alarmBas.mp3").toURI().toString())));
		soundsAlarms.add(new MediaPlayer(new Media(new File("src/application/son/alarmMoy.mp3").toURI().toString())));
		soundsAlarms.add(new MediaPlayer(new Media(new File("src/application/son/alarmMoy.mp3").toURI().toString())));
		soundsAlarms.add(new MediaPlayer(new Media(new File("src/application/son/alarmHau.mp3").toURI().toString())));
		soundsAlarms.add(new MediaPlayer(new Media(new File("src/application/son/alarmHau.mp3").toURI().toString())));
		soundsAlarms.add(new MediaPlayer(new Media(new File("src/application/son/alarmMax.mp3").toURI().toString())));
		soundsAlarms.add(new MediaPlayer(new Media(new File("src/application/son/alarmMax.mp3").toURI().toString())));
		
		
		soundsAlarms.get(0).setBalance(-1);
		soundsAlarms.get(1).setBalance(1);
		
		soundsAlarms.get(2).setBalance(-1);
		soundsAlarms.get(3).setBalance(1);
		
		soundsAlarms.get(4).setBalance(-1);
		soundsAlarms.get(5).setBalance(1);
		
		soundsAlarms.get(6).setBalance(-1);
		soundsAlarms.get(7).setBalance(1);
		
		soundsAlarms.get(6).setCycleCount(64);
		soundsAlarms.get(7).setCycleCount(64);
		
		for(MediaPlayer mp : soundsAlarms){
			double vol = Model.getVolume();
			mp.setVolume(vol/100);
		}
	}
	
	/**
	 * Initialise les labels.
	 */
	private void initLabels(){		
		nomAlarm = new Text();
		descAlarm = new Label();
		prioriteAlarm = new Text();
		treatedAlarm = new Text();
		volumeText = new Text();
		
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
		
		volumeText.setText("Vol :");
		volumeText.setLayoutX(5);
		volumeText.setLayoutY(57);
		volumeText.setId("vol-text");
	}
	
	/**
	 * Initialise les boutons.
	 */
	private void initButtons(){
		buttonAddAlarmRandom = new Button();
		buttonResetAlarms = new Button();
		buttonDeleteAlarm = new Button();
		buttonTreatAlarm = new Button();
		buttonSortByTime = new Button();
		buttonSortByPriority = new Button();
		
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
	}
    
    /**
     * Initialise et charge la liste du model.
     */
    private void initAlarmList(){
    	scrollAlarm = this.getController().getVisualAlarmsAsListView();
    	
    	scrollAlarm.setPrefWidth(800);
		scrollAlarm.setPrefHeight(550);
		Alarm a = null;
		
		for(AlarmView av : scrollAlarm.getItems()){
			av.setFondWidth(View.getPrimaryStage().getScene().getWidth() - 210);
		}
		a=findAlarmMaxPrio();
		if (this.plusHautePriorite.equals(Priorite.Max)){
			emettreSon(a);
		}
		else{
			stopSonAlarmMax();
		}

		scrollAlarm.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AlarmView>() {

		    @Override
		    public void changed(ObservableValue<? extends AlarmView> observable, AlarmView oldValue, AlarmView newValue) {
		        getController().majInfoAlarm();
		    }
		});
    }
    
    private void initOnglets(){
    	ongletVisual = new OngletListAlarms("Visuelles", this);
    	ongletAll = new OngletListAlarms("Toutes", this);
    	visualOnly = true;
    	
    	ongletVisual.setLayoutX(120);
    	ongletVisual.setLayoutY(420);
    	ongletVisual.setSelected();
    	
    	/* Events touch/click */
    	
    	ongletVisual.setOnTouchPressed(new EventHandler<TouchEvent>(){

			@Override
			public void handle(TouchEvent event) {
				ongletVisual.setSelected();
				ongletAll.setUnselected();
				visualOnly = true;
				refreshList();
				event.consume();
			}
    		
    	});
    	
    	ongletVisual.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				ongletVisual.setSelected();
				ongletAll.setUnselected();
				visualOnly = true;
				refreshList();
				event.consume();
			}
    		
    	});
    	
    	ongletAll.setLayoutX(120);
    	ongletAll.setLayoutY(450);
    	
    	/* Events touch/click */
    	
    	ongletAll.setOnTouchPressed(new EventHandler<TouchEvent>(){

			@Override
			public void handle(TouchEvent event) {
				ongletAll.setSelected();
				ongletVisual.setUnselected();
				visualOnly = false;
				refreshList();
				event.consume();
			}
    		
    	});
    	
    	ongletAll.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				ongletAll.setSelected();
				ongletVisual.setUnselected();
				visualOnly = false;
				refreshList();
				event.consume();
			}
    		
    	});
    }
    
    /**
     * Initialise le slider du volume.
     */
    private void initSlider(){
    	masterVolume = new Slider(0, 100, 100);
    	
    	masterVolume.setLayoutX(50);
    	masterVolume.setLayoutY(45);
    	
    	masterVolume.valueProperty().addListener(new ChangeListener<Number>(){

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				Model.setVolume(arg2.intValue());
				refreshVolume();
			}
    		
    	});
    }
}
