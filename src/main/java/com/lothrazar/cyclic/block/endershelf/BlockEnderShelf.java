package com.lothrazar.cyclic.block.endershelf;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilEnchant;
import com.lothrazar.cyclic.util.UtilStuff;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public class BlockEnderShelf extends BlockBase {
  public BlockEnderShelf(Properties properties) {
    super(properties);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void registerClient() {
    ClientRegistry.bindTileEntityRenderer(TileRegistry.ender_shelf, EnderShelfRenderer::new);
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
    return new TileEnderShelf();
  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
    if (entity != null) {
      world.setBlockState(pos, state.with(BlockStateProperties.HORIZONTAL_FACING, UtilStuff.getFacingFromEntityHorizontal(pos, entity)), 2);
    }
  }

  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
    ItemStack heldItem = player.getHeldItemMainhand();

    Direction face = hit.getFace();
    Vector3d hitVec = hit.getHitVec();
    int slot = getSlotFromHitVec(pos, face, hitVec);
    if (world.getTileEntity(pos) instanceof TileEnderShelf) {
      TileEnderShelf shelf = (TileEnderShelf) world.getTileEntity(pos);

      if (hand == Hand.MAIN_HAND && hit.getFace() == state.get(BlockStateProperties.HORIZONTAL_FACING)) {
        if (heldItem.getItem() == Items.ENCHANTED_BOOK) {
          shelf.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            if (h.getStackInSlot(slot) == ItemStack.EMPTY || UtilEnchant.doBookEnchantmentsMatch(h.getStackInSlot(slot), heldItem)) {
              if (!world.isRemote) {
                ItemStack remaining = h.insertItem(slot, heldItem, false);
              }
              player.setHeldItem(hand, ItemStack.EMPTY);
            }
          });
        }
        else if (heldItem.isEmpty()) {
          shelf.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            ItemStack retrievedBook = h.extractItem(slot, 1, false);
            player.setHeldItem(hand, retrievedBook);
          });
        }

      }
    }

    return super.onBlockActivated(state, world, pos, player, hand, hit);
  }

  private int getSlotFromHitVec(BlockPos pos, Direction face, Vector3d hitVec) {
    double normalizedY = hitVec.getY() - pos.getY();
    double normalizedX = hitVec.getX() - pos.getX();
    if (face == Direction.EAST || face == Direction.WEST)
      normalizedX = hitVec.getZ() - pos.getZ();
    int probableSlot = (int) Math.floor(normalizedY / 0.20);
    //System.out.printf("%d Hit at %f, %f%n", probableSlot, normalizedX, normalizedY);

    return probableSlot;
  }
}
