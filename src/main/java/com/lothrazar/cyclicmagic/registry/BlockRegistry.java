package com.lothrazar.cyclicmagic.registry;
import java.util.ArrayList;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.BlockCropMagicBean;
import com.lothrazar.cyclicmagic.component.bucketstorage.BlockBucketStorage;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideCategory;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockRegistry {
  public static ArrayList<Block> blocks = new ArrayList<Block>();
  public static BlockBucketStorage block_storelava;//TODO: FIX THIS ENTIRE THING.. maybe even fliud registry eh
  public static BlockBucketStorage block_storewater;
  public static BlockBucketStorage block_storemilk;
  public static BlockBucketStorage block_storeempty;
  //  public static void registerBlock(Block b, String name) {
  //    registerBlock(b, new ItemBlock(b), name, null);
  //  }
  public static void registerBlock(Block b, String name, @Nullable GuideCategory cat) {
    registerBlock(b, null, name, cat);
  }
  public static void registerBlock(Block b, ItemBlock ib, String name, @Nullable GuideCategory cat) {
    b.setRegistryName(new ResourceLocation(Const.MODID, name));
    b.setUnlocalizedName(name);
    ib.setRegistryName(b.getRegistryName());
//    GameRegistry.register(ib);
    if(ib!=null){
      ModCyclic.logger.error("item block registry fix "+name);
    }
    b.setCreativeTab(ModCyclic.TAB);
    blocks.add(b);
    IRecipe recipe = null;
    if (b instanceof IHasConfig) {
      ConfigRegistry.register((IHasConfig) b);
    }
    if (b instanceof IHasRecipe) {
      recipe = ((IHasRecipe) b).addRecipe();
    }
    if (cat != null) {
      GuideRegistry.register(cat, b, recipe, null);
    }
    if (!(b instanceof BlockCropMagicBean)) { //TODO FIX dirty hack to skip sprout
      JeiDescriptionRegistry.registerWithJeiDescription(b);
    }
  }

  @SubscribeEvent
  public static void onRegistryEvent(RegistryEvent.Register<Block> event) {
    for(Block b : blocks){

      event.getRegistry().register(b);
    }
  }
}
