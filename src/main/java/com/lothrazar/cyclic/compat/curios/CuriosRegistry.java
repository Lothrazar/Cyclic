package com.lothrazar.cyclic.compat.curios;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.compat.CompatConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import top.theillusivec4.curios.api.SlotTypeMessage;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CuriosRegistry {

  private static final ResourceLocation FEET_ICON = new ResourceLocation(ModCyclic.MODID, CompatConstants.CURIOS + "/feet");

  /**
   * https://github.com/TheIllusiveC4/Curios/wiki/Frequently-Used-Slots
   **/
  public static void setup(FMLCommonSetupEvent event) {
    InterModComms.sendTo(CompatConstants.CURIOS, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("back").size(1).build());
    InterModComms.sendTo(CompatConstants.CURIOS, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("belt").size(2).build());
    InterModComms.sendTo(CompatConstants.CURIOS, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("bracelet").size(2).build());
    InterModComms.sendTo(CompatConstants.CURIOS, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("charm").size(8).build());
    InterModComms.sendTo(CompatConstants.CURIOS, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("feet").size(1).icon(FEET_ICON).build());
    InterModComms.sendTo(CompatConstants.CURIOS, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("hands").size(2).build());
    InterModComms.sendTo(CompatConstants.CURIOS, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("head").size(2).build());
    InterModComms.sendTo(CompatConstants.CURIOS, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("necklace").size(2).build());
    InterModComms.sendTo(CompatConstants.CURIOS, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("ring").size(8).build());
  }

  @OnlyIn(Dist.CLIENT)
  @SubscribeEvent
  public static void onStitch(TextureStitchEvent.Pre event) {
    event.addSprite(FEET_ICON);
  }
}
