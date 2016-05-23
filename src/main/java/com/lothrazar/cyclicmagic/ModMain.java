 package com.lothrazar.cyclicmagic;
 
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import com.lothrazar.cyclicmagic.proxy.CommonProxy;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.CommandRegistry;
import com.lothrazar.cyclicmagic.registry.DispenserBehaviorRegistry;
import com.lothrazar.cyclicmagic.registry.EventRegistry; 
import com.lothrazar.cyclicmagic.registry.FuelRegistry;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.MobSpawningRegistry;
import com.lothrazar.cyclicmagic.registry.PacketRegistry;
import com.lothrazar.cyclicmagic.registry.PotionRegistry;
import com.lothrazar.cyclicmagic.registry.ProjectileRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeAlterRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeNewRegistry;
import com.lothrazar.cyclicmagic.registry.ReflectionRegistry;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.registry.SpellRegistry;
import com.lothrazar.cyclicmagic.registry.StackSizeRegistry;
import com.lothrazar.cyclicmagic.registry.TileEntityRegistry;
import com.lothrazar.cyclicmagic.registry.VillageTradeRegistry;
import com.lothrazar.cyclicmagic.registry.WorldGenRegistry;
import com.lothrazar.cyclicmagic.util.Const;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = Const.MODID, useMetadata = true, canBeDeactivated = false, updateJSON = "https://raw.githubusercontent.com/PrinceOfAmber/CyclicMagic/master/update.json", guiFactory = "com.lothrazar." + Const.MODID + ".gui.IngameConfigFactory")
public class ModMain {

