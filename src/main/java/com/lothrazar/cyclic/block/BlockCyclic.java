package com.lothrazar.cyclic.block;

import com.lothrazar.cyclic.net.PacketDisplayFluidMessage;
import com.lothrazar.cyclic.util.CapabilityUtil;
import net.neoforged.neoforge.network.PacketDistributor;
import com.lothrazar.library.block.EntityBlockFlib;
import com.lothrazar.library.util.SoundUtil;
import com.lothrazar.library.util.StringParseUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;

public class BlockCyclic extends EntityBlockFlib {

  public static final BooleanProperty LIT = BooleanProperty.create("lit");
  private boolean hasGui = false;
  private boolean hasFluidInteract = false;

  public BlockCyclic(Properties properties) {
    super(properties);
  }

  public static boolean never(BlockState bs, BlockGetter bg, BlockPos pos) {
    return false;
  }

  protected BlockCyclic setHasGui() {
    this.hasGui = true;
    return this;
  }

  protected BlockCyclic setHasFluidInteract() {
    this.hasFluidInteract = true;
    return this;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos p, BlockState st) {
    return null;
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState rotate(BlockState state, LevelAccessor world, BlockPos pos, Rotation direction) {
    if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
      Direction oldDir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
      Direction newDir = direction.rotate(oldDir);
      //still rotate on axis, if its valid
      if (newDir != Direction.UP && newDir != Direction.DOWN) {
        return state.setValue(BlockStateProperties.HORIZONTAL_FACING, newDir);
      }
    }
    if (state.hasProperty(BlockStateProperties.FACING)) {
      Direction oldDir = state.getValue(BlockStateProperties.FACING);
      Direction newDir = direction.rotate(oldDir);
      // rotate state on axis dir
      return state.setValue(BlockStateProperties.FACING, newDir);
    }
    // default doesnt do much
    BlockState newState = state.rotate(direction);
    return newState;
  }

//  protected ItemInteractionResult useItemOn(ItemStack p_316304_, BlockState p_316362_, Level p_316459_, BlockPos p_316366_, Player p_316132_, InteractionHand p_316595_, BlockHitResult p_316140_) {
  @Override
  public ItemInteractionResult useItemOn(ItemStack st, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
    if (hasFluidInteract) {
      if (!level.isClientSide) {
        BlockEntity tankHere = level.getBlockEntity(pos);
        if (tankHere != null) {
         //getting fluid capability from a block
          IFluidHandler handler = CapabilityUtil.fluid(level,pos,hit);
//breakpoint shows handler is null, even when blockhere is tileTank from a "Block{cyclic:tank}[above=false,below=false]"
          if (handler != null) {
            if (FluidUtil.interactWithFluidHandler(player, hand, handler)) {
              if (player instanceof ServerPlayer sp) {
                SoundUtil.playSoundFromServer(sp, SoundEvents.BUCKET_FILL, 1F, 1F);
                sp.inventoryMenu.broadcastChanges();
              }
              //success so display new amount
              if (handler.getFluidInTank(0) != null) {
                displayClientFluidMessage(player, handler);
              }
            }
            else {
              displayClientFluidMessage(player, handler);
            }
          }
        }
      }
      if (FluidUtil.getFluidHandler(player.getMainHandItem()).isPresent()) { // reverted to how 1.16.5 does it fix sapphys bug
        return ItemInteractionResult.SUCCESS;
      }
    }
    if (this.hasGui) {
      if (!level.isClientSide) {
        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity instanceof MenuProvider mp) {
          player.openMenu(mp, pos);
//          NetworkHooks.openScreen((ServerPlayer) player, (MenuProvider) tileEntity, tileEntity.getBlockPos());
        }
        else {
          throw new IllegalStateException("Our named container provider is missing!");
        }
      }
      return ItemInteractionResult.SUCCESS;
    }
    return super.useItemOn(st,state, level, pos, player, hand, hit);
  }

  @Override
  protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
    if (this.hasGui) {
      if (!level.isClientSide) {
        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity instanceof MenuProvider mp) {
          player.openMenu(mp, pos);
        } else {
          throw new IllegalStateException("Our named container provider is missing!");
        }
      }
      return InteractionResult.SUCCESS;
    }
    return super.useWithoutItem(state, level, pos, player, hit);
  }

  private void displayClientFluidMessage(Player player, IFluidHandler handler) {
    //compute the ratio key server-side, then send to the target client; the client's handler
    //checks the per-client config (ClientConfigCyclic.FLUID_BLOCK_STATUS) and displays.
    if (!(player instanceof ServerPlayer sp)) {
      return;
    }
    String key = StringParseUtil.getFluidRatioName(handler);
    PacketDistributor.sendToPlayer(sp, new PacketDisplayFluidMessage(key));
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    if (state.getBlock() != newState.getBlock()) {
      // Read inventory directly from the tile - at this point the block state has already
      // changed to newState, so level.getCapability() finds no provider (air has none) and
      // returns null. The block entity is still present until super.onRemove removes it.
      if (worldIn.getBlockEntity(pos) instanceof TileBlockEntityCyclic tile) {
        IItemHandler items = tile.getItemHandler(null);
        if (items != null) {
          for (int i = 0; i < items.getSlots(); ++i) {
            Containers.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), items.getStackInSlot(i));
          }
          worldIn.updateNeighbourForOutputSignal(pos, this);
        }
      }
      super.onRemove(state, worldIn, pos, newState, isMoving);
    }
  }

  //for comparators that dont use item inventories
  protected int calcRedstoneFromFluid(BlockEntity tileEntity) {
    final IFluidHandler fluid = CapabilityUtil.fluid(tileEntity.getLevel(), tileEntity.getBlockPos());//tileEntity.getCapability(ForgeCapabilities.FLUID_HANDLER).orElse(null);
    if (fluid.getFluidInTank(0).isEmpty()) {
      return 0;
    }
    final float cap = fluid.getTankCapacity(0);
    final float amt = fluid.getFluidInTank(0).getAmount();
    final float f = amt / cap;
    return (int) Math.floor((f * 14.0F)) + 1;
  }
}
