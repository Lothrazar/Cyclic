package com.lothrazar.cyclic.render;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;

/**
 * Render Types with help from direwolf20 MIT open source project https://github.com/Direwolf20-MC/BuildingGadgets/blob/1.15/LICENSE.md
 *
 */
public class FakeBlockRenderTypes extends RenderType {

  public FakeBlockRenderTypes(String nameIn, VertexFormat formatIn, int drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
    super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
  }

  /**
   * laser rendering from this MIT project https://github.com/Direwolf20-MC/DireGoo2/blob/master/LICENSE.md
   */
  public static RenderType LASER_MAIN_BEAM = makeType("MiningLaserMainBeam",
      DefaultVertexFormats.POSITION_COLOR_TEX, GL11.GL_QUADS, 256,
      RenderType.State.getBuilder()//.texture(new TextureState(laserBeam2, false, false))
          .layer(field_239235_M_)
          .transparency(TRANSLUCENT_TRANSPARENCY)
          .depthTest(RenderState.DEPTH_LEQUAL)
          .cull(CULL_DISABLED)
          .lightmap(LIGHTMAP_DISABLED)
          .writeMask(COLOR_WRITE)
          .build(false));
  public static RenderType LASER_MAIN_CORE = makeType("MiningLaserCoreBeam",
      DefaultVertexFormats.POSITION_COLOR_TEX, GL11.GL_QUADS, 256,
      RenderType.State.getBuilder()//.texture(new TextureState(laserBeam, false, false))
          .layer(field_239235_M_)
          .transparency(TRANSLUCENT_TRANSPARENCY)
          .depthTest(DEPTH_LEQUAL)
          .cull(CULL_DISABLED)
          .lightmap(LIGHTMAP_DISABLED)
          .writeMask(COLOR_DEPTH_WRITE)
          .build(false));
  //
  //
  //
  public final static RenderType FAKE_BLOCK = makeType("fakeBlock",
      DefaultVertexFormats.BLOCK, GL11.GL_QUADS, 256,
      RenderType.State.getBuilder()
          .shadeModel(SHADE_ENABLED)
          .lightmap(LIGHTMAP_ENABLED)
          .texture(BLOCK_SHEET_MIPPED)
          //          .layer(PROJECTION_LAYERING)
          .transparency(TRANSLUCENT_TRANSPARENCY)
          .depthTest(DEPTH_LEQUAL)
          .cull(CULL_DISABLED)
          .writeMask(COLOR_DEPTH_WRITE)
          .build(false));
  public static final RenderType TRANSPARENT_COLOUR = makeType("transparentColour",
      DefaultVertexFormats.POSITION_COLOR, GL11.GL_QUADS, 256,
      RenderType.State.getBuilder()
          //          .layer(PROJECTION_LAYERING)
          .transparency(TRANSLUCENT_TRANSPARENCY)
          .texture(NO_TEXTURE)
          .depthTest(DEPTH_LEQUAL)
          .cull(CULL_ENABLED)
          .lightmap(LIGHTMAP_DISABLED)
          .writeMask(COLOR_DEPTH_WRITE)
          .build(false));
  public static final RenderType SOLID_COLOUR = makeType("solidColour",
      DefaultVertexFormats.POSITION_COLOR, GL11.GL_QUADS, 256,
      RenderType.State.getBuilder()
          //          .layer(PROJECTION_LAYERING)
          .transparency(ADDITIVE_TRANSPARENCY)
          .texture(NO_TEXTURE)
          .depthTest(DEPTH_LEQUAL)
          .cull(CULL_ENABLED)
          .lightmap(LIGHTMAP_DISABLED)
          .writeMask(COLOR_DEPTH_WRITE)
          .build(false));
}