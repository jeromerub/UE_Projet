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
	
	private boolean isSortedTime = true;
	private boolean isSortedPrio = false;
	private boolean isSortedReverse = false;
	
	public Model(){		
		/* A la création on charge les alarmes et le type de tri dans le model */
		
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("alarms"));
			
			this.listAlarm = (List<Alarm>) ois.readObject();
			this.isSortedTime = (boolean) ois.readBoolean();
			this.isSortedPrio = (boolean) ois.readBoolean();
			this.isSortedReverse = (boolean) ois.readBoolean();
			
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
	
	public void removeAlarm(Alarm a){
		this.listAlarm.remove(a);
	}
	
	public List<Alarm> getListAlarm(){
		return this.listAlarm;
	}
	
	public void setListAlarm(List<Alarm> newlist){
		this.listAlarm = newlist;
	}
	
	public boolean getSortedTime(){
		return this.isSortedTime;
	}
	
	public boolean getSortedPrio(){
		return this.isSortedPrio;
	}
	
	public boolean getSortedReverse(){
		return this.isSortedReverse;
	}
	
	public void setSortedTime(){
		this.isSortedTime = true;
		this.isSortedPrio = false;
	}
	
	public void setSortedPrio(){
		this.isSortedTime = false;
		this.isSortedPrio = true;
	}
	
	public void setSortedNormal(){
		this.isSortedReverse = false;
	}
	
	public void setSortedReverse(){
		this.isSortedReverse = true;
	}
}
