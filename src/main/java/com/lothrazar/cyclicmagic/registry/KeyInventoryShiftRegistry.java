package com.lothrazar.cyclicmagic.registry;

import com.lothrazar.cyclicmagic.util.Const;
import net.minecraftforge.common.config.Configuration;

public class KeyInventoryShiftRegistry {

  public static boolean enableInvoKeys;

  public static void register() {
    //all the stuff is in client proxy
  }

  public static void syncConfig(Configuration c) {
    enableInvoKeys = c.getBoolean("KeybindInventoryShift", Const.ConfigCategory.inventory, true, "Set this to false (and restart your client) to remove the inventory shift keybindings");
    
  }
}
