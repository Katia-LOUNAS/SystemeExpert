import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Random;

public class Vendeur extends Agent  {
    private int highestBid = 0;
    private int currentBid = 0;
    private int round = 1;
    private int numBidders = 3;
    private boolean roundFinished = false;
    private String winner = "Persone";

    protected void setup() {
        System.out.println(" Agent Auctioneer " + getAID().getLocalName() + " ouvre l'enchère.");

        // Add a behaviour to handle bidder responses
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
                ACLMessage msg = myAgent.receive(mt);
                if (msg != null) {
                    int bid = Integer.parseInt(msg.getContent());
                    if (bid > highestBid) {
                        highestBid = bid;
                        winner = msg.getSender().getLocalName();
                        System.out.println("Auctioneer Agent: La neuvelle meillure offre est  " + highestBid + " proposer par " + msg.getSender().getLocalName());
                    }
                } else {
                    block();
                }
            }
        });

        // Add a behaviour to start a new round of bidding
        addBehaviour(new TickerBehaviour(this, 10000) {
            protected void onTick() {
                System.out.println("------------------------------------------------");
                System.out.println("Auctioneer Agent: Enchène avec le tour " + round);
                roundFinished = false;
                currentBid = highestBid + 1;
                Random rand = new Random();
                for (int i = 0; i < numBidders; i++) {
                    ACLMessage msg = new ACLMessage(ACLMessage.CFP);
                    msg.setContent(Integer.toString(currentBid + rand.nextInt(10)));
                    msg.addReceiver(new AID("Acheteur" + i, AID.ISLOCALNAME));
                    send(msg);
                }
                round++;
            }
        });

        // Add a behaviour to end the auction after a certain number of rounds
        addBehaviour(new TickerBehaviour(this, 50000) {
            protected void onTick() {
                System.out.println("Auctioneer Agent: Fin de l'enchere.");
                System.out.println("l'objet est remporté par l'"+winner+" avec l'offre de "+highestBid );
                myAgent.doDelete();
            }
        });
    }

    protected void takeDown() {
        System.out.println("Auctioneer Agent " + getAID().getLocalName() + " has terminated.");
    }
}