package com.lothrazar.cyclic.block.endershelf;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.block.enderctrl.EnderShelfHelper;
import com.lothrazar.cyclic.block.enderctrl.TileEnderCtrl;
import com.lothrazar.cyclic.data.DataTags;
import com.lothrazar.cyclic.util.UtilBlockstates;
import com.lothrazar.cyclic.util.UtilEnchant;
import com.lothrazar.cyclic.util.UtilItemStack;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class BlockEnderShelf extends BlockBase {

  public BlockEnderShelf(Properties properties, boolean isController) {
    super(properties.strength(1.2F).noOcclusion());
  }

  @Override
  public float getEnchantPowerBonus(BlockState state, LevelReader world, BlockPos pos) {
    return Blocks.BOOKSHELF.getEnchantPowerBonus(Blocks.BOOKSHELF.defaultBlockState(), world, pos);
  }

  @Override
  public void registerClient() {
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(BlockStateProperties.HORIZONTAL_FACING);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TileEnderShelf(pos, state);
  }
  //  @Override
  //  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
  //    return createTickerHelper(type,TileRegistry.ender_shelf, world.isClientSide ? TileEnderShelf::clientTick : TileEnderShelf::serverTick);
  //  }

  @Override
  public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    //    if (state.hasTileEntity() && (!state.is(newState.getBlock()) || !newState.hasTileEntity())) {
    //      worldIn.removeBlockEntity(pos);
    //    }
    if (!state.is(newState.getBlock())) {
      worldIn.removeBlockEntity(pos);
    }
  }

  @Override
  public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
    ItemStack heldItem = player.getItemInHand(hand);
    if (hand != InteractionHand.MAIN_HAND && heldItem.isEmpty()) {
      //if your hand is empty, dont process if its the OFF hand
      //otherwise: main hand inserts, off hand takes out right away
      return InteractionResult.PASS;
    }
    TileEnderShelf shelf = getTileEntity(world, pos);
    if (heldItem.is(DataTags.WRENCH)) {
      //wrench tag
      shelf.toggleShowText();
      player.swing(hand);
      return InteractionResult.PASS;
    }
    Direction face = hit.getDirection();
    Vec3 hitVec = hit.getLocation();
    int slot = getSlotFromHitVec(pos, face, hitVec);
    if (hit.getDirection() == state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
      //
      // single shelf
      //
      if (heldItem.getItem() == Items.ENCHANTED_BOOK) {
        ItemStack stackInSlot = shelf.inventory.getStackInSlot(slot);
        if (stackInSlot == ItemStack.EMPTY || UtilEnchant.doBookEnchantmentsMatch(stackInSlot, heldItem)) {
          if (!world.isClientSide) {
            ItemStack remaining = shelf.inventory.insertItem(slot, heldItem, false);
            player.setItemInHand(hand, remaining);
            player.swing(hand);
          }
        }
      }
      else if (heldItem.isEmpty()) {
        ItemStack retrievedBook = shelf.inventory.extractItem(slot, 1, false);
        player.setItemInHand(hand, retrievedBook);
        player.swing(hand);
      }
    }
    return InteractionResult.PASS;
  }

  public static int getSlotFromHitVec(BlockPos pos, Direction face, Vec3 hitVec) {
    double normalizedY = hitVec.y() - pos.getY();
    return (int) Math.floor(normalizedY / 0.20);
  }

  public TileEnderShelf getTileEntity(Level world, BlockPos pos) {
    return (TileEnderShelf) world.getBlockEntity(pos);
  }
  //

  @Override
  public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootContext.Builder builder) {
    // because harvestBlock manually forces a drop, we must do this to dodge that
    return new ArrayList<>();
  }

  @Override
  public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
    if (entity != null) {
      //facing state if needed 
      world.setBlock(pos, state.setValue(BlockStateProperties.HORIZONTAL_FACING, UtilBlockstates.getFacingFromEntityHorizontal(pos, entity)), 2);
    }
    BlockEntity tileentity = world.getBlockEntity(pos);
    TileEnderShelf shelf = (TileEnderShelf) tileentity;
    BlockPos controllerPos = EnderShelfHelper.findConnectedController(world, pos);
    if (controllerPos != null) {
      //      shelf.setControllerLocation(controllerPos);
      TileEnderCtrl controller = (TileEnderCtrl) world.getBlockEntity(controllerPos);
      if (controllerPos != null && controller != null) {
        controller.getShelves().add(pos);
        //                Set<BlockPos> shelves = EnderShelfHelper.findConnectedShelves(world, controllerPos, controller.getCurrentFacing());
        //        controller.setShelves(shelves);
      }
    }
    if (stack.getTag() != null) {
      //to tile from tag 
      shelf.inventory.deserializeNBT(stack.getTag());
    }
  }

  @Override
  public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, BlockEntity tileentity, ItemStack stackToolUsed) {
    super.playerDestroy(world, player, pos, state, tileentity, stackToolUsed);
    ItemStack newStack = new ItemStack(this);
    if (tileentity instanceof TileEnderShelf) {
      TileEnderShelf shelf = (TileEnderShelf) tileentity;
      CompoundTag tileData = shelf.inventory.serializeNBT();
      //read from tile, write to itemstack 
      newStack.setTag(tileData);
    }
    UtilItemStack.drop(world, pos, newStack);
  }
}
