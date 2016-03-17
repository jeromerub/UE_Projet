package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import application.alarm.Alarm;

public class Model implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private transient View view;
	private List<Alarm> listAlarm = new ArrayList<Alarm>();
	
	public Model(){		
		/* A la création on charge les alarmes dans le model */
		
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("alarms.txt"));
			
			this.listAlarm = (List<Alarm>) ois.readObject();
			
			ois.close();
			
		/* Captures des exceptions */
			
		} catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/* Getter/Setters */
	
	public View getView(){
		return this.view;
	}
	
	public void setView(View v){
		this.view = v;
	}
	
	public void addAlarm(Alarm a){
		this.listAlarm.add(a);
	}
	
	public List<Alarm> getListAlarm(){
		return this.listAlarm;
	}
}
