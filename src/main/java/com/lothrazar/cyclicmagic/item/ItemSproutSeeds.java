package com.lothrazar.cyclicmagic.item;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSproutSeeds extends ItemSeeds implements IHasRecipe {
  private final Block soilBlockID;
  public ItemSproutSeeds(Block crops, Block soil) {
    super(crops, soil);
    soilBlockID = soil;
  }
  @Override
  public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    net.minecraft.block.state.IBlockState state = worldIn.getBlockState(pos);
    //without this override, it gets planted on grass just like flowers. since we dont fit an EnumPlantType def
    if (state != null && state.getBlock() == this.soilBlockID) {
      return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
    else {
      return EnumActionResult.FAIL;
    }
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this, 2),
        "waw",
        "bEc",
        "wdw",
        'w', Items.WHEAT_SEEDS,
        'E', Items.EMERALD,
        'a', Items.BEETROOT_SEEDS,
        'b', Items.MELON_SEEDS,
        'c', Items.PUMPKIN_SEEDS,
        'd', Items.NETHER_WART);
  }
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    tooltip.add(UtilChat.lang("item.sprout_seed.tooltip"));
  }
}
