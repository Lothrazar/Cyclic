package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilShape;
import com.lothrazar.cyclic.util.UtilSound;
import java.util.List;
import java.util.Locale;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AntimatterEvaporatorWandItem extends ItemBase {

  private static final int SIZE = 4;
  private static final String NBT_MODE = "mode";
  public static final int COOLDOWN = 15;

  public enum EvaporateMode implements IStringSerializable {

    WATER, LAVA, GENERIC;

    @Override
    public String getString() {
      return this.name().toLowerCase(Locale.ENGLISH);
    }

    public EvaporateMode getNext() {
      switch (this) {
        case WATER:
          return LAVA;
        case LAVA:
          return GENERIC;
        case GENERIC:
          return WATER;
      }
      return WATER;
    }
  }

  public AntimatterEvaporatorWandItem(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    BlockPos pos = context.getPos();
    World world = context.getWorld();
    Direction face = context.getFace();
    ItemStack itemstack = context.getItem();
    EvaporateMode fluidMode = EvaporateMode.values()[itemstack.getOrCreateTag().getInt(NBT_MODE)];
    List<BlockPos> area = UtilShape.cubeSquareBase(pos.offset(face), SIZE, 1);
    //    AtomicBoolean removed = new AtomicBoolean(false);
    switch (fluidMode) {
      case GENERIC:
      break;
      case LAVA:
      break;
      case WATER:
      break;
      default:
      break;
    }
    int countSuccess = 0;
    boolean tryHere = false;
    for (BlockPos posTarget : area) {
      BlockState blockHere = world.getBlockState(posTarget);
      FluidState fluidHere = blockHere.getFluidState();
      if (fluidHere == null) {
        continue;
      }
      tryHere = false;
      if (fluidMode == EvaporateMode.GENERIC && fluidHere.getFluid() != null
          && fluidHere.getFluid() != Fluids.EMPTY) {
        tryHere = true;
      }
      else if (fluidMode == EvaporateMode.WATER && fluidHere.getFluid().isIn(FluidTags.WATER)) {
        tryHere = true;
      }
      else if (fluidMode == EvaporateMode.LAVA && fluidHere.getFluid().isIn(FluidTags.LAVA)) {
        tryHere = true;
      }
      if (tryHere && removeLiquid(world, blockHere, posTarget)) {
        countSuccess++;
      }
    }
    if (countSuccess > 0) {
      PlayerEntity player = context.getPlayer();
      player.swingArm(context.getHand());
      UtilItemStack.damageItem(player, itemstack);
      if (world.isRemote) {
        UtilSound.playSound(pos, SoundRegistry.PSCHEW_FIRE);
      }
    }
    return ActionResultType.SUCCESS;
  }

  private boolean removeLiquid(World world, BlockState blockHere, BlockPos pos) {
    if (blockHere.getBlock() instanceof IBucketPickupHandler) {
      IBucketPickupHandler block = (IBucketPickupHandler) blockHere.getBlock();
      Fluid res = block.pickupFluid(world, pos, blockHere);
      if (res == null || res == Fluids.EMPTY) {
        // flowing block
        return world.setBlockState(pos, Blocks.AIR.getDefaultState(), 18);
      }
      else {
        return true; // was source block
      }
    }
    else if (blockHere.hasProperty(BlockStateProperties.WATERLOGGED)) {
      // un-water log
      return world.setBlockState(pos, blockHere.with(BlockStateProperties.WATERLOGGED, false), 18);
    }
    else {
      //ok just nuke it 
      return world.setBlockState(pos, Blocks.AIR.getDefaultState(), 18);
    }
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);
    tooltip.add(getModeTooltip(stack).mergeStyle(TextFormatting.AQUA));
  }

  @Override
  public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
    stack.getOrCreateTag().putInt(NBT_MODE, EvaporateMode.WATER.ordinal());
    super.onCreated(stack, worldIn, playerIn);
  }

  private static TranslationTextComponent getModeTooltip(ItemStack stack) {
    EvaporateMode mode = EvaporateMode.values()[stack.getOrCreateTag().getInt(NBT_MODE)];
    return new TranslationTextComponent("item.cyclic.antimatter_wand.tooltip0",
        new TranslationTextComponent(String.format("item.cyclic.antimatter_wand.mode.%s",
            mode.getString())));
  }

  public static void toggleMode(PlayerEntity player, ItemStack stack) {
    if (player.getCooldownTracker().hasCooldown(stack.getItem())) {
      return;
    }
    EvaporateMode mode = EvaporateMode.values()[stack.getOrCreateTag().getInt(NBT_MODE)];
    stack.getOrCreateTag().putInt(NBT_MODE, mode.getNext().ordinal());
    player.getCooldownTracker().setCooldown(stack.getItem(), COOLDOWN);
    if (player.world.isRemote) {
      player.sendStatusMessage(getModeTooltip(stack), true);
      UtilSound.playSound(player, SoundRegistry.TOOL_MODE);
    }
  }
}
