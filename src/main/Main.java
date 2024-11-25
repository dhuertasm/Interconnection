package main;
import controller.Controller;
import model.logic.Modelo;
import view.View;

public class Main {
	
	public static void main(String[] args) 
	{
		Controller controller = new Controller(new Modelo(1), new View());
		controller.run();
	}
}