	@Instance(value = Const.MODID)
	public static ModMain						instance;
	@SidedProxy(clientSide = "com.lothrazar." + Const.MODID + ".proxy.ClientProxy", serverSide = "com.lothrazar." + Const.MODID + ".proxy.CommonProxy")
	public static CommonProxy					proxy;
	public static ModLogger						logger;
	private static Configuration				config;
	public static SimpleNetworkWrapper			network;
	public final static CreativeTabs			TAB	= new CreativeTabs(Const.MODID) {
        @Override
        public Item getTabIconItem() {
        	Item tab = ItemRegistry.itemMap.get("apple_diamond");
        	if(tab == null){tab = Items.DIAMOND;}
            return tab;
        }
    };
    private EventRegistry events;

	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event) {

		logger = new ModLogger(event.getModLog());
		config = new Configuration(event.getSuggestedConfigurationFile());
		
		events = new EventRegistry();
		ItemRegistry.construct();//MAYBE it should be a constructed, not static
		BlockRegistry.construct(); 
		
		config.load();
		syncConfig();

		network = NetworkRegistry.INSTANCE.newSimpleChannel(Const.MODID);
		
		events.register();

		 GameRegistry.register(ItemSleepingBag.instance);
         CapabilityManager.INSTANCE.register(IExtraSleeping.class, new Storage(), DefaultImpl.class);
         MinecraftForge.EVENT_BUS.register(new EventPlayerData());
		
		ReflectionRegistry.register(); 
		PacketRegistry.register(network);
		VillageTradeRegistry.register();
		SoundRegistry.register();
	}


	@EventHandler
	public void onInit(FMLInitializationEvent event) {

		PotionRegistry.register();
		ItemRegistry.register();
		BlockRegistry.register();
		SpellRegistry.register();
		MobSpawningRegistry.register();
		WorldGenRegistry.register();
		FuelRegistry.register();
		StackSizeRegistry.register();
		RecipeAlterRegistry.register();
		RecipeNewRegistry.register();

		proxy.register();

		TileEntityRegistry.register();

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new ModGuiHandler());

		ProjectileRegistry.register(event);
	}

	@EventHandler
	public void onPostInit(FMLPostInitializationEvent event) {

		// registers all plantable crops. the plan is to work with non vanilla data
		DispenserBehaviorRegistry.register();
	}

	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {
		CommandRegistry.register(event);
	}

	public static Configuration getConfig() {
		return config;
	}

	public  void syncConfig() {
		// hit on startup and on change event from
		//we cant make this a list/loop because the order does matter
		Configuration c = getConfig();
		WorldGenRegistry.syncConfig(c);
		PotionRegistry.syncConfig(c);
		events.syncConfig(c);
		BlockRegistry.syncConfig(c);
		ItemRegistry.syncConfig(c);
		FuelRegistry.syncConfig(c);
		MobSpawningRegistry.syncConfig(c);
		RecipeAlterRegistry.syncConfig(c);
		RecipeNewRegistry.syncConfig(c);
		DispenserBehaviorRegistry.syncConfig(c);
		StackSizeRegistry.syncConfig(c);
		SpellRegistry.syncConfig(c); 
		CommandRegistry.syncConfig(c);
		VillageTradeRegistry.syncConfig(c);

		c.save();
		 
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	//thank you for the examples forge !! https://github.com/MinecraftForge/MinecraftForge/blob/1.9/src/test/java/net/minecraftforge/test/NoBedSleepingTest.java
    @CapabilityInject(IExtraSleeping.class)
    private static final Capability<IExtraSleeping> SLEEP_CAP = null;
// 
//    public static abstract class CommonProxy
//    {
//        public void preInit(FMLPreInitializationEvent event)
//        {
//            GameRegistry.register(ItemSleepingBag.instance);
//            CapabilityManager.INSTANCE.register(IExtraSleeping.class, new Storage(), DefaultImpl.class);
//            MinecraftForge.EVENT_BUS.register(new EventPlayerData());
//        }
//    }
//
//    public static final class ServerProxy extends CommonProxy {}
//
//    public static final class ClientProxy extends CommonProxy
//    {
//        @Override
//        public void preInit(FMLPreInitializationEvent event)
//        {
//            super.preInit(event);
//            ModelLoader.setCustomModelResourceLocation(ItemSleepingBag.instance, 0, new ModelResourceLocation(new ResourceLocation(MODID, ItemSleepingBag.name), "inventory"));
//        }
//    }

//    @Mod.EventHandler
//    public void preInit(FMLPreInitializationEvent event)
//    {
//        proxy.preInit(event);
//    }

    public static class EventPlayerData
    {
        @SubscribeEvent
        public void onEntityConstruct(AttachCapabilitiesEvent evt)
        {
            evt.addCapability(new ResourceLocation(Const.MODID, "IModdedSleeping"), new ICapabilitySerializable<NBTTagCompound>()
            {
                IExtraSleeping inst = SLEEP_CAP.getDefaultInstance();
                @Override
                public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
                    return capability == SLEEP_CAP;
                }

                @Override
                public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
                    return capability == SLEEP_CAP ? SLEEP_CAP.<T>cast(inst) : null;
                }

                @Override
                public NBTTagCompound serializeNBT() {
                	try{
                    return (NBTTagCompound)SLEEP_CAP.getStorage().writeNBT(SLEEP_CAP, inst, null);
                	}catch(java.lang.ClassCastException e){
                		return new NBTTagCompound();
                	}
                }

                @Override
                public void deserializeNBT(NBTTagCompound nbt) {
                    SLEEP_CAP.getStorage().readNBT(SLEEP_CAP, inst, null, nbt);
                }
            });
        }

        @SubscribeEvent
        public void onBedCheck(SleepingLocationCheckEvent evt)
        {
            final IExtraSleeping sleep = evt.getEntityPlayer().getCapability(SLEEP_CAP, null);
        	System.out.println("onBedCheck isnull "+ (sleep==null));
        	if(sleep!=null)System.out.println("onWakeUp.isSleeping "+sleep.isSleeping());
        	
            if (sleep != null && sleep.isSleeping())
                evt.setResult(Result.ALLOW);
        }

        @SubscribeEvent
        public void onWakeUp(PlayerWakeUpEvent evt)
        {

            final IExtraSleeping sleep = evt.getEntityPlayer().getCapability(SLEEP_CAP, null);
        	System.out.println("onWakeUp isnull "+ (sleep==null));
        	if(sleep!=null)System.out.println("onWakeUp.isSleeping "+sleep.isSleeping());
        	
            if (sleep != null)
                sleep.setSleeping(false);
        }
    }

    public interface IExtraSleeping {
        boolean isSleeping();
        void setSleeping(boolean value);
//        boolean canSleep();
//        void setCanSleep(boolean value);
    }

    public static class Storage implements IStorage<IExtraSleeping>
    {
        @Override
        public NBTTagCompound writeNBT(Capability<IExtraSleeping> capability, IExtraSleeping instance, EnumFacing side)
        {
//NBTTagCompound extends NBTBase so its fine
        	NBTTagCompound tags = new NBTTagCompound();
        	tags.setByte("isSleeping", (byte)(instance.isSleeping() ? 1 : 0));
        	//NBTTagByte tag = new NBTTagByte((byte)(instance.isSleeping() ? 1 : 0));
            return tags;
        }

        @Override
        public void readNBT(Capability<IExtraSleeping> capability, IExtraSleeping instance, EnumFacing side, NBTBase nbt)
        {
        	NBTTagCompound tags ;
        	if(nbt instanceof NBTTagCompound == false){
        		//then discard it and make a new one
            	tags = new NBTTagCompound();
        	}
        	else
        		tags = (NBTTagCompound)nbt;
        	
            instance.setSleeping( tags.getByte("isSleeping") == 1 );
        }
    }

    public static class DefaultImpl implements IExtraSleeping
    {
        private boolean isSleeping = false;
        //private boolean canSleep = false;
        @Override public boolean isSleeping() { return isSleeping; }
        @Override public void setSleeping(boolean value) { this.isSleeping = value; }
//		@Override
//		public boolean canSleep() {
//			return canSleep;
//		}
//		@Override
//		public void setCanSleep(boolean value) {
//			canSleep = value;
//		}
    }

    public static class ItemSleepingBag extends Item
    {
        public static final ItemSleepingBag instance = new ItemSleepingBag();
        public static final String name = "sleeping_pill";

        private ItemSleepingBag()
        {
            setCreativeTab(ModMain.TAB);
            setUnlocalizedName(Const.MODID + ":" + name);
            setRegistryName(new ResourceLocation(Const.MODID, name));
        }

        @Override
        public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
        {
            if (!world.isRemote)
            {

                final EntityPlayer.SleepResult result = player.trySleep(player.getPosition());
                if (result == EntityPlayer.SleepResult.OK)
                {
                    final IExtraSleeping sleep = player.getCapability(SLEEP_CAP, null);
                    if (sleep != null)
                        sleep.setSleeping(true);
                    return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
                }
            }
            return ActionResult.newResult(EnumActionResult.PASS, stack);
        }
    }
	
	
	
	
	
	
	
	

	/* TODO LIST
	 * 
	 ***** TODO
	 *  
	 *[disabled] building spells: make phantom/ghost/outline/particle blocks
	 *
	 *some sort of noclip feature? temporary?
	 * 
	 * pets live longer and/or respawn
	 *
	 *make silk touch work on silverfish blocks
	 *
	 * clay generation in some biomes , all the way down -- plains and swamp?
	 * 
	 * ROTATE: STAIRS: allow switch frop top to bottom
	 * 
	 * crafting table hotkeys - numpad?
	 * 
	 * some sort of tool that displays trade of every nearby villager?
	 * 
	 * rebalance emerald armor numbers plan.
	 * 
	 * achievemnets give exp
	 *  // https://github.com/PrinceOfAmber/SamsPowerups/blob/5ac4556cb99266fa3f322ef8bfdf75683aa2f26a/src/main/java/com/lothrazar/samspowerups/util/AchievementFinder.java
	 * 
	 * Make minecarts kill and push through entities that get hit (ex: pigmen on rail)
	 * 
	 * Getting hit by a bat causes blindness to player
	 * 
	 * https://www.reddit.com/r/minecraftsuggestions/comments/4fgcb2/make_dry_sponges_usable_by_dispensers/
	 * 
	 * //IDEA: make boats float
	 * https://www.reddit.com/r/minecraftsuggestions/comments/4d4ob1/
	 * make_boats_float_again/
	 * 
	 * https://www.reddit.com/r/minecraftsuggestions/comments/4ff8bu/armor_stand_with_arms/
	 * ^^ and can they hold weapons???
	 * 
	 * https://www.reddit.com/r/minecraftsuggestions/comments/4f5xzq/golden_compass_points_to_its_creation_spot/
	 * 
	 * https://www.reddit.com/r/minecraftsuggestions/comments/4f6420/a_new_type_of_compass_that_points_to_your_most/
	 * 
	 * https://www.reddit.com/r/minecraftsuggestions/comments/4f35fx/red_sand_should_generate_on_lava_ocean_beaches_in/
	 * 
	 * 
	 */
}
