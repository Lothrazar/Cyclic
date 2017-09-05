package com.lothrazar.cyclicmagic.component.wandmissile;
import java.util.Random;
import com.lothrazar.cyclicmagic.entity.projectile.RenderBall;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;

/**
 * From what used to be roots 1 by @elucent
 * 
 *
 */
public class EntityHomingProjectile extends EntityFlying {// implements IRangedAttackMob {
  public float range = 64;
  public float addDirectionX = 0;
  public float addDirectionY = 0;
  public float twirlTimer = 0;
  public Vec3d color = new Vec3d(255, 255, 255);
  public int lifetime = 80;
  Random random = new Random();
  public EntityLivingBase target = null;
  public float damage = 2.0f;
  public EntityHomingProjectile(World worldIn) {
    super(worldIn);
    setSize(1.0f, 1.0f);
    this.setInvisible(true);
  }
  @Override
  public boolean isEntityInvulnerable(DamageSource source) {
    return false;
  }
  public void initSpecial(EntityLivingBase target, float damage, Vec3d color) {
    this.target = target;
    this.damage = damage;
    this.color = color;
  }
  @Override
  public void collideWithEntity(Entity entity) {
    if (target != null) {
      if (entity.getUniqueID().compareTo(target.getUniqueID()) == 0) {
        target.attackEntityFrom(DamageSource.GENERIC, damage);
        this.getEntityWorld().removeEntity(this);
        for (int i = 0; i < 40; i++) {
          UtilParticle.spawnParticle(world, EnumParticleTypes.CRIT_MAGIC, target.getPosition());
          //	Roots.proxy.spawnParticleMagicAuraFX(getEntityWorld(), posX, posY, posZ, Math.pow(1.15f*(random.nextFloat()-0.5f),3.0), Math.pow(1.15f*(random.nextFloat()-0.5f),3.0), Math.pow(1.15f*(random.nextFloat()-0.5f),3.0), color.xCoord, color.yCoord, color.zCoord);
        }
      }
    }
  }
  @Override
  public void updateAITasks() {
    super.updateAITasks();
  }
  @Override
  public void onUpdate() {
    super.onUpdate();
    lifetime--;
    if (lifetime == 0) {
      this.getEntityWorld().removeEntity(this);
    }
    if (target != null) {
      rotationYaw = (float) Math.toRadians(UtilEntity.yawDegreesBetweenPoints(posX, posY, posZ, target.posX, target.posY + target.getEyeHeight() / 2.0, target.posZ));
      rotationPitch = (float) Math.toRadians(UtilEntity.pitchDegreesBetweenPoints(posX, posY, posZ, target.posX, target.posY + target.getEyeHeight() / 2.0, target.posZ));
      Vec3d moveVec = UtilEntity.lookVector(this.rotationYaw, this.rotationPitch).scale(0.35f);
      this.motionX = 0.5f * motionX + 0.5f * moveVec.x;
      this.motionY = 0.5f * motionY + 0.5f * moveVec.y;
      this.motionZ = 0.5f * motionZ + 0.5f * moveVec.z;
      for (double i = 0; i < 1; i++) {
        double x = this.getEntityBoundingBox().minX * 0.5 + this.getEntityBoundingBox().maxX * 0.5;
        double y = this.getEntityBoundingBox().minY * 0.5 + this.getEntityBoundingBox().maxY * 0.5;
        double z = this.getEntityBoundingBox().minZ * 0.5 + this.getEntityBoundingBox().maxZ * 0.5;
        UtilParticle.spawnParticle(world, EnumParticleTypes.CRIT_MAGIC, x, y, z);
        //Roots.proxy.spawnParticleMagicAuraFX(getEntityWorld(), x, y, z, -0.125*moveVec.xCoord, -0.125*moveVec.yCoord, -0.125*moveVec.zCoord, color.xCoord, color.yCoord, color.zCoord);
      }
    }
    else {
      setDead();
    }
  }
  @Override
  public int getBrightnessForRender() {
    return 255;
  }
  @Override
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (this.ticksExisted > 2) {
      this.getEntityWorld().removeEntity(this);
      for (int i = 0; i < 40; i++) {
        UtilParticle.spawnParticle(world, EnumParticleTypes.CRIT_MAGIC, this.getPosition());
        //	Roots.proxy.spawnParticleMagicAuraFX(getEntityWorld(), posX, posY+height/2.0f, posZ, Math.pow(0.95f*(random.nextFloat()-0.5f),3.0), Math.pow(0.95f*(random.nextFloat()-0.5f),3.0), Math.pow(0.95f*(random.nextFloat()-0.5f),3.0), color.xCoord, color.yCoord, color.zCoord);
      }
    }
    return false;
  }
  @Override
  public boolean isAIDisabled() {
    return false;
  }
  @Override
  protected boolean canDespawn() {
    return true;
  }
  @Override
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1.0);
    this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.25D);
    this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(2.0D);
    this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
    this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0);
  }
  @Override
  public void onLivingUpdate() {
    super.onLivingUpdate();
  }
  @Override
  public void readEntityFromNBT(NBTTagCompound compound) {
    super.readEntityFromNBT(compound);
    this.getEntityWorld().removeEntity(this);
  }
 
  public static class FactoryMissile implements IRenderFactory<EntityHomingProjectile> {
    @Override
    public Render<? super EntityHomingProjectile> createRenderFor(RenderManager rm) {
      return new RenderBall<EntityHomingProjectile>(rm, "magic_missile");
    }
  }
}