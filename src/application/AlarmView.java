package application;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class AlarmView extends Parent {
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
