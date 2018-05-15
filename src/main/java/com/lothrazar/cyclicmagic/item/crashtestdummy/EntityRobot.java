package com.lothrazar.cyclicmagic.item.crashtestdummy;

import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
//import teamroots.emberroot.Const;

public class EntityRobot extends EntityCreature {

  public static final String NAME = "robot";
  // public static ConfigSpawnEntity config = new ConfigSpawnEntity(EntityFallenHero.class, EnumCreatureType.CREATURE);
  public static boolean avoidCreepers = true;
  public static boolean temptWithGold = true;
  //TODO CONFIG
  public static boolean renderDebugHitboxes = true;

  public EntityRobot(World worldIn) {
    super(worldIn);
    this.experienceValue = 0;
  }

  @Override
  public boolean attackEntityFrom(DamageSource source, float amount) {
    boolean success = super.attackEntityFrom(source, amount);

    if (success && source.getTrueSource() instanceof EntityPlayer) {
      //      DecimalFormat df = new DecimalFormat("0.00");
      UtilChat.addChatMessage(((EntityPlayer) source.getTrueSource()), "entity.robot.damaged." + amount);
    }
    return success;
  }

  @Override
  protected void initEntityAI() {
    super.initEntityAI();
    //this.tasks.addTask(1, new EntityAISwimming(this));//can swim but does not like it
    //   this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
    //    if (avoidCreepers) {
    //      this.tasks.addTask(3, new EntityAIAvoidEntity<EntityCreeper>(this, EntityCreeper.class, 8.0F, 1.0D, 1.2D));
    //    }
    //    if (temptWithGold) {
    //      this.tasks.addTask(3, new EntityAITempt(this, 1.25D, Items.GOLD_INGOT, false));
    //    }
    //    this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
    this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D));
    this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
    this.tasks.addTask(8, new EntityAILookIdle(this));
    //THREE STATES
    // STAND STILL
    // WANDER
    // CHASE PLAYER
    // this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    //      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityZombie.class, true));
    //    this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntitySkeleton.class, true));
    //    this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntitySpider.class, true));
    //    this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityVindicator.class, true));
    //    this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntitySilverfish.class, true));
  }

  @Override
  public void onLivingUpdate() {
    this.updateArmSwingProgress();
    float f = this.getBrightness();
    if (f > 0.5F) {
      this.idleTime += 2;
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
    setMaxHealth(1.0D);
    this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.33000000417232513D);
    //    this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(0.0f);
  }

  void setMaxHealth(double health) {
    this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(health);
  }

  @Override
  public ResourceLocation getLootTable() {
    return new ResourceLocation(Const.MODID, "entity/robot");
  }
}
