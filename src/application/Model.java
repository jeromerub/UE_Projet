package application;

import java.io.Serializable;
import java.util.List;

import application.alarm.AlarmView;

public class Model implements Serializable {
	private View view;
	private List<AlarmView> listAlarm;
	
	public Model(){}
	
	/* Getter/Setters */
	
	public View getView(){
		return this.view;
	}
	
	public void setView(View v){
		this.view = v;
	}
}
