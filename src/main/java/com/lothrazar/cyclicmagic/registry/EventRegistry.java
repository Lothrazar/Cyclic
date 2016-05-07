package com.lothrazar.cyclicmagic.registry;

import java.util.ArrayList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import com.lothrazar.cyclicmagic.event.*;
import com.lothrazar.cyclicmagic.util.Const;

public class EventRegistry {
	
	private ArrayList<IFeatureEvent> featureEvents = new ArrayList<IFeatureEvent>();
	
	public EventRegistry(){
		featureEvents.add(new EventSaplingBlockGrowth());
		featureEvents.add(new EventConfigChanged());
		featureEvents.add(new EventPotions());
		featureEvents.add(new EventSpells());
		featureEvents.add(new EventKeyInput());
		featureEvents.add(new EventHorseFood());
		featureEvents.add(new EventBucketBlocksBreak());
		featureEvents.add(new EventAppleUse());
		featureEvents.add(new EventGuiTerrariaButtons());
		featureEvents.add(new EventExtendedInventory());
		featureEvents.add(new EventOreMined());
		featureEvents.add(new EntitySafeMount());
		featureEvents.add(new EventSpawnChunks());
		featureEvents.add(new EventPassthroughAction());
	}
 
	private static boolean nameTagDeath;

	private static boolean playerWakeup;

	private static boolean signSkullName;

	private static boolean playerDeathCoords;

	private static boolean farmDropBuffs;

	private static boolean monsterDropsNerfed;

	private static boolean endermanDrop;

	private static boolean nameVillagerTag;

	private static boolean editableSigns;

	private static boolean mountedPearl;

	private static boolean stardewFurnace;

	private static boolean fragileTorches;

	private static boolean foodDetails;

	private static boolean fastLadderClimb;

	public static boolean cancelPotionInventoryShift;

	public void syncConfig(Configuration config) {

		for (IFeatureEvent e : featureEvents) {
			e.syncConfig(config);
		}
		
		String category = Const.MODCONF + "Mobs";
		

		config.setCategoryComment(category, "Changes to mobs");

		farmDropBuffs = config.getBoolean("Farm Drops Buffed", category, true,
				"Increase drops of farm animals: more leather, more wool from shearing, pigs drop a bit more pork");
		monsterDropsNerfed = config.getBoolean("Monster Drops Nerfed", category, true,
				"Zombies no longer drops crops or iron");
		nameTagDeath = config.getBoolean("Name Tag Death", category, true,
				"When an entity dies that is named with a tag, it drops the nametag");
		endermanDrop = config.getBoolean("Enderman Block", category, true,
				"Enderman will always drop block they are carrying 100%");

		category = Const.MODCONF + "Misc";

		signSkullName = config.getBoolean("Name Player Skulls with Sign", category, true,
				"Use a player skull on a sign to name the skull based on the top line");
		fragileTorches = config.getBoolean("Fragile Torches", category, true,
				"Torches can get knocked over when passed through by living entities");
		foodDetails = config.getBoolean("Food Details", category, true, "Add food value and saturation to its info");

		category = Const.MODCONF + "Player";
		config.setCategoryComment(category, "Changes to player properties or actions");

		nameVillagerTag = config.getBoolean("Villager Nametag", category, true,
				"Let players name villagers with nametags");
		editableSigns = config.getBoolean("Editable Signs", category, true, "Allow editing signs with an empty hand");
		playerWakeup = config.getBoolean("Wakeup Curse", category, true,
				"Using a bed to skip the night has some mild potion effect related drawbacks");
		playerDeathCoords = config.getBoolean("Death Coords", category, true,
				"Display your coordinates in chat when you die");
		mountedPearl = config.getBoolean("Pearls On Horseback", category, true,
				"Enderpearls work on a horse, bringing it with you");
		stardewFurnace = config.getBoolean("Furnace Speed", category, true,
				"Quickly fill a furnace by hitting it with fuel or an item, or interact with an empty hand to pull out the results [Inspired by Stardew Valley]");

		fastLadderClimb = config.getBoolean("Faster Ladders", category, true,
				"Allows you to quickly climb ladders by looking up instead of moving forward");

		cancelPotionInventoryShift = config.getBoolean("Potion Inventory Shift", category, true,
				"When true, this blocks the potions moving the inventory over");

		EventExtendedInventory.dropOnDeath = config.getBoolean("DropExtendedInventoryOnDeath", category, true,
				"When false, this never drops your extra inventories items on death (for the extended inventory).  If true, this will obey the keepInventory rule");
		
		// TODO: 'enabled', which hides the button for invo
		//  TODO: and  one for 'enabled 3x3 crafting' as well
		
		//EventEntityItemExpire.syncConfig(config);
	}

	public void register() {

		// some just always have to happen no matter what. for other features.
		// they will do nothing if for example their items do not exist or
		// otherwise disabled
		
		if(EventEntityItemExpire.enabled){
			featureEvents.add(new EventEntityItemExpire());
		}
					
		
		featureEvents.add(new EventEnderChest());
		
		if (fastLadderClimb) {
			featureEvents.add(new EventLadderClimb());
		}
		if (foodDetails) {
			featureEvents.add(new EventFoodDetails());
		}
		if (fragileTorches) {
			featureEvents.add(new EventFragileTorches());
		}
		if (stardewFurnace) {
			featureEvents.add(new EventFurnaceStardew());
		}
		if (mountedPearl) {
			featureEvents.add(new EventMountedPearl());
		}
		if (editableSigns) {
			featureEvents.add(new EventEditSign());
		}
		if (nameVillagerTag) {
			featureEvents.add(new EventNameVillager());
		}
		if (endermanDrop) {
			featureEvents.add(new EventEndermanDropBlock());
		}
		if (farmDropBuffs) {
			featureEvents.add(new EventAnimalDropBuffs());
		}
		if (monsterDropsNerfed) {
			featureEvents.add(new EventMobDropsReduced());
		}
		if (nameTagDeath) {
			featureEvents.add(new EventNametagDeath());
		}
		if (playerDeathCoords) {
			featureEvents.add(new EventPlayerDeathCoords());
		}
		if (playerWakeup) {
			featureEvents.add(new EventPlayerWakeup());
		}
		if (signSkullName) {
			featureEvents.add(new EventSignSkullName());
		}
		for (IFeatureEvent e : featureEvents) {
			MinecraftForge.EVENT_BUS.register(e);
		}
	}

	/*
	 * @SubscribeEvent public void onEntityJoinWorldEvent(EntityJoinWorldEvent
	 * event) {
	 * 
	 * if(event.entity instanceof EntityLivingBase && event.world.isRemote) {
	 * EntityLivingBase living = (EntityLivingBase)event.entity;
	 * 
	 * if(living instanceof EntityWolf && ((EntityWolf)living).isTamed()) {
	 * setMaxHealth(living,heartsWolfTamed*2); } if(living instanceof
	 * EntityOcelot && ((EntityOcelot)living).isTamed()) {
	 * setMaxHealth(living,heartsCatTamed*2); }
	 * 
	 * if(living instanceof EntityVillager && ((EntityVillager)living).isChild()
	 * == false) { setMaxHealth(living,heartsVillager*2); } } }
	 */

}
