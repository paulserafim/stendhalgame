package games.stendhal.server.entity.item;

import static org.junit.Assert.*;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.entity.status.StatusType;
import games.stendhal.server.maps.MockStendlRPWorld;
import marauroa.common.Log4J;
import org.junit.BeforeClass;
import org.junit.Test;

public class MedcinalRingTest {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		MockStendlRPWorld.get();
		Log4J.init();
	}

	@Test
	public void test(){

	final StatusResistantItem ring = (StatusResistantItem) SingletonRepository.getEntityManager().getItem("medicinal ring");
	double resistanceValue = ring.getStatusResistanceValue(StatusType.POISONED);
	System.out.println(resistanceValue);
	assertEquals(0.25,resistanceValue,0);
	
	}
}