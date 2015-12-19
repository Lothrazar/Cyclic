package com.lothrazar.cyclicmagic.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellCarbonPaper extends BaseSpell implements ISpell {
	public SpellCarbonPaper(){
		super();
		this.cooldown = 60;
	}
	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side ) {

		//TODO: only copy is done here
		//then drops new item stack
		//then paste is only from item
		
		/*if ((blockClicked == Blocks.wall_sign || blockClicked == Blocks.standing_sign) && container instanceof TileEntitySign) {
			TileEntitySign sign = (TileEntitySign) container;

			if (isEmpty) {
				ItemPaperCarbon.copySign(world, playerIn, sign, held);
				wasCopy = true;
			} else {
				ItemPaperCarbon.pasteSign(world, playerIn, sign, held);
				wasCopy = false;
			}

			isValid = true;
		}
		if (blockClicked == Blocks.noteblock && container instanceof TileEntityNote) {
			TileEntityNote noteblock = (TileEntityNote) container;

			if (isEmpty) {
				ItemPaperCarbon.copyNote(world, playerIn, noteblock, held);
				wasCopy = true;
			} else {
				ItemPaperCarbon.pasteNote(world, playerIn, noteblock, held);
				wasCopy = false;
			}

			isValid = true;
		}*/
		return true;
	}
}
