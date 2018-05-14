package com.lothrazar.cyclicmagic.item.crashtestdummy;
import com.lothrazar.cyclicmagic.core.util.Const;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
//import teamroots.emberroot.Const;

public class EntityRobot extends EntityMob {

  public static final String NAME = "robot";
  // public static ConfigSpawnEntity config = new ConfigSpawnEntity(EntityFallenHero.class, EnumCreatureType.CREATURE);
  public static boolean avoidCreepers;
  public static boolean temptWithGold;
  //TODO CONFIG
  public static boolean renderDebugHitboxes = true;

  public EntityRobot(World worldIn) {
    super(worldIn);
  }
  @Override
  protected void initEntityAI() {
    super.initEntityAI();
    this.tasks.addTask(1, new EntityAISwimming(this));//can swim but does not like it
    this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D, 0.0F));
    this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
    if (avoidCreepers) {
      this.tasks.addTask(3, new EntityAIAvoidEntity<EntityCreeper>(this, EntityCreeper.class, 8.0F, 1.0D, 1.2D));
    }
    if (temptWithGold) {
      this.tasks.addTask(3, new EntityAITempt(this, 1.25D, Items.GOLD_INGOT, false));
    }
    this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D));
    this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
    this.tasks.addTask(8, new EntityAILookIdle(this));
    //THREE STATES
    // STAND STILL
    // WANDER
    // CHASE PLAYER
    this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
    this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityZombie.class, true));
    this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntitySkeleton.class, true));
    this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntitySpider.class, true));
    this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityVindicator.class, true));
    this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntitySilverfish.class, true));
  }
  @Override
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
    //    this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.33000000417232513D);
    //ConfigSpawnEntity.syncInstance(this, config.settings);
    IAttributeInstance ai = this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE);
    if (ai == null) {
      this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
      ai = this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE);
    }
    ai.setBaseValue(0.0f);
  }
  @Override
  public IEntityLivingData onInitialSpawn(DifficultyInstance di, IEntityLivingData livingData) {
    //TODO: armor odds in config?
    if (rand.nextDouble() < 0.7) {
      setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_BOOTS));
    }
    if (rand.nextDouble() < 0.6) {
      setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
    }
    if (rand.nextDouble() < 0.4) {
      setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_BOOTS));
    }
    if (rand.nextDouble() < 0.5) {
      setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
    }
    else {
      setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
    }
    if (rand.nextDouble() < 0.5) {
      setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
    }
    if (rand.nextDouble() < 0.5) {
      setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.GOLDEN_AXE));
    }
    return super.onInitialSpawn(di, livingData);
  }
  @Override
  public ResourceLocation getLootTable() {
    return new ResourceLocation(Const.MODID, "entity/hero");
  }
  @Override
  public boolean isPreventingPlayerRest(EntityPlayer playerIn) {
    return false;
  }
}
