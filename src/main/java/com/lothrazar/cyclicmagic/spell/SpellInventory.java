package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class SpellInventory extends BaseSpell{

	public SpellInventory(int id, String n){

		super.init(id, n);
		this.cost = 0;// so far, the only spell costing zero

		this.header = new ResourceLocation(Const.MODID, "textures/spells/mouseptr.png");
		this.header_empty = header;
	}

	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side){

		if(!world.isRemote){ // TODO: does the isRemote check actually matter
			player.openGui(ModMain.instance, Const.GUI_INDEX, world, 0, 0, 0);
		}

		return true;
	}

	@Override
	public void spawnParticle(World world, EntityPlayer player, BlockPos pos){

		UtilParticle.spawnParticle(world, EnumParticleTypes.CRIT_MAGIC, pos);

	}

	@Override
	public void playSound(World world, Block block, BlockPos pos){

		UtilSound.playSound(world, pos, UtilSound.drink);
	}
}
