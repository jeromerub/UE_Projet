package application.alarm;

import application.View;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class AlarmView extends Parent {
	
	/* Constantes code couleurs */
	public static final Color rouge = new Color(0.72, 0.12, 0.12, 1);
	public static final Color orange = new Color(0.92, 0.49, 0.23, 1);
	public static final Color jaune = new Color(0.83, 0.80, 0.08, 1);
	public static final Color vert = new Color(0.18, 0.69, 0, 1);
	public static final Color gris = new Color(0.65, 0.65, 0.65, 1);
	
	private Alarm alarm;
	private Rectangle fond;
	
	public AlarmView(Alarm a, double width){
		ImageView img = new ImageView(new Image(AlarmView.class.getResourceAsStream("../images/picto_alarme.png")));
		Text nom = new Text();
		Text description = new Text();
		
		this.alarm = a;
		this.fond = new Rectangle();
		
		fond.setX(0);
		fond.setY(0);
		fond.setWidth(width);
		fond.setHeight(130);
		fond.setId("list-rect");
		
		this.getStyleClass().add("list-cell-alarm");
		
		if(this.alarm.isTreated()){
			this.fond.setFill(gris);
		} else {
			switch(this.alarm.getPriorite()){
				case Basse:
					fond.setFill(vert);
					break;
					
				case Moyenne:
					fond.setFill(jaune);
					break;
					
				case Haute:
					fond.setFill(orange);
					break;
					
				case Max:
					fond.setFill(rouge);
					break;
			}
		}
		
		img.setScaleX(0.9);
		img.setScaleY(0.9);

		nom.setText(this.alarm.getNom());
		nom.setLayoutX(140);
		nom.setLayoutY(55);
		nom.setFill(Color.WHITE);
		nom.setId("nom-alarmview");
		
		description.setText(this.alarm.getDesc());
		description.setLayoutX(140);
		description.setLayoutY(75);
		description.setFill(Color.WHITE);
		description.setId("desc-alarmview");
		
		this.getChildren().add(fond);
		this.getChildren().add(img);
		this.getChildren().add(nom);
		this.getChildren().add(description);
	}
	
	public Alarm getAlarm(){
		return this.alarm;
	}
	
	public void setFondWidth(double val){
		this.fond.setWidth(val);
	}
	
	public void setTreated(){
		this.alarm.setTreated();
		this.fond.setFill(gris);;
	}
}
