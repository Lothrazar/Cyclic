package com.lothrazar.cyclic.base;

import java.util.List;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.registry.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

public abstract class BlockBase extends Block {

  public static final BooleanProperty IS_LIT = BooleanProperty.create("lit");

  private static boolean hasCapabilityDir(Direction facing, IWorld world, BlockPos facingPos, Capability<?> cap) {
    if (facing == null) {
      return false;
    }
    TileEntity neighbor = world.getTileEntity(facingPos);
    if (neighbor != null
        && neighbor.getCapability(cap, facing.getOpposite()).orElse(null) != null) {
      return true;
    }
    return false;
  }

  public static boolean isItem(BlockState stateIn, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
    return hasCapabilityDir(facing, world, facingPos, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
  }

  public static boolean isFluid(BlockState stateIn, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
    return hasCapabilityDir(facing, world, facingPos, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
  }

  public static boolean isEnergy(BlockState stateIn, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
    return hasCapabilityDir(facing, world, facingPos, CapabilityEnergy.ENERGY);
  }

  public BlockBase(Properties properties) {
    super(properties);
    BlockRegistry.blocks.add(this);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    TranslationTextComponent t = new TranslationTextComponent(getTranslationKey() + ".tooltip");
    t.applyTextStyle(TextFormatting.GRAY);
    tooltip.add(t);
  }

  @OnlyIn(Dist.CLIENT)
  public void registerClient() {}
}
