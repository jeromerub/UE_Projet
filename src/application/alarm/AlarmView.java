package application.alarm;

import application.View;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Classe qui représente graphiquement une alarme.
 * @author Floo'
 */
public class AlarmView extends Parent {
	
	/* Constantes code couleurs */
	public static final Color rouge = new Color(0.72, 0.12, 0.12, 1);
	public static final Color orange = new Color(0.92, 0.49, 0.23, 1);
	public static final Color jaune = new Color(0.83, 0.80, 0.08, 1);
	public static final Color vert = new Color(0.18, 0.69, 0, 1);
	public static final Color gris = new Color(0.65, 0.65, 0.65, 1);
	
	private Alarm alarm;
	private Rectangle fond;
	private ButtonDelete delete;
	private boolean isDeleteVisible;
	
	/**
	 * Création de la représentation d'une alarme.
	 * @param a 
	 * 			Alarme à référencer.
	 * @param width 
	 * 			Largeur de l'élément.
	 * @param view 
	 * 			Vue du logiciel.
	 */
	public AlarmView(Alarm a, double width, View view){
		ImageView img = new ImageView(new Image(AlarmView.class.getResourceAsStream("../images/picto_alarme.png")));
		AlarmView current = this;
		Text nom = new Text();
		Text description = new Text();
		ContextMenu menu = new ContextMenu();
		MenuItem suppr = new MenuItem();
		
		this.delete = new ButtonDelete(view, current);;
		this.alarm = a;
		this.fond = new Rectangle();
		
		/* Stylisation */
		
		this.fond.setX(0);
		this.fond.setY(0);
		this.fond.setWidth(width);
		this.fond.setHeight(130);
		this.fond.setId("list-rect");

		this.delete.setLayoutX(width - 60);
		this.delete.setLayoutY(10);
		this.delete.setId("delete-button");
		this.delete.setVisible(false);
		this.isDeleteVisible = false;
		
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
		
		suppr.setText("Supprimer " + this.alarm.getNom());
		
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
		
		/* MenuItem Listener */
		
		suppr.setOnAction(new EventHandler<ActionEvent>(){
			
			/* Suppression : MenuItem "Supprimer" */
			@Override
			public void handle(ActionEvent event){
				view.getController().deleteAlarm(current.getAlarm());
			}
		});
		
		/* OnClick Listeners */
		
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
			
			/* Menu Contextuel : click droit */
	        @Override 
	        public void handle(MouseEvent event) {
	        	if (event.getButton() == MouseButton.SECONDARY) {
                    menu.show(current, event.getScreenX(), event.getScreenY());
                } else {
                	menu.hide();
                }
	            event.consume();
	        }
	        
		});
		
		/* OnTouch Listeners */
		
		this.setOnTouchPressed(new EventHandler<TouchEvent>() {
			
			/* On cache les bouton supprimer quand on touche un item */
	        @Override 
	        public void handle(TouchEvent event) {
	        	for(AlarmView av : view.getListView().getItems()){
	        		av.hideDelete();
	        	}
	        	
	            event.consume();
	        }
	        
		});
		
		this.setOnSwipeLeft(new EventHandler<SwipeEvent>() {
			
			/* Suppression : swipe right */
	        @Override 
	        public void handle(SwipeEvent event) {
	            revealDelete();
	            event.consume();
	        }
	        
		});

		this.setOnSwipeRight(new EventHandler<SwipeEvent>() {
			
			/* Cacher bouton // Traitement : swipe left */
		    @Override 
		    public void handle(SwipeEvent event) {
		    	if(isDeleteVisible){
		    		hideDelete();
		    	} else {
		    		view.getController().getModel().treatAlarm(current.getAlarm());
		    	}
		    	
		        event.consume();
		    }
		    
		});
		
		/* Ajout des éléments */
		
		menu.getItems().add(suppr);
		
		this.getChildren().add(fond);
		this.getChildren().add(img);
		this.getChildren().add(nom);
		this.getChildren().add(description);
		this.getChildren().add(delete);
	}
	
	/**
	 * @return Alarme référencée.
	 */
	public Alarm getAlarm(){
		return this.alarm;
	}
	
	/**
	 * Change la largeur de notre représentation de l'alarme.
	 * (responsive)
	 * @param val
	 * 			Nouvelle largeur.
	 */
	public void setFondWidth(double val){
		this.fond.setWidth(val);
	}
	
	/**
	 * Changer le décalage X du bouton de suppression.
	 * (responsive)
	 * @param val
	 * 			Nouveau décalage.
	 */
	public void setDeleteLayoutX(double val){
		this.delete.setLayoutX(val);
	}
	
	/**
	 * Cache le bouton supprimer.
	 */
	public void hideDelete(){
		this.isDeleteVisible = false;
		this.delete.setVisible(false);
	}
	
	/**
	 * Révèle le bouton supprimer.
	 */
	public void revealDelete(){
		this.isDeleteVisible = true;
		this.delete.setVisible(true);
	}
	
	/**
	 * Traite l'alarme référencée.
	 */
	public void setTreated(){
		this.alarm.setTreated();
		this.fond.setFill(gris);;
	}
}
