package application.alarm;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;

import application.priorite.Priorite;

/**
 * Classe qui représente une alarme.
 * @author Floo'
 */
public class Alarm implements Serializable  {
	private static final long serialVersionUID = 1L;
	
	/* Attributs static alarme random */
	public static int cptRandom = 0;
	private static Random random = new Random(123456789L);
	
	/* Attributs */
	private Priorite priorite;
	private String nom;
	private String desc;
	private boolean isTreated;
	private Timestamp created;
	private String audioVisuel;
	
	/**
	 * Génération  d'une alarme aléatoire.
	 */
	public Alarm(){
		Date d = new Date();
		

		// permet de définir si une alarme sera sonore ou visuelle en fonction du volume
		if (true){
			this.audioVisuel = "audio";
		} else {
			this.audioVisuel = "visuel";
		}

		
		this.created = new Timestamp(d.getTime());
		this.nom = "Alarme " + ++cptRandom;
		
		switch(random(4)){
			case 0: this.priorite = Priorite.Basse; break;
			case 1: this.priorite = Priorite.Moyenne; break;
			case 2: this.priorite = Priorite.Haute; break;
			case 3: this.priorite = Priorite.Max; break;
			default: System.out.println("Err rand");
		}
		
		this.desc = "Type alarm: " + this.audioVisuel +  "  - Alarme de priorité " + this.priorite;
		this.isTreated = false;
		
	}
	
	/**
	 * Création d'une alarme avec un nom, une description et une priorité.
	 * @param nom 
	 * 			Nom de l'alarme.
	 * @param desc 
	 * 			Description de l'alarme.
	 * @param p 
	 * 			Priorité de l'alarme.
	 */
	public Alarm(String nom, String desc, Priorite p){
		Date d = new Date();
		
		this.created = new Timestamp(d.getTime());
		this.nom = nom;
		this.priorite = p;		
		this.desc = desc;
		this.isTreated = false;
	}
	
	/**
	 * @return Nom de l'alarme.
	 */
	public String getNom(){
		return this.nom;
	}
	
	/**
	 * @return Description de l'alarme.
	 */
	public String getDesc(){
		return this.desc;
	}

	/**
	 * @return Priorité de l'alarme.
	 */
	public Priorite getPriorite(){
		return this.priorite;
	}
	
	/**
	 * @return Timestamp à la création de l'alarme.
	 */
	public Timestamp getTimestamp(){
		return this.created;
	}
	
	/**
	 * Passe une alarme dans l'état "traitée".
	 */
	public void setTreated(){
		this.isTreated = true;
	}
	
	/**
	 * @return True si l'alarme est traitée, False sinon.
	 */
	public boolean isTreated(){
		return this.isTreated;
	}
	
	/** 
	 * Génère un nombre entre 0 (inclu) et high (exclu).
	 * @param high
	 * 			Borne supérieure exclue.
	 * @return Nombre aléatoire généré.
	 */
	private static int random(int high) {
		return random.nextInt(high);
	}
	
	public String getAudioVisuel(){
		return this.audioVisuel;
	}
	
}





