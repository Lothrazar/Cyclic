package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry;
import com.lothrazar.cyclicmagic.registry.PotionRegistry;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry.IPlayerExtendedProperties;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

public class ItemSleepingBag extends Item  implements IHasRecipe,IHasConfig{
	// thank you for the examples forge. player data storage based on API source
	// code example:
	// !!
	// https://github.com/MinecraftForge/MinecraftForge/blob/1.9/src/test/java/net/minecraftforge/test/NoBedSleepingTest.java

	int	levelBoost = PotionRegistry.I;	
	int	sleeping_hunger_seconds	= 20;

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

//			if(UtilWorld.isNight(world) == false){
			if (result == EntityPlayer.SleepResult.OK) {
				
				//final IPlayerExtendedProperties sleep = player.getCapability(ModMain.CAPABILITYSTORAGE, null);
				final IPlayerExtendedProperties sleep = CapabilityRegistry.getPlayerProperties(player);
				if (sleep != null) {

					sleep.setSleeping(true);
					stack.stackSize--;
					if(stack.stackSize == 0){
						stack = null;
					}
					
					player.addPotionEffect(new PotionEffect(MobEffects.HUNGER, sleeping_hunger_seconds * Const.TICKS_PER_SEC, levelBoost));
					player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, sleeping_hunger_seconds * Const.TICKS_PER_SEC, levelBoost));
					player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, sleeping_hunger_seconds * Const.TICKS_PER_SEC, levelBoost));
				
				}
				else{
					ModMain.logger.error("NULL IPlayerExtendedProperties found");
					//should never happen... but just in case
					UtilChat.addChatMessage(player, "tile.bed.noSleep");
				}
				return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
			}
			else{
				UtilChat.addChatMessage(player, "tile.bed.noSleep");
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