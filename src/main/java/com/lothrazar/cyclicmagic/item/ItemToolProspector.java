package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.BlockOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemToolProspector extends BaseTool implements IHasRecipe {
  private static final int durability = 2000;
  private static final int cooldown = 12;
  private static final int range = 16;
  public ItemToolProspector() {
    super(durability);
  }
  @Override
  public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World worldObj, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    if (side == null || pos == null) { return super.onItemUse(stack, player, worldObj, pos, hand, side, hitX, hitY, hitZ); }
    //    boolean showOdds = player.isSneaking();
    boolean found = false;
    if (!worldObj.isRemote) {
      EnumFacing direction = side.getOpposite();
      BlockPos current = pos;
      IBlockState at = worldObj.getBlockState(current);
      for (int i = 0; i <= range; i++) {
        if (at != null && at.getBlock() instanceof BlockOre) {
          UtilChat.addChatMessage(player, UtilChat.lang("tool_prospector.found") + at.getBlock().getLocalizedName());
          found = true;
          break;
        }
        current = current.offset(direction);
        at = worldObj.getBlockState(current);
      }
      if (found == false) {
        UtilChat.addChatMessage(player, UtilChat.lang("tool_prospector.none") + range);
      }
    }
    player.getCooldownTracker().setCooldown(this, cooldown);
    super.onUse(stack, player, worldObj, hand);
    return super.onItemUse(stack, player, worldObj, pos, hand, side, hitX, hitY, hitZ);
  }
  @Override
  public void addRecipe() {
    GameRegistry.addShapedRecipe(new ItemStack(this),
        " sg",
        " bs",
        "b  ",
        'b', new ItemStack(Items.BLAZE_ROD),
        's', new ItemStack(Items.DIAMOND),
        'g', new ItemStack(Blocks.STAINED_GLASS, 1, EnumDyeColor.LIGHT_BLUE.getMetadata()));
  }
  public static class SpawnDetail {
    private int itemWeight;
    private String displayName;
    private String creatureTypeName;
    private boolean lightEnabled = false;
    public SpawnDetail(Biome.SpawnListEntry entry, EnumCreatureType creatureType, int currentLightLevel) {
      itemWeight = entry.itemWeight;
      creatureTypeName = creatureType.name();
      displayName = entry.entityClass.getSimpleName().replace("Entity", "");
      //one caveat: the above canSpawn ignores light level
      if (creatureType != EnumCreatureType.MONSTER) {
        //ambient/water/passive, all ignore light
        lightEnabled = true;
      }
      else {
        int reqLight = entry.entityClass == EntityBlaze.class ? Const.LIGHT_MOBSPAWN_BLAZE : Const.LIGHT_MOBSPAWN;
        if (currentLightLevel <= reqLight) {
          lightEnabled = true;
        }
      }
    }
    public SpawnDetail(String n, EnumCreatureType creatureType, int currentLightLevel, int odds) {
      //special case of JUST witherywither
      itemWeight = odds;
      displayName = n;
      creatureTypeName = creatureType.name();
      if (currentLightLevel <= Const.LIGHT_MOBSPAWN) {
        lightEnabled = true;
      }
    }
    public String getSortBy() {
      return displayName;
    }
    public String toString(boolean showOdds) {
      TextFormatting color = (lightEnabled) ? TextFormatting.WHITE : TextFormatting.DARK_GRAY;
      if (showOdds) // todo; super.tostring here?
        return color + "[" + creatureTypeName + ", " + String.format("%03d", itemWeight) + "] " + displayName;
      else
        return color + displayName;
    }
  }
}
