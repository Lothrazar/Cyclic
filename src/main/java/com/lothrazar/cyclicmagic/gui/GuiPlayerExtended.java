package com.lothrazar.cyclicmagic.gui;

import com.lothrazar.cyclicmagic.inventory.ContainerPlayerExtended;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

public class GuiPlayerExtended extends InventoryEffectRenderer {

	public static final ResourceLocation	background	= new ResourceLocation(Const.MODID, "textures/gui/inventory.png");


	//private float													xSizeFloat;

//	private float													ySizeFloat;

	public GuiPlayerExtended(EntityPlayer player) {
		super(new ContainerPlayerExtended(player.inventory, !player.worldObj.isRemote, player));
		this.allowUserInput = true;
	}
 
	@Override
	public void updateScreen() {
		try {
			((ContainerPlayerExtended) inventorySlots).inventory.blockEvents = false;
		} catch (Exception e) {}
		this.updateActivePotionEffects();
	}
 
	@Override
	public void initGui() {
		this.buttonList.clear();
		super.initGui();

	} 
	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
		//this.fontRendererObj.drawString(I18n.format("container.crafting", new Object[0]), 97, 8, 4210752);
	}
 
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		super.drawScreen(par1, par2, par3);
		//this.xSizeFloat = (float) par1;
		//this.ySizeFloat = (float) par2;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(background);
		int k = this.guiLeft;
		int l = this.guiTop;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

		for (int i1 = 0; i1 < this.inventorySlots.inventorySlots.size(); ++i1) {
			Slot slot = (Slot) this.inventorySlots.inventorySlots.get(i1);
			if (slot.getHasStack() && slot.getSlotStackLimit() == 1) {
				this.drawTexturedModalRect(k + slot.xDisplayPosition, l + slot.yDisplayPosition, 200, 0, 16, 16);
			}
		}
	}


	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id == 0) {
			this.mc.displayGuiScreen(new GuiAchievements(this, this.mc.thePlayer.getStatFileWriter()));
		}

		if (button.id == 1) {
			this.mc.displayGuiScreen(new GuiStats(this, this.mc.thePlayer.getStatFileWriter()));
		}
	}
}
