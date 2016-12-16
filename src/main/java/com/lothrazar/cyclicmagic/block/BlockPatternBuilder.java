package com.lothrazar.cyclicmagic.block;
import java.util.List;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityPatternBuilder;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPatternBuilder extends Block implements IHasRecipe {
  public BlockPatternBuilder() {
    super(Material.WOOD);
    this.setHardness(3F);
    this.setResistance(5F);
    this.setSoundType(SoundType.WOOD);
    this.setTickRandomly(true);
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityPatternBuilder();//"tile.block_fishing.name"
  }
  @Override
  public boolean hasTileEntity() {
    return true;
  }
  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {

    if (world.isRemote) { return true; }
    int x = pos.getX(), y = pos.getY(), z = pos.getZ();
    player.openGui(ModCyclic.instance, ModGuiHandler.GUI_INDEX_PATTERN, world, x, y, z);
    return true;
 
  }
  @Override
  public boolean hasTileEntity(IBlockState state) {
    return hasTileEntity();
  }
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    TileEntity tileentity = worldIn.getTileEntity(pos);
    if (tileentity instanceof IInventory) {
      InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) tileentity);
      worldIn.updateComparatorOutputLevel(pos, this);
    }
    super.breakBlock(worldIn, pos, state);
  }
  @Override
  public void addRecipe() {
//    GameRegistry.addRecipe(new ItemStack(this),
//        "pwp",
//        "wfw",
//        "pwp",
//        'w', Blocks.WEB,
//        'f', new ItemStack(Items.FISH, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata()),
//        'p', Blocks.TRAPPED_CHEST);
//    GameRegistry.addRecipe(new ItemStack(this),
//        "pwp",
//        "wfw",
//        "pwp",
//        'w', Blocks.WEB,
//        'f', new ItemStack(Items.FISH, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata()),
//        'p', Blocks.TRAPPED_CHEST);
  }
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    tooltip.add(UtilChat.lang("tile.builder_pattern.tooltip"));
  }
}
