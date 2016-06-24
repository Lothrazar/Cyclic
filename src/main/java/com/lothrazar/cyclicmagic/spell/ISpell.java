package com.lothrazar.cyclicmagic.spell;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ISpell {
  public int getID();
  public String getName();
  public String getUnlocalizedName();
  public String getInfo();
  public boolean cast(World world, EntityPlayer player, ItemStack wand, BlockPos pos, EnumFacing side);
  public void spawnParticle(World world, EntityPlayer player, BlockPos pos);
  public void playSound(World world, EntityPlayer player, Block block, BlockPos pos);
  public void onCastFailure(World world, EntityPlayer player, BlockPos pos);
  public ResourceLocation getIconDisplay();
  public boolean canPlayerCast(World world, EntityPlayer player, BlockPos pos);
}
