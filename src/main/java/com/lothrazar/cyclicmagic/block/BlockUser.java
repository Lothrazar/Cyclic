package com.lothrazar.cyclicmagic.block;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineUser;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockUser extends BlockBaseFacingInventory implements IHasRecipe {
  public static final PropertyDirection PROPERTYFACING = BlockBaseFacing.PROPERTYFACING;
  public BlockUser() {
    super(Material.IRON, ModGuiHandler.GUI_INDEX_USER);
    this.setHardness(3.0F).setResistance(5.0F);
    this.setSoundType(SoundType.METAL);
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileMachineUser();
  }
  @Override
  public boolean hasTileEntity() {
    return true;
  }
  @Override
  public boolean hasTileEntity(IBlockState state) {
    return hasTileEntity();
  }
  //  @Override
  //  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
  ////    ((TileMachineMinerSmart) worldIn.getTileEntity(pos)).breakBlock(worldIn, pos, state);
  //    super.breakBlock(worldIn, pos, state);
  //  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this),
        "rsr",
        "gbx",
        "ooo",
        'o', Blocks.QUARTZ_BLOCK,
        'g', Items.DIAMOND_PICKAXE,
        'x', Items.DIAMOND_AXE,
        's', Blocks.DISPENSER,
        'r', Blocks.LAPIS_BLOCK,
        'b', Blocks.field_189877_df);// MAGMA BLOCK is field_189877_df in 1.10.2 apparently
  }
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    tooltip.add(UtilChat.lang("tile.block_user.tooltip"));
  }
}
