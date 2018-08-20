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
package com.lothrazar.cyclicmagic.potion.effect;

import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.UtilTextureRender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class PotionBase extends Potion {

  private ResourceLocation icon;
  private boolean beneficial;

  public PotionBase(String name, boolean b, int potionColor) {
    super(false, potionColor);
    //this.applyAttributesModifiersToEntity(entityLivingBaseIn, attributeMapIn, amplifier);
    this.beneficial = b;
    this.setIcon(new ResourceLocation(Const.MODID, "textures/potions/" + name + ".png"));
    this.setPotionName("potion." + name);
  }

  @Override
  public void affectEntity(@Nullable Entity source, @Nullable Entity indirectSource, EntityLivingBase entityLivingBaseIn, int amplifier, double health) {
    super.affectEntity(source, indirectSource, entityLivingBaseIn, amplifier, health);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public boolean isBeneficial() {
    return this.beneficial;//decides top or bottom row
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void renderInventoryEffect(int x, int y, PotionEffect effect, net.minecraft.client.Minecraft mc) {
    UtilTextureRender.drawTextureSquare(getIcon(), x + 6, y + 6, Const.SQ - 2);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void renderHUDEffect(int x, int y, PotionEffect effect, net.minecraft.client.Minecraft mc, float alpha) {
    if (mc.gameSettings.showDebugInfo == false)//dont texture on top of the debug text
      UtilTextureRender.drawTextureSquare(getIcon(), x + 4, y + 4, Const.SQ - 2);
  }

  public ResourceLocation getIcon() {
    return icon;
  }

  public void setIcon(ResourceLocation icon) {
    this.icon = icon;
  }

  public void tick(EntityLivingBase entity) {}
}
