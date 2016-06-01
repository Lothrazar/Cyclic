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
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemSleepingBag extends BaseTool  implements IHasRecipe,IHasConfig{
	// thank you for the examples forge. player data storage based on API source
	// code example:
	// !!
	// https://github.com/MinecraftForge/MinecraftForge/blob/1.9/src/test/java/net/minecraftforge/test/NoBedSleepingTest.java

	private static final int	levelBoost = PotionRegistry.I;	
	private static final int	seconds	= 20;
	private static final int	durability	= 100;

	public ItemSleepingBag() {
		super(durability);  
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
				
					player.addPotionEffect(new PotionEffect(MobEffects.HUNGER, seconds * Const.TICKS_PER_SEC, levelBoost));
					player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, seconds * Const.TICKS_PER_SEC, levelBoost));
					player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, seconds * Const.TICKS_PER_SEC, levelBoost));

					this.onUse(stack, player, world, hand);
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

		GameRegistry.addRecipe(new ItemStack(this), 
				"   ", 
				"lbl", 
				"   ", 
			'b',Items.BED, 
			'l',Items.LEATHER); 
	}
}