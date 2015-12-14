package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.Const;
import com.lothrazar.cyclicmagic.ItemRegistry;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.SpellRegistry;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import com.lothrazar.cyclicmagic.util.UtilExperience;

public abstract class BaseSpellExp implements ISpell
{ 
	public abstract int getExpCost();
	//TODO: do a setExpCost, instead of hard reffing config inside the spells
	
	@Override
	public ISpell left() 
	{
		int idx = SpellRegistry.spellbook.indexOf(this);//-1 for not found
		if(idx == -1){return null;}
		
		if(idx == 0) idx = SpellRegistry.spellbook.size() - 1;
		else idx = idx-1;
		
		return SpellRegistry.spellbook.get(idx);
	}

	@Override
	public ISpell right() 
	{
		int idx = SpellRegistry.spellbook.indexOf(this);
		if(idx == -1){return null;}
		
		if(idx == SpellRegistry.spellbook.size() - 1) idx = 0;
		else idx = idx+1;
		
		return SpellRegistry.spellbook.get(idx);
	}
	
	public void onCastSuccess(World world, EntityPlayer player, BlockPos pos)
	{
		player.swingItem();
		
		UtilParticle.spawnParticle(world, EnumParticleTypes.CRIT, pos);

		UtilExperience.drainExp(player, getExpCost());
	}

	public void onCastFailure(World world, EntityPlayer player, BlockPos pos)
	{
		player.worldObj.playSoundAtEntity(player, "random.fizz",1,1);//"random.wood_click");

		//ModSpells.addChatMessage(player, ModSpells.lang("spell.exp.missing")+this.getExpCost());
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
