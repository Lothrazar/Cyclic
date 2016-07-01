package com.lothrazar.cyclicmagic.block;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraft.util.SoundEvent;

public class BlockConveyor extends BlockBasePressurePlate implements IHasRecipe{
  private static final PropertyDirection PROPERTYFACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
  private final static float ANGLE = 1;
  private float power;
  private SoundEvent sound;
  public BlockConveyor(float p, SoundEvent s) {
    super(Material.CLAY, MapColor.GRASS);
    this.setSoundType(SoundType.SLIME);
    power = p;
    sound = s;
  }
  @Override
  protected void playClickOnSound(World worldIn, BlockPos pos) {
    worldIn.playSound((EntityPlayer) null, pos, this.sound, SoundCategory.BLOCKS, 0.3F, 0.5F);
  }
  @Override
  protected void playClickOffSound(World worldIn, BlockPos pos) {
  }
  @Override
  protected int computeRedstoneStrength(World worldIn, BlockPos pos) {
    return 0;
  }
  @Override
  protected int getRedstoneStrength(IBlockState state) {
    return 0;
  }
  @Override
  protected IBlockState setRedstoneStrength(IBlockState state, int strength) {
    return null;
  }
  @Override
  public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entity) {
    EnumFacing face = getFacingFromState(state);
    UtilEntity.launchDirection(entity, ANGLE, power, face); //this.playClickOnSound(worldIn, pos);
  }
  //below is all for facing
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
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this,4), 
        "sbs", 
        "bsb", 
        "sbs", 
        's', new ItemStack(Items.IRON_INGOT),
        'b', new ItemStack(Items.DYE,1,EnumDyeColor.PURPLE.getDyeDamage()));
    
  }
}
