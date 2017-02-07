package com.lothrazar.cyclicmagic.gui.villager;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;

public class GuiMerchantBetter extends GuiMerchant {
  public GuiMerchantBetter(InventoryPlayer ip, IMerchant m, World worldIn) {
    super(ip, m, worldIn);
    System.out.println("GuiMerchantBetter");
  }
}
