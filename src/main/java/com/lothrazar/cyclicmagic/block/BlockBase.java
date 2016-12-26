package com.lothrazar.cyclicmagic.block;
import java.util.List;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockBase extends Block {
  public BlockBase(Material materialIn) {
    super(materialIn);
  }
  protected boolean isTransp = false;
  protected String myTooltip = null;
  protected void setTranslucent() {
    this.isTransp = true;
  }
  protected void setTooltip(String t) {
    this.myTooltip = t;
  }
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    if (myTooltip != null)
      tooltip.add(UtilChat.lang(myTooltip));
  }
  @Override
  public boolean isOpaqueCube(IBlockState state) {
    return !this.isTransp; // http://greyminecraftcoder.blogspot.ca/2014/12/transparent-blocks-18.html
  }
  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getBlockLayer() {
    if (this.isTransp)
      return BlockRenderLayer.TRANSLUCENT;
    else
      return super.getBlockLayer(); // SOLID
  }
}
