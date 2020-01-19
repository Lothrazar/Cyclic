package com.lothrazar.cyclic.block.battery;

import javax.annotation.Nullable;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.BlockBase;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockBattery extends BlockBase {

  public BlockBattery(Properties properties) {
    super(properties.hardnessAndResistance(1.8F));
  }

  @Override
  public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity ent, ItemStack stack) {
    super.harvestBlock(worldIn, player, pos, state, ent, stack);
    if (ent != null) {//&& ent.hasCapability(CapabilityEnergy.ENERGY, null)) {
      IEnergyStorage handlerHere = ent.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
      //      if (stack.hasCapability(CapabilityEnergy.ENERGY, null)) {
      IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
      if (storage == null) {
        ModCyclic.LOGGER.info("HOW to give capability  to BlockItem" + stack);
        //just use NBT data
        //write to the data tag eh 
        //        stack.write(handlerHere)
        return;
      }
      storage.receiveEnergy(handlerHere.getEnergyStored(), true);//.setEnergyStored(handlerHere.getEnergyStored());
      //      }
    }
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileBattery();
  }

  @Override
  public ActionResultType func_225533_a_(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
    if (!world.isRemote) {
      TileEntity tileEntity = world.getTileEntity(pos);
      if (tileEntity instanceof INamedContainerProvider) {
        NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, tileEntity.getPos());
      }
      else {
        throw new IllegalStateException("Our named container provider is missing!");
      }
      return ActionResultType.SUCCESS;
    }
    return super.func_225533_a_(state, world, pos, player, hand, result);
  }
}
