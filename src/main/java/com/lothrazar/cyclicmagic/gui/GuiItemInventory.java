package com.lothrazar.cyclicmagic.gui;

import org.lwjgl.opengl.GL11;
import com.lothrazar.cyclicmagic.Const;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiItemInventory extends GuiContainer{
	private final InventoryItem inventory;
	
	public GuiItemInventory(ContainerItem  containerItem) {
		super(containerItem);
		this.inventory = containerItem.inventory;
		// TODO Auto-generated constructor stub
	}

	private static final ResourceLocation iconLocation = new ResourceLocation(Const.MODID, "textures/gui/inventoryitem.png");

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		// TODO Auto-generated method stub
		
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(iconLocation);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}
}
