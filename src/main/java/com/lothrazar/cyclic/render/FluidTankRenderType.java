package com.lothrazar.cyclic.render;

import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Source from MIT open source https://github.com/mekanism/Mekanism/tree/1.15x
 * 
 * https://github.com/mekanism/Mekanism/blob/1.15x/LICENSE
 * 
 */
public class FluidTankRenderType extends RenderType {
// AlphaStateShard
  private static final TransparencyStateShard ALPHA =   RenderStateShard.TRANSLUCENT_TRANSPARENCY;//.AlphaStateShard(0.1F);

  private FluidTankRenderType(String nameIn, VertexFormat formatIn, int drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
    super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
  }

  public static RenderType resizableCuboid() {
    RenderType.CompositeState.CompositeStateBuilder stateBuilder = preset(InventoryMenu.BLOCK_ATLAS).setAlphaState(ALPHA);
    return create("resizable_cuboid", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, GL11.GL_QUADS, 256, true, false,
        stateBuilder.createCompositeState(true));
  }

  private static RenderType.CompositeState.CompositeStateBuilder preset(ResourceLocation resourceLocation) {
    return RenderType.CompositeState.builder()
        .setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, false))
        .setCullState(CULL)
        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
//        .setShadeModelState(SMOOTH_SHADE)
        ;
  }
}
