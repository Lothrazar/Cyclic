package com.lothrazar.cyclic.render;

import com.lothrazar.cyclic.ModCyclic;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.inventory.InventoryMenu;

/**
 * Source from MIT open source https://github.com/mekanism/Mekanism/tree/1.15x
 * <p>
 * https://github.com/mekanism/Mekanism/blob/1.15x/LICENSE
 */
public class FluidTankRenderType extends RenderType {

  private FluidTankRenderType(String nameIn, VertexFormat formatIn, VertexFormat.Mode drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
    super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
  }

  public static final RenderType RESIZABLE = create(ModCyclic.MODID + ":resizable_cuboid", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, true, false,
      RenderType.CompositeState.builder()
          .setShaderState(RENDERTYPE_CUTOUT_SHADER)
          .setTextureState(new RenderStateShard.TextureStateShard(InventoryMenu.BLOCK_ATLAS, false, false))
          .setCullState(CULL)
          .setLightmapState(LIGHTMAP)
          .setWriteMaskState(COLOR_WRITE)
          .setLightmapState(LIGHTMAP)
          //          .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
          .createCompositeState(true));
}
