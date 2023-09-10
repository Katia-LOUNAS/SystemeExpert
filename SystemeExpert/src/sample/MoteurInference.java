package sample;

import java.util.*;

/* "MoteurInference" est la classe principale contenant la methode de chainage avant */
public class MoteurInference {

    public Map<String, Boolean> baseFait;
    private List<Regle> regles;

    /* Le constructeur de la classe "MoteurInference". Il prend en entrée deux arguments à savoir "baseFait" qui contient les faits
     * initiaux et "regles" qui contient l'ensemble des listes     */

    public MoteurInference(Map<String, Boolean> baseFait, List<Regle> regles) {
        this.baseFait = baseFait;
        this.regles = regles;
    }



    private void trierReglesParPriorite() {
        regles.sort(Comparator.comparing(Regle::getPriorite));
    }

    /*  La méthode principale "chainage avant"   */
    public void chainageAvant() {
        /*  Cette variable indique si de nouveaux faits ont été inférés pendant.
         * Elle est initialisée à true pour garantir que la boucle s'exécute au moins une fois!.   */
        boolean nouveauxFaits = true;
        trierReglesParPriorite(); // Triez les règles par priorité avant de les appliquer.
        /*  C'est la boucle principale de l'algorithme. Elle s'exécute tant que de nouveaux faits sont inférés.  */
        while (nouveauxFaits) {
            /* Au début de chaque itération de la boucle, nous supposons qu'aucun nouveau fait ne sera inféré.
             * Cela est dû au fait que nous n'avons pas encore appliqué de règles.  */
            nouveauxFaits = false;
            /* Cette boucle itère sur toutes les règles stockées dans la liste "regles". */
            for (Regle regle : regles) {
                /* La méthode "toutesLesPremissesSontVraies" teste si toutes les prémices de la règle courante sont vraies.
                 * Si c'est le cas, la règle peut être appliquée. */
                if (toutesLesPremissesSontVraies(regle)&& regle.isActive()) {
                    /* Si oui, nous récupérons la conclusion de la régle courante */
                    String conclusion = regle.getConclusion();
                    /* Puis, nous vérifions si cette conclusion est déjà dans la base de la baseFait. */
                    if (!baseFait.containsKey(conclusion)) {
                        /* Si ce n'est pas le cas, on rajoute la conclusion à la base de fait (mise à jour). */
                        baseFait.put(conclusion, true);
                        nouveauxFaits = true;
                        regle.setActive(false);
                    }
                }
            }
        }
    }



    /* Cette méthode permet de vérifier si toutes les prémisses d'une règle sont vraies en parcourant la liste des prémisses,
     * en les comparant avec les faits dans baseFait et en renvoyant false
     * si une prémisses est fausse et true si toutes les prémisses sont vraies.  */
    private boolean toutesLesPremissesSontVraies(Regle regle) {
        for (String premisses : regle.getPremises()) {
            if (!baseFait.containsKey(premisses) || !baseFait.get(premisses)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        // initialiser la base de faits avec des faits initiaux
        Map<String, Boolean> baseFait = new HashMap<String, Boolean>();
        baseFait.put("Fièvre", true);
        baseFait.put("Douleur thoracique", true);
        baseFait.put("Toux", true);
        baseFait.put("Essoufflement", true);
        baseFait.put("Maux de tête", false);
        baseFait.put("Fatigue", false);
        baseFait.put("Douleurs musculaires", false);
        baseFait.put("Perte de l'odorat ou du goût", false);
        //  baseFait.put("Nausées ou vomissements", false);
        baseFait.put("Diarrhée", false);
        baseFait.put("Mal de gorge", false);
        //  baseFait.put("Congestion ou écoulement nasal", false);
        baseFait.put("Malaise général", false);

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
        regles.add(new Regle(Arrays.asList("Douleur thoracique"), "Maladie cardiaque",true,8));
        regles.add(new Regle(Arrays.asList("Toux"), "Infection respiratoire",true,9));
        regles.add(new Regle(Arrays.asList("Essoufflement"), "Maladie pulmonaire",true,10));
        regles.add(new Regle(Arrays.asList("Maux de tête"), "Migraine",true,11));
        regles.add(new Regle(Arrays.asList("Diarrhée"), "Infection gastro-intestinale",true,12));
        regles.add(new Regle(Arrays.asList("Maux de tête","Perte de l'odorat ou du goût","Fièvre"), "COVID-19",true,13));
        regles.add(new Regle(Arrays.asList("Fièvre","Mal de gorge"), "Angine",true,14));
        regles.add(new Regle(Arrays.asList("Toux","Essoufflement"), "Goitre",true,15));



        // créer le moteur d'inférence et l'exécuter sur la base de faits
        MoteurInference moteur = new MoteurInference(baseFait, regles);

        // Liste des buts à prouver
        Scanner scanner = new Scanner(System.in);

        System.out.print("Combien de buts voulez-vous prouver ? ");
        int n = scanner.nextInt();

        List<String> buts = new ArrayList<String>();
        for (int i = 1; i <= n; i++) {
            System.out.print("But " + i + ": ");
            String but = scanner.next();
            buts.add(but);
        }

        // Exécution du chainage avant pour chaque but
        for (String but : buts) {
            moteur.chainageAvant();
            boolean estVrai = moteur.baseFait.containsKey(but) ? moteur.baseFait.get(but) : false;
            System.out.println(but + " est " + estVrai);
        }
    }



}


/* C'est la classe qui représente la base de régles. Elle comporte deux variables représentent respectivement la
 * liste des prémisses de la règle et sa conclusion. Les prémisses sont une liste de chaînes de caractères
 * et la conclusion est une chaîne de caractères.  */
class Regle {

    private List<String> premisses;
    private String conclusion;
    private  boolean Active;
    private int Priorite;

    public Regle(List<String> premisses, String conclusion, boolean active, int priorite) {
        this.premisses = premisses;
        this.conclusion = conclusion;
        Active = active;
        Priorite = priorite;
    }

    public List<String> getPremises() {
        return premisses;
    }

    public String getConclusion() {
        return conclusion;
    }
    public boolean isActive() {
        return Active;
    }

    public void setActive(boolean active) {
        Active = active;
    }

    public int getPriorite() {
        return Priorite;
    }
}

