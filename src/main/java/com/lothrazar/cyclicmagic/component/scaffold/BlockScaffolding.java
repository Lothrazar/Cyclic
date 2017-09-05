package com.lothrazar.cyclicmagic.component.scaffold;
import java.util.Random;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.base.BlockBase;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * imported from https://github.com/PrinceOfAmber/SamsPowerups/blob/
 * 5083ec601e34bbe045d9a3d0ca091e3d44af562f/src/main/java/com/lothrazar/
 * samscontent/BlockRegistry.j a v a
 * 
 * @author Lothrazar
 *
 */
public class BlockScaffolding extends BlockBase implements IHasRecipe {
  private static final double CLIMB_SPEED = 0.31D;//climbing glove is 0.288D
  private static final double OFFSET = 0.0125D;//shearing & cactus are  0.0625D;
  protected static final AxisAlignedBB AABB = new AxisAlignedBB(OFFSET, 0, OFFSET, 1 - OFFSET, 1, 1 - OFFSET);//required to make entity collied happen for ladder climbing
  protected boolean dropBlock = true;//does it drop item on non-player break
  private boolean doesAutobreak = true;
  public BlockScaffolding(boolean autoBreak) {
    super(Material.GLASS);
    doesAutobreak = autoBreak;
    this.setTickRandomly(true);
    this.setHardness(0F);
    this.setResistance(0F);
    SoundEvent crackle = SoundRegistry.crackle;
    this.setSoundType(new SoundType(0.1F, 1.0F, crackle, crackle, crackle, crackle, crackle));
  }
  @Override
  public boolean isFullCube(IBlockState state) {
    return false;//required so that when climbing inside it stays invisible
  }
  @Override
  public boolean isOpaqueCube(IBlockState state) {
    return false; // http://greyminecraftcoder.blogspot.ca/2014/12/transparent-blocks-18.html
  }
  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.CUTOUT;
  }
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
    return AABB;
  }
  @Override
  public void updateTick(World worldObj, BlockPos pos, IBlockState state, Random rand) {
    if (doesAutobreak && worldObj.rand.nextDouble() < 0.5) {
      worldObj.destroyBlock(pos, dropBlock);
    }
  }
  public int tickRate(World worldIn) {
    return 200;
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this, 16), "s s", " s ", "s s", 's', "stickWood");
  }
  @Override
  public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
    if (!(entityIn instanceof EntityLivingBase)) { return; }
    EntityLivingBase entity = (EntityLivingBase) entityIn;
    if (!entityIn.isCollidedHorizontally) { return; }
    UtilEntity.tryMakeEntityClimb(worldIn, entity, CLIMB_SPEED);
  }
}