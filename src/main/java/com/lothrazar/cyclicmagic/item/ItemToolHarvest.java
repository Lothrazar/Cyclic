package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.util.UtilHarvestCrops;
import com.lothrazar.cyclicmagic.util.UtilHarvestCrops.HarestCropsConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemToolHarvest extends BaseTool implements IHasRecipe, IHasConfig {
  private static final int range = 6;
  private static final int durability = 1000;
  private static final HarestCropsConfig conf = new HarestCropsConfig();
  public ItemToolHarvest() {
    super(durability);
    conf.doesPumpkinBlocks = false;
    conf.doesMelonBlocks = false;
    conf.doesLeaves = true;
    conf.doesCrops = false;
    conf.doesFlowers = true;
    conf.doesHarvestMushroom = true;
    conf.doesHarvestSapling = false;
    conf.doesHarvestTallgrass = true;
  }
  @Override
  public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World worldObj, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    BlockPos offset = pos;
    if (side != null) {
      offset = pos.offset(side);
    }
    UtilHarvestCrops.harvestArea(worldObj, offset.down().down(), range - 2, conf);
    UtilHarvestCrops.harvestArea(worldObj, offset.down(), range - 2, conf);
    UtilHarvestCrops.harvestArea(worldObj, offset, range, conf);
    UtilHarvestCrops.harvestArea(worldObj, offset.up(), range - 2, conf);
    UtilHarvestCrops.harvestArea(worldObj, offset.up().up(), range - 2, conf);
    super.onUse(stack, player, worldObj, hand);
    return super.onItemUse(stack, player, worldObj, offset, hand, side, hitX, hitY, hitZ);
  }
  //"Tool that harvests grass, flowers, and fully grown crops from the nearby area");
  @Override
  public void syncConfig(Configuration config) {
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this),
        " gs",
        " bg",
        "b  ",
        'b', Items.BLAZE_ROD,
        'g', Items.GHAST_TEAR,
        's', Items.SHEARS);
  }
}
