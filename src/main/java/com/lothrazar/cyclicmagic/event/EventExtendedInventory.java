package com.lothrazar.cyclicmagic.event;
import java.io.File;
import java.io.IOException;
import com.google.common.io.Files;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.util.UtilPlayerInventoryFilestorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.relauncher.Side;

public class EventExtendedInventory {
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
  //  @SubscribeEvent
  //  public void playerDeath(PlayerDropsEvent event) {
  //    if(dropOnDeath == false){
  //      return;
  //    }
  //    //else drop on death is true, so do it
  //    Entity entity = event.getEntity();
  //    World world = entity.getEntityWorld();
  //    
  //    if (entity instanceof EntityPlayer && !world.isRemote && !world.getGameRules().getBoolean("keepInventory")) {
  //      UtilPlayerInventoryFilestorage.getPlayerInventory(event.getEntityPlayer()).dropItemsAt(event.getDrops(), event.getEntityPlayer());
  //    }
  //  }
}
