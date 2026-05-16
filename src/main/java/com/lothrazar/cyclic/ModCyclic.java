package com.lothrazar.cyclic;

import com.lothrazar.cyclic.event.ClientInputEvents;
import com.lothrazar.cyclic.event.EventRender;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.apache.logging.log4j.LogManager;
import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.cyclic.data.DataTags;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ClientRegistryCyclic;
import com.lothrazar.cyclic.registry.CommandRegistry;
import com.lothrazar.cyclic.registry.CyclicRecipeType;

import com.lothrazar.cyclic.registry.EntityRegistry;
import com.lothrazar.cyclic.registry.EventRegistry;
import com.lothrazar.cyclic.registry.FluidRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.LootModifierRegistry;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import com.lothrazar.cyclic.registry.PotionEffectRegistry;
import com.lothrazar.cyclic.registry.PotionRegistry;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.registry.AttachmentRegistry;
import com.lothrazar.cyclic.registry.MaterialRegistry;
import com.lothrazar.cyclic.registry.PacketRegistry;

@Mod(ModCyclic.MODID)
public class ModCyclic {

  public static final String MODID = "cyclic";
  public static final CyclicLogger LOGGER = new CyclicLogger(LogManager.getLogger());

  public ModCyclic(IEventBus bus, Dist dist, ModContainer container) {

    MaterialRegistry.ARMOR_MATERIALS.register(bus);
    MaterialRegistry.setup();
    bus.addListener(EventRegistry::setup);
    bus.addListener(PacketRegistry::setup);
    if (dist.isClient()) {

      bus.addListener(ClientRegistryCyclic::setupClient);

      NeoForge.EVENT_BUS.register(new ClientInputEvents());
      NeoForge.EVENT_BUS.register(new EventRender());
    }

//    DistExecutor.safeRunForDist(() -> ClientRegistryCyclic::new, () -> EventRegistry::new);
    ConfigRegistry cfg = new ConfigRegistry();
    cfg.setupMain();
    cfg.setupClient();
    DataTags.setup();
    NeoForge.EVENT_BUS.register(new CommandRegistry());
    BlockRegistry.BLOCKS.register(bus);
    ItemRegistry.ITEMS.register(bus);
    TileRegistry.TILES.register(bus);
    FluidRegistry.FLUID_TYPES.register(bus);
    FluidRegistry.FLUID.register(bus);
    MenuTypeRegistry.CONTAINERS.register(bus);
    CyclicRecipeType.RECIPE_TYPES.register(bus);
    CyclicRecipeType.RECIPE_SERIALIZERS.register(bus);
    EntityRegistry.ENTITIES.register(bus);
    PotionRegistry.POTIONS.register(bus);
    PotionEffectRegistry.MOB_EFFECTS.register(bus);

    AttachmentRegistry.ATTACHMENT_TYPES.register(bus);
    SoundRegistry.SOUND_EVENTS.register(bus);
    LootModifierRegistry.LOOT.register(bus);
    BlockRegistry.CREATIVE_MODE_TABS.register(bus);
    NeoForgeMod.enableMilkFluid();
  }
}
