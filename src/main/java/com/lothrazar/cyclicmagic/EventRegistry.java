package com.lothrazar.cyclicmagic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.lothrazar.cyclicmagic.proxy.ClientProxy;
import com.lothrazar.cyclicmagic.proxy.MessageKeyCast;
import com.lothrazar.cyclicmagic.proxy.MessageKeyLeft;
import com.lothrazar.cyclicmagic.proxy.MessageKeyRight;
import com.lothrazar.cyclicmagic.proxy.MessageKeyToggle;
import com.lothrazar.cyclicmagic.spell.SpellGhost;

public class EventRegistry {

	@SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) 
    {   
        if(ClientProxy.keySpellToggle.isPressed())
        {
       		ModMain.network.sendToServer( new MessageKeyToggle());
        }
        else if(ClientProxy.keySpellUp.isPressed())
        {
       		ModMain.network.sendToServer( new MessageKeyRight());
        }
        else if(ClientProxy.keySpellDown.isPressed())
        {
       		ModMain.network.sendToServer( new MessageKeyLeft());
        }
        else if(ClientProxy.keySpellCast.isPressed())
        {
        	BlockPos posMouse = null;
        	//TODO: we could make diff packets for cast on entiyt vs block
        	/*
        	 // What type of ray trace hit was this? 0 = block, 1 = entity 
    public MovingObjectPosition.MovingObjectType typeOfHit;*/
        	
        	int entity = (Minecraft.getMinecraft().objectMouseOver.entityHit == null) ? -1 :
        		Minecraft.getMinecraft().objectMouseOver.entityHit.getEntityId();
        	
        	if(Minecraft.getMinecraft().objectMouseOver == null){
        		System.out.println("CANNOT CAST: objectMouseOver null" );
        		return;
        	}
        	
    		if(Minecraft.getMinecraft().objectMouseOver.getBlockPos() != null)
    		{
    			posMouse = Minecraft.getMinecraft().objectMouseOver.getBlockPos();
    		}
    		else
    		{
    			posMouse = Minecraft.getMinecraft().thePlayer.getPosition();
    		}
    		
       		ModMain.network.sendToServer( new MessageKeyCast(posMouse
       				,Minecraft.getMinecraft().objectMouseOver.sideHit
       				,entity));
        }
    } 
	 
	@SubscribeEvent
	public void onClonePlayer(PlayerEvent.Clone event) 
	{ 
		PlayerPowerups.get(event.entityPlayer).copy(PlayerPowerups.get(event.original));
	}
	
	@SubscribeEvent
 	public void onEntityConstructing(EntityConstructing event)
 	{ 
 		if (event.entity instanceof EntityPlayer && PlayerPowerups.get((EntityPlayer) event.entity) == null)
 		{ 
 			PlayerPowerups.register((EntityPlayer) event.entity);
 		} 
 	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderTextOverlay(RenderGameOverlayEvent.Text event)
	{  
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer; 
		PlayerPowerups props = PlayerPowerups.get(player);
		
		if(props.getSpellToggle() != SpellRegistry.SPELL_TOGGLE_HIDE)
		{
			SpellRegistry.drawSpell(event);
		}
	}
	 /*
	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent event)
	{        
		if(event.pos == null || event.face == null ){return;}
	
		ItemStack held = event.entityPlayer.getCurrentEquippedItem();
	
		if(held != null && held.getItem() == ItemRegistry.chest_sack  && 
				Action.RIGHT_CLICK_BLOCK == event.action)
		{ 
			ItemChestSack.createAndFillChest(event.entityPlayer, held, event.pos.offset(event.face));
		}
	}
	*/
	@SubscribeEvent
	public void onEntityUpdate(LivingUpdateEvent event) 
	{  
		if(event.entityLiving == null){return;}
		
		if(event.entityLiving instanceof EntityPlayer)
		{
			SpellGhost.onPlayerUpdate(event); 
			
			SpellRegistry.tickSpellTimer((EntityPlayer)event.entityLiving);
		}

		PotionRegistry.tickSlowfall(event);
	     
		PotionRegistry.tickWaterwalk(event);
	     
		//PotionRegistry.tickLavawalk(event);
 
		//PotionRegistry.tickFrost(event); 
	}

	
	
	/*
	public static void playSoundAt(Entity player, String sound)
	{ 
		player.worldObj.playSoundAtEntity(player, sound, 1.0F, 1.0F);
	}
	
	public static void addChatMessage(EntityPlayer player,String string) 
	{ 
		player.addChatMessage(new ChatComponentTranslation(string));
	}

	
	public static String lang(String name)
	{
		return StatCollector.translateToLocal(name);
	}*/

}
