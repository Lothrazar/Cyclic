package com.lothrazar.cyclicmagic.component.beaconpotion;
import java.util.Map;
import com.lothrazar.cyclicmagic.component.builder.TileEntityStructureBuilder;
import com.lothrazar.cyclicmagic.component.harvester.ContainerHarvester;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.base.GuiButtonTooltip;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.RegistryManager;

public class GuiBeaconPotion extends GuiBaseContainer {
  boolean debugLabels = false;
  public GuiBeaconPotion(InventoryPlayer inventoryPlayer, TileEntityBeaconPotion tileEntity) {
    super(new ContainerBeaconPotion(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    //    this.fieldRedstoneBtn = TileEntityBlockMiner.Fields.REDSTONE.ordinal();
  }
  @Override
  public void initGui() {
    super.initGui();
    int width = 50;
    int h = 14;
    int x = Const.PAD + h, y = Const.PAD;
    int id = 0;
   
//     GameData.POTIONS   ForgeRegistries.POTIONS
    
//    RegistryManager.ACTIVE.getRegistry(GameData.POTIONS)
//    ForgeRegistry<Potion> reg = RegistryManager.FROZEN.getRegistry(GameData.POTIONS);
//    
//    for (Potion p : reg.getValues()) {
//   
//      if(p.isInstant()){
//        continue;
//      }
//      if(p.isBadEffect()){
//        continue;
//      }
//      GuiButtonTooltip b = new GuiButtonTooltip(id++,
//          this.guiLeft + x,
//          this.guiTop + y,
//          width, h,
//          UtilChat.lang(p.getName()));
//      y += h;
//      if(id % 8 == 0){
//        x += width;
//        y = Const.PAD;
//      }
//      this.addButton(b);
//    }
  }
  //  @Override
  //  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
  //    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
  //    int u = 0, v = 0;
  //    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
  //    for (int k = 0; k < this.tile.getSizeInventory(); k++) { // x had - 3 ??
  //      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerHarvester.SLOTX_START - 1 + k * Const.SQ, this.guiTop + ContainerHarvester.SLOTY - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
  //    }
  //  }
  //   
}
