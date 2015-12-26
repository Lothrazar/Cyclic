package com.lothrazar.cyclicmagic;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.lothrazar.cyclicmagic.proxy.ClientProxy;
import com.lothrazar.cyclicmagic.gui.GuiSpellbook;
import com.lothrazar.cyclicmagic.net.*;
import com.lothrazar.cyclicmagic.spell.SpellGhost;

public class EventRegistry {

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		
		if(SpellRegistry.spellsEnabled(player) == false){
			return;
		}
		
		if (ClientProxy.keySpellUp.isPressed()) {
			ModMain.network.sendToServer(new MessageKeyRight());
		}
		else if (ClientProxy.keySpellDown.isPressed()) {
			ModMain.network.sendToServer(new MessageKeyLeft());
		}
		else if (ClientProxy.keySpellCast.isPressed()) {
			BlockPos posMouse = null;

			if (Minecraft.getMinecraft().objectMouseOver.getBlockPos() != null) {
				posMouse = Minecraft.getMinecraft().objectMouseOver.getBlockPos();
			}
			else {
				posMouse = Minecraft.getMinecraft().thePlayer.getPosition();
			}

			ModMain.network.sendToServer(new MessageKeyCast(posMouse, Minecraft.getMinecraft().objectMouseOver.sideHit));
		}
	}
	
	@SubscribeEvent
	public void onPlayerInteractEvent(PlayerInteractEvent event){

		if(event.action == Action.LEFT_CLICK_BLOCK && event.world.getBlockState(event.pos) != null
				&& event.entityPlayer.getHeldItem() != null && event.entityPlayer.getHeldItem().getItem() == ItemRegistry.master_wand ){
		
			Block blockHit = event.world.getBlockState(event.pos).getBlock();
			if(blockHit == Blocks.enchanting_table){

				Minecraft.getMinecraft().displayGuiScreen(new GuiSpellbook(event.entityPlayer));
			}
			else if(blockHit == Blocks.bookshelf){
				
				SpellRegistry.caster.rechargeWithExp( event.entityPlayer);
			}
		}
	}

	@SubscribeEvent
	public void onClonePlayer(PlayerEvent.Clone event) {
		PlayerPowerups.get(event.entityPlayer).copy(PlayerPowerups.get(event.original));
	}

	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
		if (event.entity instanceof EntityPlayer && PlayerPowerups.get((EntityPlayer) event.entity) == null) {
			PlayerPowerups.register((EntityPlayer) event.entity);
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderTextOverlay(RenderGameOverlayEvent.Text event) {
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
		//PlayerPowerups props = PlayerPowerups.get(player);

		if (SpellRegistry.spellsEnabled(player)) {
			SpellRegistry.screen.drawSpellWheel();
		}
	}

	@SubscribeEvent
	public void onEntityUpdate(LivingUpdateEvent event) {
		if (event.entityLiving == null) {
			return;
		}

		if (event.entityLiving instanceof EntityPlayer) {
			SpellGhost.onPlayerUpdate(event);

			SpellRegistry.caster.tickSpellTimer((EntityPlayer) event.entityLiving);
		}

		PotionRegistry.tickSlowfall(event);

		PotionRegistry.tickWaterwalk(event);

		PotionRegistry.tickFrost(event);
	}
}
