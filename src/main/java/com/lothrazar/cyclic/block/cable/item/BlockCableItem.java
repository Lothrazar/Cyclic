package com.lothrazar.cyclic.block.cable.item;

import com.lothrazar.cyclic.block.cable.CableBase;
import com.lothrazar.cyclic.block.cable.EnumConnectType;
import com.lothrazar.cyclic.block.cable.ShapeCache;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BlockCableItem extends CableBase {

  public BlockCableItem(Properties properties) {
    super(properties.strength(0.5F));
  }

  @Override
  public void registerClient() {
    MenuScreens.register(MenuTypeRegistry.ITEM_PIPE.get(), ScreenCableItem::new);
  }

  @Override
  public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
    if (world.isClientSide) {
      BlockEntity ent = world.getBlockEntity(pos);
      for (Direction d : Direction.values()) {
        IItemHandler handlerHere = ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, d).orElse(null);
        //show current
        if (handlerHere != null) {
          ItemStack current = handlerHere.getStackInSlot(0);
          if (!current.isEmpty()) {
            player.sendMessage(new TranslatableComponent(d.toString() + " " + current.getHoverName().getString()), player.getUUID());
          }
        }
      }
    }
    return super.use(state, world, pos, player, hand, hit);
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    return ShapeCache.getOrCreate(state, CableBase::createShape);
  }

  @Override
  public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    if (state.getBlock() != newState.getBlock()) {
      TileCableItem tileentity = (TileCableItem) worldIn.getBlockEntity(pos);
      if (tileentity != null) {
        if (tileentity.filter != null) {
          Containers.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), tileentity.filter.getStackInSlot(0));
        }
        for (Direction dir : Direction.values()) {
          IItemHandler items = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir).orElse(null);
          if (items != null) {
            for (int i = 0; i < items.getSlots(); ++i) {
              Containers.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), items.getStackInSlot(i));
            }
          }
        }
      }
      worldIn.updateNeighbourForOutputSignal(pos, this);
    }
    super.onRemove(state, worldIn, pos, newState, isMoving);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TileCableItem(pos, state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
    return createTickerHelper(type, TileRegistry.ITEM_PIPE.get(), world.isClientSide ? TileCableItem::clientTick : TileCableItem::serverTick);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST);
  }

  @Override
  public void setPlacedBy(Level worldIn, BlockPos pos, BlockState stateIn, LivingEntity placer, ItemStack stack) {
    for (Direction d : Direction.values()) {
      BlockEntity facingTile = worldIn.getBlockEntity(pos.relative(d));
      IItemHandler cap = facingTile == null ? null : facingTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, d.getOpposite()).orElse(null);
      if (cap != null) {
        stateIn = stateIn.setValue(FACING_TO_PROPERTY_MAP.get(d), EnumConnectType.INVENTORY);
        worldIn.setBlockAndUpdate(pos, stateIn);
      }
    }
    super.setPlacedBy(worldIn, pos, stateIn, placer, stack);
  }

  @Override
  public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
    EnumProperty<EnumConnectType> property = FACING_TO_PROPERTY_MAP.get(facing);
    EnumConnectType oldProp = stateIn.getValue(property);
    if (oldProp.isBlocked() || oldProp.isExtraction()) {
      return stateIn;
    }
    if (isItem(stateIn, facing, facingState, world, currentPos, facingPos)) {
      BlockState with = stateIn.setValue(property, EnumConnectType.INVENTORY);
      if (world instanceof Level && world.getBlockState(currentPos).getBlock() == this) {
        //hack to force {any} -> inventory IF its here
        ((Level) world).setBlockAndUpdate(currentPos, with);
      }
      return with;
    }
    else {
      return stateIn.setValue(property, EnumConnectType.NONE);
    }
  }
}
