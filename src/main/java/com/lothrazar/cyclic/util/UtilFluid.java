package com.lothrazar.cyclic.util;

import com.lothrazar.cyclic.data.Model3D;
import com.lothrazar.cyclic.render.FluidRenderMap;
import com.lothrazar.cyclic.render.FluidRenderMap.FluidType;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.templates.FluidTank;

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

  public static IFluidBlock getFluidBlock(final World world, final BlockPos blockPos) {
    final BlockState blockState = world.getBlockState(blockPos);
    final Block block = blockState.getBlock();
    if (block instanceof IFluidBlock) {
      return (IFluidBlock) block;
    }
    return null;
  }
}
