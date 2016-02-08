package com.lothrazar.cyclicmagic.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public interface ISpell {
	public int getID();
	
	public String getName();
	public String getInfo();

	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side);

	public int getCost();

	public int getCastCooldown();

	public void onCastSuccess(World world, EntityPlayer player, BlockPos pos);

	public void onCastFailure(World world, EntityPlayer player, BlockPos pos);

	public ResourceLocation getIconDisplay();

	public ResourceLocation getIconDisplayHeaderEnabled();

	public ResourceLocation getIconDisplayHeaderDisabled();

	public boolean canPlayerCast(World world, EntityPlayer player, BlockPos pos);
}
