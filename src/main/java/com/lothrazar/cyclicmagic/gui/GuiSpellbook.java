package com.lothrazar.cyclicmagic.gui;

import com.lothrazar.cyclicmagic.PlayerPowerups;
import com.lothrazar.cyclicmagic.SpellRegistry;
import com.lothrazar.cyclicmagic.spell.ISpell;
import com.lothrazar.cyclicmagic.util.UtilTextureRender;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSpellbook extends GuiScreen {

	private final EntityPlayer entityPlayer;

	// public final ResourceLocation texture = new
	// ResourceLocation(ModSamsContent.MODID,
	// "textures/enderbook/textures/gui/book_ender.png" );

	public GuiSpellbook(EntityPlayer entityPlayer) {
		this.entityPlayer = entityPlayer;
	}

	@Override
	public void initGui() {

		// TODO: buttons to add/remove each spell from player rotation
		super.initGui();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawDefaultBackground();

		super.drawScreen(mouseX, mouseY, partialTicks);

		/*
		ItemStack wand = entityPlayer.getHeldItem();
		if (wand.hasTagCompound() == false) {
			wand.setTagCompound(new NBTTagCompound());
		}*/

		
		int xCenter = this.width/2;
		int yCenter = this.height/2;
		int FONT = 16777215;
		
		drawCenteredString(fontRendererObj, "test", xCenter,yCenter, FONT);
		
		int radius = xCenter/3;
		
		
		double arc = (2*Math.PI)/SpellRegistry.spellbook.size();
		
		double ang = 0;
		double cx,cy;
	
		PlayerPowerups props = PlayerPowerups.get(entityPlayer);
 
		ang = 0;
		
		int spellSize = 16;
		for (ISpell s : SpellRegistry.spellbook) {
			
			cx = xCenter + radius * Math.cos(ang);
			cy = yCenter + radius * Math.sin(ang);
			
			UtilTextureRender.drawTextureSquare(s.getIconDisplay(), (int)cx, (int)cy, spellSize);
	
			if (s.getID() == props.getSpellCurrent()) {
				//TODO: mark current spell with a highlight or some circle texture or something
				drawCenteredString(fontRendererObj, "current", (int)cx, (int)cy, FONT);
			}
			
			ang += arc;
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}
