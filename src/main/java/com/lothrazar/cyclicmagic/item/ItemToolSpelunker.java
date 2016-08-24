package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
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

public class ItemToolSpelunker extends BaseTool implements IHasRecipe {
  private static final int durability = 2000;
  private static final int cooldown = 12;
  private static final int range = 32;
  public ItemToolSpelunker() {
    super(durability);
  }
  @Override
  public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World worldObj, BlockPos posIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    if (side == null || posIn == null) { return super.onItemUse(stack, player, worldObj, posIn, hand, side, hitX, hitY, hitZ); }
    //    boolean showOdds = player.isSneaking();
    boolean found = false;
    if (!worldObj.isRemote) {
      EnumFacing direction = side.getOpposite();
      BlockPos pos = posIn.offset(direction);
      BlockPos current = pos;
      for (int i = 1; i <= range; i++) {
        current = current.offset(direction);
        if (worldObj.isAirBlock(current)) {
          UtilChat.addChatMessage(player, UtilChat.lang("tool_spelunker.cave") + i);
          found = true;
        }
        else if (worldObj.getBlockState(current) == Blocks.WATER.getDefaultState()
            || worldObj.getBlockState(current) == Blocks.FLOWING_WATER.getDefaultState()) {
          UtilChat.addChatMessage(player, UtilChat.lang("tool_spelunker.water") + i);
          found = true;
        }
        else if (worldObj.getBlockState(current) == Blocks.LAVA.getDefaultState()
            || worldObj.getBlockState(current) == Blocks.FLOWING_LAVA.getDefaultState()) {
          UtilChat.addChatMessage(player, UtilChat.lang("tool_spelunker.lava") + i);
          found = true;
        }
        if (found) {
          break;//stop looping
        }
      }
      if (found == false) {
        UtilChat.addChatMessage(player, UtilChat.lang("tool_spelunker.none") + range);
      }
    }
    player.getCooldownTracker().setCooldown(this, cooldown);
    super.onUse(stack, player, worldObj, hand);
    return super.onItemUse(stack, player, worldObj, posIn, hand, side, hitX, hitY, hitZ);
  }
  @Override
  public void addRecipe() {
    GameRegistry.addShapedRecipe(new ItemStack(this),
        " sg",
        " bs",
        "b  ",
        'b', new ItemStack(Items.STICK),
        's', new ItemStack(Items.FLINT),
        'g', new ItemStack(Blocks.STAINED_GLASS, 1, EnumDyeColor.BLUE.getMetadata()));
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
