package com.lothrazar.cyclicmagic.util;
import java.lang.reflect.Field;
import net.minecraft.block.Block;

public class UtilReflection {
  /**
   * Return a property of obj that is of type T the first one found
   * 
   * tried but failed to make this generic. i would like the returning casted
   * type Block to be generic and/or passed in. then you would call Block crop =
   * UtilReflection.getFirstOfType<Block >(obj); Item crop =
   * UtilReflection.getFirstOfType<Item >(obj); and so on. oh well
   * 
   * @param obj
   * @return T
   */
  public static Block getFirstPrivateBlock(Object obj) {
    // tried with public static <T extends Object> T didnt work
    // also tried using a
    // if(f.get(obj).getClass() == t.getClass()){
    // return (t.getClass())f.get(obj);
    for (Field f : obj.getClass().getDeclaredFields()) {
      f.setAccessible(true);
      try {
        if (f.get(obj) instanceof Block) { return (Block) f.get(obj); }
      }
      catch (ClassCastException e) {
        continue;
      }
      catch (IllegalArgumentException e) {
        continue;
      }
      catch (IllegalAccessException e) {
        continue;
      }
    }
    return null;
  }
}
