package com.lothrazar.cyclicmagic.gui.storage;

import java.io.IOException;
import org.lwjgl.opengl.GL11;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.gui.button.*;
import com.lothrazar.cyclicmagic.gui.wand.ButtonBuildToggle;
import com.lothrazar.cyclicmagic.gui.wand.ButtonSpellCircle;
import com.lothrazar.cyclicmagic.gui.wand.InventoryWand;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.net.PacketBuildSize;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiStorage extends GuiContainer {

	// 176x156
	private static final ResourceLocation	BACKGROUND	= new ResourceLocation(Const.MODID, "textures/gui/inventory_storage.png");

	// TODO: the swap type tooltop, if its on pattern, should show the current
	// slot number, as i '3/9'
	int																		id					= 777;
	final int															padding			= 4;

	public GuiStorage(ContainerStorage containerItem, ItemStack wand) {

		super(containerItem);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		super.drawScreen(mouseX, mouseY, partialTicks);

	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}

	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(BACKGROUND);
		int thisX = (this.width - this.xSize) / 2;
		int thisY = (this.height - this.ySize) / 2;
		int texture_width = 176;
		int texture_height = 212;
		int u = 0, v = 0;
		//old
	//	Gui.drawModalRectWithCustomSizedTexture(thisX, thisY, u, v, this.xSize, this.ySize, texture_width, texture_height);


		Gui.drawModalRectWithCustomSizedTexture(thisX, thisY, u, v,  texture_width, texture_height, texture_width, texture_height);
	//	this.drawTexturedModalRect((this.width - this.xSize) / 2, (this.height - this.ySize) / 2, 0, 0, this.xSize, this.ySize);
	}
}
