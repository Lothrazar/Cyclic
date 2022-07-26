package com.lothrazar.cyclic;

import org.apache.logging.log4j.LogManager;
import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.cyclic.data.DataTags;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.CapabilityRegistry;
import com.lothrazar.cyclic.registry.ClientRegistryCyclic;
import com.lothrazar.cyclic.registry.CommandRegistry;
import com.lothrazar.cyclic.registry.CyclicRecipeType;
import com.lothrazar.cyclic.registry.EnchantRegistry;
import com.lothrazar.cyclic.registry.EntityRegistry;
import com.lothrazar.cyclic.registry.EventRegistry;
import com.lothrazar.cyclic.registry.FluidRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import com.lothrazar.cyclic.registry.PotionEffectRegistry;
import com.lothrazar.cyclic.registry.PotionRegistry;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ModCyclic.MODID)
public class ModCyclic {

  public static final String MODID = "cyclic";
  public static final CyclicLogger LOGGER = new CyclicLogger(LogManager.getLogger());

  public ModCyclic() {
    FMLJavaModLoadingContext.get().getModEventBus().addListener(EventRegistry::setup);
    FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientRegistryCyclic::setupClient);
    DistExecutor.safeRunForDist(() -> ClientRegistryCyclic::new, () -> EventRegistry::new);
    ConfigRegistry.setup();
    ConfigRegistry.setupClient();
    DataTags.setup();
    MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, CapabilityRegistry::onAttachCapabilitiesPlayer);
    MinecraftForge.EVENT_BUS.register(new CapabilityRegistry());
    MinecraftForge.EVENT_BUS.register(new CommandRegistry());
    IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
    BlockRegistry.BLOCKS.register(bus);
    ItemRegistry.ITEMS.register(bus);
    TileRegistry.TILES.register(bus);
    FluidRegistry.FLUIDS.register(bus);
    MenuTypeRegistry.CONTAINERS.register(bus);
    CyclicRecipeType.RECIPE_TYPES.register(bus);
    CyclicRecipeType.RECIPE_SERIALIZERS.register(bus);
    EntityRegistry.ENTITIES.register(bus);
    PotionRegistry.POTIONS.register(bus);
    PotionEffectRegistry.MOB_EFFECTS.register(bus);
    EnchantRegistry.ENCHANTMENTS.register(bus);
    SoundRegistry.SOUND_EVENTS.register(bus);
    ForgeMod.enableMilkFluid();
    //TODO: 1.18 new content 
    //anti beacon
    //poison cleansing magic aura
    //    Lothrazar
    //    : what if i made a milk sponge that cleared everything and then a sponge altar with a config or recipe that says X item clears out Y potion effects
    //    Ava751
    //    : Wouldn't work if the beacon has a white or blacklist for which debuffs it affects
    //    LUL
    //
    //    Lothrazar
    //    : for your base you would want at least 16 or 32 blocks i assume
  }
}
