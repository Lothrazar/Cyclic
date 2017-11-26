package com.lothrazar.cyclicmagic.module;
import java.lang.ref.WeakReference;
import java.util.UUID;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.util.UtilFakePlayer;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilOreDictionary;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
//import net.minecraft.entity.monster.SkeletonType;
//import net.minecraft.entity.monster.ZombieType;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

public class EnvironmentTweaksModule extends BaseEventModule implements IHasConfig {
  private boolean saplingDespawnGrow;
  private boolean spawnersUnbreakable;
  private static WeakReference<FakePlayer> fakePlayer;
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
      BlockPos pos = entityItem.getPosition();
      Entity entity = event.getEntity();
      ItemStack is = entityItem.getItem();
      World world = entity.getEntityWorld();
      if (is.isEmpty()) {
        return;
      }
      // plant the sapling, replacing the air and on top of dirt/plantable
      if (Block.getBlockFromItem(is.getItem()) == Blocks.RED_MUSHROOM)
        world.setBlockState(entityItem.getPosition(), Blocks.RED_MUSHROOM.getDefaultState());
      else if (Block.getBlockFromItem(is.getItem()) == Blocks.BROWN_MUSHROOM)
        world.setBlockState(entityItem.getPosition(), Blocks.BROWN_MUSHROOM.getDefaultState());
      else if (UtilOreDictionary.doesMatchOreDict(is, "treeSapling")) {
        Block saplingBlock = Block.getBlockFromItem(is.getItem());
        boolean success = false;
        if (saplingBlock != Blocks.AIR
            && saplingBlock.canPlaceBlockAt(world, pos)) {
          success = world.setBlockState(pos, UtilItemStack.getStateFromMeta(Block.getBlockFromItem(is.getItem()), is.getItemDamage()));
        }
        //only do the fake player strategy if the normal one fails
        if (success == false && world instanceof WorldServer) {
          if (fakePlayer == null) {
            fakePlayer = UtilFakePlayer.initFakePlayer(((WorldServer) world), UUID.randomUUID());
            if (fakePlayer == null) {
              ModCyclic.logger.error("Fake player failed to init ");
              return;
            }
          }
          fakePlayer.get().setHeldItem(EnumHand.MAIN_HAND, is);
          fakePlayer.get().interactionManager.processRightClickBlock(fakePlayer.get(), world, is, EnumHand.MAIN_HAND, pos, EnumFacing.DOWN, .5F, .5F, .5F);
          //          IBlockState toPlace =   saplingBlock.getStateForPlacement(world, pos, EnumFacing.DOWN, .5F, .5F, .5F, is.getItemDamage(), fakePlayer.get() );
        }
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
