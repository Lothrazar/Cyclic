/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.block.beaconpotion;

import java.util.List;
import com.lothrazar.cyclicmagic.core.block.BaseMachineTESR;
import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineInvo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityBeaconPotionRenderer extends BaseMachineTESR<TileEntityBeaconPotion> {

  public static final ResourceLocation TEXTURE_BEACON_BEAM = TileEntityBeaconRenderer.TEXTURE_BEACON_BEAM;// new ResourceLocation("textures/entity/beacon_beam.png");

  public TileEntityBeaconPotionRenderer() {
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
