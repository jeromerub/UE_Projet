package application;

import java.util.List;

import javafx.scene.control.ListView;

public class Model {
	private View view;
	private ListView<AlarmView> listAlarm;
	
	public Model(){}
	
	public void addAlarm(AlarmView a){
		
	}
	
	/* Getter/Setters */
	
	public View getView(){
		return this.view;
	}
	
	public void setView(View v){
		this.view = v;
	}
}
