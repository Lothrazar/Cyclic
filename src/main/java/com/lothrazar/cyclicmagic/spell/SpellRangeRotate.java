package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.net.MessageSpellRotate;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellRangeRotate extends BaseSpellRange {

	public SpellRangeRotate(int id, String name) {

		super.init(id, name);
		this.cost = 35;
		this.cooldown = 2;
	}

	@Override
	public boolean cast(World world, EntityPlayer p, ItemStack wand, BlockPos pos, EnumFacing side) {

		if (world.isRemote) {
			// only client side can call this method. mouseover does not exist
			// on server
			BlockPos mouseover = ModMain.proxy.getBlockMouseoverExact(maxRange);

			if (mouseover != null) {
				ModMain.network.sendToServer(new MessageSpellRotate(mouseover, ModMain.proxy.getSideMouseover(maxRange)));
			}
		}

		return true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void castFromServer(BlockPos pos, EnumFacing side, EntityPlayer p) {

		// TODO Auto-generated method stub
		if (pos == null || p.worldObj.getBlockState(pos) == null || side == null) { return; }

		World worldObj = p.worldObj;
		IBlockState clicked = worldObj.getBlockState(pos);
		if (clicked.getBlock() == null) { return; }
		Block clickedBlock = clicked.getBlock();
		int clickedMeta = clickedBlock.getMetaFromState(clicked);

		boolean isDone = false;

		if (clickedBlock.rotateBlock(worldObj, pos, side)) {
			//the built in function doues the properties: ("facing")|| ("rotation")
			// for example, BlockMushroom.rotateBlock uses this, and hay bales
			// acts similar to axis
			isDone = true;
		}
		else {
			// any property that is not variant?
			
			//first handle any special cases
			/*
			if(clickedBlock == Blocks.double_stone_slab){

				worldObj.setBlockState(pos, Blocks.double_stone_slab.getStateFromMeta(Const.Slab.stone_double_secret));
			}
			else if(clickedBlock == Blocks.double_stone_slab && clickedMeta == Const.Slab.stone_double_secret){

				worldObj.setBlockState(pos,  Blocks.stone.getDefaultState());
			}
			else 
				*/
			for (IProperty prop : (com.google.common.collect.ImmutableSet<IProperty<?>>) clicked.getProperties().keySet()) {
				
				//never use "variant": it changes wood types, stone types, etc.

				if (prop.getName().equals("half")) {

					worldObj.setBlockState(pos, clicked.cycleProperty(prop));

					isDone = true;
					break;
				}
				else if (prop.getName().equals("seamless")) {
					//http://minecraft.gamepedia.com/Slab#Block_state
					worldObj.setBlockState(pos, clicked.cycleProperty(prop));

					isDone = true;
					break;
				}
			}
		}

		if (isDone) {
			this.playSound(worldObj, clickedBlock, pos);
			this.spawnParticle(worldObj, p, pos);
		}
		return;
	}
}
