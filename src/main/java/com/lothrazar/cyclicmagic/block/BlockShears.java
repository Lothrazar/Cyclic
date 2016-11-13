package com.lothrazar.cyclicmagic.block;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilEntity;
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
  final static int FORTUNE = 10;// f yeah why not
  @Override
  public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
    if (entity instanceof IShearable) {
      IShearable sheep = (IShearable) entity;
      ItemStack fake = new ItemStack(Items.SHEARS);
      if (sheep.isShearable(fake, world, pos)) {
        List<ItemStack> drops = sheep.onSheared(fake, world, pos, 5);//since iShearable doesnt do drops, but DOES do sound/make sheep naked
        for (ItemStack s : drops) {
          UtilEntity.dropItemStackInWorld(world, pos, s);
        }
      }
    }
  }
  @Override
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
    //if we dont make the box biger than 1x1x1, the 'Collided' event will Never fire
    //float f = 0.0625F; //same as cactus.
    float f = 0.1F;
    return new AxisAlignedBB((double) ((float) pos.getX() + f), (double) pos.getY(), (double) ((float) pos.getZ() + f), (double) ((float) (pos.getX() + 1) - f), (double) ((float) (pos.getY() + 1) - f), (double) ((float) (pos.getZ() + 1) - f));
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
