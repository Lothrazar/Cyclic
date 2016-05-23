package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.ModMain.IPlayerExtendedProperties;
import com.lothrazar.cyclicmagic.util.Const;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemSleepingBag extends Item
{
	//thank you for the examples forge. player data storage based on API source code example:
	//!! https://github.com/MinecraftForge/MinecraftForge/blob/1.9/src/test/java/net/minecraftforge/test/NoBedSleepingTest.java
	   
    public static final ItemSleepingBag instance = new ItemSleepingBag();
    public static final String name = "sleeping_bag";

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
                final IPlayerExtendedProperties sleep = player.getCapability(ModMain.CAPABILITYSTORAGE, null);
                if (sleep != null){
                    sleep.setSleeping(true);}
                return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
            }
        }
        return ActionResult.newResult(EnumActionResult.PASS, stack);
    }
}