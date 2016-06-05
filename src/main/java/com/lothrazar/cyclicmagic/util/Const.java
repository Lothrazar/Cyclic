package com.lothrazar.cyclicmagic.util;

public class Const {
	public static final String MODID = "cyclicmagic";
	public static final String MODRES = Const.MODID + ":";
	private static final String MODCONF = Const.MODID + ".";
	
	public class ConfigCategory {
		//to store categories. basically an enum/lookup table
		public static final String player = MODCONF + "player";
		public static final String worldGen = MODCONF + "world generation";
		public static final String mobs = MODCONF + "mobs";
		public static final String mobspawns = MODCONF + "Mob Spawns";
		public static final String blocks = MODCONF + "blocks";
		public static final String inventory = MODCONF + "Inventory";
		public static final String items = MODCONF + "items";
		public static final String recipes = MODCONF + "recipes";
		public static final String villagers = MODCONF + "villagers";
		
		public static final String saplingBiomes = "SaplingGrowthDetail";
		public static final String inventoryModpack = "terrariaButtonsModpackGuis";
		public static final String modpackMisc = "modpacks";
		public static final String commands = "commands";
	}


	public static final int					fish_normal	= 0;
	public static final int					fish_salmon	= 1;
	public static final int					fish_clown	= 2;
	public static final int					fish_puffer	= 3;

	public static final int						dye_cocoa	= 3;
	public static final int						dye_lapis	= 4;

	public static final int SQ = 18;
	public static final int ARMOR_SIZE = 4;
	public static final int ROWS_VANILLA = 3;
	public static final int COLS_VANILLA = 9;

	public static final int btnWidth = 60;
	public static final int btnHeight = 20;

	public static final int skull_skeleton = 0;
	public static final int skull_wither = 1;
	public static final int skull_zombie = 2;
	public static final int skull_player = 3;
	public static final int skull_creeper = 4;

	public static final int NOTIFY = 2;

	public static final int TICKS_PER_SEC = 20;
	public static final int CHUNK_SIZE = 16;

	public static final int DIR_WEST = 1;
	public static final int DIR_SOUTH = 0;
	public static final int DIR_EAST = 3;
	public static final int DIR_NORTH = 2;

	public static final int HOTBAR_SIZE = 9;
	public static final String SkullOwner = "SkullOwner";

	public class Dimension {

		public static final int overworld = 0;
		public static final int end = 1;
		public static final int nether = -1;
	}

	public static final int dye_bonemeal = 15;
	public static final int wool_white = 0;
	public static final int WORLDHEIGHT = 256;
}// ends class reference