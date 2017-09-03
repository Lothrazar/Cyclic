package com.lothrazar.cyclicmagic.item;
import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.item.base.BaseTool;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

public class ItemProspector extends BaseTool implements IHasRecipe, IHasConfig {
  private static final int DURABILITY = 2000;
  private static final int COOLDOWN = 12;
  private static int range = 16;
  private boolean isBlacklist;
  private String[] blocklist;
  public ItemProspector() {
    super(DURABILITY);
  }
  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World worldObj, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    ItemStack stack = player.getHeldItem(hand);
    if (side == null || pos == null) { return super.onItemUse(player, worldObj, pos, hand, side, hitX, hitY, hitZ); }
    Map<String, Integer> mapList = new HashMap<String, Integer>();
    String name;
    EnumFacing direction = side.getOpposite();
    BlockPos current;
    IBlockState at;
    Block blockAt;
    ItemStack s;
    for (int i = 0; i < range; i++) {
      current = pos.offset(direction, i);
      at = worldObj.getBlockState(current);
      if (at == null) {
        continue;
      }
      blockAt = at.getBlock();
      if (blockAt == null) {
        continue;
      }
      if (at == Blocks.AIR) {
        continue;
      }
      //          name = blockAt.getUnlocalizedName();
      s = new ItemStack(Item.getItemFromBlock(blockAt), 1, blockAt.getMetaFromState(at));
      if (isBlockShowable(s) == false) {
        continue;
      }
      name = s.getDisplayName();
      int previous = (mapList.containsKey(name)) ? mapList.get(name) : 0;
      mapList.put(name, previous + 1);
    }
    //now send messages
    if (worldObj.isRemote) {
      if (mapList.size() == 0) {
        UtilChat.addChatMessage(player, UtilChat.lang("tool_prospector.none") + range);
      }
      for (Map.Entry<String, Integer> entry : mapList.entrySet()) {
        UtilChat.addChatMessage(player, UtilChat.lang("tool_prospector.found") + entry.getKey() + " " + entry.getValue());
      }
    }
    player.getCooldownTracker().setCooldown(this, COOLDOWN);
    super.onUse(stack, player, worldObj, hand);
    return super.onItemUse(player, worldObj, pos, hand, side, hitX, hitY, hitZ);
  }
  public boolean isBlockShowable(ItemStack stack) {
    if (stack == null || stack.getItem() == null) { return false; } //nulls
    String itemName = UtilItemStack.getStringForItemStack(stack);//this one includes metadata
    String itemSimpleName = UtilItemStack.getStringForItem(stack.getItem());//this one doesnt
    boolean isInList = false;
    for (String s : blocklist) {//dont use .contains on the list. must use .equals on string
      if (s == null) {
        continue;
      } //lol
      //so if list has only "minecraft:stone" then all metadata is covered
      //otherwise, list might have "minecraft:stone/3" so it only matches that
      if (s.equals(itemName) || s.equals(itemSimpleName)) {
        isInList = true;
        break;
      }
    }
    //if its a blacklist, and its IN the list, DONT show it (false)
    //otherwise, its a whitelist, so if it IS in the list then show it (true)v
    boolean yesShowIt = (this.isBlacklist) ? (!isInList) : isInList;
    return yesShowIt;
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "  g",
        " i ",
        "b  ",
        'i',"ingotIron",
        'b', new ItemStack(Items.BLAZE_ROD),
        'g', "blockGlassLightBlue");
  }
  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.modpackMisc;
    ItemProspector.range = config.getInt("ProspectorRange", category, 32, 2, 256, "Block Range it will search onclick");
    isBlacklist = config.getBoolean("ProspectorIsBlacklist", category, true, "True means this (ProspectorBlockList) is a blacklist, ignore whats listed. False means its a whitelist: only print whats listed.");
    String[] deflist = new String[] { "minecraft:air", "minecraft:grass", "minecraft:dirt/0", "minecraft:dirt/1", "minecraft:stone", "minecraft:gravel", "minecraft:sand", "minecraft:bedrock" };
    blocklist = config.getStringList("ProspectorBlockList", category, deflist, "List of blocks that the Prospector knows about.");
  }
}
