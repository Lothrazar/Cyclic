package com.lothrazar.cyclic.block.scaffolding;

import com.lothrazar.cyclic.util.Const;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
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
    //    super.onItemRightClick(worldIn, playerIn, handIn)
  }

  @Override
  public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
    System.out.println("onItemUse FIRST");
    return super.onItemUseFirst(stack, context);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand hand) {
    //    System.out.println("onItemUse RIGHTCLICK");
    //place in AIR
    //    return super.onItemRightClick(worldIn, playerIn, handIn);
    //  }
    //  @Override
    //  public ActionResultType onItemUse(ItemUseContext context) {
    //    super.onItemUseFirst(stack, context)
    //    this.onItemRightClick(context.getWorld(), context.getPlayer(), context.getHand());
    //    return super.onItemUse(context);
    //    ItemStack stack = player.getHeldItem(hand);
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
          pos = pos.west();// .offset(Direction.WEST);
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
      System.out.println("!!  NEWPOS " + pos);
      //      Vec3d vec = new Vec3d(pos.getX() + 0.5D + facing.getXOffset() * 0.5D, pos.getY() + 0.5D + facing.getYOffset() * 0.5D, pos.getZ() + 0.5D + facing.getZOffset() * 0.5D);
      //      BlockRayTraceResult trace = new BlockRayTraceResult(vec, facing, pos, doHoriz);
      //      this.onItemUse(context)
      worldIn.setBlockState(pos, Block.getBlockFromItem(this).getDefaultState());
      //      return ActionResultType.SUCCESS;
      //      ItemUseContext context2 = new ItemUseContext(player, hand, trace);
      //      ActionResultType res = super.onItemUse(context2);//ActionResultType.SUCCESS;//new ActionResult<ItemStack>(this.onItemUse(context), stack);
      return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
    }
    return super.onItemRightClick(worldIn, player, hand);
  }
}
