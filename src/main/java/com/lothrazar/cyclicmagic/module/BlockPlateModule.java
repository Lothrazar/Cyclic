package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.BlockConveyor;
import com.lothrazar.cyclicmagic.block.BlockLaunch;
import com.lothrazar.cyclicmagic.component.magnet.BlockMagnet;
import com.lothrazar.cyclicmagic.component.magnet.TileEntityMagnet;
import com.lothrazar.cyclicmagic.component.magnetanti.BlockMagnetAnti;
import com.lothrazar.cyclicmagic.component.magnetanti.TileEntityMagnetAnti;
import com.lothrazar.cyclicmagic.component.vector.BlockVectorPlate;
import com.lothrazar.cyclicmagic.component.vector.ItemBlockVectorPlate;
import com.lothrazar.cyclicmagic.component.vector.TileEntityVector;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.GuideRegistry;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideCategory;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideItem;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
 
 
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockPlateModule extends BaseModule implements IHasConfig {
  private boolean enableConveyor;
  private boolean launchPads;
  private boolean enableMagnet;
  private boolean enableInterdict;
  private boolean vectorPlate;
  public void onPreInit() {
    BlockLaunch plate_launch_med = null;
    BlockConveyor plate_push_fast = null;
    if (enableInterdict) {
      BlockMagnetAnti magnet_anti_block = new BlockMagnetAnti();
      BlockRegistry.registerBlock(magnet_anti_block, "magnet_anti_block", GuideCategory.BLOCKPLATE);
      GameRegistry.registerTileEntity(TileEntityMagnetAnti.class, "magnet_anti_block_te");
    }
    if (enableMagnet) {
      BlockMagnet magnet_block = new BlockMagnet();
      BlockRegistry.registerBlock(magnet_block, "magnet_block", GuideCategory.BLOCKPLATE);
      GameRegistry.registerTileEntity(TileEntityMagnet.class, "magnet_block_te");
    }
    if (launchPads) {
      //med
      plate_launch_med = new BlockLaunch(1.3F, SoundEvents.BLOCK_SLIME_FALL);
      BlockRegistry.registerBlock(plate_launch_med, "plate_launch_med", null);
      GuideItem page = GuideRegistry.register(GuideCategory.BLOCKPLATE, plate_launch_med);
      BlockLaunch plate_launch_small = new BlockLaunch(0.8F, SoundEvents.BLOCK_SLIME_STEP);
      BlockRegistry.registerBlock(plate_launch_small, "plate_launch_small", null);
      page.addRecipePage(RecipeRegistry.addShapedRecipe(new ItemStack(plate_launch_small, 6),
          "sss", "ggg", "iii",
          's', "blockSlime",
          'g', Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE,
          'i', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE));
      page.addRecipePage(RecipeRegistry.addShapelessRecipe(new ItemStack(plate_launch_med),
          new ItemStack(plate_launch_small),
          "gemQuartz"));
      //large
      BlockLaunch plate_launch_large = new BlockLaunch(1.8F, SoundEvents.BLOCK_SLIME_BREAK);
      BlockRegistry.registerBlock(plate_launch_large, "plate_launch_large", null);
      page.addRecipePage(RecipeRegistry.addShapelessRecipe(new ItemStack(plate_launch_large),
          new ItemStack(plate_launch_med),
 
          "endstone"));
 
    }
    if (enableConveyor) {
      BlockConveyor plate_push = new BlockConveyor(0.16F);
      BlockRegistry.registerBlock(plate_push, "plate_push", null);
      GuideItem page = GuideRegistry.register(GuideCategory.BLOCKPLATE, plate_push);
      page.addRecipePage(RecipeRegistry.addShapedRecipe(new ItemStack(plate_push, 8),
          "sbs",
          "bxb",
          "sbs",
          's', "ingotIron",
          'x', "blockSlime",
          'b', "dyePurple"  )  );
      plate_push_fast = new BlockConveyor(0.32F);
      BlockRegistry.registerBlock(plate_push_fast, "plate_push_fast", null);
      page.addRecipePage(RecipeRegistry.addShapelessRecipe(new ItemStack(plate_push_fast), new ItemStack(plate_push), Items.REDSTONE));
      BlockConveyor plate_push_slow = new BlockConveyor(0.08F);
      BlockRegistry.registerBlock(plate_push_slow, "plate_push_slow", null);
      page.addRecipePage(RecipeRegistry.addShapelessRecipe(new ItemStack(plate_push_slow), new ItemStack(plate_push),
         "dyeBlue"   ));
      BlockConveyor plate_push_slowest = new BlockConveyor(0.04F);
      BlockRegistry.registerBlock(plate_push_slowest, "plate_push_slowest", null);
      page.addRecipePage(RecipeRegistry.addShapelessRecipe(new ItemStack(plate_push_slowest), new ItemStack(plate_push),
          "dyeLightBlue" ));
    }
    if (vectorPlate) {
      BlockVectorPlate plate_vector = new BlockVectorPlate();
      BlockRegistry.registerBlock(plate_vector, new ItemBlockVectorPlate(plate_vector), "plate_vector", null);
      GuideItem page = GuideRegistry.register(GuideCategory.BLOCKPLATE, plate_vector);
      GameRegistry.registerTileEntity(TileEntityVector.class, "plate_vector_te");
      ModCyclic.instance.events.register(plate_vector);
      ItemStack top = (plate_launch_med == null) ? new ItemStack(Blocks.REDSTONE_LAMP) : new ItemStack(plate_launch_med);
      ItemStack base = (plate_push_fast == null) ? new ItemStack(Blocks.EMERALD_BLOCK) : new ItemStack(plate_push_fast);
      page.addRecipePage(RecipeRegistry.addShapedRecipe(new ItemStack(plate_vector, 6),
          "ttt",
          "idi",
          "bbb",
          'i', Items.IRON_INGOT,
          'd', Items.DIAMOND,
          'b', base,
          't', top));
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    vectorPlate = config.getBoolean("AerialFaithPlate", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableInterdict = config.getBoolean("InterdictionPlate", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableConveyor = config.getBoolean("SlimeConveyor", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableMagnet = config.getBoolean("MagnetBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    launchPads = config.getBoolean("SlimePads", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    BlockConveyor.keepEntityGrounded = config.getBoolean("SlimeConveyorKeepEntityGrounded", Const.ConfigCategory.blocks, true, "If true, the Slime Conveyor will keep entities grounded so they dont get sudden bursts of speed when falling down a block onto a conveyor on a lower level");
    BlockConveyor.doCorrections = config.getBoolean("SlimeConveyorPullCenter", Const.ConfigCategory.blocks, true, "If true, the Slime Conveyor will auto-correct entities towards the center while they are moving (keeping them away from the edge)");
  }
}
