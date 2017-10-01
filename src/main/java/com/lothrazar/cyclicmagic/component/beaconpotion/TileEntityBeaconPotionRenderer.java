package com.lothrazar.cyclicmagic.component.beaconpotion;
import java.util.List;
import com.lothrazar.cyclicmagic.block.base.BaseMachineTESR;
import com.lothrazar.cyclicmagic.block.base.BaseTESR;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityBeaconPotionRenderer extends BaseMachineTESR<TileEntityBeaconPotion> {
  public static final ResourceLocation TEXTURE_BEACON_BEAM = TileEntityBeaconRenderer.TEXTURE_BEACON_BEAM;// new ResourceLocation("textures/entity/beacon_beam.png");
  public TileEntityBeaconPotionRenderer(){
    super(0);
  }
  @Override
  public void render(TileEntityBeaconPotion te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
    this.renderBeacon(x, y, z, (double) partialTicks, (double) te.shouldBeamRender(), te.getBeamSegments(),
        (double) te.getWorld().getTotalWorldTime());
  
 
  }
  public void renderBeacon(double x, double y, double z, double partialTicks, double textureScale, List<TileEntityBeaconPotion.BeamSegment> beamSegments, double totalWorldTime) {
    GlStateManager.alphaFunc(516, 0.1F);
    this.bindTexture(TEXTURE_BEACON_BEAM);
    if (textureScale > 0.0D) {
      GlStateManager.disableFog();
      int i = 0;
      for (int j = 0; j < beamSegments.size(); ++j) {
        TileEntityBeaconPotion.BeamSegment tileentitybeacon$beamsegment = beamSegments.get(j);
        TileEntityBeaconRenderer.renderBeamSegment(x, y, z, partialTicks, textureScale, totalWorldTime, i, tileentitybeacon$beamsegment.getHeight(), tileentitybeacon$beamsegment.getColors());
        i += tileentitybeacon$beamsegment.getHeight();
      }
      GlStateManager.enableFog();
    }
  }
  public boolean isGlobalRenderer(TileEntityBeaconPotion te) {
    return true;
  }
  @Override
  public void renderBasic(TileEntityBaseMachineInvo te) {
    
//    ItemStack stack = te.getStackInSlot(this.itemSlotAbove);
//    if (!stack.isEmpty()) {
//      renderItem(te, stack,0.1f, 0.5f,0.1f);
//    }
  }
}