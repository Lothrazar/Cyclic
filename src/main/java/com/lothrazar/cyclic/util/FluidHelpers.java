package com.lothrazar.cyclic.util;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.data.Model3D;
import com.lothrazar.cyclic.fluid.FluidBiomassHolder;
import com.lothrazar.cyclic.fluid.FluidHoneyHolder;
import com.lothrazar.cyclic.fluid.FluidMagmaHolder;
import com.lothrazar.cyclic.fluid.FluidSlimeHolder;
import com.lothrazar.cyclic.fluid.FluidXpJuiceHolder;
import com.lothrazar.cyclic.render.FluidRenderMap;
import com.lothrazar.cyclic.render.FluidRenderMap.FluidFlow;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class FluidHelpers {

  private static final int COLOUR_DEFAULT = 0xADD8E6; // if some random mod adds a fluid with no colour 
  private static final int COLOUR_MILK = 0xF1F1F1; // mojang/forge didnt give any
  private static final int COLOUR_LAVA = 0xff8c00; // mojang/forge didnt give lava any colour value
  public static final FluidRenderMap<Int2ObjectMap<Model3D>> CACHED_FLUIDS = new FluidRenderMap<>();
  public static final int STAGES = 1400;

  public static int getColorFromFluid(FluidStack fstack) {
    if (fstack != null && fstack.getFluid() != null) {
      //first check mine
      if (fstack.getFluid() == FluidBiomassHolder.STILL.get()) {
        return FluidBiomassHolder.COLOR;
      }
      else if (fstack.getFluid() == FluidHoneyHolder.STILL.get()) {
        return FluidHoneyHolder.COLOR;
      }
      else if (fstack.getFluid() == FluidMagmaHolder.STILL.get()) {
        return FluidMagmaHolder.COLOR;
      }
      else if (fstack.getFluid() == FluidSlimeHolder.STILL.get()) {
        return FluidSlimeHolder.COLOR;
      }
      else if (fstack.getFluid() == FluidXpJuiceHolder.STILL.get()) {
        return FluidXpJuiceHolder.COLOR;
      } //now check if the fluid has a color
      //      else if (fstack.getFluid().getAttributes().getColor() > 0) { 
      //        return fstack.getFluid().getAttributes().getColor();
      //      }
      else if (fstack.getFluid() == ForgeMod.MILK.get()) {
        return COLOUR_MILK;
      }
      else if (fstack.getFluid() == Fluids.LAVA) {
        return COLOUR_LAVA;
      }
    }
    return COLOUR_DEFAULT;
  }

  public static class FluidAttributes {

    public static final int BUCKET_VOLUME = net.minecraftforge.fluids.FluidType.BUCKET_VOLUME;
  }

  public static void extractSourceWaterloggedCauldron(Level level, BlockPos posTarget, IFluidHandler tank) {
    //fills always gonna be one bucket but we dont know what type yet
    //test if its a source block, or a waterlogged block
    BlockState targetState = level.getBlockState(posTarget);
    FluidState fluidState = level.getFluidState(posTarget);
    if (targetState.hasProperty(BlockStateProperties.WATERLOGGED) && targetState.getValue(BlockStateProperties.WATERLOGGED) == true) {
      //for waterlogged it is hardcoded to water
      int simFill = tank.fill(new FluidStack(new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME), FluidAttributes.BUCKET_VOLUME), FluidAction.SIMULATE);
      if (simFill == FluidAttributes.BUCKET_VOLUME
          && level.setBlockAndUpdate(posTarget, targetState.setValue(BlockStateProperties.WATERLOGGED, false))) {
        tank.fill(new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME), FluidAction.EXECUTE);
      }
    }
    else if (targetState.getBlock() == Blocks.WATER_CAULDRON) {
      int simFill = tank.fill(new FluidStack(new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME), FluidAttributes.BUCKET_VOLUME), FluidAction.SIMULATE);
      if (simFill == FluidAttributes.BUCKET_VOLUME
          && level.setBlockAndUpdate(posTarget, Blocks.CAULDRON.defaultBlockState())) {
        tank.fill(new FluidStack(new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME), FluidAttributes.BUCKET_VOLUME), FluidAction.EXECUTE);
      }
    }
    else if (targetState.getBlock() == Blocks.LAVA_CAULDRON) {
      //copypasta of water cauldron code
      int simFill = tank.fill(new FluidStack(new FluidStack(Fluids.LAVA, FluidAttributes.BUCKET_VOLUME), FluidAttributes.BUCKET_VOLUME), FluidAction.SIMULATE);
      if (simFill == FluidAttributes.BUCKET_VOLUME
          && level.setBlockAndUpdate(posTarget, Blocks.CAULDRON.defaultBlockState())) {
        tank.fill(new FluidStack(new FluidStack(Fluids.LAVA, FluidAttributes.BUCKET_VOLUME), FluidAttributes.BUCKET_VOLUME), FluidAction.EXECUTE);
      }
    }
    else if (fluidState != null && fluidState.isSource() && fluidState.getType() != null) { // from ze world
      //not just water. any fluid source block
      int simFill = tank.fill(new FluidStack(new FluidStack(fluidState.getType(), FluidAttributes.BUCKET_VOLUME), FluidAttributes.BUCKET_VOLUME), FluidAction.SIMULATE);
      if (simFill == FluidAttributes.BUCKET_VOLUME
          && level.setBlockAndUpdate(posTarget, Blocks.AIR.defaultBlockState())) {
        tank.fill(new FluidStack(fluidState.getType(), FluidAttributes.BUCKET_VOLUME), FluidAction.EXECUTE);
      }
    }
  }

  public static TextureAtlasSprite getSprite(ResourceLocation spriteLocation) {
    return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(spriteLocation);
  }

  public static Model3D getFluidModel(FluidStack fluid, int stage) {
    if (CACHED_FLUIDS.containsKey(fluid) && CACHED_FLUIDS.get(fluid).containsKey(stage)) {
      return CACHED_FLUIDS.get(fluid).get(stage);
    }
    Model3D model = new Model3D();
    model.setTexture(FluidRenderMap.getFluidTexture(fluid, FluidFlow.STILL));
    if (IClientFluidTypeExtensions.of(fluid.getFluid()).getStillTexture(fluid) != null) {
      double sideSpacing = 0.00625;
      double belowSpacing = 0.0625 / 4;
      double topSpacing = belowSpacing;
      model.minX = sideSpacing;
      model.minY = belowSpacing;
      model.minZ = sideSpacing;
      model.maxX = 1 - sideSpacing;
      model.maxY = 1 - topSpacing;
      model.maxZ = 1 - sideSpacing;
    }
    if (CACHED_FLUIDS.containsKey(fluid)) {
      CACHED_FLUIDS.get(fluid).put(stage, model);
    }
    else {
      Int2ObjectMap<Model3D> map = new Int2ObjectOpenHashMap<>();
      map.put(stage, model);
      CACHED_FLUIDS.put(fluid, map);
    }
    return model;
  }

  public static float getScale(FluidTank tank) {
    return getScale(tank.getFluidAmount(), tank.getCapacity(), tank.isEmpty());
  }

  public static float getScale(int stored, int capacity, boolean empty) {
    float targetScale = (float) stored / capacity;
    return targetScale;
  }

  public static IFluidHandler getTank(Level world, BlockPos pos, Direction side) {
    BlockEntity tile = world.getBlockEntity(pos);
    if (tile == null) {
      return null;
    }
    return tile.getCapability(ForgeCapabilities.FLUID_HANDLER, side).orElse(null);
  }

  public static boolean tryFillPositionFromTank(Level world, BlockPos posSide, Direction sideOpp, IFluidHandler tankFrom, final int amount) {
    if (tankFrom == null) {
      return false;
    }
    try {
      IFluidHandler fluidTo = FluidUtil.getFluidHandler(world, posSide, sideOpp).orElse(null);
      if (fluidTo != null) {
        //its not my facing dir
        // SO: pull fluid from that into myself
        FluidStack wasDrained = tankFrom.drain(amount, FluidAction.SIMULATE);
        if (wasDrained == null) {
          return false;
        }
        int filled = fluidTo.fill(wasDrained, FluidAction.SIMULATE);
        if (wasDrained != null && wasDrained.getAmount() > 0
            && filled > 0) {
          int realAmt = Math.min(filled, wasDrained.getAmount());
          wasDrained = tankFrom.drain(realAmt, FluidAction.EXECUTE);
          if (wasDrained == null) {
            return false;
          }
          int actuallyFilled = fluidTo.fill(wasDrained, FluidAction.EXECUTE);
          return actuallyFilled > 0;
        }
      }
      return false;
    }
    catch (Exception e) {
      ModCyclic.LOGGER.error("A fluid tank had an issue when we tried to fill", e);
      //charset crashes here i guess
      //https://github.com/PrinceOfAmber/Cyclic/issues/605
      // https://github.com/PrinceOfAmber/Cyclic/issues/605https://pastebin.com/YVtMYsF6
      return false;
    }
  }
}
