package com.lothrazar.cyclicmagic;

import org.apache.logging.log4j.Logger;    
import com.lothrazar.cyclicmagic.item.ItemChestSack;
import com.lothrazar.cyclicmagic.proxy.ClientProxy;
import com.lothrazar.cyclicmagic.proxy.CommonProxy;
import com.lothrazar.cyclicmagic.proxy.MessageKeyCast;
import com.lothrazar.cyclicmagic.proxy.MessageKeyLeft;
import com.lothrazar.cyclicmagic.proxy.MessageKeyRight;
import com.lothrazar.cyclicmagic.proxy.MessageKeyToggle;
import com.lothrazar.cyclicmagic.spell.ISpell;
import com.lothrazar.cyclicmagic.spell.SpellGhost;
import com.lothrazar.cyclicmagic.util.UtilTextureRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
  
/**
 * Split out into a standalone mod from my old abandoned one
 * https://github.com/PrinceOfAmber/SamsPowerups
 * @author Sam Bassett (Lothrazar)
 */
@Mod(modid = Const.MODID,  useMetadata = true )  
public class ModMain
{
	//TODO: DO NOT RESET TIMER IF CASTING FAILS
	@Instance(value = Const.MODID)
	public static ModMain instance;
	@SidedProxy(clientSide="com.lothrazar."+Const.MODID+".proxy.ClientProxy", serverSide="com.lothrazar."+Const.MODID+".proxy.CommonProxy")
	public static CommonProxy proxy;   
	public static Logger logger; 
	public static ModConfig cfg;
	public static SimpleNetworkWrapper network; 	
	public static CreativeTabs tabSamsContent = new CreativeTabs("tabScepter") 
	{ 
		@Override
		public Item getTabIconItem() 
		{ 
			return Items.stick;//TODO placeholder
		}
	};   
	

	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event)
	{ 
		logger = event.getModLog();  
		
		cfg = new ModConfig(new Configuration(event.getSuggestedConfigurationFile()));
	  
    	network = NetworkRegistry.INSTANCE.newSimpleChannel( Const.MODID );     	
    	
    	network.registerMessage(MessageKeyCast.class, MessageKeyCast.class, MessageKeyCast.ID, Side.SERVER);
    	network.registerMessage(MessageKeyLeft.class, MessageKeyLeft.class, MessageKeyLeft.ID, Side.SERVER);
    	network.registerMessage(MessageKeyRight.class, MessageKeyRight.class, MessageKeyRight.ID, Side.SERVER);
    	network.registerMessage(MessageKeyToggle.class, MessageKeyToggle.class, MessageKeyToggle.ID, Side.SERVER);

		//FMLCommonHandler.instance().bus().register(instance); 
		MinecraftForge.EVENT_BUS.register(instance); 
		
		SpellRegistry.setup();
	}

	@EventHandler
	public void onInit(FMLInitializationEvent event)
	{       
		ItemRegistry.register();
		BlockRegistry.register();
		ProjectileRegistry.register();
		
		proxy.registerRenderers();
	}
 
	public static EntityItem dropItemStackInWorld(World worldObj, BlockPos pos, ItemStack stack)
	{
		EntityItem entityItem = new EntityItem(worldObj, pos.getX(),pos.getY(),pos.getZ(), stack); 

 		if(worldObj.isRemote  ==  false)//do not spawn a second 'ghost' one on client side
 		{
 			worldObj.spawnEntityInWorld(entityItem);
 		}
 		
    	return entityItem;
	}

	public static String posToCSV(BlockPos pos)
    {
		return pos.getX()+","+pos.getY()+","+pos.getZ();
    }
	
    public static BlockPos stringCSVToBlockPos(String csv)
    {
    	String [] spl = csv.split(",");
    	//on server i got java.lang.ClassCastException: java.lang.String cannot be cast to java.lang.Integer
 //?? is it from this?
    	BlockPos p = null;
    	try{
    		p = new BlockPos(Integer.parseInt(spl[0]),Integer.parseInt(spl[1]),Integer.parseInt(spl[2]));
    	}
    	catch(java.lang.ClassCastException e)
    	{
    		System.out.println(e.getMessage());
    	}
        return p;
    }
	 
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
        	
    		if(Minecraft.getMinecraft().objectMouseOver != null && Minecraft.getMinecraft().objectMouseOver.getBlockPos() != null)
    		{
    			posMouse = Minecraft.getMinecraft().objectMouseOver.getBlockPos();
    		}
    		else
    		{
    			posMouse = Minecraft.getMinecraft().thePlayer.getPosition();
    		}
    		
       		ModMain.network.sendToServer( new MessageKeyCast(posMouse));
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
			drawSpell(event);
		}
	}
	 
	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent event)
	{        
		if(event.pos == null || event.face == null ){return;}
	
		ItemStack held = event.entityPlayer.getCurrentEquippedItem();
	
		if(held != null && held.getItem() == ItemRegistry.itemChestSack  && 
				Action.RIGHT_CLICK_BLOCK == event.action)
		{ 
			ItemChestSack.createAndFillChest(event.entityPlayer, held, event.pos.offset(event.face));
		}
	}
	
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

	
	
	@SideOnly(Side.CLIENT)
	private void drawSpell(RenderGameOverlayEvent.Text event)
	{ 
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer; 

		ISpell spell = SpellRegistry.getPlayerCurrentISpell(player);

		if(Minecraft.getMinecraft().gameSettings.showDebugInfo)
		{
			event.left.add(StatCollector.translateToLocal("key.spell."+spell.getSpellName()));
		}
		else
		{
			int ymain = 12;
			int dim = 12;
				
			int x = 12, y = 2;
			
			//Item ptr = SpellRegistry.canPlayerCastAnything(player) ? ItemRegistry.exp_cost_dummy : ItemRegistry.exp_cost_empty_dummy;
			//spell.getIconDisplayHeader()
			//UtilTextureRender.renderItemAt(new ItemStack(ptr),x,y,dim);
			
			//TODO: use the empty spell one also
			UtilTextureRender.renderResourceAt(spell.getIconDisplayHeader(),x,y,dim);
			//int ysmall = ymain - 3;
			int xmain = 10;
			ymain = 14;
			if(spell.getIconDisplay() != null)
			{
				x = xmain; 
				y = ymain;
				dim = 16;
				UtilTextureRender.renderResourceAt(spell.getIconDisplay(),x,y,dim);
			}
			
			
			ISpell spellNext = spell.left();//SpellRegistry.getSpellFromType(spell.getSpellID().next());
			ISpell spellPrev = spell.right();//SpellRegistry.getSpellFromType(spell.getSpellID().prev());
			
			
			if(spellNext != null)// && spellNext.getIconDisplay() != null
			{
				x = xmain-3; 
				y = ymain + 16;
				dim = 16/2;
				UtilTextureRender.renderResourceAt(spellNext.getIconDisplay(),x,y,dim);
				
				ISpell sLeftLeft = spellNext.left();//SpellRegistry.getSpellFromType(spellNext.getSpellID().next());

				if(sLeftLeft != null && sLeftLeft.getIconDisplay() != null)
				{
					x = xmain-3 - 1; 
					y = ymain + 16+14;
					dim = 16/2 - 2;
					UtilTextureRender.renderResourceAt(sLeftLeft.getIconDisplay(),x,y,dim);
					
					ISpell another = sLeftLeft.left();
					if(another != null)
					{
						x = xmain-3 - 3; 
						y = ymain + 16+14+10;
						dim = 16/2 - 4;
						UtilTextureRender.renderResourceAt(another.getIconDisplay(),x,y,dim);
					}
				}
			}
			if(spellPrev != null)// && spellPrev.getIconDisplay() != null
			{
				x = xmain+6; 
				y = ymain + 16;
				dim = 16/2;
				UtilTextureRender.renderResourceAt(spellPrev.getIconDisplay(),x,y,dim);

				ISpell sRightRight = spellPrev.right();//SpellRegistry.getSpellFromType(spellPrev.getSpellID().prev());

				if(sRightRight != null && sRightRight.getIconDisplay() != null)
				{
					x = xmain+6 + 4; 
					y = ymain + 16+14;
					dim = 16/2 - 2;
					UtilTextureRender.renderResourceAt(sRightRight.getIconDisplay(),x,y,dim);
					
					ISpell another = sRightRight.right();
					if(another != null)
					{
						x = xmain+6 +7; 
						y = ymain + 16+14+10;
						dim = 16/2 - 4;
						UtilTextureRender.renderResourceAt(another.getIconDisplay(),x,y,dim);
					}
				}
			}
			
		
			
		}
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


	public static void teleportWallSafe(EntityLivingBase player, World world, BlockPos coords)
	{
		player.setPositionAndUpdate(coords.getX(), coords.getY(), coords.getZ()); 

		moveEntityWallSafe(player, world);
	}
	
	public static void moveEntityWallSafe(EntityLivingBase entity, World world) 
	{
		while (!world.getCollidingBoundingBoxes(entity, entity.getEntityBoundingBox()).isEmpty())
		{
			entity.setPositionAndUpdate(entity.posX, entity.posY + 1.0D, entity.posZ);
		}
	}
	
	public static void addOrMergePotionEffect(EntityPlayer player, PotionEffect newp)
	{
		if(player.isPotionActive(newp.getPotionID()))
		{
			//do not use built in 'combine' function, just add up duration myself
			PotionEffect p = player.getActivePotionEffect(Potion.potionTypes[newp.getPotionID()]);
			
			int ampMax = Math.max(p.getAmplifier(), newp.getAmplifier());
		
			player.addPotionEffect(new PotionEffect(newp.getPotionID()
					,newp.getDuration()+p.getDuration()
					,ampMax));
		}
		else
		{
			player.addPotionEffect(newp);
		}
	}
	
	public static String posToStringCSV(BlockPos position) 
	{ 
		return position.getX() + ","+position.getY()+","+position.getZ();
	} 
	
	public static void incrementPlayerIntegerNBT(EntityPlayer player, String prop, int inc)
	{
		int prev = player.getEntityData().getInteger(prop);
		prev += inc; 
		player.getEntityData().setInteger(prop, prev);
	}
}
