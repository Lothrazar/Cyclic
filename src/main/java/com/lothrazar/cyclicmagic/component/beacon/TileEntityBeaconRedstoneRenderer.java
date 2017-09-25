package com.lothrazar.cyclicmagic.component.beacon;
import java.util.List;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityBeaconRedstoneRenderer extends TileEntitySpecialRenderer<TileEntityBeaconPowered> {
  public static final ResourceLocation TEXTURE_BEACON_BEAM = TileEntityBeaconRenderer.TEXTURE_BEACON_BEAM;// new ResourceLocation("textures/entity/beacon_beam.png");
  public void render(TileEntityBeaconPowered te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
    this.renderBeacon(x, y, z, (double) partialTicks, (double) te.shouldBeamRender(), te.getBeamSegments(),
        (double) te.getWorld().getTotalWorldTime());
  }
  public void renderBeacon(double x, double y, double z, double partialTicks, double textureScale, List<TileEntityBeaconPowered.BeamSegment> beamSegments, double totalWorldTime) {
 
           GlStateManager.alphaFunc(516, 0.1F);
    this.bindTexture(TEXTURE_BEACON_BEAM);
    if (textureScale > 0.0D) {
      GlStateManager.disableFog();
      int i = 0;
      for (int j = 0; j < beamSegments.size(); ++j) {
        TileEntityBeaconPowered.BeamSegment tileentitybeacon$beamsegment = beamSegments.get(j);
 
        TileEntityBeaconRenderer.renderBeamSegment(x, y, z, partialTicks, textureScale, totalWorldTime, i, tileentitybeacon$beamsegment.getHeight(), tileentitybeacon$beamsegment.getColors());
 
        i += tileentitybeacon$beamsegment.getHeight();
      }
      GlStateManager.enableFog();
    }
  }

  public boolean isGlobalRenderer(TileEntityBeaconPowered te) {
    return true;
  }
}