package com.lothrazar.cyclicmagic;

public class Const {
	public static final int NOTIFY = 2;

	public static final String MODID = "cyclicmagic";
	public static final String TEXTURE_LOCATION = Const.MODID + ":";

	public static final int TICKS_PER_SEC = 20;
	public static final int CHUNK_SIZE = 16;
	public static final int dye_incsac = 0;
	public static final int dye_red = 1;
	public static final int dye_cactus = 2;
	public static final int dye_cocoa = 3;
	public static final int dye_lapis = 4;
	public static final int dye_purple = 5;
	public static final int dye_cyan = 6;
	public static final int dye_lightgray = 7;
	public static final int dye_gray = 8;
	public static final int dye_pink = 9;
	public static final int dye_lime = 10;
	public static final int dye_dandelion = 11;
	public static final int dye_lightblue = 12;
	public static final int dye_magenta = 13;
	public static final int dye_orange = 14;
	public static final int dye_bonemeal = 15;

	public static final int CHEST_RARITY_COMMON = 100;
	public static final int CHEST_RARITY_REDSTONE = 50;
	public static final int CHEST_RARITY_RECORD = 5;
	public static final int CHEST_RARITY_GAPPLE = 1;

	// import net.minecraftforge.common.ChestGenHooks;
	public static final String chest_mineshaftCorridor = "mineshaftCorridor";
	public static final String chest_pyramidJungleChest = "pyramidJungleChest";
	public static final String chest_pyramidDesertyChest = "pyramidDesertyChest";
	public static final String chest_pyramidJungleDispenser = "pyramidJungleDispenser";
	public static final String chest_strongholdCorridor = "strongholdCorridor";
	public static final String chest_strongholdLibrary = "strongholdLibrary";
	public static final String chest_strongholdCrossing = "strongholdCrossing";
	public static final String chest_villageBlacksmith = "villageBlacksmith";
	public static final String chest_bonusChest = "bonusChest";
	public static final String chest_dungeonChest = "dungeonChest";
	public static final String toolClassShovel = "shovel";
	public static final String toolClassAxe = "axe";
	public static final int armor_type_helmet = 0;
	public static final int armor_type_chest = 1;
	public static final int armor_type_leg = 2;
	public static final int armor_type_boots = 3;

	public static final int potion_SPEED = 1;
	public static final int potion_SLOWNESS = 2;
	public static final int potion_HASTE = 3; // beacon
	public static final int potion_FATIGUE = 4;
	public static final int potion_STRENGTH = 5;
	public static final int potion_instanthealth = 6;
	public static final int potion_instantdamage = 7;
	public static final int potion_JUMP = 8;
	public static final int potion_NAUSEA = 9;
	public static final int potion_REGEN = 10;
	public static final int potion_RESISTANCE = 11; // beacon
	public static final int potion_FIRERESIST = 12;
	public static final int potion_WATER_BREATHING = 13; // ?? double check
															// potion
	public static final int potion_INVIS = 14;
	public static final int potion_BLINDNESS = 15;
	public static final int potion_NIGHT_VISION = 16;
	public static final int potion_HUNGER = 17;
	public static final int potion_WEAKNESS = 18;
	public static final int potion_POISON = 19;
	public static final int potion_WITHER = 20;
	public static final int potion_HEALTH_BOOST = 21; // My mod edibles
	public static final int potion_absorption = 22;// is absorption; like 21 but
													// they vanish like gold
													// apple hearts
	public static final int potion_SATURATION = 23;// fills hunger instantly,
													// kind of like peaceful


	public static final int VILLAGER_BROWN = 0;
	public static final int VILLAGER_WHITE = 1;
	public static final int VILLAGER_PURPLE = 2;
	public static final int VILLAGER_BLACK = 3;
	public static final int VILLAGER_WHITEAPRON = 4;

	public class Dimension {
		public static final int overworld = 0;
		public static final int end = 1;
		public static final int nether = -1;
	}

	public class FurnaceBurnTime {
		public static final int Sticks = 100;
		public static final int WoodenSlabs = 150;
		public static final int WoodenTools = 200;
		public static final int WoodStuff = 300;
		public static final int Coal = 1600;
		public static final int LavaBucket = 20000;
		public static final int Sapling = 100;
		public static final int BlazeRod = 2400;
	}

	public static class gamerule {
		public final static String commandBlockOutput = "commandBlockOutput";
		public static final String doDaylightCycle = "doDaylightCycle";
		public static final String doFireTick = "doFireTick";
		public static final String doMobLoot = "doMobLoot";
		public static final String doMobSpawning = "doMobSpawning";
		public static final String doTileDrops = "doTileDrops";
		public static final String keepInventory = "keepInventory";
		public static final String mobGriefing = "mobGriefing";
		public static final String naturalRegeneration = "naturalRegeneration";
		public static final String doEntityDrops = "doEntityDrops";
		public static final String reducedDebugInfo = "reducedDebugInfo";
		public static final String sendCommandFeedback = "sendCommandFeedback";
		public static final String showDeathMessages = "showDeathMessages";
	}

	public static class horse {
		public static final int variant_white = 0;
		public static final int variant_creamy = 1;
		public static final int variant_chestnut = 2;
		public static final int variant_brown = 3;
		public static final int variant_black = 4;
		public static final int variant_gray = 5;
		public static final int variant_brown_dark = 6;

		public static final int type_standard = 0;
		public static final int type_donkey = 1;
		public static final int type_mule = 2;
		public static final int type_zombie = 3;
		public static final int type_skeleton = 4;
	}
}// ends class reference