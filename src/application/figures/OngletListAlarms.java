package application.figures;

import application.View;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Représente un onglet sur notre vue.
 * @author Floo'
 */
public class OngletListAlarms extends Parent {
	private Rectangle fond;
	private Label title;
	
	/**
	 * Construit un onglet.
	 * @param name
	 * 			Nom affiché sur l'onglet.
	 * @param view
	 * 			Vue de l'application.
	 */
	public OngletListAlarms(String name, View v){
		fond = new Rectangle(0, 0, 80, 50);
		fond.setFill(Color.WHITE);
		fond.setStroke(Color.WHITE);
		fond.setStrokeWidth(3);
		
		title = new Label(name);
		title.setLayoutX(6);
		title.setLayoutY(15);
		title.setId("onglet-text");
		
		this.getChildren().add(fond);
		this.getChildren().add(title);
	}
	
	/**
	 * Rend l'objet visiblement selectioné.
	 */
	public void setSelected(){
		fond.setFill(Color.MEDIUMTURQUOISE);
	}
	
	/**
	 * Rend l'objet visiblement non-selectionné.
	 */
	public void setUnselected(){
		fond.setFill(Color.WHITE);
	}
}
