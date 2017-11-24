package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilOreDictionary;
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
import net.minecraftforge.oredict.OreDictionary;

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
      ItemStack is = entityItem.getItem();
      World world = entity.getEntityWorld();
      if (is.isEmpty()) {
        return;
      } // has not happened in the wild, yet
      Block blockhere = entity.getEntityWorld().getBlockState(entityItem.getPosition()).getBlock();
      Block blockdown = entity.getEntityWorld().getBlockState(entityItem.getPosition().down()).getBlock();
      if (blockhere == Blocks.AIR && blockdown == Blocks.DIRT || blockdown == Blocks.GRASS) {
        // plant the sapling, replacing the air and on top of dirt/plantable
   
        if (UtilOreDictionary.doesMatchOreDict(is, "treeSapling")){
          
          world.setBlockState(entityItem.getPosition(), UtilItemStack.getStateFromMeta(Block.getBlockFromItem(is.getItem()), is.getItemDamage()));
        }
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
    saplingDespawnGrow = config.getBoolean("Plant Despawning Saplings", category, false, "Plant saplings (and mushrooms) if they despawn on grass/dirt");
    spawnersUnbreakable = config.getBoolean("Spawners Unbreakable", category, false, "Make mob spawners unbreakable");
    updateHardness();
  }
}
