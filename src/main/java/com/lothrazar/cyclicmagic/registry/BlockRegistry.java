package com.lothrazar.cyclicmagic.registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.component.cable.CableBlock;
import com.lothrazar.cyclicmagic.component.cable.CableTile;
import com.lothrazar.cyclicmagic.component.fluidstorage.BlockBucketStorage;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
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
    b.setRegistryName(new ResourceLocation(Const.MODID, name));
    b.setUnlocalizedName(name);
    if (ib != null) {
      ib.setRegistryName(b.getRegistryName()); // ok good this should work yes? yes! http://mcforge.readthedocs.io/en/latest/blocks/blocks/#registering-a-block
      ItemRegistry.itemMap.put(name, ib);
    }
    b.setCreativeTab(ModCyclic.TAB);
    blocks.add(b);
    if (b instanceof IHasConfig) {
      ConfigRegistry.register((IHasConfig) b);
    }
    if (cat != null) {
      GuideRegistry.register(cat, ib);
    }
  }
  @SubscribeEvent
  public static void onRegistryEvent(RegistryEvent.Register<Block> event) {
    event.getRegistry().registerAll(blocks.toArray(new Block[0]));
 
    
    event.getRegistry().register(new CableBlock()
            .setRegistryName( new ResourceLocation(Const.MODID, "cable")  )
          .setUnlocalizedName(Const.MODID + ".cable")
//            .setCreativeTab(CreativeTabs.REDSTONE)
          )    ;
          
  }
}
