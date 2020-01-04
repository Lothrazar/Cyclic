package com.lothrazar.cyclic.block.scaffolding;

import com.lothrazar.cyclic.util.Const;
import com.lothrazar.cyclic.util.UtilWorld;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ItemScaffolding extends BlockItem {

  public ItemScaffolding(Block blockIn, Properties builder) {
    super(blockIn, builder);
  }

  @SubscribeEvent
  public void onRightClickBlock(RightClickBlock event) {
    if (event.getItemStack() != null && event.getItemStack().getItem() == this && event.getPlayer().isSneaking()) {
      Direction opp = event.getFace().getOpposite();
      BlockPos dest = UtilWorld.nextReplaceableInDirection(event.getWorld(), event.getPos(), opp, 16, this.getBlock());
      event.getWorld().setBlockState(dest, Block.getBlockFromItem(this).getDefaultState());
      ItemStack stac = event.getPlayer().getHeldItem(event.getHand());
      stac.shrink(1);
      event.setCanceled(true);
    }
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand hand) {
    if (player.isSneaking()) {// || worldIn.getBlockState(context.getPos()).isAir() == false) {
      return super.onItemRightClick(worldIn, player, hand);
    }
    //skip if sneaking
    BlockPos pos = player.getPosition().up();// at eye level
    int direction = MathHelper.floor((player.rotationYaw * 4F) / 360F + 0.5D) & 3;
    //imported from my scaffolding spell https://github.com/PrinceOfAmber/CyclicMagic/blob/37ebb722378cbf940aa9cfb4fa99ce0e80127533/src/main/java/com/lothrazar/cyclicmagic/spell/SpellScaffolding.java
    // -45 is up
    // +45 is pitch down
    // first; is it up or down?
    boolean doHoriz = true;
    Direction facing = Direction.UP;
    if (player.rotationPitch < -82) {
      // really really up
      doHoriz = false;
      pos = pos.up().up();
      facing = Direction.UP;
    }
    else if (player.rotationPitch > 82) {
      // really really down
      doHoriz = false;
      pos = pos.down();
      facing = Direction.DOWN;
    }
    else if (player.rotationPitch < -45) {
      // angle is pretty high up. so offset up again
      pos = pos.up();
      facing = Direction.UP;
      doHoriz = true;
    }
    else if (player.rotationPitch > 45) {
      // you are angled down, so bring down from eye level
      pos = pos.down();
      facing = Direction.DOWN;
      doHoriz = true;
    }
    // else doHoriz = true; stays
    // if not, go by dir
    if (doHoriz) {
      switch (direction) {
        case Const.DIR_EAST:
          pos = pos.east();
          facing = Direction.EAST;
        break;
        case Const.DIR_WEST:
          pos = pos.west();
          facing = Direction.WEST;
        break;
        case Const.DIR_SOUTH:
          pos = pos.south();
          facing = Direction.SOUTH;
        break;
        case Const.DIR_NORTH:
          pos = pos.north();
          facing = Direction.NORTH;
        break;
      }
    }
    if (worldIn.isRemote == false && worldIn.isAirBlock(pos)) {
      worldIn.setBlockState(pos, Block.getBlockFromItem(this).getDefaultState());
      ItemStack stac = player.getHeldItem(hand);
      stac.shrink(1);
      return new ActionResult<>(ActionResultType.SUCCESS, stac);
    }
    return super.onItemRightClick(worldIn, player, hand);
  }
}
