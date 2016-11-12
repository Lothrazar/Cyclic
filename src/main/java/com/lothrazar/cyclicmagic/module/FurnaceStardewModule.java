package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FurnaceStardewModule extends BaseEventModule implements IHasConfig {
  // inspired by stardew valley
  private static boolean stardewFurnace;
  @SubscribeEvent
  public void onPlayerFurnace(PlayerInteractEvent.LeftClickBlock event) {//extends PlayerInteractEvent
    if (stardewFurnace) {
      EntityPlayer entityPlayer = event.getEntityPlayer();
      // ignore in creative// left clicking just breaks it anyway
      if (entityPlayer.capabilities.isCreativeMode) { return; }
      BlockPos pos = event.getPos();
      World worldObj = event.getWorld();
      if (pos == null) { return; }
      ItemStack held = entityPlayer.getHeldItem(event.getHand());
      int playerSlot = 0;// entityPlayer.inventory.currentItem;
      boolean wasMain = event.getHand() == EnumHand.MAIN_HAND;
      if (wasMain) {
        playerSlot = entityPlayer.inventory.currentItem;
      }
      else {
        //just dont use offhand, ignore it for now. is easier
        playerSlot = 40;
      }
      TileEntity tile = worldObj.getTileEntity(pos);
      if (tile instanceof TileEntityFurnace) {
        TileEntityFurnace furnace = (TileEntityFurnace) tile;
        if (held == null) {
          UtilFurnace.extractFurnaceOutput(furnace, entityPlayer);
        }
        else {
          //holding a non null stack for sure
          if (UtilFurnace.canBeSmelted(held)) {
            UtilFurnace.tryMergeStackIntoSlot(furnace, entityPlayer, playerSlot, UtilFurnace.SLOT_INPUT);
          }
          else if (UtilFurnace.isFuel(held)) {
            UtilFurnace.tryMergeStackIntoSlot(furnace, entityPlayer, playerSlot, UtilFurnace.SLOT_FUEL);
          }
        }
      }
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.player;
    stardewFurnace = config.getBoolean("Furnace Speed", category, true,
        "Stardew Furnaces: Quickly fill a furnace by hitting it with fuel or an item, or interact with an empty hand to pull out the results [Inspired by Stardew Valley.  Left click only]");
  }
}
