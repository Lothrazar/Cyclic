package com.lothrazar.cyclic.block.tank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.render.CuboidRenderType;
import com.lothrazar.cyclic.render.FluidRenderMap;
import com.lothrazar.cyclic.render.FluidRenderMap.FluidType;
import com.lothrazar.cyclic.render.Model3D;
import com.lothrazar.cyclic.render.RenderHelper;
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
    IVertexBuilder buffer = renderer.getBuffer(CuboidRenderType.resizableCuboid());
    matrix.scale(1F, getScale(tankHere.tank), 1F);
    RenderHelper.renderObject(getFluidModel(fluid, stages - 1), matrix, buffer, RenderHelper.getColorARGB(fluid, 0.1F),
        RenderHelper.calculateGlowLight(light, fluid));
  }

  public static float getScale(FluidTank tank) {
    return getScale(tank.getFluidAmount(), tank.getCapacity(), tank.isEmpty());
  }

  public static float getScale(int stored, int capacity, boolean empty) {
    float targetScale = (float) stored / capacity;
    if (targetScale < 0.1) {
      targetScale = 0.1F;
    }
    return targetScale;
  }

  public static void renderObject(@Nullable Model3D object, @Nonnull MatrixStack matrix, IVertexBuilder buffer, int argb, int light) {
    if (object != null) {
      RenderResizableCuboid.INSTANCE.renderCube(object, matrix, buffer, argb, light);
    }
  }

  private static final FluidRenderMap<Int2ObjectMap<Model3D>> cachedCenterFluids = new FluidRenderMap<>();
  private static final FluidRenderMap<Int2ObjectMap<Model3D>> cachedValveFluids = new FluidRenderMap<>();
  private static int stages = 1400;

  private Model3D getFluidModel(@Nonnull FluidStack fluid, int stage) {
    if (cachedCenterFluids.containsKey(fluid) && cachedCenterFluids.get(fluid).containsKey(stage)) {
      return cachedCenterFluids.get(fluid).get(stage);
    }
    Model3D model = new Model3D();
    model.setTexture(FluidRenderMap.getFluidTexture(fluid, FluidType.STILL));
    if (fluid.getFluid().getAttributes().getStillTexture(fluid) != null) {
      model.minX = 0.125 + .01;
      model.minY = 0.0625 + .01;
      model.minZ = 0.125 + .01;
      model.maxX = 0.875 - .01;
      model.maxY = 0.0625 + (stage / (float) stages) * 0.875 - .01;
      model.maxZ = 0.875 - .01;
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
