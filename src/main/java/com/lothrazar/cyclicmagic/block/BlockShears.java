package com.lothrazar.cyclicmagic.block;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.base.BlockBase;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class BlockShears extends BlockBase implements IHasRecipe {
  private static final double OFFSET = 0.0625D;
  protected static final AxisAlignedBB AABB = new AxisAlignedBB(OFFSET, 0.0D, OFFSET, 1 - OFFSET, 1 - OFFSET, 1 - OFFSET);
  final static int FORTUNE = 10;// f yeah why not
  // https://github.com/PrinceOfAmber/SamsPowerups/blob/master/FarmingBlocks/src/main/java/com/lothrazar/samsfarmblocks/BlockShearWool.java
  public BlockShears() {
    super(Material.PISTON);
    this.setHardness(4.0F);
    this.setResistance(5.0F);
    this.setTranslucent();
  }
  @Override
  public IRecipe addRecipe() {
    return GameRegistry.addShapedRecipe(new ItemStack(this),
        " s ",
        "sos",
        " s ",
        's', new ItemStack(Items.SHEARS, 1, OreDictionary.WILDCARD_VALUE),
        'o', Blocks.OBSIDIAN);
  }
  @Override
  public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
    if (entity instanceof IShearable) {
      IShearable sheep = (IShearable) entity;
      ItemStack fake = new ItemStack(Items.SHEARS);
      if (sheep.isShearable(fake, world, pos)) {
        List<ItemStack> drops = sheep.onSheared(fake, world, pos, FORTUNE);//since iShearable doesnt do drops, but DOES do sound/make sheep naked
        UtilItemStack.dropItemStacksInWorld(world, pos, drops);
      }
    }
  }
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
    return AABB;
  }
}
