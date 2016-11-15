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
package games.stendhal.server.maps.quests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static utilities.SpeakerNPCTestHelper.getReply;
import utilities.PlayerTestHelper;

import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
//import games.stendhal.server.entity.item.Item;
//import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.fsm.Engine;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.MockStendhalRPRuleProcessor;
import games.stendhal.server.maps.MockStendlRPWorld;
import games.stendhal.server.maps.orril.river.JavaStudentNPC;
import games.stendhal.server.maps.semos.tavern.StichardRallmanNPC;

import java.util.Arrays;

import marauroa.common.Log4J;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class JavaLabbookTest {
	private static final String LAB_BOOK = "lab_book3";

	@BeforeClass
	public static void setupClass() {
		MockStendhalRPRuleProcessor.get();
		MockStendlRPWorld.get();
		Log4J.init();
	}


	private SpeakerNPC lilly;
	private SpeakerNPC stichard;

	@Before
	public void setUp() throws Exception {
		PlayerTestHelper.generateNPCRPClasses();
		new JavaStudentNPC().configureZone(new StendhalRPZone("testzone"), null);
		new StichardRallmanNPC().configureZone(new StendhalRPZone("testzone"), null);
		lilly = SingletonRepository.getNPCList().get("Lilly");
		stichard = SingletonRepository.getNPCList().get("Stichard Rallman");
	}

	@After
	public void tearDown() throws Exception {
		SingletonRepository.getNPCList().remove("Lilly");
		SingletonRepository.getNPCList().remove("Stichard Rallman");
	}


	@Test
	public void doQuest() throws Exception {
		
		//create the JavaLabook quest
		final JavaLabbook quest = new JavaLabbook();	
		quest.addToWorld();
		
		//create the player
		final Player pl = PlayerTestHelper.createPlayer("player");
		assertFalse(quest.isStarted(pl));
		assertFalse(quest.isCompleted(pl));
		
		//create the engine
		final Engine lillyEngine = lilly.getEngine();
		
		//test when quest is not given
		lillyEngine.step(pl, "Hi");
		assertTrue(lilly.isTalking());
		assertEquals("Hello! My name is Lily.", getReply(lilly));
		lillyEngine.step(pl, ConversationPhrases.QUEST_MESSAGES.get(0));
		assertTrue(lilly.isTalking());
		assertEquals("I am looking for a very special #logbook.",
				getReply(lilly));
		lillyEngine.step(pl, "logbook");
		assertTrue(lilly.isTalking());
		assertEquals(
				"I lost my Java lab notebook. Can you help me find it before the next lab so that I can get my work marked?",
				getReply(lilly));
		
		//quest is accepted
		lillyEngine.step(pl, ConversationPhrases.YES_MESSAGES.get(0));
		assertTrue(lilly.isTalking());
		assertEquals(
				"Thank you! Someone might have borrowed my book or it might be in the library.",
				getReply(lilly));
		assertEquals("start", pl.getQuest(JavaLabbookTest.LAB_BOOK));
		lillyEngine.step(pl, "logbook");
		assertTrue(lilly.isTalking());
		
		//quest accepted but the player has no logbook 
		assertEquals("I really need that logbook now! Go get it!",
				getReply(lilly));
		lillyEngine.step(pl, ConversationPhrases.GOODBYE_MESSAGES.get(0));
		assertFalse(lilly.isTalking());
		lillyEngine.step(pl, "Hi");
		assertTrue(lilly.isTalking());
		assertEquals("Haven't you got that #logbook",
				getReply(lilly));
		lillyEngine.step(pl, "bye");
		
		//come back with the logbook
		/*
		pl.setQuest(LAB_BOOK, "start");
		PlayerTestHelper.equipWithItem(pl, "lab_book");
		lillyEngine.step(pl, "Hi");
		assertTrue(lilly.isTalking());
		assertEquals("Thank you, I am now ready for my next lab", getReply(lilly));
		lillyEngine.step(pl, "bye");
*/
		
		//ask Stichard Rallman to search the logbook
		final Engine stichardEngine = stichard.getEngine();
		stichardEngine.step(pl, "Hi");
		assertTrue(stichard.isTalking());
		assertEquals(
				"Hi! Have you lost something? I can #search_for you!",
				getReply(stichard));
		stichardEngine.step(pl, "search_for");
		assertTrue(stichard.isTalking());
		stichardEngine.step(pl, "yes");
/*		
		assertTrue(pl.isEquipped("lab_book"));
		jynathEngine.step(pl, "bye");
		assertFalse(jynath.isTalking());
*/		
		
		//quest already done
		lillyEngine.step(pl, "Hi");
		assertTrue(lilly.isTalking());
		assertEquals("Hello! My name is Lily.", getReply(lilly));
		lillyEngine.step(pl, "quest");
		assertTrue(lilly.isTalking());
		assertEquals("I have nothing for you now.", getReply(lilly));
		

	}

	/**
	 * Tests for addToWorld.
	 */
	@Test
	public final void testAddToWorld() {
		JavaLabbook quest;
		quest = new JavaLabbook();
		quest.addToWorld();
	}

	/**
	 * Tests for getHistory.
	 */
	@Test
	public final void testGetHistory() {
		final Player pl = PlayerTestHelper.createPlayer("player");
		JavaLabbook quest;
		quest = new JavaLabbook();
		quest.addToWorld();
	
		assertTrue(quest.getHistory(pl).isEmpty());

		pl.setQuest(LAB_BOOK, "rejected");
		assertEquals(2, quest.getHistory(pl).size());
		assertEquals(Arrays.asList(
				"I have met Lilly in Orril Moutains", 
				"I do not want to find the logbook."),
				quest.getHistory(pl));

		pl.setQuest(LAB_BOOK, "start");
		assertEquals(2, quest.getHistory(pl).size());
		assertEquals(Arrays.asList(
				"I have met Lilly in Orril Moutains", 
				"I promised to find the logbook"),
				quest.getHistory(pl));


		pl.setQuest(LAB_BOOK, "done");
		assertEquals(3, quest.getHistory(pl).size());
		assertEquals(Arrays.asList(
				"I have met Lilly in Orril Moutains", 
				"I promised to find the logbook",
				"I have returned the logbook and I got the reward"), 
				quest.getHistory(pl));
	}

	/**
	 * Tests for isCompleted.
	 */
	@Test
	public final void testIsCompleted() {
		JavaLabbook quest;
		quest = new JavaLabbook();

		quest.addToWorld();
		final Player pl = PlayerTestHelper.createPlayer("player");
		assertFalse(quest.isCompleted(pl));
		pl.setQuest(JavaLabbookTest.LAB_BOOK, "done");
		assertTrue(pl.hasQuest(JavaLabbookTest.LAB_BOOK));
		assertTrue(pl.isQuestCompleted(JavaLabbookTest.LAB_BOOK));
		assertTrue(quest.isCompleted(pl));
	}

	/**
	 * Tests for isRepeatable.
	 */
	@Test
	public final void testIsRepeatable() {
		final JavaLabbook quest = new JavaLabbook();
		assertFalse(quest.isRepeatable(null));
	}

	/**
	 * Tests for isStarted.
	 */
	@Test
	public final void testIsStarted() {
		final JavaLabbook quest = new JavaLabbook();
	
		final Player bob = PlayerTestHelper.createPlayer("bob");
		assertFalse(bob.hasQuest(JavaLabbookTest.LAB_BOOK));
		assertFalse(quest.isStarted(bob));
		bob.setQuest(JavaLabbookTest.LAB_BOOK, "done");
		assertTrue(bob.hasQuest(JavaLabbookTest.LAB_BOOK));
		assertTrue(quest.isStarted(bob));
	}



}
