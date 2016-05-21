package com.lothrazar.cyclicmagic.event;

import java.text.DecimalFormat;

import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.item.ItemFoodHorse;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.util.text.translation.LanguageMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventHorseFood implements IHasConfig {

	@SubscribeEvent
	public void onEntityInteractEvent(EntityInteract event) {

		if (event.getEntity() instanceof EntityPlayer == false) { return; }
		EntityPlayer entityPlayer = (EntityPlayer) event.getEntity();
		ItemStack held = entityPlayer.getHeldItemMainhand();

		if (held != null && held.getItem() instanceof ItemFoodHorse) {
			if (event.getTarget() instanceof EntityHorse) {
				ItemFoodHorse.onHorseInteract((EntityHorse) event.getTarget(), entityPlayer, held);

				event.setCanceled(true);// stop the GUI inventory opening
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void addHorseInfo(RenderGameOverlayEvent.Text event) {

		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
		if (Minecraft.getMinecraft().gameSettings.showDebugInfo) {
			if (player.getRidingEntity() != null && player.getRidingEntity() instanceof EntityHorse) {
				EntityHorse horse = (EntityHorse) player.getRidingEntity();

				double speed = UtilEntity.getSpeedTranslated(horse.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());

				// double jump = horse.getHorseJumpStrength() ;
				// convert from scale factor to blocks
				double jumpHeight = UtilEntity.getJumpTranslated(horse.getHorseJumpStrength());

				DecimalFormat df = new DecimalFormat("0.00");

				event.getLeft().add(I18n.translateToLocal("debug.horsespeed") + "  " + df.format(speed));

				df = new DecimalFormat("0.0");

				event.getLeft().add(I18n.translateToLocal("debug.horsejump") + "  " + df.format(jumpHeight));
			}
		}
	}

	@Override
	public void syncConfig(Configuration config) {
		// TODO Auto-generated method stub
		
	}
}
