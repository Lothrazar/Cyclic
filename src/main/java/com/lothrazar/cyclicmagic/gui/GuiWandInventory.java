package com.lothrazar.cyclicmagic.gui;

import org.lwjgl.opengl.GL11;
import com.lothrazar.cyclicmagic.Const;
import com.lothrazar.cyclicmagic.gui.button.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class GuiWandInventory extends GuiContainer{

	private final InventoryWand inventory;
	//176x156
	private static final ResourceLocation BACKGROUND = new ResourceLocation(Const.MODID, "textures/gui/inventory_wand.png");

	// TODO: the swap type tooltop, if its on pattern, should show the current slot number, as i '3/9'
	int id = 777;
	final int padding = 4;

	public GuiWandInventory(ContainerWand containerItem){

		super(containerItem);
		this.inventory = containerItem.inventory;
	}

	@Override
	public void initGui(){

		super.initGui();

		int y = this.guiTop + padding  ;
		int x = this.guiLeft + 5;

		int width = 20;
		this.buttonList.add(new ButtonSpellCircle(id, x, y, width));

		 /*
		id++;
		x += width + padding;
		this.buttonList.add(new ButtonUpgrade(inventory.getPlayer(),id, x, y, width));
 */

		id++;
		x += width + padding;
		this.buttonList.add(new ButtonRecharge(id, x, y, width));
		
		//Next row
		x = this.guiLeft + 5;
		y += 20 + padding;
		
		id++;
		//x += width + padding;
		this.buttonList.add(new ButtonToggleVariant("", id, x, y, width));
		/*
		id++;
		x += width + padding;
		width = 50;
		this.buttonList.add(new ButtonPassiveToggle(inventory.getPlayer(), id, x, y, width));
*/
		id++;
		x += width + padding;
		width = 50;
		this.buttonList.add(new ButtonBuildToggle(inventory.getPlayer(), id, x, y, width));
		

		
		/*
		id++;
		x += width + padding;
		width = 50;
		this.buttonList.add(new ButtonPlaceToggle(inventory.getPlayer(), id, x, y, width));
		*/
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks){

		super.drawScreen(mouseX, mouseY, partialTicks);

		ITooltipButton btn;
		for(int i = 0; i < buttonList.size(); i++){
			if(buttonList.get(i).isMouseOver() && buttonList.get(i) instanceof ITooltipButton){
				btn = (ITooltipButton) buttonList.get(i);

				drawHoveringText(btn.getTooltips(), mouseX, mouseY, fontRendererObj);
				break;// cant hover on 2 at once
			}
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){

		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}

	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3){

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(BACKGROUND);

		this.drawTexturedModalRect((this.width - this.xSize) / 2, (this.height - this.ySize) / 2, 0, 0, this.xSize, this.ySize);
	}
}
