package com.lothrazar.cyclic.block.placerfluid;

import javax.annotation.Nullable;
import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.util.UtilSound;
import com.lothrazar.cyclic.util.UtilStuff;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class BlockPlacerFluid extends BlockBase {

  public BlockPlacerFluid(Properties properties) {
    super(properties.hardnessAndResistance(1.8F));
    this.setHasGui();
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void registerClient() {
    ScreenManager.registerFactory(BlockRegistry.ContainerScreenRegistry.placer_fluid, ScreenPlacerFluid::new);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TilePlacerFluid();
  }

  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
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
                  + handler.getFluidInTank(0).getAmount()
                  + "/" + handler.getTankCapacity(0)), true);
            }
            //and also play the fluid sound
            if (player instanceof ServerPlayerEntity) {
              UtilSound.playSoundFromServer((ServerPlayerEntity) player, SoundEvents.ITEM_BUCKET_FILL);
            }
          }
          else {
            player.sendStatusMessage(new TranslationTextComponent(""
                + handler.getFluidInTank(0).getAmount()
                + "/" + handler.getTankCapacity(0)), true);
          }
        }
      }
    }
    if (FluidUtil.getFluidHandler(player.getHeldItem(hand)).isPresent()) {
      return ActionResultType.SUCCESS;
    }
    return super.onBlockActivated(state, world, pos, player, hand, hit);
  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
    if (entity != null) {
      world.setBlockState(pos, state.with(BlockStateProperties.FACING, UtilStuff.getFacingFromEntity(pos, entity)), 2);
    }
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(BlockStateProperties.FACING).add(IS_LIT);
  }
}
