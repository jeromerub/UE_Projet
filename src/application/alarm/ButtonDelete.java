package application.alarm;

import application.View;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.TouchEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ButtonDelete extends Parent {
	public ButtonDelete(View view, AlarmView av){
		Rectangle rect = new Rectangle(4, 4, 40, 40);
		Rectangle line1 = new Rectangle(4, 4, 30, 5);
		Rectangle line2 = new Rectangle(4, 4, 30, 5);
		
		rect.setStrokeWidth(5);
		rect.setStroke(Color.BLACK);
		rect.setFill(AlarmView.rouge);
		
		line1.setStroke(Color.WHITE);
		line1.setFill(Color.WHITE);
		line1.setRotate(45);
		line1.setLayoutX(5);
		line1.setLayoutY(18);
		
		line2.setStroke(Color.WHITE);
		line2.setFill(Color.WHITE);
		line2.setRotate(-45);
		line2.setLayoutX(5);
		line2.setLayoutY(18);
		
		/* Ajout de l'évènement touch */
		
		this.setOnTouchPressed(new EventHandler<TouchEvent>() {
			
			/* Traitement swipe left */
		    @Override 
		    public void handle(TouchEvent event) {
		        view.getController().deleteAlarm(av);
		        event.consume();
		    }
		    
		});

		this.getChildren().add(rect);
		this.getChildren().add(line1);
		this.getChildren().add(line2);
	}
}
