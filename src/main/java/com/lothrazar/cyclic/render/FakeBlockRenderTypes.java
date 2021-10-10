package com.lothrazar.cyclic.render;

import java.util.OptionalDouble;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

/**
 * Render Types with help from direwolf20 MIT open source project https://github.com/Direwolf20-MC/BuildingGadgets/blob/1.15/LICENSE.md
 */
public class FakeBlockRenderTypes extends RenderType {

  public FakeBlockRenderTypes(String nameIn, VertexFormat formatIn, VertexFormat.Mode drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
    super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
  }

  /**
   * laser rendering from this MIT project https://github.com/Direwolf20-MC/DireGoo2/blob/master/LICENSE.md
   * <p>
   * 1.17 BOOLS are this.affectsCrumbling = p_173182_; this.sortOnUpload = p_173183_;
   * 
   * Used by laser and wireless redstone TESR blocks
   */
  public static final RenderType LASER_MAIN_BEAM = create("mininglasermainbeam",
      DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256,
      false, false, // affectsCrumbling, sortOnUpload
      RenderType.CompositeState.builder()
          .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_SHADER) //1.17 new
          .setLayeringState(VIEW_OFFSET_Z_LAYERING)
          .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
          .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
          .setCullState(NO_CULL)
          .setLightmapState(NO_LIGHTMAP)
          .setWriteMaskState(COLOR_WRITE)
          .createCompositeState(false));
  /**
   * used by TESR that render blocks with textures Shape builder, ghost muffler, render light camo.
   */
  public static final RenderType FAKE_BLOCK = create("fakeblock",
      DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 256, false, false, // affectsCrumbling, sortOnUpload
      RenderType.CompositeState.builder()
          .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_SHADER) //1.17 new - maybe BLOCK_SHADER
          //          .setShadeModelState(SMOOTH_SHADE)
          .setLightmapState(LIGHTMAP)
          .setTextureState(BLOCK_SHEET_MIPPED)
          //          .layer(PROJECTION_LAYERING)
          .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
          .setDepthTestState(LEQUAL_DEPTH_TEST)
          .setCullState(NO_CULL)
          .setWriteMaskState(COLOR_DEPTH_WRITE)
          .createCompositeState(false));
  /**
   * used by EventRender -> RenderWorldLastEvent by most held items that pick locations, such as cyclic:location_data
   */
  public static final RenderType TRANSPARENT_COLOUR = create("transparentcolour",
      DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, false, // affectsCrumbling, sortOnUpload
      RenderType.CompositeState.builder()
          .setShaderState(BLOCK_SHADER) //1.17 new
          .setTransparencyState(ADDITIVE_TRANSPARENCY)
          .setTextureState(NO_TEXTURE)
          .setDepthTestState(NO_DEPTH_TEST)
          .setCullState(CULL)
          .setLightmapState(NO_LIGHTMAP)
          .setWriteMaskState(COLOR_DEPTH_WRITE)
          .createCompositeState(false));
  /**
   * used by most blocks that select blocks such as cyclic:forester, cyclic:harvester, cyclic:miner in TESRs
   */
  public static final RenderType SOLID_COLOUR = create("solidcolour",
      DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, false, // affectsCrumbling, sortOnUpload
      RenderType.CompositeState.builder()
          .setShaderState(RenderStateShard.ShaderStateShard.RENDERTYPE_LINES_SHADER) //1.17 new 
          .setTransparencyState(ADDITIVE_TRANSPARENCY)
          .setTextureState(NO_TEXTURE)
          .setDepthTestState(NO_DEPTH_TEST)
          .setCullState(CULL)
          .setLightmapState(NO_LIGHTMAP)
          .setWriteMaskState(COLOR_DEPTH_WRITE)
          .createCompositeState(false));
  /**
   * Used by cyclic:prospector
   */
  public static final RenderType TOMB_LINES = create("tomb_lines",
      DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.LINES, 256, false, false,
      RenderType.CompositeState.builder()
          .setShaderState(RENDERTYPE_LINES_SHADER)
          .setLineState(new LineStateShard(OptionalDouble.of(2.5D)))
          .setLayeringState(VIEW_OFFSET_Z_LAYERING)
          .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
          .setOutputState(ITEM_ENTITY_TARGET)
          .setWriteMaskState(COLOR_DEPTH_WRITE)
          .setCullState(NO_CULL)
          .setDepthTestState(NO_DEPTH_TEST)
          .createCompositeState(false));
}
