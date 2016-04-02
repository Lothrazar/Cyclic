package com.lothrazar.cyclicmagic.registry;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.SpellRegistry;
import com.lothrazar.cyclicmagic.net.MessageKeyLeft;
import com.lothrazar.cyclicmagic.net.MessageKeyRight;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;

public class EventRegistry{

	//TODO: enabled by default, disable all at once?
	public boolean appleEmerald = true;
	public boolean smoothstoneTools = true; 
	public boolean furnaceNeedsCoal = true;  
	public boolean plantDespawningSaplings = true; 
	//public boolean wandPiston;
	public boolean simpleDispenser = true; 
	public boolean dropPlayerSkullOnDeath = true;
	public boolean cmd_searchspawner = true; 
	public boolean mushroomBlocksCreativeInventory = true;
	public boolean barrierCreativeInventory = true;
	public boolean dragonEggCreativeInventory = true;
	public boolean farmlandCreativeInventory = true;
	public boolean spawnerCreativeInventory = true;  
	public boolean removeZombieCarrotPotato = true;
	public boolean petNametagChat = true;
	public boolean playerDeathCoordinates = true;

	public boolean flintPumpkin = true;
	public boolean endermenDropCarryingBlock = true;
 
	public int chanceZombieChildFeather=50;
	public int chanceZombieVillagerEmerald=50;

	public boolean canNameVillagers = true;
	//public boolean horse_food_upgrades;
	public int cowExtraLeather=3;
	//public int sleeping_hunger_seconds;  
	//public boolean experience_bottle;
	//public boolean experience_bottle_return;

	//public boolean cmd_effectpay;

	private boolean petNametagDrops = true;
	private boolean skullSignNames = true;
	//public boolean appleFrost;
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
		//World world = entityLiving.getEntityWorld();
		
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
	
	
	

	@SubscribeEvent
	public void onLivingDropsEvent(LivingDropsEvent event)
	{
		Entity entity = event.getEntity();
		World worldObj = entity.getEntityWorld();
		List<EntityItem> drops = event.getDrops();
		
		 
		BlockPos pos = entity.getPosition();

		if( entity instanceof EntityZombie) //how to get this all into its own class
		{  
			EntityZombie z = (EntityZombie)entity;
			
			if(removeZombieCarrotPotato)
				for(int i = 0; i < drops.size(); i++) 
				{
					EntityItem item = drops.get(i);
					
					if(item.getEntityItem().getItem() == Items.carrot || item.getEntityItem().getItem() == Items.potato)
					{ 
						drops.remove(i);
					}
				}
	
			if(z.isChild() && chanceZombieChildFeather > 0 && 
					worldObj.rand.nextInt(100) <= chanceZombieChildFeather)
			{ 
				drops.add(new EntityItem(worldObj,pos.getX(),pos.getY(),pos.getZ()
						,new ItemStack(Items.feather)));
			}
			 
			if(z.isVillager() && chanceZombieVillagerEmerald > 0
					&& worldObj.rand.nextInt(100) <=  chanceZombieVillagerEmerald)
			{
				drops.add(new EntityItem(worldObj,pos.getX(),pos.getY(),pos.getZ() 
						,new ItemStack(Items.emerald)));
			} 
		} 
		 
		if(petNametagDrops //no need to restrict to pets && SamsUtilities.isPet(event.entity)
		  && entity.getCustomNameTag() != null && //'custom' is blank if no nametag
		   entity.getCustomNameTag() != ""   
		   ) 
		{  
			//item stack NBT needs the name enchanted onto it
			ItemStack nameTag = buildEnchantedNametag(entity.getCustomNameTag());
		  
			dropItemStackInWorld(worldObj, entity.getPosition(), nameTag);  
		}
		
		if(petNametagChat && 
			entity instanceof EntityLivingBase && 
			entity.getCustomNameTag() != null && //'custom' is blank if no nametag
		    entity.getCustomNameTag() != ""   &&
		    entity.worldObj.isRemote == false) 
		{    
			//show message as if player, works since EntityLiving extends EntityLivingBase
	  
			UtilChat.addChatMessage(worldObj,(event.getSource().getDeathMessage((EntityLivingBase)entity)));
		}
		 
		if(cowExtraLeather > 0 && entity instanceof EntityCow)
		{
			drops.add(new EntityItem(worldObj,pos.getX(),pos.getY(),pos.getZ(), new ItemStack(Items.leather,cowExtraLeather)));
		} 
	}
	
