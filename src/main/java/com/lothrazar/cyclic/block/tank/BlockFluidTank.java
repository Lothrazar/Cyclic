package com.lothrazar.cyclic.block.tank;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.capability.FluidHandlerCapabilityStack;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class BlockFluidTank extends BlockBase {

  public static final BooleanProperty TANK_ABOVE = BooleanProperty.create("above");
  public static final BooleanProperty TANK_BELOW = BooleanProperty.create("below");
  public static final int heightCheckMax = 16;

  public BlockFluidTank(Properties properties) {
    super(properties.harvestTool(ToolType.PICKAXE).hardnessAndResistance(1.2F)
        .func_226896_b_());
  }

  @Override
  public float func_220080_a(BlockState state, IBlockReader worldIn, BlockPos pos) {
    return 1.0f;
  }
  //  @Override
  //  public BlockRenderLayer getRenderLayer() {
  //    return BlockRenderLayer.CUTOUT;
  //  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
    return false;
  }
  //  @Override
  //  public boolean canRenderInLayer(BlockState state, BlockRenderLayer layer) {
  //    return (layer == BlockRenderLayer.CUTOUT_MIPPED || layer == BlockRenderLayer.TRANSLUCENT);
  //  }
  //  @Override
  //  public boolean doesSideBlockRendering(BlockState state, IEnviromentBlockReader world, BlockPos pos, Direction face) {
  //    return false;
  //  }

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
  public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
    TileEntity tileEntity = reader.getTileEntity(pos);
    //    TankBlockEntity tankEntity = (tileEntity != null) ? (TankBlockEntity)tileEntity : null;
    return true;// (tankEntity == null || tankEntity.getFillLevel() == 0);
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
  public ActionResultType func_225533_a_(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
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
        }
      }
    }
    if (FluidUtil.getFluidHandler(player.getHeldItem(hand)).isPresent()) {
      return ActionResultType.SUCCESS;
    }
    return super.func_225533_a_(state, world, pos, player, hand, hit);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void registerClient() {
    RenderTypeLookup.setRenderLayer(this, RenderType.func_228645_f_());
    //    ClientRegistry.bindTileEntityRenderer(TileTank.class, new RenderTank());
  }

  @Override
  public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
    //because harvestBlock manually forces a drop 
    return new ArrayList<>();
  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    try {
      IFluidHandlerItem storage = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).orElse(null);
      TileEntity container = world.getTileEntity(pos);
      if (storage != null && container != null) {
        IFluidHandler storageTile = container.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).orElse(null);
        if (storageTile != null) {
          storageTile.fill(storage.getFluidInTank(0), FluidAction.EXECUTE);
        }
      }
    }
    catch (Exception e) {
      //
      ModCyclic.LOGGER.error("fill from item ", e);
    }
  }

  @Override
  public void harvestBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity ent, ItemStack stack) {
    super.harvestBlock(world, player, pos, state, ent, stack);
    ItemStack tankStack = new ItemStack(this);
    if (ent != null) {
      IFluidHandler fluidInTile = ent.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).orElse(null);
      // note a DIFFERENT cap here for the item
      IFluidHandler fluidInStack = tankStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).orElse(null);
      if (fluidInStack != null) {
        //now give 
        FluidStack fs = fluidInTile.getFluidInTank(0);
        ((FluidHandlerCapabilityStack) fluidInStack).setFluid(fs);
      }
      else
        ModCyclic.LOGGER.info("Storage capability is null from itemstack " + tankStack);
    }
    if (world.isRemote == false)
      world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), tankStack));
  }
}
