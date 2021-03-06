package application;

import java.util.List;

import application.alarm.Alarm;
import application.alarm.AlarmView;
import application.audiovisuel.AudioVisuel;
import application.priorite.Priorite;
import application.sorttype.SortType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;

/**
 * Classe représentant le controlleur.
 * @author Floo'
 */
public class Controller {
	private Model model;
	
	/**
	 * Création du controlleur.
	 * @param m
	 * 			La modèle de donnée à utiliser.
	 */
	public Controller(Model m){
		this.model = m;
	}
	
	/**
	 * Convertie la liste d'alarme du modèle en liste d'alarmes graphique pour la vue.
	 * Récupère toute les alarmes.
	 * @return La liste d'alarme convertie en liste d'objets graphiques.
	 */
	public ListView<AlarmView> getAlarmsAsListView(){
		List<Alarm> modelList = this.getModel().getListAlarm();
		ListView<AlarmView> viewList = new ListView<AlarmView>();
		
		for(Alarm a : modelList){
				viewList.getItems().add(new AlarmView(a, View.getPrimaryStage().getScene().getWidth() - 210, this.getModel().getView()));
		}
		
		return viewList;
	}
	
	/**
	 * Convertie la liste d'alarme du modèle en liste d'alarmes graphique pour la vue.
	 * Ne récupère que les alarmes de type "Visuel" et "AudioVisuel".
	 * @return La liste d'alarme convertie en liste d'objets graphiques.
	 */
	public ListView<AlarmView> getVisualAlarmsAsListView(){
		List<Alarm> modelList = this.getModel().getListAlarm();
		ListView<AlarmView> viewList = new ListView<AlarmView>();
		
		for(Alarm a : modelList){
			if(a.getTypeAudioVisuel() != AudioVisuel.AUDIO)
				viewList.getItems().add(new AlarmView(a, View.getPrimaryStage().getScene().getWidth() - 210, this.getModel().getView()));
		}
		
		return viewList;
	}
	
	/**
	 * Met à jour les infos affichée en haut.
	 */
	public void majInfoAlarm(){
		this.getModel().getView().refreshTopDesc();
	}
	
	/**
	 * Ajoute une alarme aléatoire.
	 */
	public void putAlarm(){
		Alarm a = new Alarm();
		this.getModel().addAlarm(a);
	}
	
	/**
	 * Ajoute une alarme aléatoire de priorité p.
	 * @param p
	 * 			Priorité de l'alarme.
	 */
	public void putAlarm(Priorite p){
		Alarm a = new Alarm(p);
		this.getModel().addAlarm(a);
	}
	
	/**
	 * Action de suppression de l'alarme selectionnée (pop-up + suppression).
	 * @param a
	 * 			Alarme à supprimer.
	 */
	public void deleteAlarm(Alarm a){
		if(a != null){	 
			if (this.getModel().getView().popDeleteDialog(a) == ButtonType.OK){
				this.getModel().removeAlarm(a);
			}
		}
	}
	
	/**
	 * Action de réinitialisation (pop-up + suppression).
	 */
	public void resetAlarms(){
		if(this.getModel().getView().popResetDialog() == ButtonType.OK){
			this.getModel().clear();
		}
	}
	
	/**
	 * Traitement d'une alarme.
	 * @param a
	 * 			Alarme à traiter.
	 */
	public void treatAlarm(Alarm a){
		if(a != null){
			this.getModel().treatAlarm(a);	
		}
	}
	
	/**
	 * Tri la liste d'alarme chronologiquement.
	 */
	public void sortAlarmsByTime(){
		
		/* Permet d'alterner l'ordre du tri a chaque appel */
		if(!this.getModel().getSortedTime() || this.getModel().getSortedReverse()){
			
			/* Tri de la liste du model par date croissante */
			this.getModel().sortByTimeUp();	
		} else {
			
			/* Tri de la liste du model par date décroissante */
			this.getModel().sortByTimeDown();
		}
	}

	/**
	 * Tri la liste d'alarme par priorité.
	 */
	public void sortAlarmsByPriority(){
		
		/* Permet d'alterner l'ordre du tri a chaque appel */
		if(!this.getModel().getSortedPrio() || this.getModel().getSortedReverse()){
			
			/* Tri de la liste du model par date croissante */
			this.getModel().sortByPrioUp();	
		} else {
			
			/* Tri de la liste du model par date décroissante */
			this.getModel().sortByPrioDown();
		}
	}
	
	public SortType getSortType(){
		if(getModel().getSortedPrio() && getModel().getSortedReverse()){
			return SortType.PrioUp;
		}
		
		if(getModel().getSortedPrio() && !getModel().getSortedReverse()){
			return SortType.PrioDown;
		}
		
		if(getModel().getSortedTime() && getModel().getSortedReverse()){
			return SortType.TimeUp;
		}
		
		if(getModel().getSortedTime() && !getModel().getSortedReverse()){
			return SortType.TimeDown;
		}
		
		return null;
	}
	
	/**
	 * @return Le modèle de données.
	 */
	public Model getModel(){
		return this.model;
	}
}
