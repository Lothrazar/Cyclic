package com.lothrazar.cyclicmagic.block;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineUncrafter;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockUncrafting extends BlockBaseFacingInventory implements IHasRecipe {
  // http://www.minecraftforge.net/forum/index.php?topic=31953.0
  public BlockUncrafting() {
    super(Material.IRON, ModGuiHandler.GUI_INDEX_UNCRAFTING);
    this.setHardness(3.0F).setResistance(5.0F);
    this.setSoundType(SoundType.METAL);
    this.setTickRandomly(true);
  }
//  @Override
//  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
//    TileEntity tileEntity = world.getTileEntity(pos);
//    if (tileEntity == null || player.isSneaking()) { return false; }
//    int x = pos.getX(), y = pos.getY(), z = pos.getZ();
//    player.openGui(ModMain.instance, ModGuiHandler.GUI_INDEX_UNCRAFTING, world, x, y, z);
//    return true;
//  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileMachineUncrafter();
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
    GameRegistry.addRecipe(new ItemStack(this), 
        " r ", 
        "fdf", 
        " o ", 
        'o', Blocks.OBSIDIAN, 'f', Blocks.FURNACE, 'r', Blocks.DROPPER, 'd', Blocks.DIAMOND_BLOCK);
  }
}
