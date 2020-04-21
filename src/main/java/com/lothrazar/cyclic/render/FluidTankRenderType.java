package com.lothrazar.cyclic.render;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;

/**
 * Source from MIT open source https://github.com/mekanism/Mekanism/tree/1.15x
 * 
 * https://github.com/mekanism/Mekanism/blob/1.15x/LICENSE
 * 
 */
public class FluidTankRenderType extends RenderType {

  private static final AlphaState ALPHA = new RenderState.AlphaState(0.1F);

  private FluidTankRenderType(String nameIn, VertexFormat formatIn, int drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
    super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
  }

  private static RenderType.State.Builder preset(ResourceLocation resourceLocation) {
    return RenderType.State.getBuilder()
        .texture(new RenderState.TextureState(resourceLocation, false, false))
        .cull(CULL_ENABLED)
        .transparency(TRANSLUCENT_TRANSPARENCY)
        .shadeModel(SHADE_ENABLED);
  }

  public static RenderType resizableCuboid() {
    RenderType.State.Builder stateBuilder = preset(PlayerContainer.LOCATION_BLOCKS_TEXTURE).alpha(ALPHA);
    return makeType("resizable_cuboid", DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP, GL11.GL_QUADS, 256, true, false,
        stateBuilder.build(true));
  }
}