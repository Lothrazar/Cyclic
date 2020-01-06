package com.lothrazar.cyclic.block.tank;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ItemBlockTank extends BlockItem {

  public ItemBlockTank(Block blockIn, Properties builder) {
    super(blockIn, builder);
  }

  @Override
  public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable net.minecraft.nbt.CompoundNBT nbt) {
    //    if (this.getClass() == BucketItem.class)
    //      return new net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper(stack);
    //    else
    return super.initCapabilities(stack, nbt);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand hand) {
    //    if (player.isSneaking()) {// || worldIn.getBlockState(context.getPos()).isAir() == false) {
    ActionResult<ItemStack> res = super.onItemRightClick(worldIn, player, hand);
    //    }
    return res;
  }
}
