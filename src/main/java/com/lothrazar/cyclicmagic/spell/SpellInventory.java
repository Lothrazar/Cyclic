package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.Const;
import com.lothrazar.cyclicmagic.ModMain;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class SpellInventory extends BaseSpell{

	public SpellInventory(int id, String n) {
		super.init(id, n);
		this.cost = 0;//so far, the only spell costing zero

		this.header = new ResourceLocation(Const.MODID, "textures/spells/mouseptr.png");
		this.header_empty = header;
	}

	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side) {
		
		if (! world.isRemote){ 	//TODO: does the isRemote check actually matter
			player.openGui(ModMain.instance, Const.GUI_INDEX, world, 0, 0, 0);
		}
		
		return true;
	}

	@Override
	public void spawnParticle(World world, EntityPlayer player, BlockPos pos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playSound(World world, EntityPlayer player, BlockPos pos) {
		// TODO Auto-generated method stub
		
	}
}
