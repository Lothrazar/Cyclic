package com.lothrazar.cyclic.block.enderitemshelf;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.block.endershelf.BlockEnderShelf;
import com.lothrazar.cyclic.data.DataTags;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.library.util.BlockstatesUtil;
import com.lothrazar.library.util.ItemStackUtil;
import com.lothrazar.library.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class BlockItemShelf extends BlockCyclic {

  public BlockItemShelf(Properties properties) {
    super(properties.strength(0.8F).noOcclusion());
  }

  @Override
  public void registerClient() {}

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(BlockStateProperties.HORIZONTAL_FACING);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TileItemShelf(pos, state);
  }

  //  @Override
  //  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
  //    return createTickerHelper(type,TileRegistry.ENDER_ITEM_SHELF.get(), world.isClientSide ? TileItemShelf::clientTick : TileItemShelf::serverTick);
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
    if (hand != InteractionHand.MAIN_HAND) {
      //if your hand is empty, dont process if its the OFF hand
      //otherwise: main hand inserts, off hand takes out right away
      return InteractionResult.PASS;
    }
    TileItemShelf shelf = getTileEntity(world, pos);
    if (heldItem.is(DataTags.WRENCH)) {
      //wrench tag
      shelf.toggleShowText();
      player.swing(hand);
      return InteractionResult.PASS;
    }
    Direction face = hit.getDirection();
    Vec3 hitVec = hit.getLocation();
    int slot = BlockEnderShelf.getSlotFromHitVec(pos, face, hitVec);
    if (hit.getDirection() == state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
      //
      // single shelf
      ItemStack shelfStack = shelf.inventory.getStackInSlot(slot);
      //first , check if deposit spot is empty
      if (shelfStack.isEmpty() || heldItem.getItem() == shelfStack.getItem()) {
        //try to insert  
        boolean oldEmpty = shelfStack.isEmpty();
        ItemStack remaining = shelf.inventory.insertItem(slot, heldItem, false);
        if (remaining.isEmpty() || remaining.getCount() != shelfStack.getCount()) {
          player.setItemInHand(hand, remaining);
          player.swing(hand);
          SoundUtil.playSound(player, SoundRegistry.CRACKLE.get(), oldEmpty ? 0.3F : 0.1F, 0.3F);
          //          UtilSound.playSound(player, SoundRegistry.POW, 0.06F, 0.3F);
          //          UtilSound.playSound(player, SoundRegistry.GUITAR, 0.1F, 0.3F);
          return InteractionResult.CONSUME;
        }
      }
      if (heldItem.isEmpty()) {
        //withdraw direct to players empty hand
        int q = player.isCrouching() ? 1 : 64;
        ItemStack retrieved = shelf.inventory.extractItem(slot, q, false);
        player.setItemInHand(hand, retrieved);
        player.swing(hand);
      }
      if (!shelfStack.isEmpty() && !heldItem.isEmpty()) {
        //
        ItemStack forShelf = heldItem.copy();
        //        ItemStack forPlayer = shelfStack.copy();
        //extract all from shelf
        ItemStack forPlayer = shelf.inventory.extractItem(slot, 64, false);
        player.setItemInHand(hand, forPlayer);
        player.swing(hand);
        shelf.inventory.insertItem(slot, forShelf, false);
      }
      return InteractionResult.SUCCESS;
    }
    return InteractionResult.PASS;
  }

  public TileItemShelf getTileEntity(Level world, BlockPos pos) {
    return (TileItemShelf) world.getBlockEntity(pos);
  }

  @Override
  public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
    // because harvestBlock manually forces a drop, we must do this to dodge that0
    return new ArrayList<>();
  }

  @Override
  public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
    if (entity != null) {
      //facing state if needed 
      world.setBlock(pos, state.setValue(BlockStateProperties.HORIZONTAL_FACING, BlockstatesUtil.getFacingFromEntityHorizontal(pos, entity)), 2);
    }
    BlockEntity tileentity = world.getBlockEntity(pos);
    TileItemShelf shelf = (TileItemShelf) tileentity;
    if (stack.getTag() != null) {
      //to tile from tag 
      shelf.inventory.deserializeNBT(stack.getTag());
    }
  }

  @Override
  public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, BlockEntity tileentity, ItemStack stackToolUsed) {
    super.playerDestroy(world, player, pos, state, tileentity, stackToolUsed);
    ItemStack newStack = new ItemStack(this);
    if (tileentity instanceof TileItemShelf) {
      TileItemShelf shelf = (TileItemShelf) tileentity;
      CompoundTag tileData = shelf.inventory.serializeNBT();
      //read from tile, write to itemstack 
      newStack.setTag(tileData);
    }
    ItemStackUtil.dropItemStackMotionless(world, pos, newStack);
  }
}
