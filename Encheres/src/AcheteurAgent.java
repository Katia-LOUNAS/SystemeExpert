import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class AcheteurAgent extends Agent {
    private int budget;

    protected void setup() {
        budget = 200;

        // comportement de l'acheteur pour traiter les messages entrants
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                // attendre un message entrant
                ACLMessage msg = blockingReceive();
                if (msg != null) {
                    // traitement du message
                    switch (msg.getPerformative()) {
                        case ACLMessage.PROPOSE:
                            // message de proposition de prix du vendeur
                            int prixPropose = Integer.parseInt(msg.getContent());
                            // vérifier si le prix proposé est inférieur ou égal au budget
                            if (prixPropose <= budget) {
                                // envoyer une offre supérieure
                                int nouvelleOffre = prixPropose + 10;
                                if (nouvelleOffre <= budget) {
                                    System.out.println("j'ai de l'argent");
                                    System.out.println(getLocalName() + " propose un prix de " + nouvelleOffre);
                                    ACLMessage reply = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                                    reply.addReceiver(msg.getSender());
                                    reply.setContent(Integer.toString(prixPropose));
                                    send(reply);
                                }
                                else {
                                    // offre est supérieure au budget, envoyer un refus
                                    ACLMessage reply = msg.createReply();
                                    System.out.println("j'ai pas d'argent");
                                    reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                                    reply.setContent("Budget insuffisant");
                                    send(reply);
                                }
                            }
                            break;
                        case ACLMessage.ACCEPT_PROPOSAL:
                            // message d'acceptation de l'offre
                            int prixAccepte = Integer.parseInt(msg.getContent());
                            // mise à jour du budget
                            budget -= prixAccepte;
                            // affichage du résultat de l'enchère
                            System.out.println("Acheteur " + getAID().getName() + " a acheté l'objet pour " + prixAccepte);
                            // arrêter le comportement de l'acheteur
                            myAgent.doDelete();
                            break;
                        case ACLMessage.REJECT_PROPOSAL:
                            // message de rejet de l'offre
                            System.out.println("Acheteur " + getAID().getName() + " a reçu un rejet de l'offre: " + msg.getContent());
                            break;

                        case  ACLMessage.INFORM:
                            System.out.println("Debut des encheres " );
                            break;
                        default:
                            // message non pris en charge
                            System.out.println("Message reçu: " + msg);
                    }
                } else {
                    // pas de message entrant, attendre
                    block();
                }
            }
        });
        // comportement de l'acheteur pour envoyer une offre initiale
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                if (budget > 0) {
                    // envoyer une offre initiale
                    ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
                    cfp.addReceiver(new AID("vendeur", AID.ISLOCALNAME));
                    cfp.setContent("50");
                    send(cfp);
                }
                // arrêter le comportement de l'acheteur
                myAgent.doDelete();
            }
        });
    }


    protected void takeDown() {
        System.out.println("Acheteur agent " + getAID().getName() + " terminé.");
    }

}



