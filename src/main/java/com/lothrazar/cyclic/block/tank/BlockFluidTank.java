package com.lothrazar.cyclic.block.tank;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class BlockFluidTank extends BlockBase {

  public static final BooleanProperty TANK_ABOVE = BooleanProperty.create("above");
  public static final BooleanProperty TANK_BELOW = BooleanProperty.create("below");
  public static final int heightCheckMax = 16;

  public BlockFluidTank(Properties properties) {
    super(properties.harvestTool(ToolType.PICKAXE).hardnessAndResistance(1.2F));
    //    this.getExtendedState(state, world, pos)
    //    this.setDefaultState(this.getDefaultState().with(TANK_ABOVE, false).with(TANK_BELOW, false));
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    super.fillStateContainer(builder);
    //    builder.add(TANK_ABOVE, TANK_BELOW);
  }
  //  @Override
  //  public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos pos, BlockPos facingPos) {
  //    boolean tileAbove = world.getTileEntity(pos.up()) instanceof TileTank;
  //    boolean tileBelow = world.getTileEntity(pos.down()) instanceof TileTank;
  //    return state
  //        .with(TANK_ABOVE, tileAbove)
  //        .with(TANK_BELOW, tileBelow);
  //  }

  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.CUTOUT;
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileTank();
  }

  @Override
  public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
    if (!world.isRemote) {
      TileEntity tankHere = world.getTileEntity(pos);
      if (tankHere != null) {
        IFluidHandler handler = tankHere.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, hit.getFace()).orElse(null);
        if (handler != null) {
          //          FluidStack tankFluidBefore = tankHere.getFluid().copy();
          if (FluidUtil.interactWithFluidHandler(player, hand, handler)) {
            //success so display new amount
            if (handler.getFluidInTank(0) != null) {
              player.sendStatusMessage(new TranslationTextComponent(""
                  + handler.getFluidInTank(0).getAmount()), true);
            }
            //and also play the fluid sound
            if (player instanceof ServerPlayerEntity) {
              UtilSound.playSoundFromServer((ServerPlayerEntity) player, SoundEvents.ITEM_BUCKET_FILL);
            }
          }
        }
      }
    }
    if (FluidUtil.getFluidHandler(player.getHeldItem(hand)).isPresent()) {
      return true;
    }
    return super.onBlockActivated(state, world, pos, player, hand, hit);
  }
}
