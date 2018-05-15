package com.lothrazar.cyclicmagic.item.crashtestdummy;

import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
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
      UtilChat.addChatMessage(((EntityPlayer) source.getTrueSource()),
          amount + "  [ " + this.getHealth() + " / " + this.getMaxHealth() + " ]");
    }
    return success;
  }

  @Override
  protected void initEntityAI() {
    super.initEntityAI();
    this.tasks.addTask(3, new EntityAITempt(this, 1.25D, Items.GOLD_INGOT, false));
    this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
    //   this.tasks.addTask(8, new EntityAILookIdle(this));

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
    this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(100.0D);
    this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.33000000417232513D);
    //    this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(0.0f);
    this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50);
  }



  @Override
  public ResourceLocation getLootTable() {
    return new ResourceLocation(Const.MODID, "entity/robot");
  }
}
