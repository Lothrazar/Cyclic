package com.lothrazar.cyclicmagic.block;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFarming extends Block implements IHasRecipe {
  // https://github.com/PrinceOfAmber/SamsPowerups/blob/master/FarmingBlocks/src/main/java/com/lothrazar/samsfarmblocks/BlockShearWool.java
  public BlockFarming() {
    super(Material.PISTON);
    // TODO Auto-generated constructor stub
  }
  @Override
  public void addRecipe() {
    // TODO Auto-generated method stub
  }
  @Override
  public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
    if (entity instanceof EntityCow) {
      EntityCow cow = (EntityCow) entity;
      cow.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
      UtilEntity.dropItemStackInWorld(cow.worldObj, cow.getPosition(), new ItemStack(Items.MILK_BUCKET));
    }
    if (entity instanceof EntitySheep) {
      EntitySheep sheep = (EntitySheep) entity;
      if (sheep.getSheared() == false && sheep.worldObj.isRemote == false) {
        //this part is the same as how EntitySheep goes
        sheep.setSheared(true);
        sheep.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
        int i = 1 + sheep.worldObj.rand.nextInt(3);
        for (int j = 0; j < i; ++j) {
          UtilEntity.dropItemStackInWorld(sheep.worldObj, sheep.getPosition(), new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, sheep.getFleeceColor().getMetadata()));
        }
      }
    }
  }
  @Override
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
    //if we dont make the box biger than 1x1x1, the 'Collided' event will Never fire
    float f = 0.0625F; //same as cactus.
    return new AxisAlignedBB((double) ((float) pos.getX() + f), (double) pos.getY(), (double) ((float) pos.getZ() + f), (double) ((float) (pos.getX() + 1) - f), (double) ((float) (pos.getY() + 1) - f), (double) ((float) (pos.getZ() + 1) - f));
  }
}
