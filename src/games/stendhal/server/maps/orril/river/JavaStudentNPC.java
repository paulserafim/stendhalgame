package games.stendhal.server.maps.orril.river;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
// this one is just because our NPC is a seller
import games.stendhal.server.entity.npc.ShopList;
import games.stendhal.server.entity.npc.SpeakerNPC;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class JavaStudentNPC implements ZoneConfigurator {
        // this is just because he has a 'shop' to sell potions
	private final ShopList shops = SingletonRepository.getShopList();

	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildNPC(zone);
	}
    private void buildNPC(final StendhalRPZone zone) {
	    final SpeakerNPC npc = new SpeakerNPC("Lilly") {
        protected void createPath() {
            List<Node> nodes=new LinkedList<Node>();
            nodes.add(new Node(9,5));
            nodes.add(new Node(14,5));
            setPath(new FixedPath(nodes,true));
        }

        protected void createDialog() {
            // Lets the NPC reply with "Hallo" when a player greets him. But we could have set a custom greeting inside the ()
            addGreeting("Hello! My name is Lily.");
            addJob(" I am a student.");
            // Lets the NPC reply when a player asks for help
            addHelp("Thank you! Someone might have borrowed my book or it might be in the library.");
            // use standard goodbye, but you can also set one inside the ()
            addGoodbye("Bye.");
        }
    };

	    // This determines how the NPC will look like. welcomernpc.png is a picture in data/sprites/npc/
	    npc.setEntityClass("welcomernpc");
	    // set a description for when a player does 'Look'
	    npc.setDescription("You see Mr Healer, he looks a a bit busy at the moment but perhaps he can help you anyway.");
	    // Set the initial position to be the first node on the Path you defined above.
	    npc.setPosition(9, 5);
	    npc.initHP(100);
	
	    zone.add(npc);   
    }
}