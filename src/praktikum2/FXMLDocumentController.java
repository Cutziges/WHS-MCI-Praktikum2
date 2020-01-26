
package praktikum2;

import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * FXML Controller class.
 *
 * @author Sarah Grugiel
 */
public class FXMLDocumentController implements Initializable {

    /**
     * Objekte.
     */
    @FXML
    private ComboBox<String> groessenwahl;
    @FXML
    private TextField tfFlaschenpreis;
    @FXML
    private Button bRunter;
    @FXML
    private Button bRauf;
    
    @FXML
    private TextField tfLiterpreis;
    
    /**
     * Abfragevariablen.
     */
    private boolean isBottleCalcLast = false;
    private boolean isLiterCalcLast = false;
    // tausender-Trenner und dezimal
    private static final String REGEX = "(\\d*,?\\d*)|(\\d{0,3}"
            + "(\\.\\d{3})*,?\\d*)";
    
    /**
     * Aktion beim Klicken des runter Buttons.
     * @param event Event
     */
    @FXML
    private void bRunterAction(ActionEvent event) {
        berechneLiterpreis();   
    }
    
    /**
     * Aktion beim Klicken des rauf Buttons.
     * @param event Event
     */
    @FXML
    private void bRaufAction(ActionEvent event) {
        berechneFlaschenpreis();
    }
    
    // Liste mit gängigen Flaschengrößen
    ObservableList<String> groessenListe = 
            FXCollections.observableArrayList(
        "0,187 l", 
        "0,25 l", 
        "0,375 l", 
        "0,5 l", 
        "0,62 l", 
        "0,7 l", 
        "0,75 l", 
        "0,8 l", 
        "1 l", 
        "1,5 l" 
    );
    
    /**
     * Initializes the controller class.
     * @param url URL
     * @param rb ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        groessenwahl.setItems(groessenListe);
        groessenwahl.setValue("0,75 l");
        tfLiterpreis.setAlignment(Pos.CENTER_RIGHT);
        tfFlaschenpreis.setAlignment(Pos.CENTER_RIGHT);
        
//        tfFlaschenpreis.focusedProperty().addListener((ov, oldV, newV) -> {
//            if (newV) {
//                //tfFlaschenpreis.setText("");
//                tfLiterpreis.setText("");
//            }
//        });
//        
//        tfLiterpreis.focusedProperty().addListener((ov, oldV, newV) -> {
//            if (newV) {
//                tfFlaschenpreis.setText("");
//                //tfLiterpreis.setText("");
//            }
//        });
//        
        
        tfFlaschenpreis.textProperty().addListener((ov, oldV, newV) -> {
            if (tfFlaschenpreis.isFocused()) {
                tfLiterpreis.setText("");
                isBottleCalcLast = false;
                isLiterCalcLast = false;
            }
        });
        
        tfLiterpreis.textProperty().addListener((ov, oldV, newV) -> {
            if (tfLiterpreis.isFocused()) {
                tfFlaschenpreis.setText("");
                isBottleCalcLast = false;
                isLiterCalcLast = false;
            }
        });
        
    }    
    
    /**
     * ComboBox Action.
     * @param event Auswahl
     */
    @FXML
    private void boxAction(ActionEvent event) {
        if (isBottleCalcLast) {
            berechneLiterpreis();
        }
        if (isLiterCalcLast) {
            berechneFlaschenpreis();
        }
    }
    

    /**
     * Methode zur berechnung des Literpreises.
     */
    private void berechneLiterpreis() {
        
        NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        
        if (groessenwahl.getValue() == null) {
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Auswahlfehler");
            alert.setHeaderText(null);
            alert.setContentText("Bitte wählen Sie eine Flaschengröße aus.");
            alert.showAndWait();
            
        } else if (!tfFlaschenpreis.getText().matches(REGEX)) {
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eingabefehler");
            alert.setHeaderText(null);
            alert.setContentText("Bitte geben Sie eine Zahl im "
                    + "Format x,xx ein.");
            alert.showAndWait();
            
            tfFlaschenpreis.selectAll();
            tfFlaschenpreis.requestFocus();
            
        } else {
            try {
                
                double dSize = nf.parse(groessenwahl.getValue())
                        .doubleValue();
                double dPrice = nf.parse(tfFlaschenpreis.getText())
                        .doubleValue();
                double newPrice = dPrice / dSize;
                tfLiterpreis.setText(nf.format(newPrice));
                
                isBottleCalcLast = true;
                isLiterCalcLast = false;
            
            } catch (java.text.ParseException ex) {
                
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Eingabefehler");
                alert.setHeaderText(null);
                alert.setContentText("Bitte geben Sie eine Zahl im "
                        + "Format x,xx ein.");
                alert.showAndWait();
                
                tfFlaschenpreis.selectAll();
                tfFlaschenpreis.requestFocus();
            }
        }
   
    }
    
    /**
     * Methode zur Berechnung des Flaschenpreises.
     */
    private void berechneFlaschenpreis() {
        NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        
        if (groessenwahl.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Auswahlfehler");
            alert.setHeaderText(null);
            alert.setContentText("Bitte wählen Sie eine Flaschengröße aus.");
            alert.showAndWait();
            groessenwahl.isFocused();
        }
        
        if (!tfLiterpreis.getText().matches(REGEX)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eingabefehler");
            alert.setHeaderText(null);
            alert.setContentText("Bitte geben Sie eine Zahl im "
                    + "Format x,xx ein.");
            alert.showAndWait();
            
            tfLiterpreis.selectAll();
            tfLiterpreis.requestFocus();
        } else {
            try {
                double dSize = nf.parse(groessenwahl.getValue()).doubleValue();
                double dPrice = nf.parse(tfLiterpreis.getText()).doubleValue();
                double newPrice = dPrice * dSize;
                tfFlaschenpreis.setText(nf.format(newPrice));
                tfFlaschenpreis.setAlignment(Pos.CENTER_RIGHT);
                isBottleCalcLast = false;
                isLiterCalcLast = true;

            } catch (java.text.ParseException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Eingabefehler");
                alert.setHeaderText(null);
                alert.setContentText("Bitte geben Sie eine Zahl im "
                    + "Format x,xx ein.");
                alert.showAndWait();
                
                tfLiterpreis.selectAll();
                tfLiterpreis.requestFocus();
            }
        }
    }

}