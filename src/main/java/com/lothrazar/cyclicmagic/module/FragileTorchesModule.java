package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FragileTorchesModule extends BaseEventModule  implements IHasConfig {
  private boolean fragileTorches;
  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    if (fragileTorches) { 
      Entity ent = event.getEntity();
      if (ent instanceof EntityLiving == false) { return; }
      EntityLivingBase living = (EntityLivingBase) event.getEntity();
      if (living == null) { return; }
      if (living.worldObj.getBlockState(living.getPosition()).getBlock() == Blocks.TORCH) {
        float oddsWillBreak = 0.01F;
        boolean playerCancelled = false;
        if (living instanceof EntityPlayer) {
          // EntityPlayer p = (EntityPlayer) living;
          // just dont let players break them. only other mobs.
          // if(p.isSneaking()){
          playerCancelled = true;// torches are safe from breaking
          // }
        }
        if (playerCancelled == false // if its a player, then the player is not
            // sneaking
            && living.worldObj.rand.nextDouble() < oddsWillBreak && living.worldObj.isRemote == false) {
          living.worldObj.destroyBlock(living.getPosition(), true);
        }
      }
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.blocks;
    config.addCustomCategoryComment(category, "Tweaks to new and existing blocks");
    fragileTorches = config.getBoolean("Fragile Torches", category, true,
        "Torches can get knocked over when passed through by living entities");
  }
}
