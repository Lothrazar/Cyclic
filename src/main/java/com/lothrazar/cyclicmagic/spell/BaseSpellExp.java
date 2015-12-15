package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.Const;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import com.lothrazar.cyclicmagic.util.UtilExperience;

public abstract class BaseSpellExp extends BaseSpell implements ISpell
{ 
	private int expCost = 20;//default cost
	
	public int getExpCost()
	{
		return expCost; 
	}
	public BaseSpell setExpCost(int cost) {
		expCost = cost;
		return this;
	}
	//
	public void onCastSuccess(World world, EntityPlayer player, BlockPos pos)
	{
		player.swingItem();
		
		UtilParticle.spawnParticle(world, EnumParticleTypes.CRIT, pos);

		UtilExperience.drainExp(player, getExpCost());
	}

	public void onCastFailure(World world, EntityPlayer player, BlockPos pos)
	{
		UtilSound.playSoundAt(player, UtilSound.fizz);

		//nope, casting might fail for a reason unrelated to exp
		//but kepe this in case we have an alternate fail?
		//player.addChatMessage(new ChatComponentTranslation(StatCollector.translateToLocal("spell.exp.missing") + this.getExpCost()));
	}
	
	@Override
	public boolean canPlayerCast(World world, EntityPlayer player, BlockPos pos)
	{
		if(player.capabilities.isCreativeMode){return true;}
		
		return (getExpCost() <= UtilExperience.getExpTotal(player)); 
	}
	
	private final ResourceLocation header = new ResourceLocation(Const.MODID,"textures/spells/exp_cost_dummy.png");
	private final ResourceLocation header_empty = new ResourceLocation(Const.MODID,"textures/spells/exp_cost_empty_dummy.png");
	
	@Override
	public ResourceLocation getIconDisplayHeaderEnabled()
	{
		return header;
	}
	@Override
	public ResourceLocation getIconDisplayHeaderDisabled()
	{
		return header_empty;
	}
}
