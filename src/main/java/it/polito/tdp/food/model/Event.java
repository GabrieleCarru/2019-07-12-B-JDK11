package it.polito.tdp.food.model;

public class Event implements Comparable<Event>{

	public enum EventType {
		INIZIO_PREPARAZIONE, 	//viene assegnato un cibo ad una stazione 
		FINE_PREPARAZIONE, 		//la stazione ha completato la preparazione di un cibo
	}
	
	private Double time; //Tempo in minuti
	private Stazione stazione;
	private Food food;
	private EventType type;

	/**
	 * @param time
	 * @param stazione
	 * @param food
	 */
	public Event(double time, EventType type, Stazione stazione, Food food) {
		super();
		this.time = time;
		this.type = type;
		this.stazione = stazione;
		this.food = food;
	}
	
	public Double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	public EventType getType() {
		return type;
	}
	public void setType(EventType type) {
		this.type = type;
	}
	public Stazione getStazione() {
		return stazione;
	}
	public void setStazione(Stazione stazione) {
		this.stazione = stazione;
	}
	public Food getFood() {
		return food;
	}
	public void setFood(Food food) {
		this.food = food;
	}

	@Override
	public int compareTo(Event other) {
		return this.time.compareTo(other.time);
	}
	
	
	
}

/* COMMENTO PER LINEE GUIDA:
 * S0[Food1] S1[Food2] S2[Food3]
 * 		FINE_PREPARAZIONE (F2, Stazione 1) ---> Tempo T
 * 		INIZIO_PREPARAZIONE (F2 (cibo concluso), Stazione 1) ---> Tempo T
 * 			scelgo il prossimo cibo
 * 			calcolo la durata
 * 			schedulo evento FINE(cibo nuovo appena scelto)
 * 		FINE_PREPARAZIONE (F4, STAZIONE 1) ---> T + DELTA    CON DELTA(peso di F2 -- F4)
 * 			rischedulo evento di INIZIO allo stesso istante, ricordando il cibo terminato
 */

