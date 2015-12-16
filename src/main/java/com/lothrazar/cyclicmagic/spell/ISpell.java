package com.lothrazar.cyclicmagic.spell;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public interface ISpell {
	public ISpell left();

	public ISpell right();

	public int getSpellID();

	public ISpell setSpellID(int id);

	public void cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side, Entity target);

	public int getCastCooldown();

	public void onCastSuccess(World world, EntityPlayer player, BlockPos pos);

	//TODO: differentiate between canCast returning false,
	//vs canCast was true, but then it failed after that?
	//that is, if you have enough resources(exp) to cast, but then it fails later on
	//vs it doesnt fail, but you just couldnt afford it to start with
	public void onCastFailure(World world, EntityPlayer player, BlockPos pos);

	public ResourceLocation getIconDisplay();

	public ResourceLocation getIconDisplayHeaderEnabled();

	public ResourceLocation getIconDisplayHeaderDisabled();

	public boolean canPlayerCast(World world, EntityPlayer player, BlockPos pos);
}
