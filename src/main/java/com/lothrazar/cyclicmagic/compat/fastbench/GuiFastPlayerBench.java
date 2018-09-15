/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.compat.fastbench;

import java.util.Collection;
import com.google.common.collect.Ordering;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.gui.GuiButton;
//import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import shadows.fastbench.gui.GuiFastBench;

public class GuiFastPlayerBench extends GuiFastBench {

	public static final ResourceLocation background = new ResourceLocation(Const.MODID, "textures/gui/inventorycraft.png");
	protected boolean hasActivePotionEffects;
	private float oldMouseX;
	private float oldMouseY;

	public GuiFastPlayerBench(EntityPlayer player) {
    super(player.inventory, player.world, BlockPos.ORIGIN);
		this.inventorySlots = new ClientContainerFastPlayerBench(player, player.world);
		this.allowUserInput = true;
	}

	@Override
	public void updateScreen() {
		this.updateActivePotionEffects();
	}

	@Override
	public void initGui() {
		this.buttonList.clear();
		super.initGui();
		this.updateActivePotionEffects();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		if (this.hasActivePotionEffects) {
			this.drawActivePotionEffects();
		}
		this.renderHoveredToolTip(mouseX, mouseY);
		this.oldMouseX = mouseX;
		this.oldMouseY = mouseY;

	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(background);
		int i = this.guiLeft;
		int j = this.guiTop;
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
		//COPIED FROM GuiInventory
		GuiInventory.drawEntityOnScreen(i + 51, j + 75, 30, i + 51 - this.oldMouseX, j + 75 - 50 - this.oldMouseY, this.mc.player);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		//    if (button.id == 0) {
		//      this.mc.displayGuiScreen(new GuiAchievements(this, this.mc.player.getStatFileWriter()));
		//    }
		if (button.id == 1) {
			this.mc.displayGuiScreen(new GuiStats(this, this.mc.player.getStatFileWriter()));
		}
	}

	protected void updateActivePotionEffects() {
		boolean hasVisibleEffect = false;
		for (PotionEffect potioneffect : this.mc.player.getActivePotionEffects()) {
			Potion potion = potioneffect.getPotion();
			if (potion.shouldRender(potioneffect)) {
				hasVisibleEffect = true;
				break;
			}
		}
		if (this.mc.player.getActivePotionEffects().isEmpty() || !hasVisibleEffect) {
			this.guiLeft = (this.width - this.xSize) / 2;
			this.hasActivePotionEffects = false;
		} else {
			if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiScreenEvent.PotionShiftEvent(this))) this.guiLeft = (this.width - this.xSize) / 2;
			else this.guiLeft = 160 + (this.width - this.xSize - 200) / 2;
			this.hasActivePotionEffects = true;
		}
	}

	private void drawActivePotionEffects() {
		int i = this.guiLeft - 124;
		int j = this.guiTop;
		Collection<PotionEffect> collection = this.mc.player.getActivePotionEffects();

		if (!collection.isEmpty()) {
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.disableLighting();
			int l = 33;

			if (collection.size() > 5) {
				l = 132 / (collection.size() - 1);
			}

			for (PotionEffect potioneffect : Ordering.natural().sortedCopy(collection)) {
				Potion potion = potioneffect.getPotion();
				if (!potion.shouldRender(potioneffect)) continue;
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				this.mc.getTextureManager().bindTexture(INVENTORY_BACKGROUND);
				this.drawTexturedModalRect(i, j, 0, 166, 140, 32);

				if (potion.hasStatusIcon()) {
					int i1 = potion.getStatusIconIndex();
					this.drawTexturedModalRect(i + 6, j + 7, 0 + i1 % 8 * 18, 198 + i1 / 8 * 18, 18, 18);
				}

				potion.renderInventoryEffect(i, j, potioneffect, mc);
				if (!potion.shouldRenderInvText(potioneffect)) {
					j += l;
					continue;
				}
				String s1 = I18n.format(potion.getName());

				if (potioneffect.getAmplifier() == 1) {
					s1 = s1 + " " + I18n.format("enchantment.level.2");
				} else if (potioneffect.getAmplifier() == 2) {
					s1 = s1 + " " + I18n.format("enchantment.level.3");
				} else if (potioneffect.getAmplifier() == 3) {
					s1 = s1 + " " + I18n.format("enchantment.level.4");
				}

				this.fontRenderer.drawStringWithShadow(s1, i + 10 + 18, j + 6, 16777215);
				String s = Potion.getPotionDurationString(potioneffect, 1.0F);
				this.fontRenderer.drawStringWithShadow(s, i + 10 + 18, j + 6 + 10, 8355711);
				j += l;
			}
		}
	}
}
