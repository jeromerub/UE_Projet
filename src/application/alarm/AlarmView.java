package application.alarm;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class AlarmView extends Parent {
	
	/* Constantes code couleurs */
	public static final Color rouge = new Color(0.84, 0.12, 0.12, 1);
	public static final Color orange = new Color(0.92, 0.49, 0.23, 1);
	public static final Color jaune = new Color(0.92, 0.84, 0.30, 1);
	public static final Color vert = new Color(0.25, 0.85, 0.35, 1);
	
	private Alarm alarm;
	
	public AlarmView(Alarm a){
		Rectangle fond = new Rectangle();
		ImageView img = new ImageView(new Image(AlarmView.class.getResourceAsStream("images/picto_alarme.png")));
		Text nom = new Text();
		Text description = new Text();
		
		this.alarm = a;
		
		fond.setX(0);
		fond.setY(0);
		fond.setWidth(500);
		fond.setHeight(100);
		
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
		
		img.setLayoutX(-15);
		img.setLayoutY(-15);
		img.setScaleX(0.75);
		img.setScaleY(0.75);
		
		nom.setText(this.alarm.getNom());
		nom.setLayoutX(110);
		nom.setLayoutY(35);
		nom.setFill(Color.WHITE);
		nom.setId("nom");
		
		description.setText(this.alarm.getDesc());
		description.setLayoutX(110);
		description.setLayoutY(55);
		description.setFill(Color.WHITE);
		description.setId("desc");
		
		this.getChildren().add(fond);
		this.getChildren().add(img);
		this.getChildren().add(nom);
		this.getChildren().add(description);
	}
}
