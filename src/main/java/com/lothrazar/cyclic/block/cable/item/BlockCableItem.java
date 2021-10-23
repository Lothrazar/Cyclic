package com.lothrazar.cyclic.block.cable.item;

import com.lothrazar.cyclic.block.cable.CableBase;
import com.lothrazar.cyclic.block.cable.EnumConnectType;
import com.lothrazar.cyclic.block.cable.ShapeCache;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BlockCableItem extends CableBase {

  public BlockCableItem(Properties properties) {
    super(properties.hardnessAndResistance(0.5F));
  }

  @Override
  public void registerClient() {
    ScreenManager.registerFactory(ContainerScreenRegistry.item_pipe, ScreenCableItem::new);
  }

  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
    if (world.isRemote) {
      TileEntity ent = world.getTileEntity(pos);
      for (Direction d : Direction.values()) {
        IItemHandler handlerHere = ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, d).orElse(null);
        //show current
        if (handlerHere != null) {
          ItemStack current = handlerHere.getStackInSlot(0);
          if (!current.isEmpty()) {
            player.sendMessage(new TranslationTextComponent(d.toString() + " " + current.getDisplayName().getString()), player.getUniqueID());
          }
        }
      }
    }
    return super.onBlockActivated(state, world, pos, player, hand, hit);
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return ShapeCache.getOrCreate(state, CableBase::createShape);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    if (state.getBlock() != newState.getBlock()) {
      TileCableItem tileentity = (TileCableItem) worldIn.getTileEntity(pos);
      if (tileentity != null && tileentity.filter != null) {
        InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), tileentity.filter.getStackInSlot(0));
      }
      for (Direction dir : Direction.values()) {
        IItemHandler items = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir).orElse(null);
        if (items != null) {
          for (int i = 0; i < items.getSlots(); ++i) {
            InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), items.getStackInSlot(i));
          }
        }
      }
      worldIn.updateComparatorOutputLevel(pos, this);
    }
    super.onReplaced(state, worldIn, pos, newState, isMoving);
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileCableItem();
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    super.fillStateContainer(builder);
    builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST);
  }

  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState stateIn, LivingEntity placer, ItemStack stack) {
    for (Direction d : Direction.values()) {
      TileEntity facingTile = worldIn.getTileEntity(pos.offset(d));
      IItemHandler cap = facingTile == null ? null : facingTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, d.getOpposite()).orElse(null);
      if (cap != null) {
        stateIn = stateIn.with(FACING_TO_PROPERTY_MAP.get(d), EnumConnectType.INVENTORY);
      }
    }
    super.onBlockPlacedBy(worldIn, pos, stateIn, placer, stack);
  }

  @Override
  public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
    EnumProperty<EnumConnectType> property = FACING_TO_PROPERTY_MAP.get(facing);
    EnumConnectType oldProp = stateIn.get(property);
    if (oldProp.isBlocked() || oldProp.isExtraction()) {
      return stateIn;
    }
    if (isItem(stateIn, facing, facingState, world, currentPos, facingPos)) {
      BlockState with = stateIn.with(property, EnumConnectType.INVENTORY);
      if (world instanceof World && world.getBlockState(currentPos).getBlock() == this) {
        //hack to force {any} -> inventory IF its here
        ((World) world).setBlockState(currentPos, with);
      }
      return with;
    }
    else {
      return stateIn.with(property, EnumConnectType.NONE);
    }
  }
}
