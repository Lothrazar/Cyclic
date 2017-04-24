package com.lothrazar.cyclicmagic.component.disenchanter;
import com.lothrazar.cyclicmagic.block.tileentity.BaseMachineTesr;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityBaseMachineInvo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class DisenchantPylonTESR extends BaseMachineTesr<TileEntityDisenchanter> {
  public DisenchantPylonTESR() {
    super();
  }
  @Override
  public void render(TileEntityBaseMachineInvo te) {
    renderItem(te, te.getStackInSlot(1), 0, 0.5F, 1);
    renderItem(te, te.getStackInSlot(2), 1, 0.5F, 1);
    renderItem(te, te.getStackInSlot(3), 1, 0.5F, 0);
    renderItem(te, te.getStackInSlot(4), 0, 0.5F, 0);
    //and the enchanted item goes here
    renderItem(te, te.getStackInSlot(0), 0.5F, 0.5F, 0.5F);
  }
}
