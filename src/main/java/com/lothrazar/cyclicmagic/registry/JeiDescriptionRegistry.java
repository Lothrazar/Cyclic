package com.lothrazar.cyclicmagic.registry;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class JeiDescriptionRegistry {
  public static List<ItemStack> itemsJei = new ArrayList<ItemStack>();
  public static void registerWithJeiDescription(Item item) {
    if (item != null)
      itemsJei.add(new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE));
  }
  public static void registerWithJeiDescription(Block b) {
    if (b != null)
      itemsJei.add(new ItemStack(b, 1, OreDictionary.WILDCARD_VALUE));
  }
}
