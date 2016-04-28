package application.alarm;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

import application.Model;
import application.audiovisuel.AudioVisuel;
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
	private AudioVisuel type;
	
	/**
	 * Génération  d'une alarme aléatoire.
	 */
	public Alarm(){		
		this.created = new Timestamp(new Date().getTime());
		this.nom = "Alarme " + ++cptRandom;
		
		switch(random(4)){
			case 0: 
				this.priorite = Priorite.Basse;
				this.type = AudioVisuel.AUDIO;
				break;
				
			case 1: 
				this.priorite = Priorite.Moyenne;
				this.type = AudioVisuel.VISUEL;
				break;
				
			case 2: 
				this.priorite = Priorite.Haute;
				this.type = AudioVisuel.AUDIOVISUEL;
				break;
				
			case 3: 
				this.priorite = Priorite.Max;
				this.type = AudioVisuel.AUDIOVISUEL;
				break;
				
			default: 
				System.out.println("Err rand");
				break;
		}
		
		if(this.type == AudioVisuel.AUDIO && Model.getVolume() <= 25){
			this.type = AudioVisuel.VISUEL;
		}
		
		this.desc = "Alarme type : " + this.type + " - Alarme de priorité " + this.priorite;
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
		this.created = new Timestamp(new Date().getTime());
		this.nom = nom;
		this.priorite = p;
		
		switch(p){
			case Basse:
				this.type = AudioVisuel.AUDIO;
				break;
			case Moyenne:
				this.type = AudioVisuel.VISUEL;
				break;
			case Haute:
				this.type = AudioVisuel.AUDIOVISUEL;
				break;
			case Max:
				this.type = AudioVisuel.AUDIOVISUEL;
				break;
		}
		
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
	 * @return Type AudioVisuel de l'alarme.
	 */
	public AudioVisuel getTypeAudioVisuel(){
		return this.type;
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
	public static int random(int high) {
		return random.nextInt(high);
	}
}
