package com.lothrazar.cyclicmagic.util;

public class Const {

	public static final int			SQ							= 18;
	public static final int			ARMOR_SIZE			= 4;
	public static final int			ROWS_VANILLA		= 3;
	public static final int			COLS_VANILLA		= 9;

	public static final int			btnWidth				= 60;
	public static final int			btnHeight				= 20;

	public static final int			skull_skeleton	= 0;
	public static final int			skull_wither		= 1;
	public static final int			skull_zombie		= 2;
	public static final int			skull_player		= 3;
	public static final int			skull_creeper		= 4;

	public static final int			NOTIFY					= 2;

	public static final String	MODID						= "cyclicmagic";
	public static final String	MODRES					= Const.MODID + ":";
	public static final String	MODCONF					= Const.MODID + ".";

	public static final int			TICKS_PER_SEC		= 20;
	public static final int			CHUNK_SIZE			= 16;

	public static final int			DIR_WEST				= 1;
	public static final int			DIR_SOUTH				= 0;
	public static final int			DIR_EAST				= 3;
	public static final int			DIR_NORTH				= 2;

	public static final int			HOTBAR_SIZE			= 9;
	public static final String	SkullOwner			= "SkullOwner";
/*
	public class Slab {
		// http://minecraft.gamepedia.com/Slab#Block_data
		public static final int			stone_double			= 0;
		public static final int			stone_double_secret			= 8;

		public static final int			sandstone_stone_double			= 1;
		public static final int			sandstone_stone_double_secret			= 9;
		
	}
	public class Slab2 {
		//hey, minecraft calls it double_stone_slab2 so
		// http://minecraft.gamepedia.com/Slab#Block_data
		public static final int			redsandstone_double			= 0;
		public static final int			redsandstone_double_secret			= 8;

		
	}
	*/
	public class Dimension {

		public static final int	overworld	= 0;
		public static final int	end				= 1;
		public static final int	nether		= -1;
	}

	public static final int	dye_bonemeal	= 15;
	public static final int	wool_white		= 0;
}// ends class reference