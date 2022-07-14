package com.lothrazar.cyclic.block.enderctrl;

import java.util.Map;
import java.util.Set;
import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.block.endershelf.TileEnderShelf;
import com.lothrazar.cyclic.block.endershelf.TileEnderShelf.RenderTextType;
import com.lothrazar.cyclic.data.DataTags;
import com.lothrazar.cyclic.util.BlockstatesUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

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
  public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
    ItemStack heldItem = player.getItemInHand(hand);
    if (hand != InteractionHand.MAIN_HAND && heldItem.isEmpty()) {
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
      world.getBlockEntity(pos).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
        insertIntoController(player, hand, heldItem, h);
      });
    }
    return InteractionResult.CONSUME;
  }

  private void insertIntoController(Player player, InteractionHand hand, ItemStack heldItem, IItemHandler controller) {
    Map<Enchantment, Integer> allofthem = EnchantmentHelper.getEnchantments(heldItem);
    if (allofthem == null || allofthem.size() == 0) {
      return;
    }
    if (allofthem.size() == 1) {
      ItemStack insertResult = controller.insertItem(0, heldItem, false);
      player.setItemInHand(hand, insertResult);
    }
    else {
      //loop and make books of each, if we have any 
      Enchantment[] flatten = allofthem.keySet().toArray(new Enchantment[0]);
      for (Enchantment entry : flatten) {
        // try it
        ItemStack fake = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantedBookItem.addEnchantment(fake, new EnchantmentInstance(entry, allofthem.get(entry)));
        ItemStack insertResult = controller.insertItem(0, fake, false);
        if (insertResult.isEmpty()) {
          //ok it worked, so REMOVE that from the og set
          allofthem.remove(entry);
        }
      }
      //now set it back into the book
      if (allofthem.isEmpty()) {
        player.setItemInHand(hand, ItemStack.EMPTY);
      }
      else {
        //        apply all to the book and give the book back
        ItemStack newFake = new ItemStack(Items.ENCHANTED_BOOK);
        for (Enchantment newentry : allofthem.keySet()) {
          EnchantedBookItem.addEnchantment(newFake, new EnchantmentInstance(newentry, allofthem.get(newentry)));
        }
        player.setItemInHand(hand, newFake);
      }
    }
  }
}