	@SubscribeEvent
	public void onEntityInteractEvent(EntityInteractEvent event)
  	{
		EntityPlayer entityPlayer = event.getEntityPlayer();
		ItemStack held = entityPlayer.getHeldItem(event.getHand()); 
		Entity target = event.getTarget();
		/*
		if(held != null && held.getItem() == ItemRegistry.respawn_egg_empty )
		{
			ItemRespawnEggEmpty.entitySpawnEgg(event.entityPlayer, event.target); 
		}*/
		  
		if(canNameVillagers &&  //how to get this all into its own class
		  held != null && held.getItem() == Items.name_tag && 
		  held.hasDisplayName()  )
		{    
			if(target instanceof EntityVillager)
			{
				
				EntityVillager v = (EntityVillager)target;
				  
				v.setCustomNameTag(held.getDisplayName()); 
				
				if (entityPlayer.capabilities.isCreativeMode == false)
		        {
					entityPlayer.inventory.decrStackSize(entityPlayer.inventory.currentItem, 1);
		        }
				
				event.setCanceled(true);//stop the GUI inventory opening 
			} 
		} 
  	} 

	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent event)
  	{        
		EntityPlayer entityPlayer = event.getEntityPlayer();
		BlockPos pos = event.getPos();
		World worldObj = event.getWorld();
		
		if(pos == null){return;}
		IBlockState bstate = worldObj.getBlockState(pos);
		if(bstate == null){return;}
		
		//event has no hand??
		ItemStack held = entityPlayer.getActiveItemStack();

		TileEntity container = worldObj.getTileEntity(pos);
 
		if(skullSignNames &&  //how to get this all into its own class
				event.getAction() == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK && 
				entityPlayer.isSneaking() && 
				held != null && held.getItem() == Items.skull && 
				held.getItemDamage() == Const.skull_player	&& 
				container != null &&
				container instanceof TileEntitySign)
		{
			TileEntitySign sign = (TileEntitySign)container; 
			String firstLine = sign.signText[0].getUnformattedText();
			
			if(firstLine == null) { firstLine = ""; }
			if(firstLine.isEmpty() || firstLine.split(" ").length == 0)
			{
				held.setTagCompound(null); 
			}
			else
			{ 
				firstLine = firstLine.split(" ")[0];
				
				if(held.getTagCompound() == null) held.setTagCompound(new NBTTagCompound());
				
				held.getTagCompound().setString("SkullOwner",firstLine);
			} 
		} //end of skullSignNames
   	}
   	
	/*
	 //TODO: this should be in standalone , but what goes with it?
	@SubscribeEvent
	public void onPlayerWakeUpEvent(PlayerWakeUpEvent event)
	{
		if(event.entityPlayer.worldObj.isRemote == false)
		{
			boolean didSleepAllNight = !event.updateWorld;
			
			if(didSleepAllNight && sleeping_hunger_seconds > 0)
			{ 
				int levelBoost = 0;//1 means Hunger II. int = 2 means Hunger III, etc.
				
				event.entityPlayer.addPotionEffect(new PotionEffect(Potion.hunger.id,  sleeping_hunger_seconds * 20, levelBoost));
			}
		}
	}*/

	@SubscribeEvent
	public void onLivingDeathEvent(LivingDeathEvent event)
	{
		Entity entity = event.getEntity();
		
		if( endermenDropCarryingBlock
			&& entity instanceof EntityEnderman)
		{ 
			EntityEnderman mob = (EntityEnderman)entity;
 
			IBlockState bs = mob.getHeldBlockState();// mob.func_175489_ck();
			
			if(bs != null && bs.getBlock() != null && entity.worldObj.isRemote == false)
			{
				dropItemStackInWorld(entity.worldObj, mob.getPosition(), bs.getBlock());
			} 
		}
		
		if(entity instanceof EntityPlayer)
		{ 
			EntityPlayer player = (EntityPlayer)entity;
			
			if(dropPlayerSkullOnDeath)
			{  
				ItemStack skull = buildNamedPlayerSkull(player);
				 
				dropItemStackInWorld(entity.worldObj, player.getPosition(), skull);
			}
			
			if(playerDeathCoordinates)
			{
				String coordsStr = posToString(player.getPosition()); 
				UtilChat.addChatMessage(player,player.getDisplayNameString() + " has died at " + coordsStr);
			}
		}
	}
	
	public static EntityItem dropItemStackInWorld(World worldObj, BlockPos pos, Block block)
	{
		return dropItemStackInWorld(worldObj, pos, new ItemStack(block));  
	}
	
	public static EntityItem dropItemStackInWorld(World worldObj, BlockPos pos, Item item)
	{
		return dropItemStackInWorld(worldObj, pos, new ItemStack(item)); 
	}
	
	public static EntityItem dropItemStackInWorld(World worldObj, BlockPos pos, ItemStack stack)
	{
		EntityItem entityItem = new EntityItem(worldObj, pos.getX(),pos.getY(),pos.getZ(), stack); 

 		if(worldObj.isRemote==false)//do not spawn a second 'ghost' one on client side
 			worldObj.spawnEntityInWorld(entityItem);
    	return entityItem;
	}
	public static String posToString(BlockPos position) 
	{ 
		return "["+ position.getX() + ", "+position.getY()+", "+position.getZ()+"]";
	} 

	public static ItemStack buildNamedPlayerSkull(EntityPlayer player) 
	{
		return buildNamedPlayerSkull(player.getDisplayNameString());
	}
	public static ItemStack buildNamedPlayerSkull(String displayNameString) 
	{
		ItemStack skull =  new ItemStack(Items.skull,1,Const.skull_player);

		if(skull.getTagCompound() == null) {skull.setTagCompound(new NBTTagCompound());}
		
		skull.getTagCompound().setString("SkullOwner",displayNameString);
		
		return skull; 
	}
