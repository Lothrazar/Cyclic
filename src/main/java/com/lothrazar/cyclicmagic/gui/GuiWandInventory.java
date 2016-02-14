package com.lothrazar.cyclicmagic.gui;

import org.lwjgl.opengl.GL11;
import com.lothrazar.cyclicmagic.Const;
import com.lothrazar.cyclicmagic.gui.button.ButtonBuildToggle;
import com.lothrazar.cyclicmagic.gui.button.ButtonPassiveToggle;
import com.lothrazar.cyclicmagic.gui.button.ButtonRecharge;
import com.lothrazar.cyclicmagic.gui.button.ButtonSpellCircle;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class GuiWandInventory extends GuiContainer{

	private final InventoryWand inventory;
	private static final ResourceLocation BACKGROUND = new ResourceLocation(Const.MODID, "textures/gui/inventory_wand.png");

	//TODO: TOOLTIPS on all buttons
	// the swap type tooltop, if its on pattern, should show the current slot number, as i '3/9' 
	final int id = 777;
	final int padding = 8;

	public GuiWandInventory(ContainerWand containerItem){

		super(containerItem);
		this.inventory = containerItem.inventory;
	}

	@Override
	public void initGui(){

		super.initGui();

		final int y = this.guiTop + 6;
		int x = this.guiLeft + 5;
		int width = 30;
		this.buttonList.add(new ButtonBuildToggle(inventory.getPlayer(), id, x, y, width));

		x += width + padding;
		this.buttonList.add(new ButtonSpellCircle(id, x, y, width));

		x += width + padding;
		this.buttonList.add(new ButtonRecharge(id, x, y, width));

		x += width + padding;
		this.buttonList.add(new ButtonPassiveToggle(inventory.getPlayer(), id, x, y, width));
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
