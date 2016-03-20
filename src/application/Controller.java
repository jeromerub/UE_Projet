package application;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import application.alarm.Alarm;
import application.alarm.AlarmView;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

public class Controller {
	private Model model;
	
	public Controller(Model m){
		this.model = m;
	}
	
	/* Mise a jour des label en haut (top) */
	public void majInfoAlarm(AlarmView av, Text nom, Label desc, Text prio, Text treated){
		if(av != null){
			nom.setText(av.getAlarm().getNom());
			desc.setText(av.getAlarm().getDesc());
			prio.setText("Priorité : " + av.getAlarm().getPriorite());
			
			if(av.getAlarm().isTreated()) treated.setText("Alarme traitée");
			else treated.setText("Alarme non-traitée");
		} else {
			nom.setText("");
			desc.setText("");
			prio.setText("");
			treated.setText("");
		}
	}
	
	/* Ajoute une alarme aléatoire dans le model, puis dans la view */
	public void putAlarm(ListView<AlarmView> list){
		AlarmView a = new AlarmView(new Alarm(), View.getPrimaryStage().getScene().getWidth() - 210);
		
		this.getModel().addAlarm(a.getAlarm());
		list.getItems().add(a);
	}
	
	/* Supprime l'alarme selectionnée dans le model et la view */
	public void deleteAlarm(ListView<AlarmView> list){
		if(list.getSelectionModel().getSelectedItem() != null){	
			this.getModel().removeAlarm(list.getSelectionModel().getSelectedItem().getAlarm());
			list.getItems().remove(list.getSelectionModel().getSelectedItem());
		}
	}
	
	/* Sauvegarde le contexte (Alarmes + Tri) dans un fichier qui sera chargé à l'ouverture */
	public void saveAlarms(){
		ObjectOutputStream oos = null;
		
		try {
			oos = new ObjectOutputStream(new FileOutputStream("alarms"));
			oos.writeObject(this.getModel().getListAlarm());
			oos.writeBoolean(this.getModel().getSortedTime());
			oos.writeBoolean(this.getModel().getSortedPrio());
			oos.writeBoolean(this.getModel().getSortedReverse());
			oos.flush();
			
		} catch (FileNotFoundException e) {
		      e.printStackTrace();
	    } catch (IOException e) {
	      e.printStackTrace();
	    } finally {
	    	try {
	    		if (oos != null) {
	    			oos.flush();
	    			oos.close();
	    		}
	    	} catch (IOException ex) {
	    		ex.printStackTrace();
	    	}
	    }
	}
	
	/*  Traite une alarme */
	public void treatAlarm(ListView<AlarmView> list, Text treated){
		if(list.getSelectionModel().getSelectedItem() != null){
			treated.setText("Alarme traitée");
			list.getSelectionModel().getSelectedItem().setTreated();	
		}
	}
	
	/* Tri la liste d'alarme chronologiquement dans le model et la view */
	public void sortAlarmsByTime(ListView<AlarmView> listview){
		List<Alarm> listmodel = this.getModel().getListAlarm();
		AlarmView selected = null;
		
		/* Mémorisation de la sélection */
		selected = listview.getSelectionModel().getSelectedItem();
		
		/* Permet d'alterner l'ordre du tri a chaque appel */
		if(!this.getModel().getSortedTime() || this.getModel().getSortedReverse()){
			
			/* Tri de la liste du model par date croissante */
			Collections.sort(listmodel, new Comparator<Alarm>(){
				public int compare(Alarm a1, Alarm a2){
					return a1.getTimestamp().compareTo(a2.getTimestamp());
				}
			});
			
			/* MaJ de la liste du model */
			this.getModel().setListAlarm(listmodel);
			this.getModel().setSortedTime();
			this.getModel().setSortedNormal();
			
		} else {
			
			/* Tri de la liste du model par date décroissante */
			Collections.sort(listmodel, new Comparator<Alarm>(){
				public int compare(Alarm a1, Alarm a2){
					return a2.getTimestamp().compareTo(a1.getTimestamp());
				}
			});
			
			/* MaJ de la liste du model */
			this.getModel().setListAlarm(listmodel);
			this.getModel().setSortedTime();
			this.getModel().setSortedReverse();
			
		}
		
		/* Vidage de la ListView */
		listview.getItems().clear();
		
		/* MaJ de la ListView */
		for(Alarm a : listmodel){
			listview.getItems().add(new AlarmView(a, View.getPrimaryStage().getScene().getWidth() - 210));
		}
		
		/* Selection de l'item selected */
		if(selected != null) {
			listview.getSelectionModel().select(selected);
			listview.scrollTo(selected);
		}
	}

	/* Tri la liste d'alarme par priorité dans le model et la view */
	public void sortAlarmsByPriority(ListView<AlarmView> listview){
		List<Alarm> listmodel = this.getModel().getListAlarm();
		AlarmView selected = null;

		/* Mémorisation de la sélection */
		selected = listview.getSelectionModel().getSelectedItem();
		
		/* Permet d'alterner l'ordre du tri a chaque appel */
		if(!this.getModel().getSortedPrio() || this.getModel().getSortedReverse()){
			
			/* Tri de la liste du model par priorité */
			Collections.sort(listmodel, new Comparator<Alarm>(){
				public int compare(Alarm a1, Alarm a2){
					return a2.getPriorite().compareTo(a1.getPriorite());
				}
			});
			
			/* MaJ de la liste du model */
			this.getModel().setListAlarm(listmodel);
			this.getModel().setSortedPrio();
			this.getModel().setSortedNormal();
			
		} else {
			
			/* Tri de la liste du model par priorité */
			Collections.sort(listmodel, new Comparator<Alarm>(){
				public int compare(Alarm a1, Alarm a2){
					return a1.getPriorite().compareTo(a2.getPriorite());
				}
			});
			
			/* MaJ de la liste du model */
			this.getModel().setListAlarm(listmodel);
			this.getModel().setSortedPrio();
			this.getModel().setSortedReverse();
		}
		
		/* Vidage de la ListView */
		listview.getItems().clear();
		
		/* MaJ de la ListView */
		for(Alarm a : listmodel){
			listview.getItems().add(new AlarmView(a, View.getPrimaryStage().getScene().getWidth() - 210));
		}
		
		/* Selection de l'item selected */
		if(selected != null) {
			listview.getSelectionModel().select(selected);
			listview.scrollTo(selected);
		}
	}
	
	public Model getModel(){
		return this.model;
	}
}
