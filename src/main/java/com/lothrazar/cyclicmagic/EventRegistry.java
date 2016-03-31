package com.lothrazar.cyclicmagic;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.net.MessageKeyLeft;
import com.lothrazar.cyclicmagic.net.MessageKeyRight;
import com.lothrazar.cyclicmagic.net.MessageOpenSpellbook;

public class EventRegistry{

	@SubscribeEvent
	public void onConfigChanged(OnConfigChangedEvent event){

		if(event.getModID().equals(Const.MODID)){
			ModMain.cfg.syncConfig();
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onMouseInput(MouseEvent event){

		// DO NOT use InputEvent.MouseInputEvent
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;

		if(SpellRegistry.spellsEnabled(player) == false){
			// you are not holding the wand - so go as normal
			return;
		}

		if(player.isSneaking()){
			if(event.getDwheel() < 0){
				ModMain.network.sendToServer(new MessageKeyRight());
				event.setCanceled(true);
			}
			else if(event.getDwheel() > 0){
				ModMain.network.sendToServer(new MessageKeyLeft());
				event.setCanceled(true);
			}
		}
	} 

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderTextOverlay(RenderGameOverlayEvent.Text event){

		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
		// PlayerPowerups props = PlayerPowerups.get(player);

		if(SpellRegistry.spellsEnabled(player)){
			SpellRegistry.screen.drawSpellWheel();
		}
	}

	@SubscribeEvent
	public void onEntityUpdate(LivingUpdateEvent event){

		EntityLivingBase entityLiving = event.getEntityLiving();
		if(entityLiving == null){
			return;
		}
		World world = entityLiving.getEntityWorld();
		
		PotionRegistry.tickSlowfall(entityLiving);

		PotionRegistry.tickMagnet(entityLiving);
	 
		/*
		if(entityLiving instanceof EntityPlayer && world.isRemote == false){
			EntityPlayer p = (EntityPlayer) entityLiving;
			//SpellGhost.onPlayerUpdate(event);

			ItemStack wand = p.getHeldItem();
			if(wand != null && wand.getItem() instanceof ItemCyclicWand){
				ItemCyclicWand.Timer.tickSpellTimer(wand);
			}
		}
		*/
	}
}
