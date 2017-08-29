package com.lothrazar.cyclicmagic.registry;
import java.util.ArrayList;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.BlockCropMagicBean;
import com.lothrazar.cyclicmagic.component.bucketstorage.BlockBucketStorage;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideCategory;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockRegistry {
  public static ArrayList<Block> blocks = new ArrayList<Block>();
  public static BlockBucketStorage block_storeempty;
  public static void registerBlock(Block b, String name, @Nullable GuideCategory cat) {
    registerBlock(b, new ItemBlock(b), name, cat);
  }
  public static void registerBlock(Block b, ItemBlock ib, String name, @Nullable GuideCategory cat) {
    b.setRegistryName(new ResourceLocation(Const.MODID, name));
    b.setUnlocalizedName(name);
    if (ib != null) {
      ib.setRegistryName(b.getRegistryName()); // ok good this should work yes? yes! http://mcforge.readthedocs.io/en/latest/blocks/blocks/#registering-a-block
      ItemRegistry.itemMap.put(name, ib);
    }
    b.setCreativeTab(ModCyclic.TAB);
    blocks.add(b);
    //IRecipe recipe = null;
    if (b instanceof IHasConfig) {
      ConfigRegistry.register((IHasConfig) b);
    }
    if (cat != null) {
      GuideRegistry.register(cat, b, null, null);
    }
    if (!(b instanceof BlockCropMagicBean)) { //TODO FIX dirty hack to skip sprout
      JeiDescriptionRegistry.registerWithJeiDescription(b);
    }
  }
  @SubscribeEvent
  public static void onRegistryEvent(RegistryEvent.Register<Block> event) {
    ModCyclic.logger.log("RegistryEvent.Register<Block> !!!");
    //    event.getRegistry().registerAll(blocks.toArray(new Block[0]));
    for (Block b : blocks) {
      event.getRegistry().register(b);
    }
  }
}
