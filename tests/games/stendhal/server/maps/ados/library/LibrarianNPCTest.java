package games.stendhal.server.maps.ados.library;

import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.fsm.Engine;

import org.junit.BeforeClass;
import org.junit.Test;

import utilities.QuestHelper;
import utilities.ZonePlayerAndNPCTestImpl;
import static org.junit.Assert.*;
import static utilities.SpeakerNPCTestHelper.getReply;

/**
 * Test response to 'gem book' phrase.
 *
 * @author Aidan Abbott
 */
public class LibrarianNPCTest extends ZonePlayerAndNPCTestImpl {
    private static final String ZONE_NAME = "int_ados_library";

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        QuestHelper.setUpBeforeClass();
        setupZone(ZONE_NAME);
    }

    public LibrarianNPCTest() {
        setNpcNames("Wikipedian");
        setZoneForPlayer(ZONE_NAME);
        addZoneConfigurator(new LibrarianNPC(), ZONE_NAME);
    }

    /**
     * Tests for 'Gem book' phrase.
     */
    @Test
    public void testGemBook() {
        final SpeakerNPC npc = getNPC("Wikipedian");
        assertNotNull(npc);
        final Engine en = npc.getEngine();

        assertTrue(en.step(player, "hi"));
        assertTrue(en.step(player, "gem book"));
        String reply = getReply(npc);
        assertNotNull(reply);
        assertEquals("Why do people in Ados only want to read books about fish", reply);

    }

    /**
     * Tests for 'Explain Gem book' phrase'.
     */
    @Test
    public void testExplainGemBook() {
        final SpeakerNPC npc = getNPC("Wikipedian");
        assertNotNull(npc);
        final Engine en = npc.getEngine();
        
        assertTrue(en.step(player, "hi"));
        assertTrue(en.step(player, "explain gem book"));
        String reply = getReply(npc);
        assertNotNull(reply);
        assertNotEquals("Sorry, this book has still to be written.", reply);

    }
}