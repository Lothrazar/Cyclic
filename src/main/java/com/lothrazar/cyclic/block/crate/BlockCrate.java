package com.lothrazar.cyclic.block.crate;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockCrate extends BlockBase {

  public BlockCrate(Properties properties) {
    super(properties.hardnessAndResistance(1.1F, 3600000.0F).sound(SoundType.STONE));
    this.setHasGui();
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileCrate();
  }

  @Override
  public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    if (state.hasTileEntity() && (!state.isIn(newState.getBlock()) || !newState.hasTileEntity())) {
      worldIn.removeTileEntity(pos);
    }
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void registerClient() {
    ScreenManager.registerFactory(ContainerScreenRegistry.crate, ScreenCrate::new);
  }

  @Override
  public List<ItemStack> getDrops(BlockState state, net.minecraft.loot.LootContext.Builder builder) {
    // because harvestBlock manually forces a drop, we must do this to dodge that
    return new ArrayList<>();
  }

  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    TileEntity tileentity = worldIn.getTileEntity(pos);
    if (stack.getTag() != null && tileentity instanceof TileCrate && stack.getTag().contains("crate0")) {
      //to tile from tag
      TileCrate crate = (TileCrate) tileentity;
      for (int i = 0; i < crate.inventory.getSlots(); i++) {
        //
        ItemStack crateStack = ItemStack.read(stack.getTag().getCompound("crate" + i));
        crate.inventory.setStackInSlot(i, crateStack);
      }
    }
  }

  @Override
  public void harvestBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, TileEntity tileentity, ItemStack stackToolUsed) {
    super.harvestBlock(world, player, pos, state, tileentity, stackToolUsed);
    ItemStack newStack = new ItemStack(this);
    if (tileentity instanceof TileCrate) {
      TileCrate crate = (TileCrate) tileentity;
      //read from tile, write to itemstack
      for (int i = 0; i < crate.inventory.getSlots(); i++) {
        CompoundNBT nbt = new CompoundNBT();
        crate.inventory.getStackInSlot(i).write(nbt);
        newStack.getOrCreateTag().put("crate" + i, nbt);
      }
    }
    if (world.isRemote == false) {
      world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), newStack));
    }
  }
}
