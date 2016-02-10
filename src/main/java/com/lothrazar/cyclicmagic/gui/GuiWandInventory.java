package com.lothrazar.cyclicmagic.gui;

import org.lwjgl.opengl.GL11;
import com.lothrazar.cyclicmagic.Const;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class GuiWandInventory extends GuiContainer {
	private final InventoryWand inventory;
	private static final ResourceLocation BACKGROUND = new ResourceLocation(Const.MODID, "textures/gui/inventory_wand.png");

	final int id = 777;
	final int padding = 4;
	
	public GuiWandInventory(ContainerWand containerItem) {
		super(containerItem);
		this.inventory = containerItem.inventory;
	}

	@Override
	public void initGui() {

		super.initGui();

		final int x = this.guiLeft + padding;
		final int y = this.guiTop + padding;
		
		this.buttonList.add(new ButtonBuildToggle(inventory.getPlayer(), id,x,y));
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}

	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(BACKGROUND);

		this.drawTexturedModalRect((this.width - this.xSize) / 2, (this.height - this.ySize) / 2, 0, 0, this.xSize, this.ySize);
	}
}
