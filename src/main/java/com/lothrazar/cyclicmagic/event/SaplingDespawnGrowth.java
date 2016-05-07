package com.lothrazar.cyclicmagic.event;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.event.terraingen.SaplingGrowTreeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SaplingDespawnGrowth {

	public static boolean plantDespawningSaplings = true;

	public SaplingDespawnGrowth() {
		//from one of my mods https://github.com/LothrazarMinecraftMods/SaplingGrowthControl/blob/master/src/main/java/com/lothrazar/samssaplings/ModConfig.java
		
	}
	/*
	private static final int sapling_oak = 0;
	private static final int sapling_spruce = 1;
	private static final int sapling_birch = 2;
	private static final int sapling_jungle = 3;
	private static final int sapling_acacia = 4;
	private static final int sapling_darkoak = 5;

	@SubscribeEvent
	public void onSaplingGrowTreeEvent(SaplingGrowTreeEvent event) {
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		Block b = world.getBlockState(pos).getBlock();

		boolean treeAllowedToGrow = false;

		if (b == Blocks.sapling)// this may not always be true: such as trees
								// added by Mods, so not a vanilla tree, but
								// throwing same event
		{
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
				if (drop_on_failed_growth) {
					dropItemStackInWorld(world, pos, new ItemStack(Blocks.sapling, 1, tree_type));
				}
			}
		}// else a tree grew that was added by some mod
	}
	*/

	@SubscribeEvent
	public void onItemExpireEvent(ItemExpireEvent event) {
		if (plantDespawningSaplings == false) {
			return;
		}

		EntityItem entityItem = event.getEntityItem();
		Entity entity = event.getEntity();
		ItemStack is = entityItem.getEntityItem();
		if (is == null) {
			return;
		}// has not happened in the wild, yet

		Block blockhere = entity.worldObj.getBlockState(entityItem.getPosition()).getBlock();
		Block blockdown = entity.worldObj.getBlockState(entityItem.getPosition().down()).getBlock();

		if (blockhere == Blocks.air && blockdown == Blocks.dirt || // includes
																	// podzol
																	// and such
		blockdown == Blocks.grass) {
			// plant the sapling, replacing the air and on top of dirt/plantable

			if (Block.getBlockFromItem(is.getItem()) == Blocks.sapling)
				entity.worldObj.setBlockState(entityItem.getPosition(), Blocks.sapling.getStateFromMeta(is.getItemDamage()));
			else if (Block.getBlockFromItem(is.getItem()) == Blocks.red_mushroom)
				entity.worldObj.setBlockState(entityItem.getPosition(), Blocks.red_mushroom.getDefaultState());
			else if (Block.getBlockFromItem(is.getItem()) == Blocks.brown_mushroom)
				entity.worldObj.setBlockState(entityItem.getPosition(), Blocks.brown_mushroom.getDefaultState());

		}
	}

	public static EntityItem dropItemStackInWorld(World worldObj, BlockPos pos, ItemStack stack) {
		EntityItem entityItem = new EntityItem(worldObj, pos.getX(), pos.getY(), pos.getZ(), stack);

		if (worldObj.isRemote == false)// do not spawn a second 'ghost' one on
										// client side
			worldObj.spawnEntityInWorld(entityItem);
		return entityItem;
	}
}