package com.lothrazar.cyclicmagic;

import org.apache.logging.log4j.Logger;    
import com.lothrazar.cyclicmagic.proxy.CommonProxy;
import com.lothrazar.cyclicmagic.proxy.MessageKeyCast;
import com.lothrazar.cyclicmagic.proxy.MessageKeyLeft;
import com.lothrazar.cyclicmagic.proxy.MessageKeyRight;
import com.lothrazar.cyclicmagic.proxy.MessageKeyToggle;
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
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
  
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
		MinecraftForge.EVENT_BUS.register(new EventRegistry()); 
		

	}

	@EventHandler
	public void onInit(FMLInitializationEvent event)
	{       
		ItemRegistry.register();
		BlockRegistry.register();
		ProjectileRegistry.register();
		PotionRegistry.register();		
		SpellRegistry.register();
		
		proxy.register();
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
