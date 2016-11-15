/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2011 - Stendhal                    *
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

import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.DropItemAction;
import games.stendhal.server.entity.npc.action.EquipItemAction;
import games.stendhal.server.entity.npc.action.IncreaseXPAction;
import games.stendhal.server.entity.npc.action.MultipleActions;
import games.stendhal.server.entity.npc.action.SetQuestAction;
import games.stendhal.server.entity.npc.action.SetQuestAndModifyKarmaAction;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.GreetingMatchesNameCondition;
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.PlayerHasItemWithHimCondition;
import games.stendhal.server.entity.npc.condition.QuestCompletedCondition;
import games.stendhal.server.entity.npc.condition.QuestInStateCondition;
import games.stendhal.server.entity.npc.condition.QuestNotStartedCondition;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.Region;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * QUEST: Look for Lilly's logbook
 * 
 * PARTICIPANTS:
 * <ul>
 * <li> Lilly </li>
 * </ul>
 * 
 * STEPS:
 * <ul>
 * <li> Talk with Lilly to activate the quest. </li>
 * <li> Return the logbook to Lilly. </li>
 * </ul>
 * 
 * REWARD:
 * <ul>
 * <li> 100 XP </li>
 * <li> 50 gold coins </li>
 * </ul>
 * 
 * REPETITIONS: None
 */
public class JavaLabbook extends AbstractQuest {
	private static final String QUEST_SLOT = "lab_book3";



	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}
	
	private void step1LearnAboutQuest() {

		final SpeakerNPC npc = npcs.get("Lilly");

		npc.add(ConversationStates.ATTENDING,
			ConversationPhrases.QUEST_MESSAGES,
			new QuestNotStartedCondition(QUEST_SLOT),
			ConversationStates.ATTENDING, 
			"I am looking for a very special #logbook.", null);

		npc.add(ConversationStates.ATTENDING,
			ConversationPhrases.QUEST_MESSAGES,
			new QuestCompletedCondition(QUEST_SLOT),
			ConversationStates.ATTENDING, 
			"I have nothing for you now.", null);


		/** If quest is not started yet, start it. */
		npc.add(
			ConversationStates.ATTENDING,
			"logbook", new QuestNotStartedCondition(QUEST_SLOT),
			ConversationStates.QUEST_OFFERED,
			"I lost my Java lab notebook. Can you help me find it before the next lab so that I can get my work marked?",
			null);

		npc.add(
			ConversationStates.QUEST_OFFERED,
			ConversationPhrases.YES_MESSAGES,
			null,
			ConversationStates.ATTENDING,
			"Thank you! Someone might have borrowed my book or it might be in the library.",
			new SetQuestAction(QUEST_SLOT, "start"));

		npc.add(
			ConversationStates.QUEST_OFFERED,
			ConversationPhrases.NO_MESSAGES,
			null,
			ConversationStates.ATTENDING,
			"Oh, please won't you change your mind and help me out?",
			new SetQuestAndModifyKarmaAction(QUEST_SLOT, "rejected", -5.0));


		/** Remind player about the quest */
		npc.add(ConversationStates.ATTENDING, "logbook",
			new QuestInStateCondition(QUEST_SLOT, "start"),
			ConversationStates.ATTENDING,
			"I really need that logbook now! Go get it!", null);

	}


	private void step3returnBook() {
		final SpeakerNPC npc = npcs.get("Lilly");

		/** Complete the quest */
		final List<ChatAction> reward = new LinkedList<ChatAction>();
		reward.add(new DropItemAction("lab_book"));
		reward.add(new EquipItemAction("money", 50));
		reward.add(new IncreaseXPAction(100));
		reward.add(new SetQuestAction(QUEST_SLOT, "done"));

		npc.add(
			ConversationStates.IDLE,
			ConversationPhrases.GREETING_MESSAGES,
			new AndCondition(new GreetingMatchesNameCondition(npc.getName()),
					new PlayerHasItemWithHimCondition("lab_book")),
			ConversationStates.ATTENDING,
			"Thank you, I am now ready for my next lab",
			new MultipleActions(reward));


		npc.add(
			ConversationStates.IDLE,
			ConversationPhrases.GREETING_MESSAGES,
			new AndCondition(new GreetingMatchesNameCondition(npc.getName()),
					new QuestInStateCondition(QUEST_SLOT, "start"),
					new NotCondition(new PlayerHasItemWithHimCondition("lab_book"))),
			ConversationStates.ATTENDING, 
			"Haven't you got that #logbook",
			new SetQuestAction(QUEST_SLOT, "start"));
	}

	
	@Override
	public List<String> getHistory(final Player player) {
		final List<String> res = new ArrayList<String>();
		if (!player.hasQuest(QUEST_SLOT)) {
			return res;
		}
		res.add("I have met Lilly in Orril Moutains");
		final String questState = player.getQuest(QUEST_SLOT);
		if (questState.equals("rejected")) {
			res.add("I do not want to find the logbook.");
		}
		if (player.isQuestInState(QUEST_SLOT, "start", "done")) {
			res.add("I promised to find the logbook");
		}
		if (questState.equals("done")) {
			res.add("I have returned the logbook and I got the reward");
		}
		return res;
	}
	
	@Override
	public void addToWorld() {
		fillQuestInfo(
				"Look for a logbook",
				"Lilly needs her logbook",
				false);
		step1LearnAboutQuest();
		step3returnBook();
	}

	@Override
	public String getName() {
		return "JavaLabbook";
	}
	
	@Override
	public String getRegion() {
		return Region.ORRIL;
	}

	@Override
	public String getNPCName() {
		return "Lilly";
	}
}
