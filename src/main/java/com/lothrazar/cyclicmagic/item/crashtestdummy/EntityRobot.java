package com.lothrazar.cyclicmagic.item.crashtestdummy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
import net.minecraft.world.WorldServer;

public class EntityRobot extends EntityCreature {

  public static final int MAX_TIMER = 100;
  public static final String NAME = "robot";
  public static boolean avoidCreepers = true;
  public static boolean temptWithGold = true;
  List<DmgTracker> trackers = new ArrayList<DmgTracker>();

  public EntityRobot(World worldIn) {
    super(worldIn);
    this.experienceValue = 0;
  }

  @Override
  public boolean attackEntityFrom(DamageSource source, float amount) {
    boolean success = super.attackEntityFrom(source, amount);
    if (success && source.getTrueSource() instanceof EntityPlayer
        && world instanceof WorldServer) {
      String m = amount + "  [ " + this.getHealth() + " / " + this.getMaxHealth() + " ]";
      trackers.add(new DmgTracker(MAX_TIMER, amount + ""));
      //    UtilChat.addChatMessage(((EntityPlayer) source.getTrueSource()), m);
      //arg server only
      EntityTracker et = ((WorldServer) world).getEntityTracker();
      et.sendToTracking(this,
          ModCyclic.network.getPacketFrom(new PacketEntitySyncToClient(
              getEntityId(), trackers)));
    }
    return false;
  }

  @Override
  protected void initEntityAI() {
    super.initEntityAI();
    this.tasks.addTask(3, new EntityAITempt(this, 1.25D, Items.GOLD_INGOT, false));
    this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    int i = 0;
    for (DmgTracker t : trackers) {
      compound.setInteger("tmr" + i, t.timer);
      compound.setString("strMsg" + i, t.message);
      i++;
    }
    compound.setInteger("SAVED", i);
    return super.writeToNBT(compound);
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    int saved = compound.getInteger("SAVED");
    for (int i = 0; i < saved; i++) {
      if (compound.hasKey("tmr" + i)) {
        int timer = compound.getInteger("tmr" + i);
        if (timer > 0) {
          trackers.add(new DmgTracker(timer, compound.getString("strMsg" + i)));
        }
      }
    }
    //    setMessage(compound.getString("strMsg"));
  }

  @Override
  public void onLivingUpdate() {
    // remove old
    for (Iterator<DmgTracker> iterator = trackers.iterator(); iterator.hasNext();) {
      DmgTracker dmg = iterator.next();
      dmg.timer--;
      if (dmg.timer < 0) {
        iterator.remove();
      }
    }
    // this.updateArmSwingProgress();
    //    float f = this.getBrightness();
    //    if (f > 0.5F) {
    //      this.idleTime += 2;
    //    }
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

  public static class DmgTracker {

    public int timer = 0;
    public String message;

    public DmgTracker(int t, String m) {
      timer = t;
      message = m;
    }
  }

  public void setTrackers(List<DmgTracker> trackers) {
    this.trackers = trackers;
  }
}
