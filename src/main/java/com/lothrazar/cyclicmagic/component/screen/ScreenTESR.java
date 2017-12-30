package com.lothrazar.cyclicmagic.component.screen;
import com.lothrazar.cyclicmagic.block.base.BaseTESR;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class ScreenTESR<T extends TileEntityScreen> extends BaseTESR<T> {
  final float horizDistFromCenter = 0.46F;
  final float leftColumn = 1.6F, rightColumn = 2.08F;
  final float topRow = -0.9F, bottomRow = -1.4125F;
  final float vOffset = -0.11F;
  public ScreenTESR(Block block) {
    super(block);
  }
  @Override
  public void render(TileEntityScreen te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
    
    float xt=leftColumn, yt=topRow, zt=horizDistFromCenter, angle=90;
    
    
    
    renderTextAt("test", x, y, z, destroyStage, xt, yt, zt, angle, 0xFF00FF);
    //    int angle = angleOfFace(face);
    //    renderTextAt(stack.shortName(), x, y, z, destroyStage, xt, yt, zt, angle);
    //    if (stack.isEmpty() == false) {
    //      renderTextAt(stack.levelName(), x, y, z, destroyStage, xt, yt + vOffset, zt, angle);
    //      renderTextAt(stack.countName(), x, y, z, destroyStage, xt, yt + 2 * vOffset, zt, angle);
    //    }
  }
}
