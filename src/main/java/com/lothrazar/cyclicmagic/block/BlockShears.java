package com.lothrazar.cyclicmagic.block;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class BlockShears extends Block implements IHasRecipe {
  protected static final AxisAlignedBB CACTUS_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.9375D, 0.9375D);
  final static int FORTUNE = 10;// f yeah why not
  // https://github.com/PrinceOfAmber/SamsPowerups/blob/master/FarmingBlocks/src/main/java/com/lothrazar/samsfarmblocks/BlockShearWool.java
  public BlockShears() {
    super(Material.PISTON);
    this.setHardness(4.0F);
    this.setResistance(5.0F);
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this),
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
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
    return CACTUS_AABB;
  }
  @Override
  public boolean isOpaqueCube(IBlockState state) {
    return false; // http://greyminecraftcoder.blogspot.ca/2014/12/transparent-blocks-18.html
  }
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    String s = UtilChat.lang("tile.block_shears.tooltip");
    tooltip.add(s);
  }
}
