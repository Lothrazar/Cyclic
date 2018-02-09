package com.lothrazar.cyclicmagic.component.cable.item;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.component.cable.BlockCableBase;
import com.lothrazar.cyclicmagic.component.cable.TileEntityCableBase;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCableItem extends BlockCableBase implements IHasRecipe {
  public BlockCableItem() {
    this.setItemTransport();
  }
  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {
    return new TileEntityItemCable();
  }
  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    TileEntityCableBase te = (TileEntityCableBase) world.getTileEntity(pos);
    if (te != null && world.isRemote == false) {
      String msg = te.getLabelTextOrEmpty();
      UtilChat.sendStatusMessage(player, msg);
    }
    // otherwise return true if it is a fluid handler to prevent in world placement
    return super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
  }
  @Override
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    TileEntity tileentity = worldIn.getTileEntity(pos);
    if (tileentity instanceof TileEntityItemCable) {
      InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityItemCable) tileentity);
    }
    super.breakBlock(worldIn, pos, state);
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this, 8),
        "sis",
        "i i",
        "sis",
        's', Blocks.BRICK_STAIRS,
        'i', "nuggetIron" );
  }
}
