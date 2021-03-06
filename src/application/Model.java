package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import application.alarm.Alarm;
import application.audiovisuel.AudioVisuel;
import application.priorite.Priorite;

/**
 * Classe représentant notre modèle de données.
 * @author Floo'
 */
public class Model implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static transient int volume = 100;
	
	private transient View view;
	private List<Alarm> listAlarm = new ArrayList<Alarm>();
	
	private boolean isSortedTime = true;
	private boolean isSortedPrio = false;
	private boolean isSortedReverse = false;
	
	/**
	 * Chargement du modèle, si le fichier n'existe pas, il est crée.
	 */
	@SuppressWarnings("unchecked")
	public Model(){	
		
		/* A la création on charge les alarmes et le type de tri dans le model */
		
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("alarms"));
			
			this.listAlarm = (List<Alarm>) ois.readObject();
			this.isSortedTime = (boolean) ois.readBoolean();
			this.isSortedPrio = (boolean) ois.readBoolean();
			this.isSortedReverse = (boolean) ois.readBoolean();
			
			ois.close();
			
		/* Captures des exceptions */
			
		} catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    } catch (FileNotFoundException e) {
			save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sauvegarde le contexte du modèle dans un fichier "alarms".
	 */
	public void save(){
		ObjectOutputStream oos = null;
		
		try {
			oos = new ObjectOutputStream(new FileOutputStream("alarms"));
			oos.writeObject(this.getListAlarm());
			oos.writeBoolean(this.getSortedTime());
			oos.writeBoolean(this.getSortedPrio());
			oos.writeBoolean(this.getSortedReverse());
			oos.flush();
			
		} catch (FileNotFoundException e) {
		      e.printStackTrace();
	    } catch (IOException e) {
	      e.printStackTrace();
	    } finally {
	    	try {
	    		if (oos != null) {
	    			oos.flush();
	    			oos.close();
	    		}
	    	} catch (IOException ex) {
	    		ex.printStackTrace();
	    	}
	    }
	}
	
	/**
	 * Indique a la vue de rafraichir l'affichage.
	 */
	public void notifyView(){
		this.getView().refreshList();
		this.getView().refreshTopDesc();
		this.getView().refreshVolume();
		this.getView().refreshSortInfo();
	}
	
	/**
	 * Ajoute une alarme à la liste.
	 * @param a 
	 * 			Alarme à ajouter.
	 */
	public void addAlarm(Alarm a){
		this.listAlarm.add(a);
		
		if(a.getTypeAudioVisuel() != AudioVisuel.AUDIO)
			this.getView().newWindow(a);

		if (getHigherPriority() != Priorite.Max){
			this.getView().emettreSon(a);
		} else {
			if (a.getPriorite() == Priorite.Max){
				this.getView().emettreSon(a);
			}
		}
		
		save();
		sort();
		notifyView();
	}
	
	/**
	 * Supprime une alarme de la liste.
	 * @param a 
	 * 			Alarme à supprimer.
	 */
	public void removeAlarm(Alarm a){
		this.listAlarm.remove(a);
		save();
		notifyView();
	}	
	
	/**
	 * Traite une alarme de la liste.
	 * @param a 
	 * 			Alarme à traiter.
	 */
	public void treatAlarm(Alarm a){
		a.setTreated();
		save();
		notifyView();
	}
	
	/**
	 * Vide la liste d'alarme.
	 */
	public void clear(){
		this.listAlarm.clear();
		save();
		notifyView();
	}

	/**
	 * Tri la liste dans l'ordre anti-chronologique.
	 */
	public void sortByTimeUp(){
		Collections.sort(this.listAlarm, new Comparator<Alarm>(){
			public int compare(Alarm a1, Alarm a2){
				return a2.getTimestamp().compareTo(a1.getTimestamp());
			}
		});
		
		/* MaJ de la liste du model */
		this.setListAlarm(this.listAlarm);
		this.setSortedTime();
		this.setSortedNormal();
		save();
		notifyView();
	}
	
	/**
	 * Tri la liste dans l'ordre chronologique.
	 */
	public void sortByTimeDown(){
		Collections.sort(this.listAlarm, new Comparator<Alarm>(){
			public int compare(Alarm a1, Alarm a2){
				return a1.getTimestamp().compareTo(a2.getTimestamp());
			}
		});
		
		/* MaJ de la liste du model */
		this.setListAlarm(this.listAlarm);
		this.setSortedTime();
		this.setSortedReverse();
		save();
		notifyView();
	}
	
	/**
	 * Tri la liste par priorité (plus grande à plus petite).
	 */
	public void sortByPrioUp(){
		Collections.sort(this.listAlarm, new Comparator<Alarm>(){
			public int compare(Alarm a1, Alarm a2){
				return a2.getPriorite().compareTo(a1.getPriorite());
			}
		});
		
		/* MaJ de la liste du model */
		this.setListAlarm(this.listAlarm);
		this.setSortedPrio();
		this.setSortedNormal();
		save();
		notifyView();
	}
	
	/**
	 * Tri la liste par priorité (plus petite à plus grande).
	 */
	public void sortByPrioDown(){
		Collections.sort(this.listAlarm, new Comparator<Alarm>(){
			public int compare(Alarm a1, Alarm a2){
				return a1.getPriorite().compareTo(a2.getPriorite());
			}
		});
		
		/* MaJ de la liste du model */
		this.setListAlarm(this.listAlarm);
		this.setSortedPrio();
		this.setSortedReverse();
		save();
		notifyView();
	}
	
	/**
	 * Tri la liste selon son état.
	 */
	private void sort(){
		if(this.isSortedPrio && !this.isSortedReverse){
			sortByPrioUp();
		}
		
		if(this.isSortedPrio && this.isSortedReverse){
			sortByPrioDown();
		}
		
		if(this.isSortedTime && !this.isSortedReverse){
			sortByTimeUp();
		}
		
		if(this.isSortedTime && this.isSortedReverse){
			sortByTimeDown();
		}
	}
	
	/* Getter/Setters */
	
	/**
	 * @return La vue.
	 */
	public View getView(){
		return this.view;
	}
	
	/**
	 * @param v
	 * 			La nouvelle vue.
	 */
	public void setView(View v){
		this.view = v;
	}
	
	/**
	 * Retourne la plus haute priorite d'alarme. Par défaut retourne "Basse".
	 * @return La priorité la plus haute parmis la liste.
	 */
	public Priorite getHigherPriority(){
		Priorite result = Priorite.Basse;
		
		for(Alarm a : getListAlarm()){
			if((a.getPriorite().compareTo(result) > 0) && a.isTreated() == false)
				result = a.getPriorite();
				
		}
		
		return result;
	}
	
	/**
	 * @return La liste d'alarmes.
	 */
	public List<Alarm> getListAlarm(){
		return this.listAlarm;
	}
	
	/**
	 * @return La liste des alarmes Visuelles et AudioVisuelles.
	 */
	public List<Alarm> getVisualListAlarm(){
		List<Alarm> result = new ArrayList<Alarm>();
		
		for(Alarm a : getListAlarm()){
			if(a.getTypeAudioVisuel() != AudioVisuel.AUDIO)
				result.add(a);
		}
		
		return result;
	}
	
	/**
	 * @param newlist
	 * 			La nouvelle liste.
	 */
	public void setListAlarm(List<Alarm> newlist){
		this.listAlarm = newlist;
	}
	
	/**
	 * @return "True" si trié par chronologie.
	 */
	public boolean getSortedTime(){
		return this.isSortedTime;
	}
	
	/**
	 * @return "True" si trié par priorité.
	 */
	public boolean getSortedPrio(){
		return this.isSortedPrio;
	}
	
	/**
	 * @return "True" si trié par ordre inverse (chronologique ou priorité faible à max).
	 */
	public boolean getSortedReverse(){
		return this.isSortedReverse;
	}
	
	/**
	 * Indique que le tri est chronologique.
	 */
	public void setSortedTime(){
		this.isSortedTime = true;
		this.isSortedPrio = false;
	}
	
	/**
	 * Indique que le tri est par priorité.
	 */
	public void setSortedPrio(){
		this.isSortedTime = false;
		this.isSortedPrio = true;
	}
	
	/**
	 * Indique que le tri est normal (antichronologique ou priorité max à faible).
	 */
	public void setSortedNormal(){
		this.isSortedReverse = false;
	}
	
	/**
	 * Indique que le tri est normal (chronologique ou priorité faible à max).
	 */
	public void setSortedReverse(){
		this.isSortedReverse = true;
	}
	
	/**
	 * Génère un volume aléatoire puis le retourne
	 * @return Le volume de l'appli.
	 */
	public static int getVolume(){
		return Model.volume;
	}
	
	/**
	 * Modifie le volume de l'appli.
	 */
	public static void setVolume(int v){
		Model.volume = v;
	}
}
