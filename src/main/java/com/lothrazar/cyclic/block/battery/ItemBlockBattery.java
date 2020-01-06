package com.lothrazar.cyclic.block.battery;

import javax.annotation.Nullable;
import com.lothrazar.cyclic.base.CustomEnergyStorage;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemBlockBattery extends BlockItem {

  public ItemBlockBattery(Block blockIn, Properties builder) {
    super(blockIn, builder);
  }

  @Override
  public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable net.minecraft.nbt.CompoundNBT nbt) {
    //    if (this.getClass() == BucketItem.class)
    //      return new net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper(stack);
    //   else
    CustomEnergyStorage cap = new CustomEnergyStorage(TileBattery.MAX, TileBattery.MAX);
    //
    //
    ICapabilityProvider s = super.initCapabilities(stack, nbt);
    //    s.//wtf 
    return s;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand hand) {
    //    if (player.isSneaking()) {// || worldIn.getBlockState(context.getPos()).isAir() == false) {
    ActionResult<ItemStack> res = super.onItemRightClick(worldIn, player, hand);
    //    }
    return res;
  }
}
