package com.lothrazar.cyclic.block.endershelf;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.block.enderctrl.EnderShelfHelper;
import com.lothrazar.cyclic.block.enderctrl.TileEnderCtrl;
import com.lothrazar.cyclic.data.DataTags;
import com.lothrazar.library.util.BlockstatesUtil;
import com.lothrazar.library.util.EnchantUtil;
import com.lothrazar.library.util.ItemStackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
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
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

public class BlockEnderShelf extends BlockCyclic {

  public BlockEnderShelf(Properties properties) {
    super(properties.strength(1.2F).noOcclusion());
  }

  @Override
  public float getEnchantPowerBonus(BlockState state, LevelReader world, BlockPos pos) {
    return 3 * Blocks.BOOKSHELF.getEnchantPowerBonus(Blocks.BOOKSHELF.defaultBlockState(), world, pos);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(BlockStateProperties.HORIZONTAL_FACING);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TileEnderShelf(pos, state);
  }

  @Override
  public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {

    if (!state.is(newState.getBlock())) {
      worldIn.removeBlockEntity(pos);
    }
  }

  @Override
  public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
    ItemStack heldItem = player.getMainHandItem();
    if (false) { // removed: was hand check
      return InteractionResult.PASS;
    }
    TileEnderShelf shelf = getTileEntity(world, pos);
    if (heldItem.is(DataTags.WRENCH)) {
      //wrench tag
      shelf.toggleShowText();
      player.swing(InteractionHand.MAIN_HAND);
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
        if (stackInSlot == ItemStack.EMPTY || EnchantUtil.doBookEnchantmentsMatch(stackInSlot, heldItem)) {
          if (!world.isClientSide()) {
            ItemStack remaining = shelf.inventory.insertItem(slot, heldItem, false);
            player.setItemInHand(InteractionHand.MAIN_HAND, remaining);
            player.swing(InteractionHand.MAIN_HAND);
            return InteractionResult.SUCCESS;
          }
        }
      }
      else if (heldItem.isEmpty()) {
        ItemStack retrievedBook = shelf.inventory.extractItem(slot, 1, false);
        player.setItemInHand(InteractionHand.MAIN_HAND, retrievedBook);
        player.swing(InteractionHand.MAIN_HAND);
        return InteractionResult.SUCCESS;
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

  @Override
  public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
    // because harvestBlock manually forces a drop, we must do this to dodge that
    return new ArrayList<>();
  }

  @Override
  public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
    if (entity != null) {
      //facing state if needed 
      world.setBlock(pos, state.setValue(BlockStateProperties.HORIZONTAL_FACING, BlockstatesUtil.getFacingFromEntityHorizontal(pos, entity)), 2);
    }
    BlockEntity tileentity = world.getBlockEntity(pos);
    TileEnderShelf shelf = (TileEnderShelf) tileentity;
    BlockPos controllerPos = EnderShelfHelper.findConnectedController(world, pos);
    if (controllerPos != null) {
      TileEnderCtrl controller = (TileEnderCtrl) world.getBlockEntity(controllerPos);
      if (controllerPos != null && controller != null) {
        controller.getShelves().add(pos);
      }
    }
    CompoundTag stored = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    if (stored != null && !stored.isEmpty()) {
      //to tile from tag
      shelf.inventory.deserialize(TagValueInput.create(ProblemReporter.DISCARDING, world.registryAccess(), stored));
    }
  }

  @Override
  public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, BlockEntity tileentity, ItemStack stackToolUsed) {
    super.playerDestroy(world, player, pos, state, tileentity, stackToolUsed);
    ItemStack newStack = new ItemStack(this);
    if (tileentity instanceof TileEnderShelf) {
      TileEnderShelf shelf = (TileEnderShelf) tileentity;
      //read from tile, write to itemstack
      TagValueOutput shelfOutput = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, world.registryAccess());
      shelf.inventory.serialize(shelfOutput);
      CompoundTag tileData = shelfOutput.buildResult();
      newStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tileData));
      // newStack.setTag(tileData); // disabled
    }
    ItemStackUtil.dropItemStackMotionless(world, pos, newStack);
  }
}
