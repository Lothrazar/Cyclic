package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.base.BaseTool;
import com.lothrazar.cyclicmagic.util.UtilHarvestCrops;
import com.lothrazar.cyclicmagic.util.UtilHarvestCrops.HarvestSetting;
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

public class ItemScythe extends BaseTool implements IHasRecipe {
  private static final int RADIUS = 6;//13x13
  private static final int RADIUS_SNEAKING = 2;//2x2
  private HarvestSetting conf;
  public enum HarvestType {
    WEEDS, LEAVES, CROPS;
  }
  private HarvestType harvestType;
  public ItemScythe(HarvestType c) {
    super(1000);
    harvestType = c;
    conf = new HarvestSetting();//by default all are set false
    conf.dropInPlace = true;
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
        conf.doesIShearable = true;
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
    int r = (player.isSneaking()) ? RADIUS_SNEAKING : RADIUS;
    UtilHarvestCrops.harvestArea(worldObj, offset.down().down(), r, conf);
    UtilHarvestCrops.harvestArea(worldObj, offset.down(), r, conf);
    UtilHarvestCrops.harvestArea(worldObj, offset, r, conf);
    UtilHarvestCrops.harvestArea(worldObj, offset.up(), r, conf);
    UtilHarvestCrops.harvestArea(worldObj, offset.up().up(), r, conf);
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
