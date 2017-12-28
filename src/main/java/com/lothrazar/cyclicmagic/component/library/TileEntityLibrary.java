package com.lothrazar.cyclicmagic.component.library;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachine;
import com.lothrazar.cyclicmagic.component.library.BlockLibrary.Quadrant;
import com.lothrazar.cyclicmagic.registry.PotionEffectRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityLibrary extends TileEntityBaseMachine {
  private EnchantStack[] storage = new EnchantStack[BlockLibrary.Quadrant.values().length];
  public TileEntityLibrary() {
    super();
    for (int i = 0; i < storage.length; i++) {
      storage[i] = new EnchantStack();
    }
  }
  public EnchantStack getEnchantStack(BlockLibrary.Quadrant area) {
    //EnchantStack
    return storage[area.ordinal()];
  }
  public boolean addEnchantment(BlockLibrary.Quadrant area, Enchantment ench, int level) {
    //    if (ench.getMaxLevel() != level) {
    //      return false;
    //    }
    int index = area.ordinal();
    EnchantStack enchStackCurrent = storage[index];
    if (enchStackCurrent.count == 0) {
      enchStackCurrent = new EnchantStack(ench, level);
      storage[index] = enchStackCurrent;
      return true;
    }
    else if (enchStackCurrent.doesMatch(ench, level)) {
      enchStackCurrent.add();
      storage[index] = enchStackCurrent;
      return true;
    }
    else {
      return false;
    }
  }
  @Override
  public void readFromNBT(NBTTagCompound tags) {
    super.readFromNBT(tags);
 
    for (Quadrant q : Quadrant.values()) {
      EnchantStack s = new EnchantStack();
      s.readFromNBT(tags, q.name());
      storage[q.ordinal()] = s;
    }
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tags) {
    for (Quadrant q : Quadrant.values()) {
      tags.setTag(q.name(), getEnchantStack(q).writeToNBT());
    }
 
    return super.writeToNBT(tags);
  }
  /**
   * One enchantment instance is an enchant combined with its level and we have a number of those
   * 
   * @author Sam
   *
   */
  public static class EnchantStack {
    private int count = 0;
    private Enchantment ench = null;
    private int level = 0;
    public EnchantStack() {}
    public EnchantStack(Enchantment e, int lvl) {
      ench = e;
      level = lvl;
      count = 1;
    }
    public void readFromNBT(NBTTagCompound tags, String key) {
      NBTTagCompound t = (NBTTagCompound) tags.getTag(key);
      this.count = t.getInteger("eCount");
      String enchString = t.getString("ench");
      if (enchString.isEmpty() == false)
        this.ench = Enchantment.getEnchantmentByLocation(enchString);
    }
    public NBTTagCompound writeToNBT() {
      NBTTagCompound t = new NBTTagCompound();
      t.setInteger("eCount", this.count);
      if (ench == null) {
        t.setString("ench", "");
      }
      else {
        t.setString("ench", ench.getRegistryName().toString());
      }
      return t;
    }
    public boolean isEmpty() {
      return ench == null || count == 0;
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
    @Override
    public String toString() {
      if (this.isEmpty()) {
        return "empty";
      }
      
      return "[" + count + "] " + UtilChat.lang(ench.getName()) + " " + PotionEffectRegistry.getStrForLevel(level);
    }
    public Enchantment getEnch() {
      return ench;
    }
  }
}
