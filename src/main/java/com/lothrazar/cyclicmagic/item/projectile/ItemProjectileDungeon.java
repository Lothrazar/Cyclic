package com.lothrazar.cyclicmagic.item.projectile;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.entity.projectile.EntityDungeonEye;
import com.lothrazar.cyclicmagic.entity.projectile.EntityThrowableDispensable;
import com.lothrazar.cyclicmagic.item.base.BaseItemProjectile;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilPlayer;
import com.lothrazar.cyclicmagic.util.UtilSound;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemProjectileDungeon extends BaseItemProjectile implements IHasRecipe, IHasConfig {
  private static final int COOLDOWN = 10;
  private static int DUNGEONRADIUS = 64;
  public ItemProjectileDungeon() {
    super();
    this.setMaxDamage(1000);
    this.setMaxStackSize(1);
  }
  @Override
  public EntityThrowableDispensable getThrownEntity(World world, double x, double y, double z) {
    return new EntityDungeonEye(world, x, y, z);
  }
  @Override
  public void syncConfig(Configuration config) {
    DUNGEONRADIUS = config.getInt("Ender Dungeon Radius", Const.ConfigCategory.items, 64, 8, 128, "Search radius of dungeonfinder");
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapelessRecipe(new ItemStack(this, 8),
        "enderpearl",
        new ItemStack(Blocks.MOSSY_COBBLESTONE),
        "cropNetherWart");// Blocks.iron_bars
  }
  @Override
  public  void onItemThrow(ItemStack held, World world, EntityPlayer player, EnumHand hand) {
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
        UtilChat.addChatMessage(player, UtilChat.lang("item.ender_dungeon.notfound") + " " + DUNGEONRADIUS);
      }
    }
    player.getCooldownTracker().setCooldown(held.getItem(), COOLDOWN);
    UtilItemStack.damageItem(player, held);
  }
  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack stack) {
    return true;
  }
}
