package com.lothrazar.cyclicmagic.gui.waypoints;

import java.util.ArrayList;
import java.util.List;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.gui.button.ITooltipButton;
import com.lothrazar.cyclicmagic.net.PacketWarpButton;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonWaypointTeleport extends GuiButton implements ITooltipButton{
	private int bookSlot; 
	List<String> tooltips = new ArrayList<String>();

	public int getSlot() {
		return bookSlot;
	}

	public ButtonWaypointTeleport(int id, int x, int y, int w, int h, String txt, int slot) {
		super(id, x, y, w, h, txt);
		bookSlot = slot;
	}
 
	@Override
	public List<String> getTooltips() {
		return tooltips;
	}
 
	public void addTooltipLine(String s) {
		tooltips.add(s);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		boolean pressed = super.mousePressed(mc, mouseX, mouseY);

		if (pressed) {

			EntityPlayer player = mc.thePlayer;
			World world = player.worldObj;

			UtilSound.playSound(player, player.getPosition(), SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT);
			
			// only spawn particles if they have enough xp
			UtilParticle.spawnParticle(world, EnumParticleTypes.PORTAL,  player.getPosition());
			UtilParticle.spawnParticle(world, EnumParticleTypes.PORTAL,  player.getPosition().up());
		 
			// but even if they dont, send packet anyway. server side has the real
			// source of truth

			ModMain.network.sendToServer(new PacketWarpButton(bookSlot));

			// we would have to wait until tp finishes and then sendToClient in a new
			// 'particle packet' for this
			// particleAtPlayer(world,mc.thePlayer);
		}

		return pressed;
	}
}
