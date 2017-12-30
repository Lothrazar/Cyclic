package com.lothrazar.cyclicmagic.component.screen;
import com.lothrazar.cyclicmagic.block.base.BlockBaseFacing;
import com.lothrazar.cyclicmagic.block.base.BlockBaseFacingOmni;
import com.lothrazar.cyclicmagic.block.base.IBlockHasTESR;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class BlockScreen extends BlockBaseFacing implements IBlockHasTESR {
  public BlockScreen() {
    super(Material.WOOD);
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityScreen();
  }
 
  @Override
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    // Bind our TESR to our tile entity
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityScreen.class, new ScreenTESR<TileEntityScreen>(this));
  }
 
}
