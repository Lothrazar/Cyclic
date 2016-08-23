package com.lothrazar.cyclicmagic.block;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityPassword;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockPassword extends Block {
  public static final PropertyBool POWERED = PropertyBool.create("powered");
  public BlockPassword() {
    super(Material.ROCK);
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityPassword();
  }
  @Override
  public boolean hasTileEntity() {
    return true;
  }
  // TODO:  onBlockActivated open gui to change the password saved
  @Override
  public boolean hasTileEntity(IBlockState state) {
    return hasTileEntity();
  }
  public IBlockState getStateFromMeta(int meta) {
    return this.getDefaultState().withProperty(POWERED, meta == 1 ? true : false);
  }
  @Override
  public int getMetaFromState(IBlockState state) {
    return (state.getValue(POWERED)) ? 1 : 0;
  }
  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { POWERED });
  }
  @Override
  public boolean canProvidePower(IBlockState state) {
    return true;
  }
  @Override
  public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    return blockState.getValue(POWERED) ? 15 : 0;
  }
  @Override
  public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    return blockState.getValue(POWERED) ? 15 : 0;
  }
  @SubscribeEvent
  public void chatEvent(ServerChatEvent event) {//forge event
    for (TileEntityPassword current : TileEntityPassword.listeningBlocks) {
      if (current.isInvalid() == false) {
        if (event.getMessage().equals(current.getMyPassword())) {
          System.out.println("password activated by " + event.getUsername());
          
          IBlockState blockState = current.getWorld().getBlockState(current.getPos());
          boolean hasPowerHere = this.getStrongPower(blockState, current.getWorld(), current.getPos(), EnumFacing.UP) > 0;

// oops : java.util.ConcurrentModificationException
          System.out.println("password activated by " + event.getUsername() +" hasPowerHere = "+hasPowerHere);
          
          
          current.getWorld().setBlockState(current.getPos(), this.getDefaultState().withProperty(BlockPassword.POWERED, !hasPowerHere));
        }
        //else password was wrong
      }
      else {
        // current.remove(); ?? deleted or unloaded chunk?
      }
    }
  }
}
