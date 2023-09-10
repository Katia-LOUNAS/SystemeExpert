import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Random;

public class Acheteur extends Agent {

    private int balance = 100;
    private int bidIncrement = 5;

    protected void setup() {
        System.out.println("Un Agent Bidder " + getAID().getName() + " vient d'arrivé.");

        // Add a behaviour to handle auctioneer requests
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
                ACLMessage msg = myAgent.receive(mt);
                if (msg != null) {
                    int currentBid = Integer.parseInt(msg.getContent());
                    int bid = currentBid + bidIncrement;
                    if (balance >= bid) {
                        ACLMessage reply = msg.createReply();
                        reply.setPerformative(ACLMessage.PROPOSE);
                        reply.setContent(Integer.toString(bid));
                        send(reply);
                        System.out.println("Bidder Agent: propose une offre de  " + bid + " pour l'Agent Auctioneer ");
                    } else {
                        System.out.println("Bidder Agent: Ne peut plus proposer d'offre seul atteint");
                    }
                } else {
                    block();
                }
            }
        });
    }
}






