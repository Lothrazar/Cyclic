package com.lothrazar.cyclic.util;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.data.Model3D;
import com.lothrazar.cyclic.render.FluidRenderMap;
import com.lothrazar.cyclic.render.FluidRenderMap.FluidType;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
  public static TextureAtlasSprite getBaseFluidTexture(@Nonnull Fluid fluid, @Nonnull FluidType type) {
    ResourceLocation spriteLocation;
    if (type == FluidType.STILL) {
      spriteLocation = fluid.getAttributes().getStillTexture();
    }
    else {
      spriteLocation = fluid.getAttributes().getFlowingTexture();
    }
    return getSprite(spriteLocation);
  }

  public static TextureAtlasSprite getSprite(ResourceLocation spriteLocation) {
    return Minecraft.getInstance().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(spriteLocation);
  }

  public static Model3D getFluidModel(@Nonnull FluidStack fluid, int stage) {
    if (CACHED_FLUIDS.containsKey(fluid) && CACHED_FLUIDS.get(fluid).containsKey(stage)) {
      return CACHED_FLUIDS.get(fluid).get(stage);
    }
    Model3D model = new Model3D();
    model.setTexture(FluidRenderMap.getFluidTexture(fluid, FluidType.STILL));
    if (fluid.getFluid().getAttributes().getStillTexture(fluid) != null) {
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

  public static IFluidHandler getTank(World world, BlockPos pos, Direction side) {
    TileEntity tile = world.getTileEntity(pos);
    if (tile == null) {
      return null;
    }
    return tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).orElse(null);
  }

  public static boolean tryFillPositionFromTank(World world, BlockPos posSide, Direction sideOpp, IFluidHandler tankFrom, int amount) {
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
          return fluidTo.fill(wasDrained, FluidAction.EXECUTE) > 0;
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
