package com.lothrazar.cyclic.block.enderctrl;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.block.endershelf.TileEnderShelf;
import com.lothrazar.cyclic.block.endershelf.TileEnderShelf.RenderTextType;
import com.lothrazar.cyclic.data.DataTags;
import com.lothrazar.cyclic.util.UtilBlockstates;
import java.util.Map;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BlockEnderCtrl extends BlockBase {

  public BlockEnderCtrl(Properties properties, boolean isController) {
    super(properties.hardnessAndResistance(1.0F));
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(BlockStateProperties.HORIZONTAL_FACING);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileEnderCtrl();
  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
    if (entity != null) {
      world.setBlockState(pos, state.with(BlockStateProperties.HORIZONTAL_FACING, UtilBlockstates.getFacingFromEntityHorizontal(pos, entity)), 2);
      TileEnderCtrl ctrl = (TileEnderCtrl) world.getTileEntity(pos);
      //i placed a shelf? find nearby
      if (ctrl != null) {
        Set<BlockPos> shlf = EnderShelfHelper.findConnectedShelves(world, pos, ctrl.getCurrentFacing());
        ctrl.setShelves(shlf);
      }
    }
  }

  @Override
  public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    boolean isCurrentlyShelf = EnderShelfHelper.isShelf(state);
    boolean isNewShelf = EnderShelfHelper.isShelf(newState);
    TileEnderCtrl ctrl = (TileEnderCtrl) worldIn.getTileEntity(pos);
    if (isCurrentlyShelf && !isNewShelf && ctrl != null) {
      //trigger controller reindex
      Set<BlockPos> shlf = EnderShelfHelper.findConnectedShelves(worldIn, pos, ctrl.getCurrentFacing());
      ctrl.setShelves(shlf);
    }
    if (state.getBlock() != newState.getBlock()) {
      worldIn.removeTileEntity(pos);
      worldIn.updateComparatorOutputLevel(pos, this);
    }
  }

  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
    ItemStack heldItem = player.getHeldItem(hand);
    if (hand != Hand.MAIN_HAND && heldItem.isEmpty()) {
      //if your hand is empty, dont process if its the OFF hand
      //otherwise: main hand inserts, off hand takes out right away
      return ActionResultType.PASS;
    }
    if (heldItem.getItem().isIn(DataTags.WRENCH)) {
      TileEnderCtrl contrl = (TileEnderCtrl) world.getTileEntity(pos);
      contrl.toggleShowText();
      RenderTextType nt = contrl.renderStyle;
      for (BlockPos shelf : contrl.getShelves()) {
        TileEntity shelfy = world.getTileEntity(shelf);
        if (shelfy instanceof TileEnderShelf) {
          ((TileEnderShelf) shelfy).renderStyle = nt;
        }
      }
      player.swingArm(Hand.MAIN_HAND);
      return ActionResultType.PASS;
    }
    if (heldItem.getItem() == Items.ENCHANTED_BOOK) {
      world.getTileEntity(pos).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
        insertIntoController(player, hand, heldItem, h);
      });
    }
    return ActionResultType.CONSUME;
  }

  private void insertIntoController(PlayerEntity player, Hand hand, ItemStack heldItem, IItemHandler controller) {
    Map<Enchantment, Integer> allofthem = EnchantmentHelper.getEnchantments(heldItem);
    if (allofthem == null || allofthem.size() == 0) {
      return;
    }
    if (allofthem.size() == 1) {
      ItemStack insertResult = controller.insertItem(0, heldItem, false);
      player.setHeldItem(hand, insertResult);
    }
    else {
      //loop and make books of each, if we have any 
      Enchantment[] flatten = allofthem.keySet().toArray(new Enchantment[0]);
      for (Enchantment entry : flatten) {
        // try it
        ItemStack fake = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantedBookItem.addEnchantment(fake, new EnchantmentData(entry, allofthem.get(entry)));
        ItemStack insertResult = controller.insertItem(0, fake, false);
        if (insertResult.isEmpty()) {
          //ok it worked, so REMOVE that from the og set
          allofthem.remove(entry);
        }
      }
      //now set it back into the book
      if (allofthem.isEmpty()) {
        player.setHeldItem(hand, ItemStack.EMPTY);
      }
      else {
        //        apply all to the book and give the book back
        ItemStack newFake = new ItemStack(Items.ENCHANTED_BOOK);
        for (Enchantment newentry : allofthem.keySet()) {
          EnchantedBookItem.addEnchantment(newFake, new EnchantmentData(newentry, allofthem.get(newentry)));
        }
        player.setHeldItem(hand, newFake);
      }
    }
  }
}
