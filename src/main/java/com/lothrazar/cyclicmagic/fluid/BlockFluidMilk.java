package com.lothrazar.cyclicmagic.fluid;
import javax.annotation.Nonnull;
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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFluidMilk extends BlockFluidBase {
  public static FluidStack stack;
  public BlockFluidMilk() {
    super(FluidsRegistry.fluid_milk, Material.WATER);
    FluidsRegistry.fluid_milk.setBlock(this);
    stack = new FluidStack(FluidsRegistry.fluid_milk, Fluid.BUCKET_VOLUME);
  }
  @SideOnly(Side.CLIENT)
  @Override
  public void initModel() {
    Block block = FluidsRegistry.block_milk;
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
  @Override
  @Nonnull
  public Vec3d modifyAcceleration(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull Entity entity, @Nonnull Vec3d vec) {
    if (entity instanceof EntityLivingBase) {
      EntityLivingBase living = (EntityLivingBase) entity;
      living.curePotionEffects(new ItemStack(Items.MILK_BUCKET));//item stack does not get used or saved or anything
    }
    return super.modifyAcceleration(world, pos, entity, vec);
  }
}
