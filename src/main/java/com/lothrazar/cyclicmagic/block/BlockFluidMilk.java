package com.lothrazar.cyclicmagic.block;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.FluidsRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFluidMilk extends BlockFluidClassic implements IBlockHasTESR  {
  
  public static FluidStack stack ;//= new FluidStack(FluidsRegistry.fluid_milk,1000);
  
  public BlockFluidMilk() {
    super(FluidsRegistry.fluid_milk,Material.WATER);
   // setRegistryName(Const.MODID,"milk");
 
    this.setQuantaPerBlock(6);
    FluidsRegistry.fluid_milk.setBlock(this);
    
    
    stack = new FluidStack(FluidsRegistry.fluid_milk,1000);
 
//        GameRegistry.register(new ItemBlock(this).setRegistryName(this.getRegistryName()));
  }
  
  @Override
  public boolean isSideSolid(IBlockState state,IBlockAccess world, BlockPos pos, EnumFacing side){
    return false;
  }
  
  @Override
  public boolean isOpaqueCube(IBlockState state){
    return false;
  }
  
  @Override
  public boolean isFullCube(IBlockState state){
    return false;
  }
  
  @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
  
  @SideOnly(Side.CLIENT)
  @Override
  public void initModel(){
        Block block = FluidsRegistry.block_milk;
        Item item = Item.getItemFromBlock(block);   

        ModelBakery.registerItemVariants(item);
        
        final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(Const.MODID+ ":fluid", stack.getFluid().getName());

        ModelLoader.setCustomModelResourceLocation(item, 0, modelResourceLocation);

        ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState bs) {
                return modelResourceLocation;
            }
        });
  }
  
  @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getBlockState().getBaseState().withProperty(LEVEL, meta);
    }
  
  
  
}
