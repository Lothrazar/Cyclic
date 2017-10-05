package com.lothrazar.cyclicmagic.item;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.base.BaseTool;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilScythe;
import com.lothrazar.cyclicmagic.util.UtilShape; 
import com.lothrazar.cyclicmagic.util.UtilHarvester;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemScythe extends BaseTool implements IHasRecipe {
  private static final int RADIUS = 6;//13x13
  private static final int RADIUS_SNEAKING = 2;//2x2
 
  public enum ScytheType {
    WEEDS, LEAVES, CROPS;
  }
  private ScytheType harvestType;
  public ItemScythe(ScytheType c) {
    super(1000);
    harvestType = c;
 
  }
  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    ItemStack stack = player.getHeldItem(hand);
    BlockPos offset = pos;
    if (side != null) {
      offset = pos.offset(side);
    }
    int radius = (player.isSneaking()) ? RADIUS_SNEAKING : RADIUS;
    List<BlockPos> shape = getShape(offset, radius);
    switch (harvestType) {
      case CROPS:
        //here we use UtilHarvester, which is the new v2 one
        final NonNullList<ItemStack> drops = NonNullList.create();
        for (BlockPos p : shape) {//now it lands drops on top of player
          drops.addAll(UtilHarvester.harvestSingle(world, p));
        }
        for (ItemStack d : drops) {
          UtilItemStack.dropItemStackInWorld(world, player.getPosition(), d);
        }
      break;
      case LEAVES:
      case WEEDS://NO LONGER DOES SAPLINGS
        for (BlockPos p : shape) {
          UtilScythe.harvestSingle(world, p, this.harvestType);
        }
      break;
    }
    super.onUse(stack, player, world, hand);
    return super.onItemUse(player, world, offset, hand, side, hitX, hitY, hitZ);
  }
  private List<BlockPos> getShape(BlockPos center, int radius) {
    List<BlockPos> shape = new ArrayList<BlockPos>();
    shape.addAll(UtilShape.squareHorizontalFull(center.down().down(), radius));
    shape.addAll(UtilShape.squareHorizontalFull(center.down(), radius));
    shape.addAll(UtilShape.squareHorizontalFull(center, radius));
    shape.addAll(UtilShape.squareHorizontalFull(center.up(), radius));
    shape.addAll(UtilShape.squareHorizontalFull(center.up().up(), radius));
    return shape;
  }
  @Override
  public IRecipe addRecipe() {
    switch (harvestType) {
      case CROPS:
        return RecipeRegistry.addShapedRecipe(new ItemStack(this),
            " gs",
            " bg",
            "b  ",
            'b', Items.BLAZE_ROD,
            'g', "gemQuartz",
            's', Items.STONE_HOE);
      case LEAVES:
        return RecipeRegistry.addShapedRecipe(new ItemStack(this),
            " gs",
            " bg",
            "b  ",
            'b', Items.STICK,
            'g', "string",
            's', Items.STONE_AXE);
      case WEEDS:
        return RecipeRegistry.addShapedRecipe(new ItemStack(this),
            " gs",
            " bg",
            "b  ",
            'b', Items.STICK,
            'g', "string",
            's', Items.STONE_HOE);
      default:
      break;
    }
    return null;
  }
}
