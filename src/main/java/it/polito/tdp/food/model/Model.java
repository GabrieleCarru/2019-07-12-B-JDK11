package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	private List<Food> cibi;
	private FoodDao dao;
	private Graph<Food, DefaultWeightedEdge> graph;
	
	public Model() {
		dao = new FoodDao();
	}
	
	public List<Food> getFoodsByPortions(int portions) {
		this.cibi = dao.getFoodsByPortions(portions);
		return this.cibi;
	}
	
	public void creaGrafo() {
		
		// Crea un grafo nuovo ogni volta che facciamo un'analisi
		// Esercizio come in videolezione, scegliendo un grafo non orientato (a differenza pdf)
		this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//Aggiungo i vertici
		Graphs.addAllVertices(this.graph, cibi);
		
		//Aggiungo gli archi: necessito di altri dati dal database
		for(Food f1 : this.cibi) {
			for(Food f2 : this.cibi) {
				// La seconda condizione è perchè il grafo NON è orientato.
				if(!f1.equals(f2) && f1.getFood_code() < f2.getFood_code()) {
					Double peso = dao.calorieCongiunte(f1, f2);
					if(peso != null) {
						Graphs.addEdge(this.graph, f1, f2, peso);
					}
				}
			}
		} 
		System.out.println(this.graph);
	}
	
	public List<FoodCalories> elencoCibiConnessi (Food partenza) {
		
		// Interrogazione del grafo
		List<FoodCalories> result = new ArrayList<>();
		
		List<Food> vicini = Graphs.neighborListOf(this.graph, partenza);
		
		for(Food v : vicini) {
			Double calorie = this.graph.getEdgeWeight(this.graph.getEdge(partenza, v));
			result.add(new FoodCalories(v, calorie));
		}
		
		Collections.sort(result);
		
		return result;
	}
	
	public String Simula(Food cibo, int k) {
		Simulator sim = new Simulator(this.graph, this);
		sim.setK(k);
		sim.init(cibo);
		sim.run();
		String messaggio = String.format("Preparati %d cibi in %f minuti. \n", 
										sim.getCibiPreparati(), sim.getTempoPreparazione());
		return messaggio;
	}
	
}
