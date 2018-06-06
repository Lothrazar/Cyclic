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
package com.lothrazar.cyclicmagic.core.entity;

import com.lothrazar.cyclicmagic.item.dynamite.EntityDynamiteBlockSafe;
import com.lothrazar.cyclicmagic.item.dynamite.EntityDynamiteMining;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderProjectile<T extends Entity> extends RenderSnowball<T> {

  public RenderProjectile(RenderManager renderManagerIn, Item itemIn, RenderItem itemRendererIn) {
    super(renderManagerIn, itemIn, itemRendererIn);
  }

  public static class FactoryDynMining implements IRenderFactory<EntityDynamiteMining> {

    @Override
    public Render<? super EntityDynamiteMining> createRenderFor(RenderManager rm) {
      return new RenderProjectile<EntityDynamiteMining>(rm, EntityDynamiteMining.renderSnowball, Minecraft.getMinecraft().getRenderItem());
    }
  }

  public static class FactoryDynSafe implements IRenderFactory<EntityDynamiteBlockSafe> {

    @Override
    public Render<? super EntityDynamiteBlockSafe> createRenderFor(RenderManager rm) {
      return new RenderProjectile<EntityDynamiteBlockSafe>(rm, EntityDynamiteBlockSafe.renderSnowball, Minecraft.getMinecraft().getRenderItem());
    }
  }
}
