package com.lothrazar.cyclicmagic.module;
import java.util.List;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteractSpecific;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MobDropChangesModule extends BaseEventModule {
  private static final int chanceZombieVillagerEmerald = 25;
  public static boolean sheepShearBuffed;//used by entity shearing bolt also
  private boolean zombieVillagerEmeralds;
  private boolean extraLeather;
  private boolean bonusPork;
  private boolean bonusGolemIron;
  private boolean zombieDropsNerfed;
  private boolean endermanDrop;
  @SubscribeEvent
  public void onEntityInteractSpecific(EntityInteractSpecific event) {
    if (sheepShearBuffed && event.getEntityPlayer() != null && event.getTarget() instanceof EntitySheep) {
      EntityPlayer p = event.getEntityPlayer();
      EntitySheep s = (EntitySheep) event.getTarget();
      if (event.getHand() != null && p.getHeldItem(event.getHand()) != null &&
          p.getHeldItem(event.getHand()).getItem() == Items.SHEARS) {
        int meta = s.getFleeceColor().getMetadata();
        int rand = MathHelper.getRandomIntegerInRange(event.getWorld().rand, 1, 6);
        UtilEntity.dropItemStackInWorld(event.getWorld(), event.getPos(), new ItemStack(Blocks.WOOL, rand, meta));
      }
    }
  }
  @SubscribeEvent
  public void onLivingDropsEvent(LivingDropsEvent event) {
    Entity entity = event.getEntity();
    World worldObj = entity.getEntityWorld();
    List<EntityItem> drops = event.getDrops();
    BlockPos pos = entity.getPosition();
    if (endermanDrop && entity instanceof EntityEnderman) {
      EntityEnderman mob = (EntityEnderman) entity;
      IBlockState bs = mob.getHeldBlockState();// mob.func_175489_ck();
      if (bs != null && bs.getBlock() != null && entity.worldObj.isRemote == false) {
        UtilEntity.dropItemStackInWorld(entity.worldObj, mob.getPosition(), bs.getBlock());
      }
    }
    if (entity instanceof EntityZombie && zombieVillagerEmeralds) {
      EntityZombie z = (EntityZombie) entity;
      if (z.isVillager() && chanceZombieVillagerEmerald > 0 && worldObj.rand.nextInt(100) <= chanceZombieVillagerEmerald) {
        drops.add(new EntityItem(worldObj, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.EMERALD)));
      }
    }
    if (extraLeather && entity instanceof EntityCow) {
      int rand = MathHelper.getRandomIntegerInRange(worldObj.rand, 1, 8);
      UtilEntity.dropItemStackInWorld(worldObj, pos, new ItemStack(Items.LEATHER, rand));
    }
    if (bonusPork && entity instanceof EntityPig) {
      int rand = MathHelper.getRandomIntegerInRange(worldObj.rand, 1, 8);
      UtilEntity.dropItemStackInWorld(worldObj, pos, new ItemStack(Items.PORKCHOP, rand));
    }
    if (bonusGolemIron && entity instanceof EntityIronGolem) {
      //vanilla drops 3-5 ingots
      int rand = MathHelper.getRandomIntegerInRange(worldObj.rand, 1, 18);
      UtilEntity.dropItemStackInWorld(worldObj, pos, new ItemStack(Items.IRON_INGOT, rand));
      if (worldObj.rand.nextDouble() < 0.10) {
        //10% chance for extra iron block
        UtilEntity.dropItemStackInWorld(worldObj, pos, new ItemStack(Blocks.IRON_BLOCK, 1));
      }
    }
    if (zombieDropsNerfed) {
      if (entity instanceof EntityZombie) {
        Item item;
        for (int i = 0; i < drops.size(); i++) {
          //EntityItem item = ;
          item = drops.get(i).getEntityItem().getItem();
          if (item == Items.CARROT || item == Items.POTATO || item == Items.IRON_INGOT) {
            drops.remove(i);
          }
        }
      }
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.mobs;
    config.addCustomCategoryComment(category, "Changes to vanilla mobs");
    endermanDrop = config.getBoolean("Enderman Block", category, true,
        "Enderman will always drop block they are carrying 100%");
    zombieDropsNerfed = config.getBoolean("Zombie Drops Nerfed", category, true,
        "Zombies no longer drops carrots, potatoes, or iron ingots");
    sheepShearBuffed = config.getBoolean("Sheep Shear Bonus", category, true,
        "Shearing sheep randomly adds bonus wool");
    zombieVillagerEmeralds = config.getBoolean("Zombie Villager Emerald", category, true,
        "Zombie villagers have a " + chanceZombieVillagerEmerald + "% chance to drop an emerald");
    extraLeather = config.getBoolean("Leather Bonus", category, true,
        "Leather drops from cows randomly increased");
    bonusPork = config.getBoolean("Pork Bonus", category, true,
        "Pig drops randomly increased");
    bonusGolemIron = config.getBoolean("Iron Bonus", category, true,
        "Iron Golem drops randomly increased by a few ingots, and a low chance to drop an iron block");
  }
}
