package com.lothrazar.cyclicmagic.item.tool;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.BaseTool;
import com.lothrazar.cyclicmagic.util.UtilHarvestCrops;
import com.lothrazar.cyclicmagic.util.UtilHarvestCrops.HarestCropsConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemToolHarvest extends BaseTool implements IHasRecipe {
  private static final int range = 6;
  private static final int durability = 1000;
  private HarestCropsConfig conf;
  public enum HarvestType {
    WEEDS, LEAVES, CROPS;
  }
  private HarvestType harvestType;
  public ItemToolHarvest(HarvestType c) {
    super(durability);
    harvestType = c;
    conf = new HarestCropsConfig();//by default all are set false
    switch (harvestType) {
      case CROPS:
        conf.doesPumpkinBlocks = true;
        conf.doesMelonBlocks = true;
        conf.doesCrops = true;
        conf.doesCactus = true;
        conf.doesReeds = true;
      break;
      case WEEDS:
        conf.doesFlowers = true;
        conf.doesMushroom = true;
        conf.doesTallgrass = true;
        conf.doesSapling = true;
      break;
      case LEAVES:
        conf.doesLeaves = true;
      break;
      default:
      break;
    }
  }
  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World worldObj, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    ItemStack stack = player.getHeldItem(hand);
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
    return super.onItemUse(player, worldObj, offset, hand, side, hitX, hitY, hitZ);
  }
  @Override
  public IRecipe addRecipe() {
    switch (harvestType) {
      case CROPS:
        return GameRegistry.addShapedRecipe(new ItemStack(this),
            " gs",
            " bg",
            "b  ",
            'b', Items.BLAZE_ROD,
            'g', Items.QUARTZ,
            's', Items.STONE_HOE);
      case LEAVES:
        return GameRegistry.addShapedRecipe(new ItemStack(this),
            " gs",
            " bg",
            "b  ",
            'b', Items.STICK,
            'g', Items.STRING,
            's', Items.STONE_AXE);
      case WEEDS:
        return GameRegistry.addShapedRecipe(new ItemStack(this),
            " gs",
            " bg",
            "b  ",
            'b', Items.STICK,
            'g', Items.STRING,
            's', Items.STONE_HOE);
      default:
      break;
    }
    return null;
  }
}
