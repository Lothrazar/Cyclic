package com.lothrazar.cyclicmagic.block.base;
import com.lothrazar.cyclicmagic.gui.ITilePreviewToggle;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Thanks to this tutorial http://modwiki.temporal-reality.com/mw/index.php/Render_Block_TESR_/_OBJ-1.9
 * 
 * @author Sam
 *
 */
@SideOnly(Side.CLIENT)
public class MachineTESR extends BaseMachineTESR<TileEntityBaseMachineInvo> {
  float itemRenderHeight = 0.99F;
  float red = 0.7F;
  float green = 0F;
  float blue = 1F;
  public MachineTESR(Block block, int slot) {
    super(block, slot);
  }
  public MachineTESR(Block block) {
    this(block, -1);
  }
  @Override
  public void render(TileEntityBaseMachineInvo te, double x, double y, double z, float partialTicks, int destroyStage, float p) {
    super.render(te, x, y, z, partialTicks, destroyStage, p);
    if (te instanceof ITilePreviewToggle) {
      ITilePreviewToggle tilePreview = (ITilePreviewToggle) te;
      if (tilePreview.isPreviewVisible()) {
        UtilWorld.RenderShadow.renderBlockList(tilePreview.getShape(), te.getPos(), x, y, z, red, green, blue);
      }
    }
  }
  @Override
  public void renderBasic(TileEntityBaseMachineInvo te) {
    renderAnimation(te);
    if (this.itemSlotAbove >= 0) {
      ItemStack stack = te.getStackInSlot(this.itemSlotAbove);
      if (stack.isEmpty() == false) {
        renderItem(te, stack, itemRenderHeight);
      }
    }
  }
}
