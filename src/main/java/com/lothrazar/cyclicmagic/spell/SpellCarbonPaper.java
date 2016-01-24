package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.item.ItemPaperCarbon;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellCarbonPaper extends BaseSpell implements ISpell {
	public SpellCarbonPaper(int id ,String name){
		super(id,name);
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
		
		if ((blockClicked == Blocks.wall_sign || blockClicked == Blocks.standing_sign) && container instanceof TileEntitySign) {
		 
			ItemPaperCarbon.copySignAndSpawnItem(world,  (TileEntitySign) container, pos);
			
			return true;
		}
		else if (blockClicked == Blocks.noteblock && container instanceof TileEntityNote) {
		 
			ItemPaperCarbon.copyNoteAndSpawnItem(world, (TileEntityNote) container, pos);

			return true;
		}
		
		return false;
	}
}
