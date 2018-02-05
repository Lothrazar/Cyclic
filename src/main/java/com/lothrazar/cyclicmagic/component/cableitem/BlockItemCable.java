package com.lothrazar.cyclicmagic.component.cableitem;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.component.cable.BlockBaseCable;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.CapabilityItemHandler;

public class BlockItemCable extends BlockBaseCable implements IHasRecipe {
  public BlockItemCable() {
    super(Material.CLAY);
    this.setItemTransport();
  }
 
  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    return new TileEntityItemCable();
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    // TODO: display text if any
    boolean success = false;
    TileEntityItemCable te = (TileEntityItemCable) world.getTileEntity(pos);
    if (te != null && world.isRemote == false) {
      String msg = null;
      if (te.getLabelText() != "") {
        msg = te.getLabelText();
        if (te.getIncomingStrings() != "") {
          msg += " (" + UtilChat.lang("cyclic.item.flowing") + te.getIncomingStrings() + ")";
        }
      }
      else {
        msg = UtilChat.lang("cyclic.item.empty");
      }
      UtilChat.sendStatusMessage(player, msg);
      success = true;
    }
    // otherwise return true if it is a fluid handler to prevent in world placement
    return success || FluidUtil.getFluidHandler(player.getHeldItem(hand)) != null || super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this, 32),
        "sis",
        " x ",
        "sis",
        's', Blocks.SANDSTONE_STAIRS, 'i', "ingotIron", 'x', "string");
  }
}
