package com.lothrazar.cyclicmagic.event;
import com.lothrazar.cyclicmagic.util.UtilPlayerInventoryFilestorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.relauncher.Side;

public class EventExtendedInventory {
  public static boolean keepOnDeath;
  @SubscribeEvent
  public void playerLoggedInEvent(PlayerLoggedInEvent event) {
    Side side = FMLCommonHandler.instance().getEffectiveSide();
    if (side == Side.SERVER) {
      UtilPlayerInventoryFilestorage.playerEntityIds.add(event.player.getEntityId());
    }
  }
  @SubscribeEvent
  public void playerTick(PlayerEvent.LivingUpdateEvent event) {
    // player events
    if (event.getEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) event.getEntity();
      if (!UtilPlayerInventoryFilestorage.playerEntityIds.isEmpty() && UtilPlayerInventoryFilestorage.playerEntityIds.contains(player.getEntityId())) {
        UtilPlayerInventoryFilestorage.syncItems(player);
        UtilPlayerInventoryFilestorage.playerEntityIds.remove(player.getEntityId());
      }
    }
  }
  @SubscribeEvent
  public void playerLoad(PlayerEvent.LoadFromFile event) {
    UtilPlayerInventoryFilestorage.playerSetupOnLoad(event);
  }
  @SubscribeEvent
  public void playerSave(PlayerEvent.SaveToFile event) {
    UtilPlayerInventoryFilestorage.savePlayerItems(event.getEntityPlayer(), UtilPlayerInventoryFilestorage.getPlayerFile(UtilPlayerInventoryFilestorage.ext, event.getPlayerDirectory(), event.getEntityPlayer().getDisplayNameString()), UtilPlayerInventoryFilestorage.getPlayerFile(UtilPlayerInventoryFilestorage.extback, event.getPlayerDirectory(), event.getEntityPlayer().getDisplayNameString()));
  }
  @SubscribeEvent
  public void playerDeath(PlayerDropsEvent event) {
    if (keepOnDeath == false) {
      World world = event.getEntityPlayer().getEntityWorld();
      if (!world.isRemote && world.getGameRules().getBoolean("keepInventory") == false) {
        //so config says dont keep it on death. AND the gamerule says dont keep as well
        UtilPlayerInventoryFilestorage.getPlayerInventory(event.getEntityPlayer()).dropItems(event.getDrops(), event.getEntityPlayer().getPosition());
      }
    }
  }
}
