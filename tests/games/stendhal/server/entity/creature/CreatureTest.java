/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2010 - Stendhal                    *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.server.entity.creature;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.rule.EntityManager;
import games.stendhal.server.entity.RPEntity;
import games.stendhal.server.entity.creature.impl.DropItem;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.MockStendlRPWorld;

import java.util.LinkedList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import utilities.PlayerTestHelper;
import utilities.RPClass.CreatureTestHelper;

public class CreatureTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		MockStendlRPWorld.get();
		CreatureTestHelper.generateRPClasses();
	}

	/**
	 * Tests for getNearestEnemy.
	 */
	@Test
	public void testGetNearestEnemy() {
		
		final Player onebyone = PlayerTestHelper.createPlayer("bob");
		onebyone.setPosition(6, 0);
		final MockCreature sevenbyseven = new MockCreature();
	
		final StendhalRPZone zone = new StendhalRPZone("test", 20 , 20);
		zone.add(sevenbyseven);
		zone.add(onebyone);
		enemies.add(onebyone);
		assertSame(onebyone, sevenbyseven.getNearestEnemy(6));
		assertSame(onebyone, sevenbyseven.getNearestEnemy(5));
		assertNull(sevenbyseven.getNearestEnemy(4));
		
		sevenbyseven.setSize(7, 7);
		onebyone.setPosition(10, 10);
		assertSame(onebyone, sevenbyseven.getNearestEnemy(7));
		assertSame(onebyone, sevenbyseven.getNearestEnemy(6));
		assertSame(onebyone, sevenbyseven.getNearestEnemy(5));
		assertNull(sevenbyseven.getNearestEnemy(4));
	}

	
	private static List<RPEntity> enemies  = new LinkedList<RPEntity>();
	private static class MockCreature extends Creature {

		@Override
		public List<RPEntity> getEnemyList() {
			
			return enemies; 
		}
	}

	/**
	 * Tests for hasTargetMoved.
	 */
	@Test
	public void testhasTargetMoved() {
		final StendhalRPZone zone = new StendhalRPZone("testzone");
		final Creature attacker = new Creature();

		final Creature attackTarget = new Creature();
		zone.add(attacker);
		zone.add(attackTarget);
		attacker.setTarget(attackTarget);
		assertFalse(attacker.hasTargetMoved());
		assertFalse(attacker.hasTargetMoved());
		attackTarget.setPosition(1, 0);
		assertTrue(attacker.hasTargetMoved());
		assertFalse(attacker.hasTargetMoved());
	}

	/**
	 * Tests for isAttackTurn.
	 */
	@Test
	public void testIsAttackTurn() {
		final Creature creature = new Creature();
		int counter = 0;
		for (int i = 0; i < 10; i++) {
			if (creature.isAttackTurn(i)) {
				counter++;
			}
		}
		assertThat(counter, is(2));
	}
	
	// Function to check if an item with the given name is in the given list.
	private static boolean findItemInList(List<DropItem> givenList, String givenItemName){
		for (DropItem item : givenList){
			if (item.name.equals(givenItemName))
				return true;
		}
		return false;
	}
	
	/**
	 * Test that the lower level sheepmen drop regular poison.
	 */
	@Test
	public void testSheepmenPoisonDrop() {
		final EntityManager manager = SingletonRepository.getEntityManager();

		final Creature armoredSheepman = manager.getCreature("armored sheepman");
		final Creature sheepman = manager.getCreature("sheepman");
		
		assertTrue("Check if armored sheepman creature has poison as a drop item", findItemInList(armoredSheepman.dropsItems,"poison"));
		assertTrue("Check if sheepman creature has poison as a drop item", findItemInList(sheepman.dropsItems, "poison"));
	}
	
}
