package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.item.base.BaseTool;
import com.lothrazar.cyclicmagic.net.PacketMoveBlock;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemRotateBlock extends BaseTool implements IHasRecipe {
  private static final int durability = 1024;
  public ItemRotateBlock() {
    super(durability);
  }
  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World worldObj, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    ItemStack stack = player.getHeldItem(hand);
    //    UtilPlaceBlocks.rotateBlockValidState(worldObj, player, pos, side);
    if (worldObj.isRemote) {
      ModCyclic.network.sendToServer(new PacketMoveBlock(pos, ItemPistonWand.ActionType.ROTATE, side));
    }
    //hack the sound back in
    IBlockState placeState = worldObj.getBlockState(pos);
    if (placeState != null && placeState.getBlock() != null) {
      UtilSound.playSoundPlaceBlock(player, pos, placeState.getBlock());
    }
    onUse(stack, player, worldObj, hand);
    return EnumActionResult.SUCCESS;
  }
  @Override
  public IRecipe addRecipe() {
    RecipeRegistry.addShapedRecipe(new ItemStack(this),
        " gp",
        " bg",
        "b  ",
        'b', "stickWood",
        'g', Blocks.STONE_SLAB,
        'p', Blocks.STONE_BRICK_STAIRS);
    return null;
  }
}
