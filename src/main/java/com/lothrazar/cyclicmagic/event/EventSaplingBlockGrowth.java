package com.lothrazar.cyclicmagic.event;

import java.util.ArrayList;
import java.util.List;

import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilEntity;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.terraingen.SaplingGrowTreeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventSaplingBlockGrowth implements IHasConfig{

	private static final int sapling_oak = 0;
	private static final int sapling_spruce = 1;
	private static final int sapling_birch = 2;
	private static final int sapling_jungle = 3;
	private static final int sapling_acacia = 4;
	private static final int sapling_darkoak = 5;
	
	private static List<Integer> acaciaBiomes;
	private static List<Integer> spruceBiomes;
	private static List<Integer> oakBiomes;
	private static List<Integer> birchBiomes;
	private static List<Integer> darkoakBiomes;
	private static List<Integer> jungleBiomes;
	private boolean enabled;

	@SubscribeEvent
	public void onSaplingGrowTreeEvent(SaplingGrowTreeEvent event) {
		
		if(!enabled){
			return;
		}
		
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		Block b = world.getBlockState(pos).getBlock();

		// this may not always be true: such as trees
		// added by Mods, so not a vanilla tree, but
		// throwing same event
		if (b != Blocks.sapling){
			return;
		}
		
		boolean treeAllowedToGrow = false;
		
		int meta = Blocks.sapling.getMetaFromState(world.getBlockState(pos));
 
		int biomeID = BiomeGenBase.getIdForBiome(world.getBiomeGenForCoords(pos));// event.world.getBiomeGenForCoords(event.pos).biomeID;

		int growth_data = 8;// 0-5 is the type, then it adds on a 0x8
		// and we know that it is always maxed out at ready to grow 8 since
		// it is turning into a tree.

		int tree_type = meta - growth_data;

		// IDS:
		// http://www.minecraftforum.net/forums/minecraft-discussion/recent-updates-and-snapshots/381405-full-list-of-biome-ids-as-of-13w36b
		// as of 12 march 2015, it seems biome id 168 does not exist, so 167
		// is highest used (vanilla minecraft)
		switch (tree_type) {
		case sapling_acacia:
			treeAllowedToGrow = acaciaBiomes.contains(biomeID);
			break;
		case sapling_spruce:
			treeAllowedToGrow = spruceBiomes.contains(biomeID);
			break;
		case sapling_oak:
			treeAllowedToGrow = oakBiomes.contains(biomeID);
			break;
		case sapling_birch:
			treeAllowedToGrow = birchBiomes.contains(biomeID);
			break;
		case sapling_darkoak:
			treeAllowedToGrow = darkoakBiomes.contains(biomeID);
			break;
		case sapling_jungle:
			treeAllowedToGrow = jungleBiomes.contains(biomeID);
			break;

		}

		if (treeAllowedToGrow == false) {
			event.setResult(Result.DENY);

			// overwrite the sapling. - we could set to Air first, but dont
			// see much reason to
			world.setBlockState(pos, Blocks.deadbush.getDefaultState());
			
			UtilEntity.dropItemStackInWorld(world, pos, new ItemStack(Blocks.sapling, 1, tree_type));
			
		}
	}
	
	@Override
	public void syncConfig(Configuration config) {
		

		String category = Const.ConfigCategory.environment;
		
		enabled = config.getBoolean("SaplingHomeBiomes", category, true, "Saplings are only allowed to grow into trees in their home biome, otherwise they turn to dead bushes");
	 
		category = Const.ConfigCategory.modpacks;

		config.addCustomCategoryComment(category, "A list of biome IDs that each sapling is allowed to grow in.  Useful for modpacks that add extra biomes.  ");

		String oakCSV = config.get(category, "oak", "4, 18, 132, 39, 166, 167, 21, 23, 151, 149, 22, 6, 134, 3, 20, 34, 12, 29, 157").getString();
		oakBiomes = csvToInt(oakCSV);

		String acaciaCSV = config.get(category, "acacia", "35, 36, 38, 163, 164").getString();
		acaciaBiomes = csvToInt(acaciaCSV);

		String spruceCSV = config.get(category, "spruce", "5, 19, 32, 160, 161, 33, 30, 31, 158, 3, 20, 34, 21, 12, 13").getString();
		spruceBiomes = csvToInt(spruceCSV);

		String birchCSV = config.get(category, "birch", "27, 28, 155, 156, 4, 18, 132, 29, 157").getString();
		birchBiomes = csvToInt(birchCSV);

		String darkCSV = config.get(category, "dark_oak", "29, 157").getString();
		darkoakBiomes = csvToInt(darkCSV);

		String jungleCSV = config.get(category, "jungle", "21, 23, 22, 149, 151").getString();
		jungleBiomes = csvToInt(jungleCSV);
 
	}

	private static List<Integer> csvToInt(String csv) {
		// does not check validity of biome ids
		List<Integer> bi = new ArrayList<Integer>();

		String[] list = csv.split(",");

		int biome;

		for (String s_id : list) {
			try {
				biome = Integer.parseInt(s_id.trim());

				bi.add(biome);
			}
			catch (Exception e) {
				ModMain.logger.warn( "Invalid biome id from config file, must be integer: " + s_id);
			}
		}

		return bi;
	}
}
