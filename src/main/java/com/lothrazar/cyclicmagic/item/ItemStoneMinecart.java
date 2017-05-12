package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.entity.EntityCarryMinecart;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemStoneMinecart extends BaseItem implements IHasRecipe {
  public ItemStoneMinecart() {
    super();
    this.maxStackSize = 16;
  }
  public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    IBlockState iblockstate = worldIn.getBlockState(pos);
    if (!BlockRailBase.isRailBlock(iblockstate)) {
      return EnumActionResult.FAIL;
    }
    else {
      ItemStack itemstack = player.getHeldItem(hand);
      if (!worldIn.isRemote) {
        BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = iblockstate.getBlock() instanceof BlockRailBase ? ((BlockRailBase) iblockstate.getBlock()).getRailDirection(worldIn, pos, iblockstate, null) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
        double d0 = 0.0D;
        if (blockrailbase$enumraildirection.isAscending()) {
          d0 = 0.5D;
        }
        EntityCarryMinecart entityminecart = new EntityCarryMinecart(worldIn, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.0625D + d0, (double) pos.getZ() + 0.5D);
        if (itemstack.hasDisplayName()) {
          entityminecart.setCustomNameTag(itemstack.getDisplayName());
        }
        entityminecart.setDisplayTile(Blocks.COBBLESTONE.getDefaultState());
        worldIn.spawnEntity(entityminecart);
      }
      itemstack.shrink(1);
      return EnumActionResult.SUCCESS;
    }
  }
  @Override
  public IRecipe addRecipe() {
    return    GameRegistry.addShapedRecipe(new ItemStack(this),
        "   ",
        "gmg",
        "ggg",
        'g',Blocks.COBBLESTONE,
        'm',Items.MINECART);
     
  }
}
