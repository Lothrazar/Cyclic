package com.lothrazar.cyclicmagic.item.crashtestdummy;

import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.net.PacketEntitySyncToClient;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
//import teamroots.emberroot.Const;
import net.minecraft.world.WorldServer;

public class EntityRobot extends EntityCreature {

  public static final String NAME = "robot";
  // public static ConfigSpawnEntity config = new ConfigSpawnEntity(EntityFallenHero.class, EnumCreatureType.CREATURE);
  public static boolean avoidCreepers = true;
  public static boolean temptWithGold = true;
  //TODO CONFIG
  public static boolean renderDebugHitboxes = true;
  private int timer = 0;
  private String message;

  public EntityRobot(World worldIn) {
    super(worldIn);
    this.experienceValue = 0;
  }

  @Override
  public boolean attackEntityFrom(DamageSource source, float amount) {
    boolean success = super.attackEntityFrom(source, amount);
    if (success && source.getTrueSource() instanceof EntityPlayer) {

      String m = amount + "  [ " + this.getHealth() + " / " + this.getMaxHealth() + " ]";
      // UtilChat.addChatMessage(((EntityPlayer) source.getTrueSource()), m);
      setMessage(m);
      //arg server only
      EntityTracker et = ((WorldServer) this.world).getEntityTracker();
      et.sendToTracking(this,
          ModCyclic.network.getPacketFrom(new PacketEntitySyncToClient(
              this.getEntityId(), amount + "")));

    }
    return success;
  }

  @Override
  protected void initEntityAI() {
    super.initEntityAI();
    this.tasks.addTask(3, new EntityAITempt(this, 1.25D, Items.GOLD_INGOT, false));
    this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setInteger("tmr", getTimer());
    compound.setString("strMsg", getMessage());
    return super.writeToNBT(compound);
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    setTimer(compound.getInteger("tmr"));
    setMessage(compound.getString("strMsg"));
  }

  @Override
  public void onLivingUpdate() {
    // this.updateArmSwingProgress();
    //    float f = this.getBrightness();
    //    if (f > 0.5F) {
    //      this.idleTime += 2;
    //    }
    if (getTimer() > 0) {
      setTimer(getTimer() - 1);
    }
    if (getTimer() == 0) {
      setMessage("");
    }
    super.onLivingUpdate();
  }

  @Override
  public boolean getCanSpawnHere() {
    return false;
  }

  @Override
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(0.0D);
    this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(100.0D);
    this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.33000000417232513D);
    //    this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(0.0f);
    this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50);
  }

  @Override
  public ResourceLocation getLootTable() {
    return new ResourceLocation(Const.MODID, "entity/robot");
  }

  public int getTimer() {
    return timer;
  }

  public void setTimer(int timer) {
    this.timer = timer;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
    timer = 100;
  }
}
