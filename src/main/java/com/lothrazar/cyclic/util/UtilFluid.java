package com.lothrazar.cyclic.util;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.data.Model3D;
import com.lothrazar.cyclic.render.FluidRenderMap;
import com.lothrazar.cyclic.render.FluidRenderMap.FluidType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class UtilFluid {

  public static final FluidRenderMap<Int2ObjectMap<Model3D>> CACHED_FLUIDS = new FluidRenderMap<>();
  public static final int STAGES = 1400;

  /**
   * Thank you Mekanism which is MIT License https://github.com/mekanism/Mekanism
   * 
   * @param fluid
   * @param type
   * @return
   */
  public static TextureAtlasSprite getBaseFluidTexture(Fluid fluid, FluidType type) {
    final FluidAttributes fluidAttributes = fluid.getAttributes();
    final ResourceLocation spriteLocation = (type == FluidType.STILL)
        ? fluidAttributes.getStillTexture()
        : fluidAttributes.getFlowingTexture();
    return getSprite(spriteLocation);
  }

  public static TextureAtlasSprite getSprite(ResourceLocation spriteLocation) {
    return Minecraft.getInstance().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(spriteLocation);
  }

  public static Model3D getFluidModel(FluidStack fluid, int stage) {
    final Int2ObjectMap<Model3D> fluidCache = CACHED_FLUIDS
        .computeIfAbsent(fluid, fluidStack -> new Int2ObjectOpenHashMap<>());
    Model3D model = fluidCache.get(stage);
    if (model != null) {
      return model;
    }
    model = new Model3D();
    model.setTexture(FluidRenderMap.getFluidTexture(fluid, FluidType.STILL));
    if (fluid.getFluid().getAttributes().getStillTexture(fluid) != null) {
      double sideSpacing = 0.00625;
      double belowSpacing = 0.0625 / 4;
      model.minX = sideSpacing;
      model.minY = belowSpacing;
      model.minZ = sideSpacing;
      model.maxX = 1 - sideSpacing;
      model.maxY = 1 - belowSpacing;
      model.maxZ = 1 - sideSpacing;
    }
    fluidCache.put(stage, model);
    return model;
  }

  public static float getScale(FluidTank tank) {
    return getScale(tank.getFluidAmount(), tank.getCapacity(), tank.isEmpty());
  }

  public static float getScale(int stored, int capacity, boolean empty) {
    return (float) stored / capacity;
  }

  public static IFluidHandler getTank(World world, BlockPos pos, Direction side) {
    final TileEntity tile = world.getTileEntity(pos);
    if (tile == null) {
      return null;
    }
    return tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).orElse(null);
  }

  public static boolean tryFillPositionFromTank(World world, BlockPos posSide, Direction sideOpp, IFluidHandler tankFrom, int amount) {
    if (amount <= 0) {
      return false;
    }
    if (tankFrom == null) {
      return false;
    }
    LazyOptional<IFluidHandler> testNull = FluidUtil.getFluidHandler(world, posSide, sideOpp); //yes i got concurrent/NPE on this line
    final IFluidHandler fluidTo = testNull == null ? null : testNull.orElse(null);
    if (fluidTo == null) {
      return false;
    }
    //first we simulate
    final FluidStack toBeDrained = tankFrom.drain(amount, FluidAction.SIMULATE);
    if (toBeDrained.isEmpty()) {
      return false;
    }
    final int filledAmount = fluidTo.fill(toBeDrained, FluidAction.EXECUTE);
    if (filledAmount <= 0) {
      return false;
    }
    final FluidStack drained = tankFrom.drain(filledAmount, FluidAction.EXECUTE);
    final int drainedAmount = drained.getAmount();
    //sanity check
    if (filledAmount != drainedAmount) {
      ModCyclic.LOGGER.error("Imbalance filling fluids, filled " + filledAmount + " drained " + drainedAmount);
    }
    return true;
  }
}
