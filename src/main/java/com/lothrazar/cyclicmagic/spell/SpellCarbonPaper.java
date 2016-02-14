package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.item.ItemPaperCarbon;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class SpellCarbonPaper extends BaseSpell implements ISpell {
	public SpellCarbonPaper(int id ,String name){
		super.init(id,name);
	}
	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side ) {

		if(pos == null){
			return false;
		}
		// only copy is done here
		//then drops new item stack
		//then paste is only from item
		
		Block blockClicked = world.getBlockState(pos) == null ? null : world.getBlockState(pos).getBlock();
		TileEntity container = world.getTileEntity(pos);
		
		if(blockClicked == null || container == null){
			return false;
		}
		
		boolean isDone = false;
		
		if ((blockClicked == Blocks.wall_sign || blockClicked == Blocks.standing_sign) && container instanceof TileEntitySign) {
		 
			ItemPaperCarbon.copySignAndSpawnItem(world,  (TileEntitySign) container, pos);
			
			isDone =  true;
		}
		else if (blockClicked == Blocks.noteblock && container instanceof TileEntityNote) {
		 
			ItemPaperCarbon.copyNoteAndSpawnItem(world, (TileEntityNote) container, pos);

			isDone =  true;
		}
		
		if(isDone){
			this.spawnParticle(world, player, pos);
			this.playSound(world, blockClicked, pos);
		}
		
		return isDone;
	}
	
	@Override
	public void spawnParticle(World world, EntityPlayer player, BlockPos pos) {
		UtilParticle.spawnParticle(world, EnumParticleTypes.CRIT_MAGIC, pos);
	}

	@Override
	public void playSound(World world, Block block, BlockPos pos) {
		UtilSound.playSound(world, pos, block.stepSound.getPlaceSound());
	}
}
