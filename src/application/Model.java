package application;

import java.util.List;

public class Model {
	private View view;
	private List<AlarmView> listAlarm;
	private AlarmView selectedAlarm = null;
	
	public Model(){}
	
	
	/* Getter/Setters */
	
	public View getView(){
		return this.view;
	}
	
	public void setView(View v){
		this.view = v;
	}
}
