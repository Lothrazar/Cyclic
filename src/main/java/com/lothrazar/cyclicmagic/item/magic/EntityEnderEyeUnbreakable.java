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
package com.lothrazar.cyclicmagic.item.magic;

import java.lang.reflect.Field;
import com.lothrazar.cyclicmagic.core.entity.RenderBall;
import com.lothrazar.cyclicmagic.util.UtilReflection;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class EntityEnderEyeUnbreakable extends EntityEnderEye {

  public static class FactoryMissile implements IRenderFactory<EntityEnderEyeUnbreakable> {

    @Override
    public Render<? super EntityEnderEyeUnbreakable> createRenderFor(RenderManager rm) {
      return new RenderBall<EntityEnderEyeUnbreakable>(rm, "ender_eye_orb");
    }
  }

  public EntityEnderEyeUnbreakable(World worldIn) {
    super(worldIn);
    this.setSize(0.25F, 0.25F);
  }

  public EntityEnderEyeUnbreakable(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }

  public void moveTowards(BlockPos pos) {
    super.moveTowards(pos);
    this.shatterOff();
  }

  /**
   * Called to update the entity's position/logic.
   */
  @Override
  public void onUpdate() {
    this.shatterOff();
    super.onUpdate();
  }

  private void shatterOff() {
    Field f = UtilReflection.getPrivateField("shatterOrDrop", "field_70221_f", EntityEnderEye.class);
    try {
      f.set(this, false);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
