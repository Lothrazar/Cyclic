package com.lothrazar.cyclic.block.scaffolding;

import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

import net.minecraft.world.item.Item.Properties;

public class ItemScaffolding extends BlockItem {

  public ItemScaffolding(Block blockIn, Properties builder) {
    super(blockIn, builder);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level worldIn, Player player, InteractionHand hand) {
    if (player.isCrouching()) {
      // || worldIn.getBlockState(context.getPos()).isAir() == false) {
      return super.use(worldIn, player, hand);
    }
    //NOT crouchign so this is the MID AIR PLACEMENT section
    //skip if sneaking
    BlockPos pos = player.blockPosition().above();
    // at eye level
    int direction = Mth.floor((player.yRot * 4F) / 360F + 0.5D) & 3;
    //imported from my scaffolding spell https://github.com/PrinceOfAmber/CyclicMagic/blob/37ebb722378cbf940aa9cfb4fa99ce0e80127533/src/main/java/com/lothrazar/cyclicmagic/spell/SpellScaffolding.java
    // -45 is up
    // +45 is pitch down
    // first; is it up or down?
    boolean doHoriz = true;
    //    Direction facing = Direction.UP;
    if (player.xRot < -82) {
      // really really up
      doHoriz = false;
      pos = pos.above().above();
      //      facing = Direction.UP;
    }
    else if (player.xRot > 82) {
      // really really down
      doHoriz = false;
      pos = pos.below();
      //      facing = Direction.DOWN;
    }
    else if (player.xRot < -45) {
      // angle is pretty high up. so offset up again
      pos = pos.above();
      //      facing = Direction.UP;
      doHoriz = true;
    }
    else if (player.xRot > 45) {
      // you are angled down, so bring down from eye level
      pos = pos.below();
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
    if (worldIn.isClientSide == false && worldIn.isEmptyBlock(pos)) {
      ItemStack stac = player.getItemInHand(hand);
      if (worldIn.setBlockAndUpdate(pos, Block.byItem(this).defaultBlockState())) {
        UtilItemStack.shrink(player, stac);
      }
      return new InteractionResultHolder<>(InteractionResult.SUCCESS, stac);
    }
    return super.use(worldIn, player, hand);
  }
}
