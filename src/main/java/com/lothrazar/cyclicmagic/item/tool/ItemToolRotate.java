package com.lothrazar.cyclicmagic.item.tool;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.item.BaseTool;
import com.lothrazar.cyclicmagic.net.PacketMoveBlock;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemToolRotate extends BaseTool implements IHasRecipe {
  private static final int durability = 1024;
  public ItemToolRotate() {
    super(durability);
  }
  @Override
  public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World worldObj, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    //    UtilPlaceBlocks.rotateBlockValidState(worldObj, player, pos, side);
    if (worldObj.isRemote) {
      ModCyclic.network.sendToServer(new PacketMoveBlock(pos, ItemToolPiston.ActionType.ROTATE, side));
    }
    //hack the sound back in
    IBlockState placeState = worldObj.getBlockState(pos);
    if (placeState != null && placeState.getBlock() != null) {
      UtilSound.playSoundPlaceBlock(player, pos, placeState.getBlock());
    }
    onUse(stack, player, worldObj, hand);
    return EnumActionResult.SUCCESS;
  }
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    tooltip.add("item.tool_rotate.tooltip");
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this),
        " gp",
        " bg",
        "b  ",
        'b', Items.STICK,
        'g', Blocks.STONE_SLAB,
        'p', Blocks.STONE_BRICK_STAIRS);
  }
}
