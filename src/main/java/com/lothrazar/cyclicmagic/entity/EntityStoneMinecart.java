package com.lothrazar.cyclicmagic.entity;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecartFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class EntityStoneMinecart extends EntityMinecartFurnace {
  public static Item dropItem = Items.MINECART;//override with gold minecart on registry, this is here just for nonnull
  public EntityStoneMinecart(World worldIn) {
    super(worldIn);
    this.setCartBlock(Blocks.COBBLESTONE.getDefaultState());
  }
  public EntityStoneMinecart(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }
  private void setCartBlock(IBlockState b) {
    dropCartBlock();
    this.setDisplayTile(b);
  }
  public void dropCartBlock() {
    IBlockState current = this.getDisplayTile();
    if (current.getBlock() != Blocks.AIR) {
      UtilItemStack.dropBlockState(this.world, this.getPosition(), current);
    }
  }
  @Override
  public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
    if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.minecart.MinecartInteractEvent(this, player, hand))) return true;
    ItemStack held = player.getHeldItem(hand);
    IBlockState heldBlock = UtilItemStack.getStateFromStack(held);
    this.setCartBlock(heldBlock);
    if (heldBlock.getBlock() != Blocks.AIR) {
      held.shrink(1);
    }
    return true;
  }
  @Override
  public void onActivatorRailPass(int x, int y, int z, boolean receivingPower) {
    if (receivingPower) {
      this.setCartBlock(Blocks.AIR.getDefaultState());
    }
  }
  @Override
  public void killMinecart(DamageSource source) {
    this.setDead();
    if (this.world.getGameRules().getBoolean("doEntityDrops")) {
      dropCartBlock();
      ItemStack itemstack = getCartItem();
      if (this.hasCustomName()) {
        itemstack.setStackDisplayName(this.getCustomNameTag());
      }
      this.entityDropItem(itemstack, 0.0F);
    }
  }
  @Override
  public ItemStack getCartItem() {
    return new ItemStack(dropItem);
  }
}
