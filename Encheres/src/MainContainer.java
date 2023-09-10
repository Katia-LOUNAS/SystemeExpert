import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;


public class MainContainer {
    public static void main(String[] args) {
        Runtime rt = Runtime.instance();
        Profile p = new ProfileImpl();
        p.setParameter(Profile.MAIN_HOST, "localhost");
        p.setParameter(Profile.GUI, "true");
        ContainerController cc = rt.createMainContainer(p);

        try {
            AgentController auctioneer = cc.createNewAgent("Vendeur", Vendeur.class.getName(), new Object[]{});
            AgentController bidder1 = cc.createNewAgent("Acheteur1", Acheteur.class.getName(), new Object[]{});
            AgentController bidder2 = cc.createNewAgent("Acheteur2", Acheteur.class.getName(), new Object[]{});
            auctioneer.start();
            bidder1.start();
            bidder2.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
