package com.lothrazar.cyclicmagic.gui.button;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.net.PacketWarpButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiButtonEnderbookTeleport extends GuiButton {
	private int bookSlot;

	public int getSlot() {
		return bookSlot;
	}

	public GuiButtonEnderbookTeleport(int id, int x, int y, int w, int h, String txt, int slot) {
		super(id, x, y, w, h, txt);
		bookSlot = slot;
	}

	private String tooltip = null;

	public void setTooltip(String s) {
		tooltip = s;
	}

	public String getTooltip() {
		return tooltip;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		boolean pressed = super.mousePressed(mc, mouseX, mouseY);

		if (pressed) {
			World world = mc.thePlayer.worldObj;

			// only spawn particles if they have enough xp
			particleAtPlayer(world, mc.thePlayer);

			// but even if they dont, send packet anyway. server side has the real
			// source of truth

			ModMain.network.sendToServer(new PacketWarpButton(bookSlot));

			// we would have to wait until tp finishes and then sendToClient in a new
			// 'particle packet' for this
			// particleAtPlayer(world,mc.thePlayer);
		}

		return pressed;
	}

	private void particleAtPlayer(World world, EntityPlayer p) {
		spawnParticle(world, p.getPosition().getX(), p.getPosition().getY(), p.getPosition().getZ());
		spawnParticle(world, p.getPosition().getX(), p.getPosition().getY() + 1, p.getPosition().getZ());
		spawnParticle(world, p.getPosition().getX(), p.getPosition().getY() + 2, p.getPosition().getZ());
	}

	private void spawnParticle(World world, double x, double y, double z) {
		// http://www.minecraftforge.net/forum/index.php?topic=9744.0
		for (int countparticles = 0; countparticles <= 12; ++countparticles) {
			world.spawnParticle(EnumParticleTypes.PORTAL, x + (world.rand.nextDouble() - 0.5D) * (double) 0.8, y + world.rand.nextDouble() * (double) 1.5 - (double) 0.1, z + (world.rand.nextDouble() - 0.5D) * (double) 0.8, 0.0D, 0.0D, 0.0D);
		}
	}
}
