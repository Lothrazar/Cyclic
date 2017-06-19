package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FragileTorchesModule extends BaseEventModule implements IHasConfig {
  private static final float oddsWillBreak = 0.01F;
  private boolean fragileTorches;
  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    if (fragileTorches) {
      Entity ent = event.getEntity();
      World world = ent.getEntityWorld();
      if (world.isRemote) { return; } //we only need to break block on server, it gets propogated for us
      if (world.rand.nextDouble() > oddsWillBreak) { return; } //no chance of breaking anyway, just stop
      //ok the dice roll passed
      if (ent instanceof EntityLiving == false) { return; }
      EntityLivingBase living = (EntityLivingBase) event.getEntity();
      if (living == null) { return; }
      if (living instanceof EntityPlayer && ((EntityPlayer) living).isSneaking()) { return; } //if you are a player, then cancel if sneaking
      if (world.getGameRules().getBoolean("mobGriefing") == false) { return; }
      if (UtilWorld.isBlockTorch(world, living.getPosition())) {
        world.destroyBlock(living.getPosition(), true);
      }
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.blocks;
    config.addCustomCategoryComment(category, "Tweaks to new and existing blocks");
    fragileTorches = config.getBoolean("Fragile Torches", category, false,
        "Torches can get knocked over when passed through by living entities");
  }
}
