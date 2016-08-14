package com.lothrazar.cyclicmagic.module;
import java.text.DecimalFormat;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.item.ItemHorseUpgrade;
import com.lothrazar.cyclicmagic.item.ItemHorseUpgrade.HorseUpgradeType;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HorseFoodModule extends BaseEventModule {
  private boolean enableHorseFoodUpgrades;
  @Override
  public void onInit() {
    if(enableHorseFoodUpgrades){
      ItemRegistry.addItem(new ItemHorseUpgrade(HorseUpgradeType.TYPE,new ItemStack(Items.EMERALD)), "horse_upgrade_type");
      ItemRegistry.addItem(new ItemHorseUpgrade(HorseUpgradeType.VARIANT,new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage())), "horse_upgrade_variant");
      ItemRegistry.addItem(new ItemHorseUpgrade(HorseUpgradeType.HEALTH,new ItemStack(Items.DIAMOND)), "horse_upgrade_health");
      ItemRegistry.addItem(new ItemHorseUpgrade(HorseUpgradeType.SPEED,new ItemStack(Items.REDSTONE)), "horse_upgrade_speed");
      ItemRegistry.addItem(new ItemHorseUpgrade(HorseUpgradeType.JUMP,new ItemStack(Items.ENDER_EYE)), "horse_upgrade_jump");
      ModMain.instance.events.addEvent(this);//for SubcribeEvent hooks
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.modpackMisc;
    ItemHorseUpgrade.HEARTS_MAX = config.getInt("HorseFood Max Hearts", category, 20, 1, 100, "Maximum number of upgraded hearts");
    ItemHorseUpgrade.JUMP_MAX = config.getInt("HorseFood Max Jump", category, 6, 1, 20, "Maximum value of jump.  Naturally spawned/bred horses seem to max out at 5.5");
    ItemHorseUpgrade.SPEED_MAX = config.getInt("HorseFood Max Speed", category, 50, 1, 99, "Maximum value of speed (this is NOT blocks/per second or anything like that)");
    category = Const.ConfigCategory.content;
    enableHorseFoodUpgrades = config.getBoolean("HorseFood", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }
  public boolean isEnabled() {
    return enableHorseFoodUpgrades;
  }
  @SubscribeEvent
  public void onEntityInteractEvent(EntityInteract event) {
    if (event.getEntity() instanceof EntityPlayer == false) { return; }
    EntityPlayer entityPlayer = (EntityPlayer) event.getEntity();
    ItemStack held = entityPlayer.getHeldItemMainhand();
    if (held != null && held.getItem() instanceof ItemHorseUpgrade) {
      if (event.getTarget() instanceof EntityHorse) {
        ItemHorseUpgrade.onHorseInteract((EntityHorse) event.getTarget(), entityPlayer, (ItemHorseUpgrade)held.getItem());
        event.setCanceled(true);// stop the GUI inventory opening
      }
    }
  }
  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public void addHorseInfo(RenderGameOverlayEvent.Text event) {
    EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
    if (Minecraft.getMinecraft().gameSettings.showDebugInfo) {
      if (player.getRidingEntity() != null && player.getRidingEntity() instanceof EntityHorse) {
        EntityHorse horse = (EntityHorse) player.getRidingEntity();
        double speed = UtilEntity.getSpeedTranslated(horse.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
        // double jump = horse.getHorseJumpStrength() ;
        // convert from scale factor to blocks
        double jumpHeight = UtilEntity.getJumpTranslated(horse.getHorseJumpStrength());
        DecimalFormat df = new DecimalFormat("0.00");
        event.getLeft().add(I18n.format("debug.horsespeed") + "  " + df.format(speed));
        df = new DecimalFormat("0.0");
        event.getLeft().add(I18n.format("debug.horsejump") + "  " + df.format(jumpHeight));
      }
    }
  }
}
