package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
//import net.minecraft.entity.monster.SkeletonType;
//import net.minecraft.entity.monster.ZombieType;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnvironmentTweaksModule extends BaseEventModule implements IHasConfig {
  private boolean saplingDespawnGrow;
  private boolean spawnersUnbreakable;
 
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
  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.blocks;
    saplingDespawnGrow = config.getBoolean("Plant Despawning Saplings", category, true, "Plant saplings (and mushrooms) if they despawn on grass/dirt");
    spawnersUnbreakable = config.getBoolean("Spawners Unbreakable", category, false, "Make mob spawners unbreakable");
    updateHardness();
  }
}
