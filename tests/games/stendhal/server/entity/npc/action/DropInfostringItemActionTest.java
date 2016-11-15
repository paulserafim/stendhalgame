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
package games.stendhal.server.entity.npc.action;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.MockStendlRPWorld;
import marauroa.common.Log4J;
import marauroa.server.game.db.DatabaseFactory;

import org.junit.BeforeClass;
import org.junit.Test;

import utilities.PlayerTestHelper;

public class DropInfostringItemActionTest {

	@BeforeClass
	public static void beforeClass() {
		Log4J.init();
		MockStendlRPWorld.get();
		new DatabaseFactory().initializeDatabase();
	}

	@Test
	public void testFire() {
		Player player = PlayerTestHelper.createPlayer("alex");
		PlayerTestHelper.equipWithItem(player, "water");
		PlayerTestHelper.equipWithItem(player, "water", "clean");
		List<Item> items = player.getAllEquipped("water");
		boolean playerHasCleanWater = false;
		for (Item item : items) {
			if ("clean".equalsIgnoreCase(item.getInfoString())) {
				playerHasCleanWater = true;
				break;
			}
			else
				playerHasCleanWater = false;
		}
		assertTrue(playerHasCleanWater);
		DropInfostringItemAction action = new DropInfostringItemAction("water", "clean");
		action.fire(player, null, null);
		List<Item> afterDropItems = player.getAllEquipped("water");
		for (Item afterDropItem : afterDropItems) {
			if ("clean".equalsIgnoreCase(afterDropItem.getInfoString())) {
				playerHasCleanWater = true;
				break;
			}
			else
				playerHasCleanWater = false;
		}
		assertFalse(playerHasCleanWater);
	}
}
