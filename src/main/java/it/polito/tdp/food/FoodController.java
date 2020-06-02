/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.FoodCalories;
import it.polito.tdp.food.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtPorzioni"
    private TextField txtPorzioni; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="btnGrassi"
    private Button btnGrassi; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="boxFood"
    private ComboBox<Food> boxFood; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	
    	try {
    		int portions = Integer.parseInt(txtPorzioni.getText());
        	List<Food> cibi = model.getFoodsByPortions(portions);
        	
        	// La svuoto per evitare che più pressioni del bottone riempiano più volte la tendina
        	boxFood.getItems().clear(); 
        	boxFood.getItems().addAll(cibi);
        	
        	model.creaGrafo();
        	
        	if(cibi.isEmpty()) {
        		txtResult.appendText("È stato selezionato un numero di portioni "
        				+ "per cui non esistono cibi. \n");
        	} else {
        		txtResult.appendText("Il menù a tendina è stato caricato correttamente. \n");
        	}
        	
        	
    	} catch(NumberFormatException e) {
    		e.printStackTrace();
    		txtResult.setText("ERRORE: Devi inserire un numero.");
    		return;
    	}
    	
    	
    }

    @FXML
    void doGrassi(ActionEvent event) {
    	txtResult.clear();
    	
    	Food selezionato = boxFood.getValue();
    	
    	if(selezionato == null) {
    		txtResult.appendText("ERRORE: devi selezionare un cibo dal menù. \n");
    		return;
    	}
    	
    	List<FoodCalories> lista = model.elencoCibiConnessi(selezionato);    
    	
    	for(int i = 0; i < 5 && i<lista.size(); i++) {
    		txtResult.appendText(String.format("%s %f \n", 
    				lista.get(i).getFood().getDisplay_name(), 
    				lista.get(i).getCalories()));
    	}
    	
    }

    @FXML
    void doSimula(ActionEvent event) {
    	txtResult.clear();
    	
    	Food selezionato = boxFood.getValue();
    	
    	if(selezionato == null) {
    		txtResult.appendText("ERRORE: devi selezionare un cibo dal menù. \n");
    		return;
    	}
    	
    	int k;
    	try {
    		k = Integer.parseInt(txtK.getText());
    	} catch (NumberFormatException e) {
    		e.printStackTrace();
    		txtResult.appendText("K deve essere un numero. \n");
    		return;
    	}
    	
    	String messaggio = model.Simula(selezionato, k);
    	txtResult.appendText(messaggio);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtPorzioni != null : "fx:id=\"txtPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnGrassi != null : "fx:id=\"btnGrassi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxFood != null : "fx:id=\"boxFood\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
