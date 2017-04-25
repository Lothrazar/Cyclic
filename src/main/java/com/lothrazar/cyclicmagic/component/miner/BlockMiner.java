package com.lothrazar.cyclicmagic.component.miner;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.BlockBaseFacing;
import com.lothrazar.cyclicmagic.block.BlockBaseFacingInventory;
import com.lothrazar.cyclicmagic.block.IBlockHasTESR;
import com.lothrazar.cyclicmagic.block.tileentity.MachineTESR;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMiner extends BlockBaseFacingInventory implements IHasRecipe, IBlockHasTESR {
  public static final PropertyDirection PROPERTYFACING = BlockBaseFacing.PROPERTYFACING;
  public static enum MinerType {
    SINGLE, TUNNEL
  }
  private MinerType minerType;
  public BlockMiner(MinerType t) {
    super(Material.IRON, ModGuiHandler.GUI_INDEX_BLOCKMINER);
    this.setHardness(3.0F).setResistance(5.0F);
    this.setSoundType(SoundType.METAL);
    this.minerType = t;
  }
  public MinerType getMinerType() {
    return minerType;
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityBlockMiner();
  }
  @SideOnly(Side.CLIENT)
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    // Bind our TESR to our tile entity
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBlockMiner.class, new MachineTESR(this.getUnlocalizedName(), 0));
  }
  @Override
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    ((TileEntityBlockMiner) worldIn.getTileEntity(pos)).breakBlock(worldIn, pos, state);
    super.breakBlock(worldIn, pos, state);
  }
  @Override
  public IRecipe addRecipe() {
    switch (minerType) {
      case SINGLE:
        return     GameRegistry.addShapedRecipe(new ItemStack(this),
            "rsr",
            " g ",
            "ooo",
            'o', Blocks.MOSSY_COBBLESTONE,
            'g', Items.IRON_PICKAXE, // new ItemStack(Items.DIAMOND_PICKAXE,1,OreDictionary.WILDCARD_VALUE),
            's', Blocks.DISPENSER,
            'r', Items.BONE
        //            'b', Items.BLAZE_POWDER
        );
   
      case TUNNEL:
        return  GameRegistry.addShapedRecipe(new ItemStack(this),
            "rsr",
            "gbg",
            "ooo",
            'o', Blocks.OBSIDIAN,
            'g', Items.IRON_PICKAXE, // new ItemStack(Items.DIAMOND_PICKAXE,1,OreDictionary.WILDCARD_VALUE),
            's', Blocks.DISPENSER,
            'r', Items.QUARTZ,
            'b', Blocks.MAGMA);// MAGMA BLOCK is field_189877_df in 1.10.2 apparently
      
      default:
      break;
    }
    return null;
  }
}
