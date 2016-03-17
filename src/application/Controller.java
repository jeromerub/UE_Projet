package application;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import application.alarm.Alarm;
import application.alarm.AlarmView;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

public class Controller {
	private Model model;
	
	public Controller(Model m){
		this.model = m;
	}
	
	public void majInfoAlarm(AlarmView av, Text nom, Text desc, Text prio, Text treated){
		nom.setText(av.getAlarm().getNom());
		desc.setText(av.getAlarm().getDesc());
		prio.setText("Priorité " + av.getAlarm().getPriorite());
		
		if(av.getAlarm().isTreated()) treated.setText("Alarme traitée");
		else treated.setText("Alarme non-traitée");
	}
	
	public void putAlarm(ListView<AlarmView> list){
		AlarmView a = new AlarmView(new Alarm());
		
		list.getItems().add(a);
		this.getModel().addAlarm(a.getAlarm());
	}
	
	public void deleteAlarm(ListView<AlarmView> list){
		if(list.getSelectionModel().getSelectedItem() != null){
			list.getItems().remove(list.getSelectionModel().getSelectedItem());
			this.getModel().removeAlarm(list.getSelectionModel().getSelectedItem().getAlarm());
		}
	}
	
	public void saveAlarms(){
		ObjectOutputStream oos = null;
		
		try {
			oos = new ObjectOutputStream(new FileOutputStream("alarms.txt"));
			oos.writeObject(this.getModel().getListAlarm());
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
	
	public void treatAlarm(ListView<AlarmView> list, Text treated){
		if(list.getSelectionModel().getSelectedItem() != null){
			treated.setText("Alarme traitée");
			list.getSelectionModel().getSelectedItem().setTreated();	
		}
	}
	
	public Model getModel(){
		return this.model;
	}
}
