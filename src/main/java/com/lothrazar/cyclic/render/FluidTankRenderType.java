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
  // AlphaStateShard
  //  private static final TransparencyStateShard ALPHA = RenderStateShard.TRANSLUCENT_TRANSPARENCY;//.AlphaStateShard(0.1F);

  private FluidTankRenderType(String nameIn, VertexFormat formatIn, VertexFormat.Mode drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
    super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
  }

  public static final RenderType TYPE_MELTER = create(ModCyclic.MODID + ":resizable_cuboid", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, true, false,
      RenderType.CompositeState.builder()
          .setShaderState(RENDERTYPE_CUTOUT_SHADER) //1.17 new
          .setTextureState(new RenderStateShard.TextureStateShard(InventoryMenu.BLOCK_ATLAS, false, false))
          .setCullState(CULL)
          .setLightmapState(LIGHTMAP)
          .setWriteMaskState(COLOR_WRITE)
          .setLightmapState(LIGHTMAP)
          //          .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
          .createCompositeState(true));
  public static final RenderType TYPE_SPRINKLER = create(ModCyclic.MODID + ":resizable_cuboidsprinkler", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, true, false,
      RenderType.CompositeState.builder()
          .setShaderState(RENDERTYPE_CUTOUT_MIPPED_SHADER) //1.17 new
          .setTextureState(new RenderStateShard.TextureStateShard(InventoryMenu.BLOCK_ATLAS, false, false))
          .setLightmapState(LIGHTMAP)
          .setDepthTestState(NO_DEPTH_TEST)
          .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
          .createCompositeState(true));
  public static final RenderType TYPE_SOLIDIFIER = create(ModCyclic.MODID + ":resizable_cuboidtest", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, true, false,
      RenderType.CompositeState.builder()
          .setShaderState(RENDERTYPE_CUTOUT_MIPPED_SHADER) //1.17 new
          .setTextureState(new RenderStateShard.TextureStateShard(InventoryMenu.BLOCK_ATLAS, false, false))
          .setCullState(CULL)
          .setLightmapState(LIGHTMAP)
          .setDepthTestState(NO_DEPTH_TEST)
          .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
          .createCompositeState(true));
  public static final RenderType TYPE_TANK = create(ModCyclic.MODID + ":resizable_cuboidlol", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, true, false,
      RenderType.CompositeState.builder()
          .setShaderState(RENDERTYPE_CUTOUT_MIPPED_SHADER) //1.17 new
          .setTextureState(new RenderStateShard.TextureStateShard(InventoryMenu.BLOCK_ATLAS, false, false))
          //          .setTransparencyState(ADDITIVE_TRANSPARENCY)
          //          .setCullState(CULL)
          .setLightmapState(LIGHTMAP)
          .setDepthTestState(NO_DEPTH_TEST)
          .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
          .createCompositeState(true));
}
