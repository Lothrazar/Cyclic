package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilShape;
import com.lothrazar.cyclic.util.UtilSound;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nullable;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.SoundEvents;
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
  private static final List<Fluid> VALID_WATER = new ArrayList<>();
  private static final List<Fluid> VALID_LAVA = new ArrayList<>();

  public enum Mode implements IStringSerializable {

    WATER, LAVA, GENERIC;

    @Override
    public String getString() {
      return this.name().toLowerCase();
    }

    public Mode getNext() {
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
    VALID_WATER.add(Fluids.WATER);
    VALID_WATER.add(Fluids.FLOWING_WATER);
    VALID_LAVA.add(Fluids.LAVA);
    VALID_LAVA.add(Fluids.FLOWING_LAVA);
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    BlockPos pos = context.getPos();
    World world = context.getWorld();
    Direction face = context.getFace();
    Mode fluidMode = Mode.values()[context.getItem().getOrCreateTag().getInt(NBT_MODE)];
    List<BlockPos> area = UtilShape.cubeSquareBase(pos.offset(face), SIZE, 1);
    List<Fluid> theList = fluidMode == Mode.WATER ? VALID_WATER : fluidMode == Mode.LAVA ? VALID_LAVA : null;
    AtomicBoolean removed = new AtomicBoolean(false);
    if (theList != null) {
      area.stream().filter(pos2 -> theList.contains(world.getFluidState(pos2).getFluid())).forEach(pos3 -> {
        removeLiquid(world, pos3);
        removed.set(true);
      });
    }
    else {
      area.stream().filter(pos2 -> !world.getFluidState(pos2).isEmpty()).forEach(pos3 -> {
        removeLiquid(world, pos3);
        removed.set(true);
      });
    }
    //
    if (removed.get()) {
      PlayerEntity player = context.getPlayer();
      player.swingArm(context.getHand());
      context.getItem().damageItem(1, player, (e) -> {
        // TODO : test break
      });
      if (world.isRemote) {
        UtilSound.playSound(pos, SoundEvents.ITEM_BUCKET_FILL);
      }
    }
    return ActionResultType.SUCCESS;
  }

  private void removeLiquid(World world, BlockPos pos) {
    world.setBlockState(pos, Blocks.AIR.getDefaultState(), 18);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);
    tooltip.add(getModeTooltip(stack).mergeStyle(TextFormatting.AQUA));
  }

  @Override
  public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
    stack.getOrCreateTag().putInt(NBT_MODE, Mode.WATER.ordinal());
    super.onCreated(stack, worldIn, playerIn);
  }

  private static TranslationTextComponent getModeTooltip(ItemStack stack) {
    Mode mode = Mode.values()[stack.getOrCreateTag().getInt(NBT_MODE)];
    return new TranslationTextComponent("item.cyclic.antimatter_wand.tooltip0",
        new TranslationTextComponent(String.format("item.cyclic.antimatter_wand.mode.%s",
            mode.getString())));
  }

  public static void toggleMode(PlayerEntity player, ItemStack stack) {
    if (player.getCooldownTracker().hasCooldown(stack.getItem())) {
      return;
    }
    Mode mode = Mode.values()[stack.getOrCreateTag().getInt(NBT_MODE)];
    stack.getOrCreateTag().putInt(NBT_MODE, mode.getNext().ordinal());
    player.getCooldownTracker().setCooldown(stack.getItem(), COOLDOWN);
    if (player.world.isRemote) {
      player.sendStatusMessage(getModeTooltip(stack), true);
      UtilSound.playSound(player, SoundRegistry.tool_mode);
    }
  }
}
