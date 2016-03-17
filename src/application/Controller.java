package application;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import application.alarm.Alarm;
import application.alarm.AlarmView;
import javafx.scene.control.ListView;

public class Controller {
	private Model model;
	
	public Controller(Model m){
		this.model = m;
	}
	
	public void putAlarm(ListView<AlarmView> list){
		AlarmView a = new AlarmView(new Alarm());
		
		list.getItems().add(a);
		this.getModel().addAlarm(a.getAlarm());
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
	
	public Model getModel(){
		return this.model;
	}
}
