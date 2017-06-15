package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraftforge.common.config.Configuration;

public class KeyInventoryShiftModule extends BaseModule implements IHasConfig {
  public static boolean enableInvoKeys;//static because mod proxy looks at this
  public void syncConfig(Configuration c) {
    enableInvoKeys = c.getBoolean("KeybindInventoryShift", Const.ConfigCategory.inventory, true, "Set this to false (and restart your client) to remove the inventory shift keybindings");
  }
}
