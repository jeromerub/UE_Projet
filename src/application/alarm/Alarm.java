package application.alarm;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

import application.priorite.Priorite;

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
	
	/* Génération d'une alarme aléatoire */
	public Alarm(){
		Date d = new Date();
		
		this.created = new Timestamp(d.getTime());
		this.nom = "Alarme " + ++cptRandom;
		
		switch(random(4)){
			case 0: this.priorite = Priorite.Basse; break;
			case 1: this.priorite = Priorite.Moyenne; break;
			case 2: this.priorite = Priorite.Haute; break;
			case 3: this.priorite = Priorite.Max; break;
			default: System.out.println("Err rand");
		}
		
		this.desc = "Alarme de priorité " + this.priorite;
		this.isTreated = false;
		
	}
	
	/* Génération d'une alarm avec un nom, une description aléatoire et une priorité aléatoire */
	public Alarm(String nom){
		Date d = new Date();
		
		this.created = new Timestamp(d.getTime());
		this.nom = nom;
		
		switch(random(4)){
			case 0: this.priorite = Priorite.Basse; break;
			case 1: this.priorite = Priorite.Moyenne; break;
			case 2: this.priorite = Priorite.Haute; break;
			case 3: this.priorite = Priorite.Max; break;
			default: System.out.println("Err rand");
		}
		
		this.desc = "Alarme de priorité " + this.priorite;
		this.isTreated = false;
	}
	
	/* Génération d'une alarme avec un nom et une description et une priorité aléatoire */
	public Alarm(String nom, String desc){
		Date d = new Date();
		
		this.created = new Timestamp(d.getTime());
		this.nom = nom;
		this.desc = desc;
		this.isTreated = false;
		
		switch(random(4)){
			case 0: this.priorite = Priorite.Basse; break;
			case 1: this.priorite = Priorite.Moyenne; break;
			case 2: this.priorite = Priorite.Haute; break;
			case 3: this.priorite = Priorite.Max; break;
			default: System.out.println("Err rand");
		}		
	}
	
	/* Génération d'une alarme avec un nom, une description et une priorité */
	public Alarm(String nom, String desc, Priorite p){
		Date d = new Date();
		
		this.created = new Timestamp(d.getTime());
		this.nom = nom;
		this.priorite = p;		
		this.desc = desc;
		this.isTreated = false;
	}
	
	public String getNom(){
		return this.nom;
	}
	
	public String getDesc(){
		return this.desc;
	}

	public Priorite getPriorite(){
		return this.priorite;
	}
	
	public Timestamp getTimestamp(){
		return this.created;
	}
	
	public void setTreated(){
		this.isTreated = true;
	}
	
	public boolean isTreated(){
		return this.isTreated;
	}
	
	/* Affiche sur stdout des infos sur l'alarme */
	public void display(){
		System.out.println(nom);
		System.out.println(desc);
		System.out.println();
	}
	
	/* Génére un nombre entre 0 (inclu) et high (exclu) */
	private static int random(int high) {
		return random.nextInt(high);
	}
}
