package application.alarm;

import application.View;
import application.etat.Etat;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.TouchEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Représentation graphique du bouton de suppression qui apparait au swipe
 * @author Floo'
 */
public class ButtonDelete extends Parent {
	
	/**
	 * Création d'un Bouton de suppression pour une AlarmView donnée
	 * @param view 
	 * 			Vue du logiciel.
	 * @param av 
	 * 			AlarmView référencé par le bouton.
	 */
	public ButtonDelete(View view, AlarmView av){
		Rectangle rect = new Rectangle(4, 4, 40, 40);
		Rectangle line1 = new Rectangle(4, 4, 30, 5);
		Rectangle line2 = new Rectangle(4, 4, 30, 5);
		
		/* Stylisation */
		
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
		
		/* OnTouch Listeners */
		
		this.setOnTouchPressed(new EventHandler<TouchEvent>() {
			
			/* Suppression : Touch Pressed */
		    @Override 
		    public void handle(TouchEvent event) {
		        view.getController().deleteAlarm(av.getAlarm());
		        view.setNbAlarms(view.getListView().getItems().size());
				
				switch (view.getEtat()){
					case noAlarm:
						//Interdit
						break;
					case oneAlarmNotSelected:
						//Interdit
						break;
					case oneAlarmSelected:
						if (view.getNbAlarms() == 0){
							view.setEtat(Etat.noAlarm);
							view.disableButtonResetSortDeleteAndTreat();
						} else {
							view.setEtat(Etat.noAlarm);
							view.disableButtonResetSortDeleteAndTreat();
						}
						break;
					case oneAlarmSelectedTreated:
						if (view.getNbAlarms() == 0){
							view.setEtat(Etat.noAlarm);
							view.disableButtonResetSortDeleteAndTreat();
						} else {
							view.setEtat(Etat.noAlarm);
							view.disableButtonResetSortDeleteAndTreat();
						}
						break;
					case manyAlarmNotSelected:
						//Interdit
						break;
					case manyAlarmSelected:
						if (view.getNbAlarms() == 1){
							view.setEtat(Etat.oneAlarmNotSelected);
							view.disableButtonSortDeleteAndTreat();
						} else {
							view.setEtat(Etat.manyAlarmNotSelected);
							view.disableButtonDeleteAndTreat();
						}
						break;
					case manyAlarmSelectedTreated:
						if (view.getNbAlarms() == 1){
							view.setEtat(Etat.oneAlarmNotSelected);
							view.disableButtonSortDeleteAndTreat();
						} else {
							view.setEtat(Etat.manyAlarmNotSelected);
							view.disableButtonDeleteAndTreat();
						}
						break;
				}	
		        
		        event.consume();
		    }
		    
		});

		this.getChildren().add(rect);
		this.getChildren().add(line1);
		this.getChildren().add(line2);
	}
}
