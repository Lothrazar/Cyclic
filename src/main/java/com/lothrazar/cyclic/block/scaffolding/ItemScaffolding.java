package com.lothrazar.cyclic.block.scaffolding;

import com.lothrazar.cyclic.util.UtilItemStack;
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

public class ItemScaffolding extends BlockItem {

  public ItemScaffolding(Block blockIn, Properties builder) {
    super(blockIn, builder);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand hand) {
    if (player.isCrouching()) {
      // || worldIn.getBlockState(context.getPos()).isAir() == false) {
      return super.onItemRightClick(worldIn, player, hand);
    }
    //NOT crouchign so this is the MID AIR PLACEMENT section
    //skip if sneaking
    BlockPos pos = player.getPosition().up();
    // at eye level
    int direction = MathHelper.floor((player.rotationYaw * 4F) / 360F + 0.5D) & 3;
    //imported from my scaffolding spell https://github.com/PrinceOfAmber/CyclicMagic/blob/37ebb722378cbf940aa9cfb4fa99ce0e80127533/src/main/java/com/lothrazar/cyclicmagic/spell/SpellScaffolding.java
    // -45 is up
    // +45 is pitch down
    // first; is it up or down?
    boolean doHoriz = true;
    //    Direction facing = Direction.UP;
    if (player.rotationPitch < -82) {
      // really really up
      doHoriz = false;
      pos = pos.up().up();
      //      facing = Direction.UP;
    }
    else if (player.rotationPitch > 82) {
      // really really down
      doHoriz = false;
      pos = pos.down();
      //      facing = Direction.DOWN;
    }
    else if (player.rotationPitch < -45) {
      // angle is pretty high up. so offset up again
      pos = pos.up();
      //      facing = Direction.UP;
      doHoriz = true;
    }
    else if (player.rotationPitch > 45) {
      // you are angled down, so bring down from eye level
      pos = pos.down();
      //      facing = Direction.DOWN;
      doHoriz = true;
    }
    // else doHoriz = true; stays
    // if not, go by dir
    if (doHoriz) {
      if (direction == Direction.EAST.ordinal()) {
        pos = pos.east();
        //        facing = Direction.EAST;
      }
      if (direction == Direction.WEST.ordinal()) {
        pos = pos.west();
        //        facing = Direction.WEST;
      }
      if (direction == Direction.SOUTH.ordinal()) {
        pos = pos.south();
        //        facing = Direction.SOUTH;
      }
      if (direction == Direction.NORTH.ordinal()) {
        pos = pos.north();
        //        facing = Direction.NORTH;
      }
    }
    if (!worldIn.isRemote && worldIn.isAirBlock(pos)) {
      ItemStack stac = player.getHeldItem(hand);
      if (worldIn.setBlockState(pos, Block.getBlockFromItem(this).getDefaultState())) {
        UtilItemStack.shrink(player, stac);
      }
      return new ActionResult<>(ActionResultType.SUCCESS, stac);
    }
    return super.onItemRightClick(worldIn, player, hand);
  }
}
