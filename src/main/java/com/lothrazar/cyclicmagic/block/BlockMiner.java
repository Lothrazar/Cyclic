package com.lothrazar.cyclicmagic.block;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityMiner;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockMiner extends Block implements IHasRecipe {
  public static final PropertyDirection PROPERTYFACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
  public static enum MinerType {
    SINGLE, TUNNEL
  }
  private MinerType minerType;
  public BlockMiner(MinerType t) {
    super(Material.IRON);
    this.setHardness(3.0F).setResistance(5.0F);
    this.setSoundType(SoundType.METAL);
    minerType = t;
  }
  public MinerType getMinerType() {
    return minerType;
  }
  @Override
  public IBlockState getStateFromMeta(int meta) {
    EnumFacing facing = EnumFacing.getHorizontal(meta);
    return this.getDefaultState().withProperty(PROPERTYFACING, facing);
  }
  public EnumFacing getFacingFromState(IBlockState state) {
    return (EnumFacing) state.getValue(PROPERTYFACING);
  }
  @Override
  public int getMetaFromState(IBlockState state) {
    EnumFacing facing = (EnumFacing) state.getValue(PROPERTYFACING);
    int facingbits = facing.getHorizontalIndex();
    return facingbits;
  }
  @Override
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    return state;
  }
  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { PROPERTYFACING });
  }
  @Override
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing blockFaceClickedOn, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    // find the quadrant the player is facing
    EnumFacing enumfacing = (placer == null) ? EnumFacing.NORTH : EnumFacing.fromAngle(placer.rotationYaw);
    return this.getDefaultState().withProperty(PROPERTYFACING, enumfacing);
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityMiner();
  }
  @Override
  public boolean hasTileEntity() {
    return true;
  }
  @Override
  public boolean hasTileEntity(IBlockState state) {
    return hasTileEntity();
  }
  @Override
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    ((TileEntityMiner) worldIn.getTileEntity(pos)).breakBlock(worldIn, pos, state);
    super.breakBlock(worldIn, pos, state);
  }
  @Override
  public void addRecipe() {
    switch(minerType){
    case SINGLE:
        GameRegistry.addRecipe(new ItemStack(this), 
            "rsr", 
            "gbg", 
            "ooo",
            'o', Blocks.MOSSY_COBBLESTONE,
            'g', Items.IRON_PICKAXE, // new ItemStack(Items.DIAMOND_PICKAXE,1,OreDictionary.WILDCARD_VALUE),
            's', Blocks.DISPENSER,
            'r', Items.QUARTZ,
            'b', Items.BLAZE_POWDER);
      break;
    case TUNNEL:
      GameRegistry.addRecipe(new ItemStack(this), 
          "rsr", 
          "gbg", 
          "ooo",
          'o', Blocks.OBSIDIAN,
          'g', Items.DIAMOND_PICKAXE, // new ItemStack(Items.DIAMOND_PICKAXE,1,OreDictionary.WILDCARD_VALUE),
          's', Blocks.DISPENSER,
          'r', Items.BLAZE_POWDER,
          'b', Blocks.field_189877_df);// MAGMA BLOCK is field_189877_df in 1.10.2 apparently
      break;
    default:
      break;
    
    }
  }
}
