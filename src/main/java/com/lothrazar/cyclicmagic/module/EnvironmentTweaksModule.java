package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.SkeletonType;
import net.minecraft.entity.monster.ZombieType;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnvironmentTweaksModule extends BaseEventModule implements IHasConfig {
  private boolean saplingDespawnGrow;
  private boolean spawnersUnbreakable;
  private boolean skelLightning;
  private boolean zombLightning;
  @Override
  public void onInit() {
    updateHardness();
  }
  private void updateHardness() {
    if (spawnersUnbreakable) {
      Blocks.MOB_SPAWNER.setBlockUnbreakable();//just like .setHardness(-1.0F);
    }
    else {
      Blocks.MOB_SPAWNER.setHardness(5.0F);//reset to normal http://minecraft.gamepedia.com/Monster_Spawner
    }
  }
  @SubscribeEvent
  public void onItemExpireEvent(ItemExpireEvent event) {
    if (saplingDespawnGrow) {
      EntityItem entityItem = event.getEntityItem();
      Entity entity = event.getEntity();
      ItemStack is = entityItem.getEntityItem();
      World world = entity.getEntityWorld();
      if (is == null) { return; } // has not happened in the wild, yet
      Block blockhere = entity.getEntityWorld().getBlockState(entityItem.getPosition()).getBlock();
      Block blockdown = entity.getEntityWorld().getBlockState(entityItem.getPosition().down()).getBlock();
      if (blockhere == Blocks.AIR && blockdown == Blocks.DIRT || blockdown == Blocks.GRASS) {
        // plant the sapling, replacing the air and on top of dirt/plantable
        //BlockSapling.TYPE
        if (Block.getBlockFromItem(is.getItem()) == Blocks.SAPLING)
          world.setBlockState(entityItem.getPosition(), UtilItemStack.getStateFromMeta(Blocks.SAPLING, is.getItemDamage()));
        else if (Block.getBlockFromItem(is.getItem()) == Blocks.RED_MUSHROOM)
          world.setBlockState(entityItem.getPosition(), Blocks.RED_MUSHROOM.getDefaultState());
        else if (Block.getBlockFromItem(is.getItem()) == Blocks.BROWN_MUSHROOM)
          world.setBlockState(entityItem.getPosition(), Blocks.BROWN_MUSHROOM.getDefaultState());
      }
    }
  }
  @SuppressWarnings("deprecation")
  @SubscribeEvent
  public void onEntityStruckByLightning(EntityStruckByLightningEvent event) {
    if (skelLightning && event.getEntity() instanceof EntitySkeleton &&
        event.getLightning() != null) {
      EntitySkeleton skel = (EntitySkeleton) event.getEntity();
   
      if (skel.getSkeletonType() == SkeletonType.NORMAL) {
        SkeletonType newType = skel.getEntityWorld().rand.nextDouble() > 0.5 ? SkeletonType.WITHER : SkeletonType.STRAY;
        skel.setSkeletonType(newType);
        skel.heal(skel.getMaxHealth());
      }
    }
    if (zombLightning && event.getEntity() instanceof EntityZombie &&
        event.getLightning() != null) {
      EntityZombie zomb = (EntityZombie) event.getEntity();
      //it says  //Do not use, Replacement TBD
      //but , if there is no replacement, why is it deprecated? makes no sense i say!
      if (zomb.getZombieType() == ZombieType.NORMAL) {
        zomb.setZombieType(ZombieType.HUSK);
        zomb.heal(zomb.getMaxHealth());
      }
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    skelLightning = config.getBoolean("LightningSkeletonMutate", Const.ConfigCategory.mobs, true, "Skeletons turn into either a Stray or a Wither Skeleton when hit by lightning");
    zombLightning = config.getBoolean("LightningZombieMutate", Const.ConfigCategory.mobs, true, "Zombies turn into a Husk when hit by lightning");
    String category = Const.ConfigCategory.blocks;
    saplingDespawnGrow = config.getBoolean("Plant Despawning Saplings", category, true, "Plant saplings (and mushrooms) if they despawn on grass/dirt");
    spawnersUnbreakable = config.getBoolean("Spawners Unbreakable", category, false, "Make mob spawners unbreakable");
    updateHardness();
  }
}
