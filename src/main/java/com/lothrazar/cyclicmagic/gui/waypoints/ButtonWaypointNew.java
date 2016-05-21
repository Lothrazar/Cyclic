package com.lothrazar.cyclicmagic.gui.waypoints;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;

public class ButtonWaypointNew extends GuiButton {
	private int bookSlot;

	public int getSlot() {
		return bookSlot;
	}

	public ButtonWaypointNew(int id, int x, int y, int w, int h, int slot) {
		super(id, x, y, w, h, I18n.format("gui.enderbook.new"));
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
