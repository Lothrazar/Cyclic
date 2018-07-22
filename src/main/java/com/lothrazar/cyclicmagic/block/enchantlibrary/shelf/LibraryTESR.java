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
package com.lothrazar.cyclicmagic.block.enchantlibrary.shelf;

import com.lothrazar.cyclicmagic.block.enchantlibrary.EnchantStack;
import com.lothrazar.cyclicmagic.block.enchantlibrary.QuadrantEnum;
import com.lothrazar.cyclicmagic.core.block.BaseTESR;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class LibraryTESR<T extends TileEntityLibrary> extends BaseTESR<T> {

  private static final int TEXTCOLOR = 0;
  //TODO: nameplage ugly when block on top. also redundant. revive one day??
  boolean doNameplate = false;
  final float horizDistFromCenter = 0.46F;
  final float leftColumn = 1.6F, rightColumn = 2.08F;
  final float topRow = -0.9F, bottomRow = -1.4125F;
  final float vOffset = -0.11F;

  public LibraryTESR(Block block) {
    super(block);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void render(TileEntityLibrary te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
    EnumFacing face = te.getCurrentFacing();
    //    for (EnumFacing face : EnumFacing.HORIZONTALS) {
    //TODO: loop on storage length. FOR NOW we have fixed size of 4 so do this
    renderEnchantStack(te, te.getEnchantStack(QuadrantEnum.TL), QuadrantEnum.TL, face, x, y, z, destroyStage, leftColumn, topRow, horizDistFromCenter);
    renderEnchantStack(te, te.getEnchantStack(QuadrantEnum.TR), QuadrantEnum.TR, face, x, y, z, destroyStage, rightColumn, topRow, horizDistFromCenter);
    renderEnchantStack(te, te.getEnchantStack(QuadrantEnum.BL), QuadrantEnum.BL, face, x, y, z, destroyStage, leftColumn, bottomRow, horizDistFromCenter);
    renderEnchantStack(te, te.getEnchantStack(QuadrantEnum.BR), QuadrantEnum.BR, face, x, y, z, destroyStage, rightColumn, bottomRow, horizDistFromCenter);
    if (doNameplate && te.getLastClicked() != null && te.getEnchantStack(te.lastClicked).isEmpty() == false) {
      //TODO: we could xyz offset in different ways too
      this.drawNameplate((T) te, te.getEnchantStack(te.lastClicked).toString(), x, y, z, 50);
    }
    //    } 
  }

  private void renderStack(TileEntityLibrary te, EnchantStack stack, EnumFacing face, QuadrantEnum quad, double x, double y, double z) {
    float scaleFactor = 0.045F;
    int angle = angleOfFace(face);
    ItemStack s = stack.getRenderIcon();
    GlStateManager.pushAttrib();
    GlStateManager.pushMatrix();
    // Translate to the location of our tile entity
    GlStateManager.translate(x, y, z);
    GlStateManager.disableRescaleNormal();
    float startX = 0, startY = 0;
    switch (quad) {
      case TL:
        startX = 0.089F;
        startY = 0.956F;
      break;
      case TR:
        startX = 0.6F;
        startY = 0.956F;
      break;
      case BL:
        startX = 0.089F;
        startY = 0.45F;
      break;
      case BR:
        startX = 0.6F;
        startY = 0.45F;
      break;
    }
    float bookY = startY, bookX = startX;
    for (int i = 0; i < stack.getCount(); i++) {
      bookX += scaleFactor;
      if (i % 8 == 0) {
        bookX = startX;
        bookY -= scaleFactor;
      }
      renderItem(te, s, bookX, bookY, 1F, angle, false, scaleFactor);
    }
    GlStateManager.popMatrix();
    GlStateManager.popAttrib();
  }

  private void renderEnchantStack(TileEntityLibrary te, EnchantStack stack, QuadrantEnum quad, EnumFacing face, double x, double y, double z, int destroyStage, float xt, float yt, float zt) {
    if (stack.isEmpty()) {
      return;
    }
    if (te.displaysText()) {
      int angle = angleOfFace(face);
      renderTextAt(stack.shortName(), x, y, z, destroyStage, xt - 1.5F, yt + 0.8F, zt - 0.44F, angle, TEXTCOLOR);
      if (stack.isEmpty() == false) {
        renderTextAt(stack.levelName(), x, y, z, destroyStage, xt - 1.5F, yt + 0.8F + vOffset, zt - 0.44F, angle, TEXTCOLOR);
        renderTextAt(stack.countName(), x, y, z, destroyStage, xt - 1.5F, yt + 0.8F + 2 * vOffset, zt - 0.44F, angle, TEXTCOLOR);
      }
    }
    else {
      renderStack(te, stack, face, quad, x, y, z);
    }
  }
}
