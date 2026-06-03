package com.lothrazar.cyclic.block.enderctrl;

import java.util.Set;
import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.block.endershelf.TileEnderShelf;
import com.lothrazar.cyclic.block.endershelf.TileEnderShelf.RenderTextType;
import com.lothrazar.cyclic.data.DataTags;
import com.lothrazar.cyclic.util.CapabilityUtil;
import com.lothrazar.library.util.BlockstatesUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
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
import net.neoforged.neoforge.items.IItemHandler;

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
  public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    boolean isCurrentlyShelf = EnderShelfHelper.isShelf(state);
    boolean isNewShelf = EnderShelfHelper.isShelf(newState);
    TileEnderCtrl ctrl = (TileEnderCtrl) worldIn.getBlockEntity(pos);
    if (isCurrentlyShelf && !isNewShelf && ctrl != null) {
      //trigger controller reindex
      ctrl.setAndSort(EnderShelfHelper.findConnectedShelves(worldIn, pos, ctrl.getCurrentFacing()));
    }
    if (state.getBlock() != newState.getBlock()) {
      worldIn.removeBlockEntity(pos);
      worldIn.updateNeighbourForOutputSignal(pos, this);
    }
  }

  @Override
  public ItemInteractionResult useItemOn(ItemStack st,BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
    ItemStack heldItem = player.getMainHandItem();
    if (false && heldItem.isEmpty()) {
      //if your hand is empty, dont process if its the OFF hand
      //otherwise: main hand inserts, off hand takes out right away
      return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
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
      return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
    if (heldItem.getItem() == Items.ENCHANTED_BOOK) {
      var h = CapabilityUtil.item(world,pos);
//      world.getBlockEntity(pos).getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(h -> {
        insertIntoController(player, InteractionHand.MAIN_HAND, heldItem, h);
//      });
    }
    return ItemInteractionResult.CONSUME;
  }

  private void insertIntoController(Player player, InteractionHand hand, ItemStack heldItem, IItemHandler controller) {
  }
}
