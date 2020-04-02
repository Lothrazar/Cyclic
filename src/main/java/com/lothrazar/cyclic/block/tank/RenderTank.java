package com.lothrazar.cyclic.block.tank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.data.Model3D;
import com.lothrazar.cyclic.render.FluidTankRenderType;
import com.lothrazar.cyclic.render.FluidRenderMap;
import com.lothrazar.cyclic.render.FluidRenderMap.FluidType;
import com.lothrazar.cyclic.util.UtilRender;
import com.lothrazar.cyclic.render.RenderResizableCuboid;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

@OnlyIn(Dist.CLIENT)
public class RenderTank extends TileEntityRenderer<TileTank> {

  public static boolean ENABLED = true;

  public RenderTank(TileEntityRendererDispatcher d) {
    super(d);
  }

  @Override
  public void render(TileTank tankHere, float v, MatrixStack matrix,
      IRenderTypeBuffer renderer, int light, int overlayLight) {
    if (!ENABLED) {
      return;
    }
    IFluidHandler handler = tankHere.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).orElse(null);
    if (handler == null || handler.getFluidInTank(0) == null) {
      return;
    }
    FluidStack fluid = handler.getFluidInTank(0);
    if (fluid.isEmpty()) {
      return;
    }
    IVertexBuilder buffer = renderer.getBuffer(FluidTankRenderType.resizableCuboid());
    matrix.scale(1F, getScale(tankHere.tank), 1F);
    UtilRender.renderObject(getFluidModel(fluid, stages - 1), matrix, buffer, UtilRender.getColorARGB(fluid, 0.1F),
        UtilRender.calculateGlowLight(light, fluid));
  }

  public static float getScale(FluidTank tank) {
    return getScale(tank.getFluidAmount(), tank.getCapacity(), tank.isEmpty());
  }

  public static float getScale(int stored, int capacity, boolean empty) {
    float targetScale = (float) stored / capacity;
    return targetScale;
  }

  public static void renderObject(@Nullable Model3D object, @Nonnull MatrixStack matrix, IVertexBuilder buffer, int argb, int light) {
    if (object != null) {
      RenderResizableCuboid.INSTANCE.renderCube(object, matrix, buffer, argb, light);
    }
  }

  private static final FluidRenderMap<Int2ObjectMap<Model3D>> cachedCenterFluids = new FluidRenderMap<>();
  private static final int stages = 1400;

  private Model3D getFluidModel(@Nonnull FluidStack fluid, int stage) {
    if (cachedCenterFluids.containsKey(fluid) && cachedCenterFluids.get(fluid).containsKey(stage)) {
      return cachedCenterFluids.get(fluid).get(stage);
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
    if (cachedCenterFluids.containsKey(fluid)) {
      cachedCenterFluids.get(fluid).put(stage, model);
    }
    else {
      Int2ObjectMap<Model3D> map = new Int2ObjectOpenHashMap<>();
      map.put(stage, model);
      cachedCenterFluids.put(fluid, map);
    }
    return model;
  }
}
