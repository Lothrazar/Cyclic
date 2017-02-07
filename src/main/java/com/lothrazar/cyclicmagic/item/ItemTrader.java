package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import com.lothrazar.cyclicmagic.gui.villager.GuiMerchantBetter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTrader extends BaseItem {
  public ItemTrader() {
    super();
  }
  @Override
  public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
    System.out.println("itemInteractionForEntity");
    if (entity instanceof EntityVillager) {
      World world = entity.getEntityWorld();
      EntityVillager villager = (EntityVillager) entity;
      
      
//      ItemStack buy;
//      ItemStack buySecond;
//      ItemStack sell;
//      MerchantRecipeList trades = villager.getRecipes(player);
//      for (MerchantRecipe rec : trades) {
//        buy = rec.getItemToBuy();
//        buySecond = rec.getSecondItemToBuy();
//        sell = rec.getItemToSell();
//        
//        
//        
//        
//      }
      if(player instanceof EntityPlayerSP){
        EntityPlayerSP psp = (EntityPlayerSP)player;
//        ModCyclic.proxy.getClientPlayer()
        showGuiClient(world, villager, psp);
   
//  player.openGui(ModCyclic.instance, ModGuiHandler.GUI_INDEX_VILLAGER, world, 0, 0, 0);
        
      }
//      entityplayerSP does this
//
//      public void displayVillagerTradeGui(IMerchant villager)
//      {
//          this.mc.displayGuiScreen(new GuiMerchant(this.inventory, villager, this.worldObj));
//      }
      
    }
    return true;//Returns true if the item can be used on the given entity, e.g. shears on sheep.
  }
  @SideOnly(Side.CLIENT)
  private void showGuiClient(World world, EntityVillager villager, EntityPlayerSP psp) {
    Minecraft.getMinecraft().displayGuiScreen(new GuiMerchantBetter(psp.inventory, villager, world));
  }
}
