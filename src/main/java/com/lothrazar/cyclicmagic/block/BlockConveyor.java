package com.lothrazar.cyclicmagic.block;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
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
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockConveyor extends BlockBasePressurePlate implements IHasRecipe {
  private static final int RECIPE_OUTPUT = 8;
  protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1D, 0.03125D, 1D);
  private static final PropertyDirection PROPERTYFACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
  private final static float ANGLE = 1;
  private static final float powerCorrection = 0.02F;
  public static enum SpeedType {
    TINY, SMALL, MEDIUM, LARGE;
  }
  private SpeedType type;
  private float power;
  private SoundEvent sound;
  public static boolean doCorrections = true;
  public static boolean keepEntityGrounded = true;
  public static boolean sneakPlayerAvoid;
  public BlockConveyor(SpeedType t) {
    super(Material.IRON, MapColor.GRASS);
    this.setSoundType(SoundType.SLIME);
    this.setHardness(2.0F).setResistance(2.0F);
    type = t;
    sound = SoundEvents.BLOCK_ANVIL_BREAK;
    this.setHardness(2.0F).setResistance(2.0F);
    switch (type) {
      case LARGE:
        this.power = 0.32F;
      break;
      case MEDIUM:
        this.power = 0.16F;
      break;
      case SMALL:
        this.power = 0.08F;
      break;
      case TINY:
        this.power = 0.04F;
      break;
      default:
      break;
    }
    //fixing y rotation in blockstate json: http://www.minecraftforge.net/forum/index.php?topic=25937.0
  }
  @Override
  protected void playClickOnSound(World worldIn, BlockPos pos) {
    worldIn.playSound((EntityPlayer) null, pos, this.sound, SoundCategory.BLOCKS, 0.3F, 0.5F);
  }
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return AABB;
  }
  @Override
  protected void playClickOffSound(World worldIn, BlockPos pos) {}
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
    if (keepEntityGrounded) {
      entity.onGround = true;//THIS is to avoid the entity ZOOMING when slightly off the ground
    }
    if (sneakPlayerAvoid && entity instanceof EntityPlayer && ((EntityPlayer) entity).isSneaking()) {
      return;
    }
    //for example when you have these layering down stairs, and then they speed up when going down one block ledge
    UtilEntity.launchDirection(entity, ANGLE, power, face); //this.playClickOnSound(worldIn, pos);
    if (doCorrections) {
      if (face == EnumFacing.NORTH || face == EnumFacing.SOUTH) {
        //then since +Z is south, and +X is east: so
        double xDiff = (pos.getX() + 0.5) - entity.posX;
        if (Math.abs(xDiff) > 0.09) {//max is .5
          if (xDiff < 0) {
            UtilEntity.launchDirection(entity, ANGLE, powerCorrection, EnumFacing.WEST);
          }
          else {
            UtilEntity.launchDirection(entity, ANGLE, powerCorrection, EnumFacing.EAST);
          }
        }
      }
      else if (face == EnumFacing.EAST || face == EnumFacing.WEST) {
        //then since +Z is south, and +X is east: so
        double diff = (pos.getZ() + 0.5) - entity.posZ;
        //??NOPE  &&  ((int) entity.posZ) == entity.getPosition().getZ()
        if (Math.abs(diff) > 0.09) {//max is .5
          if (diff < 0) {
            UtilEntity.launchDirection(entity, ANGLE, powerCorrection, EnumFacing.NORTH);
          }
          else {
            UtilEntity.launchDirection(entity, ANGLE, powerCorrection, EnumFacing.SOUTH);
          }
        }
      }
    }
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
  public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing blockFaceClickedOn, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    // find the quadrant the player is facing
    EnumFacing enumfacing = (placer == null) ? EnumFacing.NORTH : EnumFacing.fromAngle(placer.rotationYaw);
    return this.getDefaultState().withProperty(PROPERTYFACING, enumfacing);
  }
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing blockFaceClickedOn, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    return this.getStateForPlacement(worldIn, pos, blockFaceClickedOn, hitX, hitY, hitZ, meta, placer);//110 support
  }
  @SideOnly(Side.CLIENT)
  @Override
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.TRANSLUCENT;
  }
  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, net.minecraft.client.util.ITooltipFlag advanced) {
    int speed = (int) (this.power * 100);
    tooltip.add(UtilChat.lang("tile.plate_push.tooltip") + speed);
  }
  @Override
  public IRecipe addRecipe() {
    switch (type) {
      case LARGE:
        RecipeRegistry.addShapedRecipe(new ItemStack(this, RECIPE_OUTPUT),
            "sbs",
            "bxb",
            "sbs",
            's', "ingotIron",
            'x', "slimeball",
            'b', "dyeRed");
      break;
      case MEDIUM:
        RecipeRegistry.addShapedRecipe(new ItemStack(this, RECIPE_OUTPUT),
            "sbs",
            "bxb",
            "sbs",
            's', "ingotIron",
            'x', "slimeball",
            'b', "dyePurple");
      break;
      case SMALL:
        RecipeRegistry.addShapedRecipe(new ItemStack(this, RECIPE_OUTPUT),
            "sbs",
            "bxb",
            "sbs",
            's', "ingotIron",
            'x', "slimeball",
            'b', "dyeMagenta");
      break;
      case TINY:
        RecipeRegistry.addShapedRecipe(new ItemStack(this, RECIPE_OUTPUT),
            "sbs",
            "bxb",
            "sbs",
            's', "ingotIron",
            'x', "slimeball",
            'b', "dyeLightBlue");
      break;
      default:
      break;
    }
    return null;
  }
}
