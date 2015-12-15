package com.lothrazar.cyclicmagic.spell;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public interface ISpell
{
	public ISpell left();
	public ISpell right();

	public int getSpellID();
	public ISpell setSpellID(int id);
 
	public void cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side, Entity target);

	public int getCastCooldown();
	
	public void onCastSuccess(World world, EntityPlayer player, BlockPos pos);
	
	public void onCastFailure(World world, EntityPlayer player, BlockPos pos);
	
	public ResourceLocation getIconDisplay();
	
	public ResourceLocation getIconDisplayHeaderEnabled();
	
	public ResourceLocation getIconDisplayHeaderDisabled();
	
	public boolean canPlayerCast(World world, EntityPlayer player, BlockPos pos);
}
