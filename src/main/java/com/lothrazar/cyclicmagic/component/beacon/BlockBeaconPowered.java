package com.lothrazar.cyclicmagic.component.beacon;
import com.lothrazar.cyclicmagic.block.base.BlockBaseFacingInventory;
import com.lothrazar.cyclicmagic.block.base.IBlockHasTESR;
import com.lothrazar.cyclicmagic.block.base.MachineTESR;
import com.lothrazar.cyclicmagic.component.autouser.TileEntityUser;
import net.minecraft.block.BlockBeacon;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBeaconPowered extends BlockBaseFacingInventory implements IBlockHasTESR {
  public BlockBeaconPowered() {
    super(Material.IRON, 0);
    BlockBeacon x;
    TileEntityBeacon y;
  }
  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getBlockLayer()
  {
      return BlockRenderLayer.CUTOUT;
  }
  public EnumBlockRenderType getRenderType(IBlockState state)
  {
      return EnumBlockRenderType.MODEL;
  }
  public boolean isOpaqueCube(IBlockState state)
  {
      return false;
  }

  public boolean isFullCube(IBlockState state)
  {
      return false;
  }

  
  
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityBeaconPowered();
  }
  @Override
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBeaconPowered.class, new TileEntityBeaconRedstoneRenderer());
  }
}
