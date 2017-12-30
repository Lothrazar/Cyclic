package com.lothrazar.cyclicmagic.component.screen;
import java.util.Map;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachine;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;

public class TileEntityScreen extends TileEntityBaseMachine {
 
  private int timer = 0;
  public TileEntityScreen() {
    super();
   
  }
   
    
  @Override
  public void readFromNBT(NBTTagCompound tags) {
    super.readFromNBT(tags);
    this.timer = tags.getInteger("t");
 
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tags) {
 
    tags.setInteger("t", timer);
    return super.writeToNBT(tags);
  }
  
}
