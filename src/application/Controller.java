package application;

public class Controller {
	private Model model;
	
	public Controller(Model m){
		this.model = m;
	}
	
	public Model getModel(){
		return this.model;
	}
}
