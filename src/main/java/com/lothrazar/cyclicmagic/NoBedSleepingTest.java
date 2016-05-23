package com.lothrazar.cyclicmagic;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.entity.player.EntityPlayer.EnumStatus;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTBase.NBTPrimitive;
import net.minecraft.nbt.NBTTagByte;
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
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = NoBedSleepingTest.MODID, version = NoBedSleepingTest.VERSION)
public class NoBedSleepingTest
{
	//thank you for the examples forge !! https://github.com/MinecraftForge/MinecraftForge/blob/1.9/src/test/java/net/minecraftforge/test/NoBedSleepingTest.java
    public static final String MODID = "ForgeDebugNoBedSleeping";
    public static final String VERSION = "1.0";
    @CapabilityInject(IExtraSleeping.class)
    private static final Capability<IExtraSleeping> SLEEP_CAP = null;

    @SidedProxy
    public static CommonProxy proxy = null;

    public static abstract class CommonProxy
    {
        public void preInit(FMLPreInitializationEvent event)
        {
            GameRegistry.register(ItemSleepingBag.instance);
            CapabilityManager.INSTANCE.register(IExtraSleeping.class, new Storage(), DefaultImpl.class);
            MinecraftForge.EVENT_BUS.register(new EventPlayerData());
        }
    }

    public static final class ServerProxy extends CommonProxy {}

    public static final class ClientProxy extends CommonProxy
    {
        @Override
        public void preInit(FMLPreInitializationEvent event)
        {
            super.preInit(event);
            ModelLoader.setCustomModelResourceLocation(ItemSleepingBag.instance, 0, new ModelResourceLocation(new ResourceLocation(MODID, ItemSleepingBag.name), "inventory"));
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.preInit(event);
    }

    public static class EventPlayerData
    {
        @SubscribeEvent
        public void onEntityConstruct(AttachCapabilitiesEvent evt)
        {
            evt.addCapability(new ResourceLocation(MODID, "IModdedSleeping"), new ICapabilitySerializable<NBTTagCompound>()
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
            setUnlocalizedName(MODID + ":" + name);
            setRegistryName(new ResourceLocation(MODID, name));
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
}