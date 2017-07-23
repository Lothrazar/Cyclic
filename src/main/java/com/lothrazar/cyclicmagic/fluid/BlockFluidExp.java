package com.lothrazar.cyclicmagic.fluid;
import com.lothrazar.cyclicmagic.block.base.BlockFluidBase;
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
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFluidExp extends BlockFluidBase {
  public static FluidStack stack;//= new FluidStack(FluidsRegistry.fluid_milk,1000);
  public BlockFluidExp() {
    super(FluidsRegistry.fluid_exp, Material.WATER);
    FluidsRegistry.fluid_exp.setBlock(this);
    stack = new FluidStack(FluidsRegistry.fluid_exp, Fluid.BUCKET_VOLUME);
  }
  @SideOnly(Side.CLIENT)
  @Override
  public void initModel() {
    Block block = FluidsRegistry.block_exp;
    Item item = Item.getItemFromBlock(block);
    ModelBakery.registerItemVariants(item);
    final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(Const.MODID + ":fluid", stack.getFluid().getName());
    ModelLoader.setCustomModelResourceLocation(item, 0, modelResourceLocation);
    ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
      @Override
      protected ModelResourceLocation getModelResourceLocation(IBlockState bs) {
        return modelResourceLocation;
      }
    });
  }
}
