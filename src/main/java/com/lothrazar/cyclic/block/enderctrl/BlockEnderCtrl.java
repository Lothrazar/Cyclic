package com.lothrazar.cyclic.block.enderctrl;

import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.block.endershelf.TileEnderShelf;
import com.lothrazar.cyclic.block.endershelf.TileEnderShelf.RenderTextType;
import com.lothrazar.cyclic.data.DataTags;
import com.lothrazar.library.util.BlockstatesUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Set;

public class BlockEnderCtrl extends BlockCyclic {

  public BlockEnderCtrl(Properties properties) {
    super(properties.strength(1.0F));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(BlockStateProperties.HORIZONTAL_FACING);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TileEnderCtrl(pos, state);
  }

  @Override
  public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
    if (entity != null) {
      world.setBlock(pos, state.setValue(BlockStateProperties.HORIZONTAL_FACING, BlockstatesUtil.getFacingFromEntityHorizontal(pos, entity)), 2);
      TileEnderCtrl ctrl = (TileEnderCtrl) world.getBlockEntity(pos);
      //i placed a shelf? find nearby
      if (ctrl != null) {
        Set<BlockPos> shlf = EnderShelfHelper.findConnectedShelves(world, pos, ctrl.getCurrentFacing());
        ctrl.setAndSort(shlf);
      }
    }
  }

  @Override
  protected void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean movedByPiston) {
    super.onPlace(state, worldIn, pos, oldState, movedByPiston);
    // same-block state-property transition (e.g. shelf flag toggling off) - onRemove used to also fire for
    // this case, now only onPlace does, since affectNeighborsAfterRemoval is guaranteed block-changed only
    boolean wasShelf = EnderShelfHelper.isShelf(oldState);
    boolean isCurrentlyShelf = EnderShelfHelper.isShelf(state);
    TileEnderCtrl ctrl = (TileEnderCtrl) worldIn.getBlockEntity(pos);
    if (wasShelf && !isCurrentlyShelf && ctrl != null) {
      //trigger controller reindex
      ctrl.setAndSort(EnderShelfHelper.findConnectedShelves(worldIn, pos, ctrl.getCurrentFacing()));
    }
  }

  @Override
  protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel worldIn, BlockPos pos, boolean movedByPiston) {
    worldIn.removeBlockEntity(pos);
    worldIn.updateNeighbourForOutputSignal(pos, this);
  }

  @Override
  public InteractionResult useItemOn(ItemStack st, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
    ItemStack heldItem = player.getMainHandItem();
    if (false && heldItem.isEmpty()) {
      //if your hand is empty, dont process if its the OFF hand
      //otherwise: main hand inserts, off hand takes out right away
      return InteractionResult.PASS;
    }
    if (heldItem.is(DataTags.WRENCH)) {
      TileEnderCtrl contrl = (TileEnderCtrl) world.getBlockEntity(pos);
      contrl.toggleShowText();
      RenderTextType nt = contrl.renderStyle;
      for (BlockPos shelf : contrl.getShelves()) {
        BlockEntity shelfy = world.getBlockEntity(shelf);
        if (shelfy instanceof TileEnderShelf) {
          ((TileEnderShelf) shelfy).renderStyle = nt;
        }
      }
      player.swing(InteractionHand.MAIN_HAND);
      return InteractionResult.PASS;
    }
    if (heldItem.getItem() == Items.ENCHANTED_BOOK) {
      if (!world.isClientSide()) {
        TileEnderCtrl ctrl = (TileEnderCtrl) world.getBlockEntity(pos);
        if (ctrl != null) {
          ItemStack remaining = ctrl.controllerInv.insertItem(0, heldItem, false);
          player.setItemInHand(InteractionHand.MAIN_HAND, remaining);
          player.swing(InteractionHand.MAIN_HAND);
        }
      }
      return InteractionResult.CONSUME;
    }
    return InteractionResult.CONSUME;
  }
}
