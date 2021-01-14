package com.lothrazar.cyclic.block.expcollect;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.fluid.FluidXpJuiceHolder;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class BlockExpPylon extends BlockBase {

  public BlockExpPylon(Properties properties) {
    super(properties.hardnessAndResistance(1.8F).sound(SoundType.GLASS).notSolid());
    this.setHasGui();
  }

  @Override
  public boolean shouldDisplayFluidOverlay(BlockState state, IBlockDisplayReader world, BlockPos pos, FluidState fluidState) {
    return true;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void registerClient() {
    RenderTypeLookup.setRenderLayer(this, RenderType.getCutoutMipped());
    ScreenManager.registerFactory(ContainerScreenRegistry.experience_pylon, ScreenExpPylon::new);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileExpPylon();
  }

  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
    if (!world.isRemote) {
      TileExpPylon pylon = (TileExpPylon) world.getTileEntity(pos);
      int fluidPerAction = ExpItemGain.EXP_PER_FOOD * ExpItemGain.FLUID_PER_EXP;
      if (player.getHeldItemMainhand().getItem() == Items.SUGAR) {
        if (pylon.tank.getFluidAmount() >= fluidPerAction) {
          pylon.tank.drain(fluidPerAction, IFluidHandler.FluidAction.EXECUTE);
          player.addItemStackToInventory(new ItemStack(ItemRegistry.experience_food, 1));
          player.getHeldItemMainhand().shrink(1);
          world.playSound((PlayerEntity) null, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.NEUTRAL, 0.5F, world.rand.nextFloat());
        }
        return ActionResultType.SUCCESS;
      }
      else if (player.getHeldItemMainhand().getItem() == ItemRegistry.experience_food) {
        if (pylon.tank.getFluidAmount() + fluidPerAction < pylon.tank.getCapacity()) {
          pylon.tank.fill(new FluidStack(FluidXpJuiceHolder.STILL.get(), fluidPerAction), IFluidHandler.FluidAction.EXECUTE);
          player.getHeldItemMainhand().shrink(1);
          world.playSound((PlayerEntity) null, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.NEUTRAL, 0.2F, world.rand.nextFloat());
        }
        return ActionResultType.SUCCESS;
      }
    }
    return super.onBlockActivated(state, world, pos, player, hand, result);
  }
}
