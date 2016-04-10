package com.lothrazar.cyclicmagic.gui.button;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.text.translation.I18n;

public class GuiButtonEnderbookNew extends GuiButton {
	private int bookSlot;

	public int getSlot() {
		return bookSlot;
	}

	public GuiButtonEnderbookNew(int id, int x, int y, int w, int h, int slot) {
		super(id, x, y, w, h, I18n.translateToLocal("gui.enderbook.new"));
		bookSlot = slot;
	}

	private String tooltip = null;

	public void setTooltip(String s) {
		tooltip = s;
	}

	public String getTooltip() {
		return tooltip;
	}
}
