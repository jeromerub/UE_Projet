package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import application.alarm.Alarm;

public class Model implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private transient View view;
	private List<Alarm> listAlarm = new ArrayList<Alarm>();
	
	private boolean isSortedTime = true;
	private boolean isSortedPrio = false;
	private boolean isSortedReverse = false;
	
	@SuppressWarnings("unchecked")
	public Model(){		
		/* A la cr�ation on charge les alarmes et le type de tri dans le model */
		
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
	
	public void save(){
		ObjectOutputStream oos = null;
		
		try {
			oos = new ObjectOutputStream(new FileOutputStream("alarms"));
			oos.writeObject(this.getListAlarm());
			oos.writeBoolean(this.getSortedTime());
			oos.writeBoolean(this.getSortedPrio());
			oos.writeBoolean(this.getSortedReverse());
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
	
	public void notifyView(){
		this.getView().refreshList();
		this.getView().refreshTopDesc();
	}
	
	public void addAlarm(Alarm a){
		this.listAlarm.add(a);
		save();
		notifyView();
	}
	
	public void removeAlarm(Alarm a){
		this.listAlarm.remove(a);
		save();
		notifyView();
	}	
	
	public void treatAlarm(Alarm a){
		a.setTreated();
		save();
		notifyView();
	}
	
	public void clear(){
		this.listAlarm.clear();
		save();
		notifyView();
	}

	public void sortByTimeUp(){
		Collections.sort(this.listAlarm, new Comparator<Alarm>(){
			public int compare(Alarm a1, Alarm a2){
				return a1.getTimestamp().compareTo(a2.getTimestamp());
			}
		});
		
		/* MaJ de la liste du model */
		this.setListAlarm(this.listAlarm);
		this.setSortedTime();
		this.setSortedNormal();
		notifyView();
	}
	
	public void sortByTimeDown(){
		Collections.sort(this.listAlarm, new Comparator<Alarm>(){
			public int compare(Alarm a1, Alarm a2){
				return a2.getTimestamp().compareTo(a1.getTimestamp());
			}
		});
		
		/* MaJ de la liste du model */
		this.setListAlarm(this.listAlarm);
		this.setSortedTime();
		this.setSortedReverse();
		notifyView();
	}
	
	public void sortByPrioUp(){
		Collections.sort(this.listAlarm, new Comparator<Alarm>(){
			public int compare(Alarm a1, Alarm a2){
				return a2.getPriorite().compareTo(a1.getPriorite());
			}
		});
		
		/* MaJ de la liste du model */
		this.setListAlarm(this.listAlarm);
		this.setSortedPrio();
		this.setSortedNormal();
		notifyView();
	}
	
	public void sortByPrioDown(){
		Collections.sort(this.listAlarm, new Comparator<Alarm>(){
			public int compare(Alarm a1, Alarm a2){
				return a1.getPriorite().compareTo(a2.getPriorite());
			}
		});
		
		/* MaJ de la liste du model */
		this.setListAlarm(this.listAlarm);
		this.setSortedPrio();
		this.setSortedReverse();
		notifyView();
	}
	
	/* Getter/Setters */
	
	public View getView(){
		return this.view;
	}
	
	public void setView(View v){
		this.view = v;
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
