package application;

import java.util.Random;

public class Alarm {
	
	/* Attributs static alarme random */
	private static int cptRandom = 0;
	private static Random random = new Random(123456789L);
	
	/* Attributs */
	private Priorite priorite;
	private String nom;
	private String desc;
	
	/* G�n�ration d'une alarme al�atoire */
	public Alarm(){
		this.nom = "Alarme " + ++cptRandom;
		
		switch(random(4)){
			case 0: this.priorite = Priorite.Basse; break;
			case 1: this.priorite = Priorite.Moyenne; break;
			case 2: this.priorite = Priorite.Haute; break;
			case 3: this.priorite = Priorite.Max; break;
			default: System.out.println("Err rand");
		}
		
		this.desc = "Alarme de priorit� " + this.priorite;
	}
	
	/* G�n�ration d'une alarm avec un nom, une description al�atoire et une priorit� al�atoire */
	public Alarm(String nom){
		this.nom = nom;
		
		switch(random(4)){
			case 0: this.priorite = Priorite.Basse; break;
			case 1: this.priorite = Priorite.Moyenne; break;
			case 2: this.priorite = Priorite.Haute; break;
			case 3: this.priorite = Priorite.Max; break;
			default: System.out.println("Err rand");
		}
		
		this.desc = "Alarme de priorit� " + this.priorite;
	}
	
	/* G�n�ration d'une alarme avec un nom et une description et une priorit� al�atoire */
	public Alarm(String nom, String desc){
		this.nom = nom;
		this.desc = desc;
		
		switch(random(4)){
			case 0: this.priorite = Priorite.Basse; break;
			case 1: this.priorite = Priorite.Moyenne; break;
			case 2: this.priorite = Priorite.Haute; break;
			case 3: this.priorite = Priorite.Max; break;
			default: System.out.println("Err rand");
		}		
	}
	
	/* G�n�ration d'une alarme avec un nom, une description et une priorit� */
	public Alarm(String nom, String desc, Priorite p){
		this.nom = nom;
		this.priorite = p;		
		this.desc = desc;
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
	
	/* Affiche sur stdout des infos sur l'alarme */
	public void display(){
		System.out.println(nom);
		System.out.println(desc);
		System.out.println();
	}
	
	/* G�n�re un nombre entre 0 (inclu) et high (exclu) */
	private static int random(int high) {
		return random.nextInt(high);
	}
}
