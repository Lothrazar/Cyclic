package com.lothrazar.cyclicmagic.component.library;
import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.registry.EnchantRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * One enchantment instance is an enchant combined with its level and we have a number of those
 * 
 * @author Sam
 *
 */
public class EnchantStack {
  public static Map<String, ItemStack> renderMap;
  private static final String NBT_COUNT = "eCount";
  private static final String NBT_ENCH = "ench";
  private int count = 0;
  private int level = 0;
  private Enchantment ench = null;
  public EnchantStack() {}
  public EnchantStack(Enchantment e, int lvl) {
    ench = e;
    level = lvl;
    count = 1;
  }
  public static void postInitRenderMap() {
    renderMap = new HashMap<String, ItemStack>();
    renderMap.put("minecraft:sharpness", new ItemStack(Items.DIAMOND_SWORD));
    renderMap.put("minecraft:protection", new ItemStack(Items.DIAMOND_CHESTPLATE));
    renderMap.put("minecraft:fire_protection", new ItemStack(Items.BLAZE_POWDER));
    renderMap.put("minecraft:blast_protection", new ItemStack(Blocks.TNT));
    //TODO: more. and config etc
  }
  public Enchantment getEnch() {
    return ench;
  }
  public Integer getLevel() {
    return level;
  }
  public int getCount() {
    return count;
  }
  public void readFromNBT(NBTTagCompound tags, String key) {
    NBTTagCompound t = (NBTTagCompound) tags.getTag(key);
    this.count = t.getInteger(NBT_COUNT);
    String enchString = t.getString(NBT_ENCH);
    if (enchString.isEmpty() == false)
      this.ench = Enchantment.getEnchantmentByLocation(enchString);
  }
  public NBTTagCompound writeToNBT() {
    NBTTagCompound t = new NBTTagCompound();
    t.setInteger(NBT_COUNT, this.count);
    if (ench == null) {
      t.setString(NBT_ENCH, "");
    }
    else {
      t.setString(NBT_ENCH, ench.getRegistryName().toString());
    }
    return t;
  }
  public boolean isEmpty() {
    return ench == null || getCount() == 0;
  }
  public boolean doesMatch(Enchantment e, int lvl) {
    return ench.equals(e) && level == lvl;
  }
  public void add() {
    count++;
  }
  public void remove() {
    count--;
    if (count <= 0) {
      ench = null;
      level = 0;
    }
  }
  public boolean equals(EnchantStack e) {
    return this.doesMatch(e.ench, e.getLevel()) && this.getCount() == e.getCount();
  }
  @Override
  public String toString() {
    if (this.isEmpty()) {
      return UtilChat.lang("enchantment_stack.empty");
    }
    return countName() + " " + UtilChat.lang(ench.getName()) + " " + levelName();
  }
  public String countName() {
    return "[" + count + "]";
  }
  public String levelName() {
    return EnchantRegistry.getStrForLevel(level);
  }
  public String shortName() {
    if (this.isEmpty()) {
      return "--";
    }
    return UtilChat.lang(ench.getName()).substring(0, 5);
  }
  public ItemStack getRenderIcon() {
    if (this.isEmpty()) {
      return ItemStack.EMPTY;
    }
    String key = this.ench.getRegistryName().toString();
    if (renderMap.containsKey(key)) {
      return renderMap.get(key);
    }
    else {
      return new ItemStack(Items.ENCHANTED_BOOK);
    }
  }
}
