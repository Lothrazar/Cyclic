package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry.IPlayerExtendedProperties;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilInventory;
import com.lothrazar.cyclicmagic.util.UtilWorld;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

public class ItemSleepingBag extends Item  implements IHasRecipe,IHasConfig{
	// thank you for the examples forge. player data storage based on API source
	// code example:
	// !!
	// https://github.com/MinecraftForge/MinecraftForge/blob/1.9/src/test/java/net/minecraftforge/test/NoBedSleepingTest.java

	//public static final ItemSleepingBag instance = new ItemSleepingBag();
	//public static final String name = "sleeping_bag";

	public ItemSleepingBag() {
		super();  
		this.setMaxStackSize(1);
		setCreativeTab(ModMain.TAB);
		//setUnlocalizedName(Const.MODID + ":" + name);
		//setRegistryName(new ResourceLocation(Const.MODID, name));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {	
		if (!world.isRemote) {
			final EntityPlayer.SleepResult result = player.trySleep(player.getPosition());
			
			if (result == EntityPlayer.SleepResult.OK) {
				
				//final IPlayerExtendedProperties sleep = player.getCapability(ModMain.CAPABILITYSTORAGE, null);
				final IPlayerExtendedProperties sleep = CapabilityRegistry.getPlayerProperties(player);
				if (sleep != null) {
					if(UtilWorld.isNight(world) == false){
						UtilChat.addChatMessage(player, "tile.bed.noSleep");
					}
					else{
						sleep.setSleeping(true);
	
						stack.stackSize--;
						
						if(stack.stackSize == 0){
							stack = null;
						}
					}
				}
				else{
					ModMain.logger.error("NULL IPlayerExtendedProperties found");
				}
				return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
			}
		}
		return ActionResult.newResult(EnumActionResult.PASS, stack);
	}

	@Override
	public void syncConfig(Configuration config) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addRecipe() {
		// TODO Auto-generated method stub
		
	}
}