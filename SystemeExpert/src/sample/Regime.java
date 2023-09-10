package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.*;

public class Regime implements Initializable {


    @FXML
    private TextFlow Diagnostique;

    @FXML
    private ComboBox Cmedic;

    @FXML
    private Label activitephy;

    @FXML
    private ComboBox activitphy;

    @FXML
    private TextField age;

    @FXML
    private ComboBox alergieAlimantaire;

    @FXML
    private Label condition;

    @FXML
    private TextField objectif;

    @FXML
    private TextField poids;

    @FXML
    private ComboBox pref;

    @FXML
    private Label preferences;

    @FXML
    private ComboBox sexe;

    @FXML
    private Button start;

    @FXML
    private TextField taille;


    public Map<String, Boolean> baseFait = new HashMap<String, Boolean>();


    @FXML
    void SelectObjectif(MouseEvent event) {

    }

    @FXML
    void selectAPHY(ActionEvent event) {
        Object selectedItem = activitphy.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            String s = String.valueOf(selectedItem);
            baseFait.put(s, true);
        }
    }


    @FXML
    void selectAa(ActionEvent event) {
        String s= String.valueOf(alergieAlimantaire.getSelectionModel().getSelectedItem());
        baseFait.put(s, true);

    }

    @FXML
    void selectCONDmedic(ActionEvent event) {
        String s= String.valueOf(Cmedic.getSelectionModel().getSelectedItem());
        baseFait.put(s, true);

    }

    @FXML
    void selectCond(MouseEvent event) {

    }

    @FXML
    void selectPreferenece(MouseEvent event) {

    }

    @FXML
    void selectage(ActionEvent event) {

    }

    @FXML
    void selectobjectif(ActionEvent event) {

    }

    @FXML
    void selectpoid(ActionEvent event) {

    }

    @FXML
    void selectpref(ActionEvent event) {
        String s= String.valueOf(pref.getSelectionModel().getSelectedItem());
        baseFait.put(s, true);

    }

    @FXML
    void selectsexe(ActionEvent event) {
        String s = String.valueOf(sexe.getSelectionModel().getSelectedItem());
        if (s.equals("Femme")) {
            baseFait.put("Femme", true);
        } else {
            baseFait.put("Homme", true);
        }

    }

    @FXML
    void selecttaille(ActionEvent event) {

    }

    @FXML
    void selelectAa(MouseEvent event) {

    }

    @FXML
    void selrctAPH(MouseEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> list1 = FXCollections.observableArrayList("Diabète de type 2", "Hypertension artérielle", "Syndrome de l'intestin irritable");
        Cmedic.setItems(list1);
        ObservableList<String> list2 = FXCollections.observableArrayList("Niveau Sédentaire", "Niveau Léger", "Niveau Modéré", "Niveau Actif", "Niveau Très actif");
        activitphy.setItems(list2);
        ObservableList<String> list3 = FXCollections.observableArrayList("Végétarien", "Carnivore", "Végétalien");
        pref.setItems(list3);
        ObservableList<String> list4 = FXCollections.observableArrayList("Homme", "Femme");
        sexe.setItems(list4);
        ObservableList<String> list5 = FXCollections.observableArrayList("Allergie aux arachides", "Allergie au gluten", "Allergie au lait");
        alergieAlimantaire.setItems(list5);
    }

    @FXML
    void startOnaction(ActionEvent event) {
        // Récupération de l'objectif
        String text = objectif.getText();


        // Récupération de l'âge
        String text2 = age.getText();
        if (!text2.isEmpty()) {
            int AAge = Integer.parseInt(text2);
            if (AAge < 18) {
                baseFait.put("Enfant", true);
            } else {
                baseFait.put("Adulte", true);
            }
        }

        // Récupération de la taille
        String text3 = taille.getText();
        if (!text3.isEmpty()) {
            int taillle = Integer.parseInt(text3);
            if (taillle < 150) {
                baseFait.put("Petit de taille", true);
            } else {
                baseFait.put("Grand de taille", true);
            }
        }

        // Récupération du poids
        String text4 = poids.getText();
        if (!text4.isEmpty()) {
            int poidss = Integer.parseInt(text4);
            if (poidss > 100) {
                baseFait.put("Lourd", true);
            } else if (poidss > 60) {
                baseFait.put("Normal", true);
            } else {
                baseFait.put("Tres leger", true);
            }
        }

        Diagnostique.getChildren().clear();
        // définir quelques règles
        List<Regle> regles = new ArrayList<Regle>();
        //Par défaut, toutes les règles sont actives
        // Règles basées sur les allergies alimentaires
        regles.add(new Regle(Arrays.asList("Allergie aux arachides", "Femme" , "Adulte", "Tres leger"), "Régime sans arachides", true, 1));
        regles.add(new Regle(Arrays.asList("Allergie au gluten","Homme", "Normal", "Adulte"), "Régime sans gluten", true, 2));
        regles.add(new Regle(Arrays.asList("Allergie au lait","Normal", "Adulte"), "Régime sans lactose", true, 3));
        regles.add(new Regle(Arrays.asList("Homme", "Allergie aux arachides", "Normal", "Adulte"), "Régime sans arachides", true, 4));

        regles.add(new Regle(Arrays.asList("Allergie aux arachides"), "Régime sans arachides", true, 19));
        regles.add(new Regle(Arrays.asList("Allergie au gluten"), "Régime sans gluten", true, 20));
        regles.add(new Regle(Arrays.asList("Allergie au lait"), "Régime sans lactose", true, 21));


        // Règle basée sur le niveau d'activité physique, les préférences alimentaires et les conditions médicales
        regles.add(new Regle(Arrays.asList("Niveau Modéré", "Végétarien", "Syndrome de l'intestin irritable"), "Régime végétarien", true, 5));

        // Règles basées sur les conditions médicales
        regles.add(new Regle(Arrays.asList("Diabète de type 2", "Normal"), "Réduction du sucre", true, 6));
        regles.add(new Regle(Arrays.asList("Hypertension artérielle", "Lourd", "Adulte"), "Régime pour l'hypertension", true, 7));
        regles.add(new Regle(Arrays.asList("Syndrome de l'intestin irritable", "Adulte", "Normal"), "Réduction du cholestérol", true, 8));

        // Règles basées sur le niveau d'activité physique
        regles.add(new Regle(Arrays.asList("Niveau Sédentaire", "Enfant", "Lourd"), "Perte de poids", true, 9));
        regles.add(new Regle(Arrays.asList("Niveau Léger", "Femme", "Adulte", "Lourd"), "Perte de poids", true, 10));
        regles.add(new Regle(Arrays.asList("Niveau Modéré", "Homme", "Adulte", "Allergie au gluten"), "Régime sans gluten", true, 11));
        regles.add(new Regle(Arrays.asList("Actif", "Adulte", "Tres leger" ), "Prise de masse musculaire", true, 12));
        regles.add(new Regle(Arrays.asList("Très actif", "Adulte", "Tres leger"), "Prise de masse musculaire", true, 13));

        // Règles basées sur les préférences alimentaires
        regles.add(new Regle(Arrays.asList("Végétarien", "Normal", "Adulte"), "Régime végétarien", true, 14));
        regles.add(new Regle(Arrays.asList("Carnivore","Normal", "Adulte"), "Perte de poids", true, 15));
        regles.add(new Regle(Arrays.asList("Végétalien", "Adulte", "Tres leger"), "Prise de masse musculaire", true, 16));

        // Règles basées sur le sexe
        regles.add(new Regle(Arrays.asList("Femme" , "Adulte", "Tres leger"), "Prise de masse musculaire", true, 17));

        // Règle basée sur l'objectif nutritionnel, l'âge, le poids, le sexe et les allergies alimentaires
        regles.add(new Regle(Arrays.asList("Perte de poids", "Adulte", "Normal", "Femme", "Allergie au gluten"), "Maintien du poids", true, 18));



        // créer le moteur d'inférence et l'exécuter sur la base de faits
        MoteurInference moteur = new MoteurInference(baseFait, regles);


        String msg = null;
        List<String> buts = new ArrayList<String>();
        if(!text.isEmpty()){
            buts.add(text);
            msg = null;
            for (String but : buts) {
                moteur.chainageAvant();
                System.out.println("Yoo");
                boolean estVrai = moteur.baseFait.containsKey(but) ? moteur.baseFait.get(but) : false;
                System.out.println(estVrai);
                if (estVrai){
                    msg="\nLe résultat de notre diagnostique montre que il vous faut un ' "+but+" '\n\n\n Assurez-vous de prendre soin de vous " +
                            " en adoptant une alimentation équilibrée " + "N'hésiter pas de consulter un médecin et suivre ses recommandations"+
                            "\n\n\n Merci pour votre confiance ";

                }else{ msg= "\nNous vous conseillons d'être attentif lors du choix d'un régime, car c'est une décision délicate qui nécessite une réflexion approfondie. \n\n" +
                        "Le régime actuel ne semble pas adapté à votre situation. Afin de vous fournir un régime plus approprié, nous vous recommandons d'utiliser notre système sans spécifier d'objectif particulier.\n" +
                        " Cela nous permettra d'évaluer votre situation de manière plus globale et de vous proposer un régime plus adapté à vos besoins spécifiques. "+
                        "\n\n\n Merci pour votre confiance ";}
            }

        }else {// Exécution du chainage avant pour chaque but
            buts.add("Régime sans arachides");
            buts.add("Régime sans gluten");
            buts.add("Régime sans lactose");
            buts.add("Régime végétarien");
            buts.add("Réduction du sucre");
            buts.add("Régime pour l'hypertension");
            buts.add("Réduction du cholestérol");
            buts.add("Perte de poids");
            buts.add("Prise de masse musculaire");
            buts.add("Régime carnivore");
            buts.add("Maintien du poids");

            for (String but : buts) {
                moteur.chainageAvant();
                System.out.println("Yoo");
                boolean estVrai = moteur.baseFait.containsKey(but) ? moteur.baseFait.get(but) : false;
                System.out.println(estVrai);
                if (estVrai){
                    msg="\nLe résultat de notre diagnostique montre que il vous faut un ' "+but+" '\n\n\n Assurez-vous de prendre soin de vous " +
                            " en adoptant une alimentation équilibrée " + "N'hésiter pas de consulter un médecin et suivre ses recommandations"+
                            "\n\n\n Merci pour votre confiance ";
                    break;
                }
                else{msg= "Il n y a pas un régime qyu semble  adapté à votre situation. \n" +
                        " Cela nous permettra d'évaluer votre situation de manière plus globale et de vous proposer un régime plus adapté à vos besoins spécifiques. "+
                        "\n\n\n Merci pour votre confiance ";}
            }

        }




        Text text1 = new Text(msg);
        text1.setStyle("-fx-font-weight: bold; -fx-fill:  #ffffff;");
        Diagnostique.getChildren().add(text1);
        Diagnostique.setTextAlignment(TextAlignment.CENTER);






    }


}