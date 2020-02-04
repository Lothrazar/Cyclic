package com.lothrazar.cyclic.registry;

import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import top.theillusivec4.curios.api.imc.CurioIMCMessage;

public class CuriosRegistry {

  public static void setup(FMLCommonSetupEvent event) {
    //https://github.com/TheIllusiveC4/Curios/blob/fc77c876b630dc6e4a325cb9ac627b551749a19b/src/main/java/top/theillusivec4/curios/api/CurioTags.java
    InterModComms.sendTo("curios", "register_type", () -> new CurioIMCMessage("charm").setSize(2).setEnabled(true).setHidden(false));
    InterModComms.sendTo("curios", "register_type", () -> new CurioIMCMessage("necklace").setSize(1).setEnabled(true).setHidden(false));
    InterModComms.sendTo("curios", "register_type", () -> new CurioIMCMessage("hands").setSize(2).setEnabled(true).setHidden(false));
    InterModComms.sendTo("curios", "register_type", () -> new CurioIMCMessage("belt").setSize(2).setEnabled(true).setHidden(false));
    InterModComms.sendTo("curios", "register_type", () -> new CurioIMCMessage("ring").setSize(2).setEnabled(true).setHidden(false));
  }
}
