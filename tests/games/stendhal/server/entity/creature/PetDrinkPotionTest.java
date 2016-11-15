package games.stendhal.server.entity.creature;

import static org.junit.Assert.*;
import games.stendhal.common.EquipActionConsts;
import games.stendhal.server.maps.MockStendlRPWorld;
import marauroa.common.Log4J;
import marauroa.common.game.RPAction;

import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.server.actions.equip.DropAction;
import games.stendhal.server.actions.equip.EquipmentAction;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.item.StackableItem;
import games.stendhal.server.entity.player.Player;
import utilities.PlayerTestHelper;
import utilities.RPClass.PetTestHelper;

public class PetDrinkPotionTest {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    MockStendlRPWorld.get();
    Log4J.init();
  }

  /**
   * Tests for pets drinking a potion at different hp'S
   * All the tests should fail as they are all above half the pets health
   */
  @Test
  public final void testHealAt99(){
	//Create a zone and add it to the world
    StendhalRPZone localzone = new StendhalRPZone("testzone", 2, 2);
    MockStendlRPWorld.get().addRPZone(localzone);
    
    //Create a player and set up the necessary pet RPClasses
    final Player player = PlayerTestHelper.createPlayer("bob");
    PetTestHelper.generateRPClasses();
    
    //Get a potion and add equip it to the player. Add the player to the zone
    StackableItem item = (StackableItem)SingletonRepository.getEntityManager().getItem("potion");
    item.setQuantity(1);
    localzone.add(player);
    player.equip("bag", item);
    
    //Check to see that the player has the potion
    assertTrue(player.isEquipped("potion", 1));
    
    //Set up the action to drop the potion
    RPAction drop = new RPAction();
    drop.put("type", "drop");
    drop.put("baseobject", player.getID().getObjectID());
    drop.put("baseslot", "bag");
    drop.put("x", player.getX());
    drop.put("y", player.getY() + 1);
    drop.put("quantity", "1");
    drop.put("baseitem", item.getID().getObjectID());

    //Check that there is no item on the zone and that the potion can be droped
    final EquipmentAction action = new DropAction();
    assertEquals(0, localzone.getItemsOnGround().size());
    assertTrue(drop.has(EquipActionConsts.BASE_ITEM));
    
    //Create and add the pet
    final Pet pet = new BabyDragon(player);
    localzone.add(pet);
    
    //Cause the player to drop the potion
    action.onAction(player, drop);
   
    //Check that the potion is dropped
    assertEquals(1, localzone.getItemsOnGround().size());
   
    //Set up and activate the pet logic
    pet.setBaseHP(100);
    pet.setHP(99);
    
    pet.logic();
    
    //Check the potion is still on the ground in the zone
    assertEquals(1, localzone.getItemsOnGround().size());
  }

  @Test
  public final void testHealAt80(){
    StendhalRPZone localzone = new StendhalRPZone("testzone", 2, 2);
    MockStendlRPWorld.get().addRPZone(localzone);
    final Player player = PlayerTestHelper.createPlayer("bob");
    PetTestHelper.generateRPClasses();
    StackableItem item = (StackableItem)SingletonRepository.getEntityManager().getItem("potion");
    item.setQuantity(1);
    localzone.add(player);
    player.equip("bag", item);
    
    
    assertTrue(player.isEquipped("potion"));
    
    RPAction drop = new RPAction();
    drop.put("type", "drop");
    drop.put("baseobject", player.getID().getObjectID());
    drop.put("baseslot", "bag");
    drop.put("x", player.getX());
    drop.put("y", player.getY() + 1);
    drop.put("quantity", "1");
    drop.put("baseitem", item.getID().getObjectID());

    final EquipmentAction action = new DropAction();
    assertEquals(0, localzone.getItemsOnGround().size());
    assertTrue(drop.has(EquipActionConsts.BASE_ITEM));
    
    final Pet pet = new BabyDragon(player);
    
    localzone.add(pet);
    
    action.onAction(player, drop);
   
    assertEquals(1, localzone.getItemsOnGround().size());
   
    pet.setBaseHP(100);
    pet.setHP(80);
    
    pet.logic();
    
    assertEquals(1, localzone.getItemsOnGround().size());
  }
  
  @Test
  public final void testHealAt60(){
    StendhalRPZone localzone = new StendhalRPZone("testzone", 2, 2);
    MockStendlRPWorld.get().addRPZone(localzone);
    final Player player = PlayerTestHelper.createPlayer("bob");
    PetTestHelper.generateRPClasses();
    StackableItem item = (StackableItem)SingletonRepository.getEntityManager().getItem("potion");
    item.setQuantity(1);
    localzone.add(player);
    player.equip("bag", item);
    
    
    assertTrue(player.isEquipped("potion"));
    
    RPAction drop = new RPAction();
    drop.put("type", "drop");
    drop.put("baseobject", player.getID().getObjectID());
    drop.put("baseslot", "bag");
    drop.put("x", player.getX());
    drop.put("y", player.getY() + 1);
    drop.put("quantity", "1");
    drop.put("baseitem", item.getID().getObjectID());

    final EquipmentAction action = new DropAction();
    assertEquals(0, localzone.getItemsOnGround().size());
    assertTrue(drop.has(EquipActionConsts.BASE_ITEM));
    
    final Pet pet = new BabyDragon(player);
    
    localzone.add(pet);
    
    action.onAction(player, drop);
   
    assertEquals(1, localzone.getItemsOnGround().size());
   
    pet.setBaseHP(100);
    pet.setHP(60);
    
    pet.logic();
    
    assertEquals(1, localzone.getItemsOnGround().size());
  }
  
  @Test
  public final void testHealAt50(){
    StendhalRPZone localzone = new StendhalRPZone("testzone", 2, 2);
    MockStendlRPWorld.get().addRPZone(localzone);
    final Player player = PlayerTestHelper.createPlayer("bob");
    PetTestHelper.generateRPClasses();
    StackableItem item = (StackableItem)SingletonRepository.getEntityManager().getItem("potion");
    item.setQuantity(1);
    localzone.add(player);
    player.equip("bag", item);
    
    
    assertTrue(player.isEquipped("potion"));
    
    RPAction drop = new RPAction();
    drop.put("type", "drop");
    drop.put("baseobject", player.getID().getObjectID());
    drop.put("baseslot", "bag");
    drop.put("x", player.getX());
    drop.put("y", player.getY() + 1);
    drop.put("quantity", "1");
    drop.put("baseitem", item.getID().getObjectID());

    final EquipmentAction action = new DropAction();
    assertEquals(0, localzone.getItemsOnGround().size());
    assertTrue(drop.has(EquipActionConsts.BASE_ITEM));
    
    final Pet pet = new BabyDragon(player);
    
    localzone.add(pet);
    
    action.onAction(player, drop);
   
    assertEquals(1, localzone.getItemsOnGround().size());
   
    pet.setBaseHP(100);
    pet.setHP(50);
    
    pet.logic();
    
    assertEquals(1, localzone.getItemsOnGround().size());
  }
}