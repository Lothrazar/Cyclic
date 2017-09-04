package com.lothrazar.cyclicmagic.entity.projectile;
import com.lothrazar.cyclicmagic.item.ItemProjectileMagicNet;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class EntityMagicNetEmpty extends EntityThrowableDispensable {
  public static Item renderSnowball;
  public static final FactoryBallEmpty FACTORY_BALLEMPTY = new FactoryBallEmpty();
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
  @Override
  protected void processImpact(RayTraceResult mop) {
    if (mop.entityHit != null && mop.entityHit instanceof EntityLivingBase && (mop.entityHit instanceof EntityPlayer) == false) {
      ItemStack captured = new ItemStack(renderSnowball);
      NBTTagCompound entity = new NBTTagCompound();
      mop.entityHit.writeToNBT(entity);
      //id is the special magic tag thats used by EntityList to respawn it. see EntityList.createEntityFromNBT
      entity.setString(ItemProjectileMagicNet.NBT_ENTITYID, EntityList.getKey(mop.entityHit.getClass()).toString()); // was getEntityStringFromClass
      entity.setString("tooltip", mop.entityHit.getName());
      captured.setTagCompound(entity);
      mop.entityHit.setDead();
      UtilItemStack.dropItemStackInWorld(this.getEntityWorld(), this.getPosition(), captured);
      UtilSound.playSound((EntityLivingBase) mop.entityHit, SoundRegistry.pew);
      //      UtilSound.playSound((EntityLivingBase)mop.entityHit, SoundRegistry.pow);
    }
    else {
      UtilItemStack.dropItemStackInWorld(this.getEntityWorld(), this.getPosition(), renderSnowball);
    }
    this.setDead();
  }
}