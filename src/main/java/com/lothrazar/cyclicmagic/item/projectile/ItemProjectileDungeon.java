package com.lothrazar.cyclicmagic.item.projectile;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.entity.projectile.EntityDungeonEye;
import com.lothrazar.cyclicmagic.entity.projectile.EntityThrowableDispensable;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemProjectileDungeon extends BaseItemProjectile implements IHasRecipe, IHasConfig {
  private static int DUNGEONRADIUS = 64;
  @Override
  public EntityThrowableDispensable getThrownEntity(World world, double x, double y, double z) {
    return new EntityDungeonEye(world, x,y,z);
  }
  @Override
  public void syncConfig(Configuration config) {
    DUNGEONRADIUS = config.getInt("Ender Dungeon Radius", Const.ConfigCategory.items, 128, 8, 128, "Search radius of dungeonfinder");
  }
  @Override
  public void addRecipe() {
    GameRegistry.addShapelessRecipe(new ItemStack(this, 8), new ItemStack(Items.ENDER_PEARL), new ItemStack(Blocks.MOSSY_COBBLESTONE), new ItemStack(Items.NETHER_WART));// Blocks.iron_bars
  }
  @Override
  void onItemThrow(ItemStack held, World world, EntityPlayer player, EnumHand hand) {
    BlockPos blockpos = UtilWorld.findClosestBlock(player, Blocks.MOB_SPAWNER, DUNGEONRADIUS);
    if (blockpos != null) {
      EntityDungeonEye entityendereye = new EntityDungeonEye(world, player);
      doThrow(world, player, hand, entityendereye, 0.5F);
      entityendereye.moveTowards(blockpos);
    }
    else {
      // not found, so play different sound
      UtilSound.playSound(player, player.getPosition(), SoundEvents.BLOCK_FIRE_EXTINGUISH);
      if (world.isRemote) {
        UtilChat.addChatMessage(player, "item.ender_dungeon.notfound");
      }
    }
  }
  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack stack) {
    return true;
  }
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    tooltip.add(UtilChat.lang("item.ender_dungeon.tooltip"));
  }
}
