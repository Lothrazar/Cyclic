package com.lothrazar.cyclicmagic;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.potion.PotionTypeCyclic;
import com.lothrazar.cyclicmagic.registry.EnchantRegistry;
import com.lothrazar.cyclicmagic.registry.FluidsRegistry;
import com.lothrazar.cyclicmagic.registry.PotionTypeRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTabCyclic extends CreativeTabs {
  Item tabItem = null;
  Comparator<ItemStack> comparator = new Comparator<ItemStack>() {
    @Override
    public int compare(final ItemStack first, final ItemStack second) {
      return first.getDisplayName().compareTo(second.getDisplayName());
    }
  };
  public CreativeTabCyclic() {
    super(Const.MODID);
  }
  @Override
  public ItemStack getTabIconItem() {
    return tabItem == null ? new ItemStack(Items.DIAMOND) : new ItemStack(tabItem);
  }
  @Override
  @SideOnly(Side.CLIENT)
  public void displayAllRelevantItems(NonNullList<ItemStack> list) {
    super.displayAllRelevantItems(list);
    Iterator<ItemStack> i = list.iterator();
    while (i.hasNext()) {
      Item s = i.next().getItem(); // must be called before you can call i.remove()
      if (s == Items.ENCHANTED_BOOK
          || s == Items.LINGERING_POTION
          || s == Items.POTIONITEM
          || s == Items.SPLASH_POTION
          || s == Items.TIPPED_ARROW) {
        i.remove();
      }
    }
    Collections.sort(list, comparator);
    for (Enchantment e : EnchantRegistry.enchants) {
      ItemStack ebook = new ItemStack(Items.ENCHANTED_BOOK);
      ItemEnchantedBook.addEnchantment(ebook, new EnchantmentData(e, e.getMaxLevel()));
      list.add(ebook);
    }
    //fluids and potions that I add are under minecraft: prefix so copy them in here
    //TODO: add fluid registry arraylist
    if (FluidsRegistry.fluid_poison != null) {
      list.add(FluidUtil.getFilledBucket(new FluidStack(FluidsRegistry.fluid_poison, Fluid.BUCKET_VOLUME)));
    }
    if (FluidsRegistry.fluid_exp != null) {
      list.add(FluidUtil.getFilledBucket(new FluidStack(FluidsRegistry.fluid_exp, Fluid.BUCKET_VOLUME)));
    }
    if (FluidsRegistry.fluid_milk != null) {
      list.add(FluidUtil.getFilledBucket(new FluidStack(FluidsRegistry.fluid_milk, Fluid.BUCKET_VOLUME)));
    }
    for (PotionTypeCyclic pt : PotionTypeRegistry.potions) {
      list.add(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), pt));
      list.add(PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), pt));
      list.add(PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), pt));
      list.add(PotionUtils.addPotionToItemStack(new ItemStack(Items.TIPPED_ARROW), pt));
    }
  }
  public void setTabItemIfNull(Item i) {
    if (tabItem == null)
      tabItem = i;
  }
}
