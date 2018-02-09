package com.lothrazar.cyclicmagic.registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.component.cable.CableBlockPrimary;
import com.lothrazar.cyclicmagic.component.cable.bundle.BlockCableBundle;
import com.lothrazar.cyclicmagic.component.cable.energy.BlockPowerCable;
import com.lothrazar.cyclicmagic.component.cable.fluid.CableBlockFluid;
import com.lothrazar.cyclicmagic.component.cable.item.CableBlockItem;
import com.lothrazar.cyclicmagic.component.fluidstorage.BlockBucketStorage;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.module.BlockUtilityModule;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideCategory;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockRegistry {
  public static ArrayList<Block> blocks = new ArrayList<Block>();
  public static Map<Block, GuideCategory> map = new HashMap<Block, GuideCategory>();
  public static BlockBucketStorage block_storeempty;
  public static void registerBlock(Block b, String name, @Nullable GuideCategory cat) {
    registerBlock(b, new ItemBlock(b), name, cat);
  }
  public static void registerBlock(@Nonnull Block b, @Nonnull ItemBlock ib, @Nonnull String name, @Nullable GuideCategory cat) {
    initBlock(b, name);
    if (ib != null) {
      ib.setRegistryName(b.getRegistryName()); // ok good this should work yes? yes! http://mcforge.readthedocs.io/en/latest/blocks/blocks/#registering-a-block
      ItemRegistry.itemMap.put(name, ib);
    }
    blocks.add(b);
    if (cat != null) {
      GuideRegistry.register(cat, ib);
    }
  }
  private static Block initBlock(Block b, String name) {
    b.setCreativeTab(ModCyclic.TAB);
    b.setRegistryName(new ResourceLocation(Const.MODID, name));
    b.setUnlocalizedName(name);
    if (b instanceof IHasConfig) {
      ConfigRegistry.register((IHasConfig) b);
    }
    return b;
  }
  @SubscribeEvent
  public static void onRegistryEvent(RegistryEvent.Register<Block> event) {
    event.getRegistry().registerAll(blocks.toArray(new Block[0]));
    //TODO fix refactor this
    initCables(event);
  }
  //workaround since they break if they hit item registry
  private static void initCables(RegistryEvent.Register<Block> event) {
    if (BlockUtilityModule.enablePumpAndPipes) {
      event.getRegistry().register(initBlock(new CableBlockItem(), "item_pipe"));
      event.getRegistry().register(initBlock(new CableBlockFluid(), "fluid_pipe"));
      event.getRegistry().register(initBlock(new BlockPowerCable(), "energy_pipe"));
      event.getRegistry().register(initBlock(new BlockCableBundle(), "bundled_pipe"));
    }
  }
}
