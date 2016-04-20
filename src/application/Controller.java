package application;

import java.util.List;

import application.alarm.Alarm;
import application.alarm.AlarmView;

import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;

public class Controller {
	private Model model;
	
	public Controller(Model m){
		this.model = m;
	}
	
	public ListView<AlarmView> getAlarmsAsListView(){
		List<Alarm> modelList = this.getModel().getListAlarm();
		ListView<AlarmView> viewList = new ListView<AlarmView>();
		
		for(Alarm a : modelList){
			viewList.getItems().add(new AlarmView(a, View.getPrimaryStage().getScene().getWidth() - 210, this.getModel().getView()));
		}
		
		return viewList;
	}
	
	/* Mise a jour des label en haut (top) */
	public void majInfoAlarm(){
		this.getModel().getView().refreshTopDesc();
	}
	
	/* Ajoute une alarme aléatoire dans le model, puis dans la view */
	public void putAlarm(){
		Alarm a = new Alarm();
		this.getModel().addAlarm(a);
	}
	
	/* Supprime l'alarme selectionnée dans le model et la view */
	public void deleteAlarm(ListView<AlarmView> list){
		if(list.getSelectionModel().getSelectedItem() != null){	
			Alarm selected = list.getSelectionModel().getSelectedItem().getAlarm();
			
			if (this.getModel().getView().popDeleteDialog(selected) == ButtonType.OK){
				this.getModel().removeAlarm(selected);
			}
		}
	}
	
	/* Supprime l'alarme selectionnée dans le model et la view a l'aide d'un swipe */
	public void deleteAlarm(AlarmView swiped){
		if(swiped != null){	
			Alarm selected = swiped.getAlarm();
			
			if (this.getModel().getView().popDeleteDialog(selected) == ButtonType.OK){
				this.getModel().removeAlarm(selected);
			}
		}
	}
	
	public void resetAlarms(){
		if(this.getModel().getView().popResetDialog() == ButtonType.OK){
			this.getModel().clear();
		}
	}
	
	/*  Traite une alarme */
	public void treatAlarm(ListView<AlarmView> list){
		if(list.getSelectionModel().getSelectedItem() != null){
			this.getModel().treatAlarm(list.getSelectionModel().getSelectedItem().getAlarm());	
		}
	}
	
	/* Tri la liste d'alarme chronologiquement dans le model et la view */
	public void sortAlarmsByTime(ListView<AlarmView> listview){
		
		/* Permet d'alterner l'ordre du tri a chaque appel */
		if(!this.getModel().getSortedTime() || this.getModel().getSortedReverse()){
			
			/* Tri de la liste du model par date croissante */
			this.getModel().sortByTimeUp();	
		} else {
			
			/* Tri de la liste du model par date décroissante */
			this.getModel().sortByTimeDown();
		}
	}

	/* Tri la liste d'alarme par priorité dans le model et la view */
	public void sortAlarmsByPriority(ListView<AlarmView> listview){
		
		/* Permet d'alterner l'ordre du tri a chaque appel */
		if(!this.getModel().getSortedPrio() || this.getModel().getSortedReverse()){
			
			/* Tri de la liste du model par date croissante */
			this.getModel().sortByPrioUp();	
		} else {
			
			/* Tri de la liste du model par date décroissante */
			this.getModel().sortByPrioDown();
		}
	}
	
	public Model getModel(){
		return this.model;
	}
}
