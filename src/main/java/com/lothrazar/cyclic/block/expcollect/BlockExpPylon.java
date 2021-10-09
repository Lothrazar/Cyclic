package com.lothrazar.cyclic.block.expcollect;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.fluid.FluidXpJuiceHolder;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class BlockExpPylon extends BlockBase {

  public BlockExpPylon(Properties properties) {
    super(properties.strength(1.8F).sound(SoundType.GLASS).noOcclusion());
    this.setHasGui();
  }

  @Override
  public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter world, BlockPos pos, FluidState fluidState) {
    return true;
  }

  @Override
  public void registerClient() {
    ItemBlockRenderTypes.setRenderLayer(this, RenderType.cutoutMipped());
    MenuScreens.register(ContainerScreenRegistry.EXPERIENCE_PYLON, ScreenExpPylon::new);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TileExpPylon(pos, state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
    return createTickerHelper(type, TileRegistry.experience_pylontile, world.isClientSide ? TileExpPylon::clientTick : TileExpPylon::serverTick);
  }

  @Override
  public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
    if (!world.isClientSide) {
      TileExpPylon pylon = (TileExpPylon) world.getBlockEntity(pos);
      int fluidPerAction = ExpItemGain.EXP_PER_FOOD * ExpItemGain.FLUID_PER_EXP;
      if (player.getMainHandItem().getItem() == Items.SUGAR) {
        if (pylon.tank.getFluidAmount() >= fluidPerAction) {
          pylon.tank.drain(fluidPerAction, IFluidHandler.FluidAction.EXECUTE);
          player.addItem(new ItemStack(ItemRegistry.experience_food, 1));
          player.getMainHandItem().shrink(1);
          world.playSound((Player) null, pos, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.NEUTRAL, 0.5F, world.random.nextFloat());
        }
        return InteractionResult.SUCCESS;
      }
      else if (player.getMainHandItem().getItem() == ItemRegistry.experience_food) {
        if (pylon.tank.getFluidAmount() + fluidPerAction < pylon.tank.getCapacity()) {
          pylon.tank.fill(new FluidStack(FluidXpJuiceHolder.STILL.get(), fluidPerAction), IFluidHandler.FluidAction.EXECUTE);
          player.getMainHandItem().shrink(1);
          world.playSound((Player) null, pos, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.NEUTRAL, 0.2F, world.random.nextFloat());
        }
        return InteractionResult.SUCCESS;
      }
    }
    return super.use(state, world, pos, player, hand, result);
  }
}