/*
	@SubscribeEvent
	public void onEntityJoinWorldEvent(EntityJoinWorldEvent event)
	{ 
		
		if(event.entity instanceof EntityLivingBase && event.world.isRemote)
		{
			EntityLivingBase living = (EntityLivingBase)event.entity;
		
			if(living instanceof EntityWolf && ((EntityWolf)living).isTamed())
			{
				setMaxHealth(living,heartsWolfTamed*2);
			}
			if(living instanceof EntityOcelot && ((EntityOcelot)living).isTamed())
			{
				setMaxHealth(living,heartsCatTamed*2);
			}
			
			if(living instanceof EntityVillager && ((EntityVillager)living).isChild() == false)
			{
				setMaxHealth(living,heartsVillager*2);			
			}
		}
	}*/


	public static ItemStack buildEnchantedNametag(String customNameTag) 
	{
		//build multi-level NBT tag so it matches a freshly enchanted one
		
		ItemStack nameTag = new ItemStack(Items.name_tag, 1); 
		  
		NBTTagCompound nbt = new NBTTagCompound(); 
		NBTTagCompound display = new NBTTagCompound();
		display.setString("Name", customNameTag);//NOT "CustomName" implied by commandblocks/google 
		nbt.setTag("display",display);
		nbt.setInteger("RepairCost", 1);
		
		nameTag.setTagCompound(nbt);//put the data into the item stack
		 
		return nameTag;
	}
}
