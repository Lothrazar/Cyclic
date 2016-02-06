package com.lothrazar.cyclicmagic;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
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
import com.lothrazar.cyclicmagic.spell.SpellGhost;

public class EventRegistry {

	@SubscribeEvent
	public void onConfigChanged(OnConfigChangedEvent event) {
		if (event.modID.equals(Const.MODID)){
			ModMain.cfg.syncConfig();
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onMouseInput(MouseEvent event){
		//DO NOT use InputEvent.MouseInputEvent 
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;

		if(SpellRegistry.spellsEnabled(player) == false){
			//you are not holding the wand - so go as normal
			return;
		}
		
		if(player.isSneaking()){
			if(event.dwheel < 0){
				ModMain.network.sendToServer(new MessageKeyRight());
				event.setCanceled(true);
			}
			else if(event.dwheel > 0){
				ModMain.network.sendToServer(new MessageKeyLeft());
				event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerInteractEvent(PlayerInteractEvent event){

		if(event.action == Action.LEFT_CLICK_BLOCK && event.world.getBlockState(event.pos) != null
				&& event.entityPlayer.getHeldItem() != null && event.entityPlayer.getHeldItem().getItem() instanceof ItemCyclicWand ){
		
			// important: LEFT_CLICK_BLOCK only fires on the server, not the client. Yo
			// http://www.minecraftforge.net/forum/index.php?topic=22348.0
			Block blockHit = event.world.getBlockState(event.pos).getBlock();
	
			if(blockHit == Blocks.crafting_table && event.entityPlayer instanceof EntityPlayerMP){
	
				ModMain.network.sendTo(new MessageOpenSpellbook(), (EntityPlayerMP)event.entityPlayer);
			}
			else if(blockHit != null){//was only bookshelf
				
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
		
		PotionRegistry.tickMagnet(event);
	}
}
