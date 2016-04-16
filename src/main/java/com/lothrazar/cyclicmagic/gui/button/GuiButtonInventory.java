package com.lothrazar.cyclicmagic.gui.button;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.GL11;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;

public class GuiButtonInventory extends GuiButton{

	public GuiButtonInventory(int buttonId,  int x, int y) {
		super(buttonId,  x, y, 10, Const.btnHeight, "I");

	}
}
