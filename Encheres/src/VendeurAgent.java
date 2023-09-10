import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class VendeurAgent extends Agent {
    private int prixReserve;
    private int prixActuel;
    private boolean vendu;


    protected void setup() {
        prixReserve = 100;
        prixActuel = 50;
        vendu = false;

        // comportement du vendeur pour traiter les messages entrants
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                // attendre un message entrant
                ACLMessage msg = receive();
                if (msg != null) {
                    // traitement du message
                    switch (msg.getPerformative()) {
                        case ACLMessage.CFP:
                            // message d'offre d'un acheteur
                            int prixOffert = Integer.parseInt(msg.getContent());
                            if (prixOffert > prixActuel) {
                                prixActuel = prixOffert;
                            }
                            // réponse au message
                            ACLMessage reply = msg.createReply();
                            if (prixOffert >= prixReserve) {
                                // l'objet est vendu
                                reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                                reply.setContent(String.valueOf(prixOffert));
                                vendu = true;
                            } else {
                                // l'offre est inférieure au prix de réserve
                                reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                                reply.setContent("Prix de réserve non atteint");
                            }
                            send(reply);
                            break;
                        case ACLMessage.INFORM:
                            // message d'initialisation de la vente
                            prixActuel = Integer.parseInt(msg.getContent());
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
                // Vérifier si le temps imparti pour l'enchère est écoulé
                if (System.currentTimeMillis() > 600000000) {
                    // Enchère terminée
                    if (prixActuel > prixReserve) {
                        System.out.println("Vendu au plus offrant : " + prixActuel);
                    } else {
                        System.out.println("Objet non vendu, prix de réserve non atteint.");
                    }

                }
            }
        });

    }


    protected void takeDown() {
        System.out.println("Vendeur agent " + getAID().getName() + " terminé.");
    }

    public int getPrixReserve() {
        return prixReserve;
    }

    public int getPrixActuel() {
        return prixActuel;
    }

    public boolean isVendu() {
        return vendu;
    }

}
