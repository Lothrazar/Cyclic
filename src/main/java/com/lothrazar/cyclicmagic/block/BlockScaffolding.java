package com.lothrazar.cyclicmagic.block;
import java.util.Random;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
  protected boolean doesAutobreak = true;
  public BlockScaffolding() {
    super(Material.GLASS);
    this.setTickRandomly(true);
    this.setHardness(0F);
    this.setResistance(0F);
    this.setTranslucent();
    SoundEvent crackle = SoundRegistry.crackle;
    this.setSoundType(new SoundType(0.1F, 1.0F, crackle, crackle, crackle, crackle, crackle));
  }
  @Override
  public boolean isFullCube(IBlockState state) {
    return false;//required so that when climbing inside it stays invisible
  }
  @Override
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
    return AABB;
  }
  @Override
  public void updateTick(World worldObj, BlockPos pos, IBlockState state, Random rand) {
    if (doesAutobreak && worldObj.rand.nextDouble() < 0.5){
      worldObj.destroyBlock(pos, dropBlock);
    }
  }
  public int tickRate(World worldIn) {
    return 200;
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this, 16), "s s", " s ", "s s", 's', new ItemStack(Items.STICK));
  }
  //doesnt work because it only fires when youre not sneaking. so yeah.
//  @Override
//  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
//  {
//    System.out.println("onBlockActivated::"+hitX+"::"+hitY+"::"+hitZ);
//    if(playerIn.isSneaking()){
//      EnumFacing opposite = side.getOpposite();
//      
//     BlockPos opp = UtilWorld.nextAirInDirection(worldIn, pos, opposite, 16, this);
//     
//     if(opp != null && worldIn.isAirBlock(opp)==false){
//       worldIn.setBlockState(opp, this.getDefaultState());
//     }
//      
//    }
//      return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
//  }
  @Override
  public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
    if (!(entityIn instanceof EntityLivingBase)) { return; }
    EntityLivingBase entity = (EntityLivingBase) entityIn;
    if (!entityIn.isCollidedHorizontally) { return; }
    UtilEntity.tryMakeEntityClimb(worldIn, entity, CLIMB_SPEED);
  }
}