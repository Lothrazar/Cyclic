package com.lothrazar.cyclicmagic.block.base;
import com.lothrazar.cyclicmagic.gui.ITilePreviewToggle;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Thanks to this tutorial
 * http://modwiki.temporal-reality.com/mw/index.php/Render_Block_TESR_/_OBJ-1.9
 * 
 * @author Sam
 *
 */
@SideOnly(Side.CLIENT)
public class MachineTESR extends BaseMachineTesr<TileEntityBaseMachineInvo> {
  public MachineTESR(String block, int slot) {
    super("tesr/" + block.replace("tile.", "").replace(".name", ""), slot);
  }
  public MachineTESR(String block) {
    this(block, -1);
  }
  @Override
  public void renderTileEntityAt(TileEntityBaseMachineInvo te, double x, double y, double z, float partialTicks, int destroyStage) {
    super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);
    if (te instanceof ITilePreviewToggle) {
      ITilePreviewToggle tilePreview = (ITilePreviewToggle) te;
      if (tilePreview.isPreviewVisible()) {
        UtilWorld.RenderShadow.renderBlockList(tilePreview.getShape(), te.getPos(), x, y, z, 0.7F, 0F, 1F);
      }
    }
  }
  @Override
  public void render(TileEntityBaseMachineInvo te) {
    renderAnimation(te);
    if (this.itemSlotAbove >= 0) {
      ItemStack stack = te.getStackInSlot(this.itemSlotAbove);
      if (stack.isEmpty() == false) {
        renderItem(te, stack, 0.99f);
      }
    }
  }
}
