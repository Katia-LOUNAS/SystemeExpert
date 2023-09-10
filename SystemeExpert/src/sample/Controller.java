package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import java.util.*;

public class Controller {

    @FXML
    private TextFlow Diagnostique;

    @FXML
    private RadioButton Fait1;

    @FXML
    private RadioButton Fait10;

    @FXML
    private RadioButton Fait11;

    @FXML
    private RadioButton Fait2;

    @FXML
    private RadioButton Fait3;

    @FXML
    private RadioButton Fait4;

    @FXML
    private RadioButton Fait5;

    @FXML
    private RadioButton Fait6;

    @FXML
    private RadioButton Fait7;

    @FXML
    private RadioButton Fait8;

    @FXML
    private RadioButton Fait9;

    @FXML
    private Button button;


    public Map<String, Boolean> baseFait = new HashMap<String, Boolean>();

    @FXML
    void Fait10checked(ActionEvent event) {

        baseFait.put("Maux de tête", false);

    }

    @FXML
    void Fait11checked(ActionEvent event) {

        baseFait.put("Mal de gorge", false);

    }

    @FXML
    void Fait1checked(ActionEvent event) {

        baseFait.put("Fièvre", true);

    }

    @FXML
    void Fait2checked(ActionEvent event) {

        baseFait.put("Toux", true);

    }

    @FXML
    void Fait3checked(ActionEvent event) {

        baseFait.put("Fatigue", false);

    }

    @FXML
    void Fait4checked(ActionEvent event) {

        baseFait.put("Diarrhée", false);

    }

    @FXML
    void Fait5checked(ActionEvent event) {

        baseFait.put("Douleur thoracique", true);

    }

    @FXML
    void Fait6checked(ActionEvent event) {

        baseFait.put("Perte de l'odorat ou du goût", false);

    }

    @FXML
    void Fait7checked(ActionEvent event) {

        baseFait.put("Douleurs musculaires", false);

    }

    @FXML
    void Fait8checked(ActionEvent event) {

        baseFait.put("Malaise général", false);

    }

    @FXML
    void Fait9checked(ActionEvent event) {

        baseFait.put("Essoufflement", true);

    }

    @FXML
    void buttonChecked(ActionEvent event) {

        Diagnostique.getChildren().clear();
        // définir quelques règles
        List<Regle> regles = new ArrayList<Regle>();
        //Par défaut, toutes les règles sont actives
        regles.add(new Regle(Arrays.asList("Fièvre", "Douleur thoracique", "Toux", "Essoufflement"), "COVID-19",true,1));
        regles.add(new Regle(Arrays.asList("Fièvre", "Douleur thoracique", "Toux"), "Pneumonie",true,2));
        regles.add(new Regle(Arrays.asList("Fièvre", "Toux"), "Grippe",true,3));
        regles.add(new Regle(Arrays.asList("Fièvre", "Maux de tête", "Fatigue"), "Malaria",true,4));
        regles.add(new Regle(Arrays.asList("Fièvre", "Douleurs musculaires", "Fatigue"), "Dengue",true,5));
        regles.add(new Regle(Arrays.asList("Fièvre", "Perte de l'odorat ou du goût", "Mal de gorge"), "COVID-19",true,6));
        regles.add(new Regle(Arrays.asList("Fièvre"), "Infection",true,7));
        regles.add(new Regle(Arrays.asList("Douleur thoracique"), "Infection",true,8));
        regles.add(new Regle(Arrays.asList("Toux"), "Infection respiratoire",true,15));
        regles.add(new Regle(Arrays.asList("Essoufflement"), "Maladie pulmonaire",true,14));
        regles.add(new Regle(Arrays.asList("Maux de tête"), "Migraine",true,11));
        regles.add(new Regle(Arrays.asList("Diarrhée"), "Infection gastro-intestinale",true,13));
        regles.add(new Regle(Arrays.asList("Maux de tête","Perte de l'odorat ou du goût","Fièvre"), "COVID-19",true,12));
        regles.add(new Regle(Arrays.asList("Fièvre","Mal de gorge"), "Angine",true,10));
        regles.add(new Regle(Arrays.asList("Toux","Essoufflement"), "Goitre",true,9));

        // créer le moteur d'inférence et l'exécuter sur la base de faits
        MoteurInference moteur = new MoteurInference(baseFait, regles);

        // Exécution du chainage avant pour chaque but
        List<String> buts = new ArrayList<String>();
        buts.add("COVID-19");
        buts.add("Pneumonie");
        buts.add("Malaria");
        buts.add("Dengue");
        buts.add("Infection");
        buts.add("Migraine");
        buts.add("Infection gastro-intestinale");
        buts.add("Angine");
        buts.add("Goitre");
        buts.add("Infection respiratoire");
        buts.add("Maladie pulmonaire");

        String msg = null;
        for (String but : buts) {
            System.out.println(baseFait);
            moteur.chainageAvant();
            boolean estVrai = moteur.baseFait.containsKey(but) ? moteur.baseFait.get(but) : false;
            if (estVrai){
                msg="\n\nLe résultat de notre diagnostique montre que vous avez la maladie ' "+but+" '\n\n\n Assurez-vous de prendre soin de vous en suivant les recommandations de votre médecin," +
                        " en prenant des médicaments pour soulager vos symptômes, et en obtenant suffisamment de repos pour aider votre corps à guérir. " +
                        "\n\n\n\n Merci pour votre confiance ";
                break;
            }
        }

        Text text = new Text(msg);
        text.setStyle("-fx-font-weight: bold; -fx-fill:  #141D3C;");
        Diagnostique.getChildren().add(text);
        Diagnostique.setTextAlignment(TextAlignment.CENTER);



    }

}
