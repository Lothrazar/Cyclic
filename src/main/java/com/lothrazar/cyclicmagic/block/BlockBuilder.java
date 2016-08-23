package com.lothrazar.cyclicmagic.block;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineBuilder;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockBuilder extends BlockBaseFacingInventory implements IHasRecipe {
  // dont use blockContainer !!
  // http://www.minecraftforge.net/forum/index.php?topic=31953.0
  public BlockBuilder() {
    super(Material.IRON);
    this.setHardness(3.0F).setResistance(5.0F);
    this.setSoundType(SoundType.METAL);
    this.setTickRandomly(true);
  }
  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
    TileMachineBuilder tileEntity = (TileMachineBuilder) world.getTileEntity(pos);
    if (tileEntity == null || player.isSneaking()) { return false; }
    if (world.isRemote) { return true; }
    int x = pos.getX(), y = pos.getY(), z = pos.getZ();
    player.openGui(ModMain.instance, ModGuiHandler.GUI_INDEX_BUILDER, world, x, y, z);
    return true;
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileMachineBuilder();
  }
  @Override
  public boolean hasTileEntity() {
    return true;
  }
  @Override
  public boolean hasTileEntity(IBlockState state) {
    return hasTileEntity();
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this), "rsr", "gbg", "ooo",
        'o', Blocks.OBSIDIAN,
        'g', Items.GHAST_TEAR,
        's', Blocks.DISPENSER,
        'r', Blocks.REDSTONE_BLOCK,
        'b', Blocks.DIAMOND_BLOCK);
  }
}
