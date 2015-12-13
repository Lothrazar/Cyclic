package com.lothrazar.samsmagic.spell;

import com.lothrazar.samsmagic.ItemRegistry;
import com.lothrazar.samsmagic.ModSpells;
import com.lothrazar.samsmagic.SpellRegistry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public abstract class BaseSpellExp implements ISpell
{ 
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
		
		ModSpells.spawnParticle(world, EnumParticleTypes.CRIT, pos);

		ModSpells.drainExp(player, getExpCost());
	}

	public void onCastFailure(World world, EntityPlayer player, BlockPos pos)
	{
		ModSpells.playSoundAt(player, "random.fizz");//"random.wood_click");

		//ModSpells.addChatMessage(player, ModSpells.lang("spell.exp.missing")+this.getExpCost());
	}
	
	@Override
	public boolean canPlayerCast(World world, EntityPlayer player, BlockPos pos)
	{
		if(player.capabilities.isCreativeMode){return true;}
		
		return (getExpCost() <= ModSpells.getExpTotal(player)); 
	}
	
	@Override
	public ItemStack getIconDisplayHeader()
	{
		return new ItemStack(ItemRegistry.exp_cost_dummy);
	}
}
