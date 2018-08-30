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
package com.lothrazar.cyclicmagic.item.mobcapture;

import com.lothrazar.cyclicmagic.entity.EntityThrowableDispensable;
import com.lothrazar.cyclicmagic.entity.RenderBall;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilSound;
import com.lothrazar.cyclicmagic.util.UtilString;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class EntityMagicNetEmpty extends EntityThrowableDispensable {

  public static Item renderSnowball;
  public static NonNullList<String> blacklistIds;

  public static class FactoryBallEmpty implements IRenderFactory<EntityMagicNetEmpty> {

    @Override
    public Render<? super EntityMagicNetEmpty> createRenderFor(RenderManager rm) {
      return new RenderBall<EntityMagicNetEmpty>(rm, "net");
    }
  }

  public EntityMagicNetEmpty(World worldIn) {
    super(worldIn);
  }

  public EntityMagicNetEmpty(World worldIn, EntityLivingBase ent) {
    super(worldIn, ent);
  }

  public EntityMagicNetEmpty(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }

  private boolean isInBlacklist(Entity thing) {
    ResourceLocation test = UtilEntity.getResourceLocation(thing);
    return UtilString.isInList(blacklistIds, test);
  }

  @Override
  protected void processImpact(RayTraceResult mop) {
    if (mop.entityHit != null
        && mop.entityHit instanceof EntityLivingBase
        && (mop.entityHit instanceof EntityPlayer) == false
        && isInBlacklist(mop.entityHit) == false) {
      ItemStack captured = new ItemStack(renderSnowball);
      NBTTagCompound entity = new NBTTagCompound();
      mop.entityHit.writeToNBT(entity);
      //id is the special magic tag thats used by EntityList to respawn it. see EntityList.createEntityFromNBT
      entity.setString(ItemProjectileMagicNet.NBT_ENTITYID, EntityList.getKey(mop.entityHit.getClass()).toString()); // was getEntityStringFromClass
      entity.setString("tooltip", mop.entityHit.getName());
      captured.setTagCompound(entity);
      mop.entityHit.setDead();
      UtilItemStack.dropItemStackInWorld(this.getEntityWorld(), this.getPosition(), captured);
      UtilSound.playSound((EntityLivingBase) mop.entityHit, SoundRegistry.monster_ball_capture);
    }
    else {
      UtilItemStack.dropItemStackInWorld(this.getEntityWorld(), this.getPosition(), renderSnowball);
    }
    this.setDead();
  }
}
