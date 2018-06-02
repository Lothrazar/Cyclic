package com.lothrazar.cyclicmagic.item.cannon;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.proxy.ClientProxy;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityGolemLaser extends Entity {

  public static enum VariantColors {
    RED, ORANGE, YELLOW, GREEN, BLUE, PURPLE;

    public String nameLower() {
      return this.name().toLowerCase();
    }

    /**
     * r,g,b passed into projectile shot
     * 
     * @return
     */
    public Color getColor() {
      switch (this) {
        case RED:
          return new Color(179, 3, 2);
        case ORANGE:
          return new Color(255, 64, 16);
        case YELLOW:
          return new Color(227, 225, 2);
        case GREEN:
          return new Color(57, 255, 56);
        case BLUE:
          return new Color(0, 173, 255);
        case PURPLE:
          return new Color(255, 56, 249);
        default:
        break;
      }
      return null;//new Color(0, 0, 0);
    }
  }

  public static final DataParameter<Float> value = EntityDataManager.createKey(EntityGolemLaser.class, DataSerializers.FLOAT);
  public static final DataParameter<Boolean> dead = EntityDataManager.createKey(EntityGolemLaser.class, DataSerializers.BOOLEAN);
  public static final DataParameter<Integer> lifetime = EntityDataManager.createKey(EntityGolemLaser.class, DataSerializers.VARINT);
  public static final DataParameter<Integer> variant = EntityDataManager.<Integer> createKey(EntityGolemLaser.class, DataSerializers.VARINT);
  public static final String NAME = "firecannon";
  public BlockPos dest = new BlockPos(0, 0, 0);
  public UUID id = null;
  BlockPos pos = new BlockPos(0, 0, 0);

  public EntityGolemLaser(World worldIn) {
    super(worldIn);
    this.setInvisible(true);
    this.getDataManager().register(variant, Integer.valueOf(0));
    this.getDataManager().register(value, Float.valueOf(0));
    this.getDataManager().register(dead, false);
    this.getDataManager().register(lifetime, Integer.valueOf(160));
  }

  public void initCustom(double x, double y, double z, double vx, double vy, double vz, double value, UUID playerId) {
    this.posX = x;
    this.posY = y;
    this.posZ = z;
    this.motionX = vx;
    this.motionY = vy;
    this.motionZ = vz;
    // setSize((float) value / 10.0f, (float) value / 10.0f);
    getDataManager().set(EntityGolemLaser.value, (float) value);
    getDataManager().setDirty(EntityGolemLaser.value);
    setSize((float) value / 10.0f, (float) value / 10.0f);
    this.id = playerId;
  }

  @Override
  protected void entityInit() {}

  @Override
  protected void readEntityFromNBT(NBTTagCompound compound) {
    getDataManager().set(EntityGolemLaser.value, compound.getFloat("value"));
    getDataManager().setDirty(EntityGolemLaser.value);
    if (compound.hasKey("UUIDmost")) {
      id = new UUID(compound.getLong("UUIDmost"), compound.getLong("UUIDleast"));
    }
  }

  @Override
  protected void writeEntityToNBT(NBTTagCompound compound) {
    compound.setFloat("value", getDataManager().get(value));
    if (id != null) {
      compound.setLong("UUIDmost", id.getMostSignificantBits());
      compound.setLong("UUIDleast", id.getLeastSignificantBits());
    }
  }

  //  @Override
  //  protected void onImpact(RayTraceResult mop) {
  //    
  //    
  //  }
  @Override
  public void onUpdate() {
    super.onUpdate();
    if (!getEntityWorld().isRemote && getDataManager().get(lifetime) > 18 && getDataManager().get(dead)) {
      ModCyclic.network.sendToAll(new MessageGolemLaserFX(posX, posY, posZ, getDataManager().get(value) / 1.75f, this.getRed(), this.getGreen(), this.getBlue()));
    }
    getDataManager().set(lifetime, getDataManager().get(lifetime) - 1);
    getDataManager().setDirty(lifetime);
    if (getDataManager().get(lifetime) <= 0) {
      getEntityWorld().removeEntity(this);
      this.setDead();
    }
    if (!getDataManager().get(dead)) {
      Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);
      Vec3d vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
      RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d, vec3d1);
      if (raytraceresult != null) {
        if (raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK && this.world.getBlockState(raytraceresult.getBlockPos()).getBlock() == Blocks.PORTAL) {
          this.setPortal(raytraceresult.getBlockPos());
        }
        else {
          //              if(!net.minecraftforge.common.ForgeHooks.onThrowableImpact(this, raytraceresult))
          //             EmberRootZoo.log("LASER IMPACT YOOOO");
          getEntityWorld().removeEntity(this);
          this.setDead();
          return;
        }
      }
      getDataManager().set(value, getDataManager().get(value) - 0.025f);
      if (getDataManager().get(value) <= 0) {
        getEntityWorld().removeEntity(this);
      }
      posX += motionX;
      posY += motionY;
      posZ += motionZ;
      IBlockState state = getEntityWorld().getBlockState(getPosition());
      if (state.isFullCube() && state.isOpaqueCube()) {
        if (!getEntityWorld().isRemote) {
          ModCyclic.network.sendToAll(new MessageGolemLaserFX(posX, posY, posZ, getDataManager().get(value) / 1.75f, this.getRed(), this.getGreen(), this.getBlue()));
          getDataManager().set(lifetime, 20);
          getDataManager().setDirty(lifetime);
          this.getDataManager().set(dead, true);
          getDataManager().setDirty(dead);
        }
      }
      if (getEntityWorld().isRemote) {
        for (double i = 0; i < 9; i++) {
          double coeff = i / 9.0;
          spawnParticleGlow(getEntityWorld(),
              (float) (prevPosX + (posX - prevPosX) * coeff), (float) (prevPosY + (posY - prevPosY) * coeff), (float) (prevPosZ + (posZ - prevPosZ) * coeff),
              0.0125f * (rand.nextFloat() - 0.5f), 0.0125f * (rand.nextFloat() - 0.5f), 0.0125f * (rand.nextFloat() - 0.5f),
              getRed(), getGreen(), getBlue(),
              getDataManager().get(value) / 1.75f, 24);
        }
      }
      List<EntityLivingBase> rawEntities = getEntityWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(posX - getDataManager().get(value) * 0.125, posY - getDataManager().get(value) * 0.125, posZ - getDataManager().get(value) * 0.125, posX + getDataManager().get(value) * 0.125, posY + getDataManager().get(value) * 0.125, posZ + getDataManager().get(value) * 0.125));
      ArrayList<EntityLivingBase> entities = new ArrayList<EntityLivingBase>();
      for (int j = 0; j < rawEntities.size(); j++) {
        if (id != null) {
          if (rawEntities.get(j).getUniqueID().compareTo(id) != 0) {
            entities.add(rawEntities.get(j));
          }
        }
      }
      if (entities.size() > 0) {
        for (EntityLivingBase target : entities) {
          DamageSource source = DamageSource.MAGIC;
          if (getEntityWorld().getPlayerEntityByUUID(id) != null) {
            EntityPlayer player = getEntityWorld().getPlayerEntityByUUID(id);
            source = DamageSource.causePlayerDamage(player);
            target.setFire(1);
            target.attackEntityFrom(source, getDataManager().get(value));
            target.setLastAttackedEntity(player);
            target.setRevengeTarget(player);
            target.knockBack(this, 0.5f, -motionX, -motionZ);
          }
          else {
            target.attackEntityFrom(source, getDataManager().get(value));
          }
          if (!getEntityWorld().isRemote) {
            ModCyclic.network.sendToAll(new MessageGolemLaserFX(posX, posY, posZ,
                getDataManager().get(value) / 1.75f, this.getRed(), this.getGreen(), this.getBlue()));
            getDataManager().set(lifetime, 20);
            getDataManager().setDirty(lifetime);
            this.getDataManager().set(dead, true);
            getDataManager().setDirty(dead);
          }
        }
      }
    }
    else {
      motionX = 0;
      motionY = 0;
      motionZ = 0;
    }
  }

  private Color getColor() {
    VariantColors var = VariantColors.values()[getDataManager().get(variant)];
    return var.getColor();
  }

  private int getBlue() {
    return getColor().getBlue();
  }

  private int getGreen() {
    return getColor().getGreen();
  }

  private int getRed() {
    return getColor().getRed();
  }

  private static Random random = new Random();
  private static int counter = 0;

  @SideOnly(Side.CLIENT)
  private static void spawnParticleGlow(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float scale, int lifetime) {
    counter += random.nextInt(3);
    if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
      ClientProxy.particleRenderer.addParticle(new ParticleGolemLaser(world, x, y, z, vx, vy, vz, r, g, b, 1.0f, scale, lifetime));
    }
  }
}
