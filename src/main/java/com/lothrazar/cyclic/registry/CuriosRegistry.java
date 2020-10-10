package com.lothrazar.cyclic.registry;

import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import top.theillusivec4.curios.api.SlotTypeMessage;

public class CuriosRegistry {

  public static void setup(FMLCommonSetupEvent event) {
    //https://github.com/TheIllusiveC4/Curios/blob/fc77c876b630dc6e4a325cb9ac627b551749a19b/src/main/java/top/theillusivec4/curios/api/CurioTags.java
    InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("charm").size(2).build());
    InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("necklace").size(1).build());
    InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("hands").size(2).build());
    InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("belt").size(2).build());
    InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("feet").size(1).build());
    InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("ring").size(2).priority(9).build());
  }
}
