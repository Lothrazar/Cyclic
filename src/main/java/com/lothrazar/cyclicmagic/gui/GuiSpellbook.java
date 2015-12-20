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
	public void drawScreen(int x, int y, float par3) {
		drawDefaultBackground();

		super.drawScreen(x, y, par3);

		ItemStack wand = entityPlayer.getHeldItem();
		if (wand.hasTagCompound() == false) {
			wand.setTagCompound(new NBTTagCompound());
		}

		PlayerPowerups props = PlayerPowerups.get(entityPlayer);

		int xs = 50;
		int ys = 14;
		int spellSize = 16;
		for (ISpell s : SpellRegistry.spellbook) {

			UtilTextureRender.drawTextureSquare(s.getIconDisplay(), xs, ys, spellSize);

			if (s.getID() == props.getSpellCurrent()) {
				drawCenteredString(fontRendererObj, StatCollector.translateToLocal("spell.current"), xs + 20, ys, 16777215);

			}

			ys += spellSize + 5;
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}
