package com.lothrazar.cyclicmagic.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface ISpell
{
	public ISpell left();
	public ISpell right();

	public String getSpellName();
	
	public int getSpellID();
 
	public int getExpCost();

	public void cast(World world, EntityPlayer player, BlockPos pos);
	
	public void onCastSuccess(World world, EntityPlayer player, BlockPos pos);
	
	public void onCastFailure(World world, EntityPlayer player, BlockPos pos);
	
	public ItemStack getIconDisplay();
	
	public ItemStack getIconDisplayHeader();
	
	public boolean canPlayerCast(World world, EntityPlayer player, BlockPos pos);
}
