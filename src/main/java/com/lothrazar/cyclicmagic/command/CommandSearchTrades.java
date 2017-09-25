package com.lothrazar.cyclicmagic.command;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class CommandSearchTrades extends BaseCommand implements ICommand {
  // https://github.com/LothrazarMinecraftMods/MinecraftSearchCommands/blob/master/src/main/java/com/lothrazar/searchcommands/command/CommandSearchTrades.java
  public static final String name = "searchtrade";
  public CommandSearchTrades(boolean op) {
    super(name, op);
  }
  @Override
  public String getUsage(ICommandSender ic) {
    return "/" + getName() + " <item name> <qty>";
  }
  @Override
  public void execute(MinecraftServer server, ICommandSender ic, String[] args) {
    if (ic instanceof EntityPlayer == false) {
      return;
    }
    EntityPlayer p = (EntityPlayer) ic;
    if (args.length == 0) {
      UtilChat.addChatMessage(p, getUsage(ic));
      return;
    }
    String searching = args[0].toLowerCase();
    int searchingQty = -1;
    if (args.length > 1) {
      searchingQty = Integer.parseInt(args[1]);
      if (searchingQty < 0) {
        searchingQty = 0;
      }
    }
    // step 1: get list of nearby villagers, seearch entities nearby in world
    double X = ic.getPosition().getX();// ic.getPlayerCoordinates().posX;
    double Z = ic.getPosition().getZ();// ic.getPlayerCoordinates().posZ;
    double range = 64;
    AxisAlignedBB searchRange = new AxisAlignedBB(X + 0.5D - range, 0.0D, Z + 0.5D - range, X + 0.5D + range, 255.0D, Z + 0.5D + range);
    List<EntityVillager> merchants = ic.getEntityWorld().getEntitiesWithinAABB(EntityVillager.class, searchRange);
    // List merchants =
    // ic.getEntityWorld().getEntitiesWithinAABB(IMerchant.class, searchRange);
    List<EntityVillager> villagers = new ArrayList<EntityVillager>();
    // double check that it should be an adult villager
    // recall that
    // public class EntityVillager extends EntityAgeable implements INpc,
    // IMerchant
    for (EntityVillager m : merchants) {
      // && (IMerchant)m != null
      if (m.isChild() == false) {
        villagers.add(m);
      }
    }
    MerchantRecipeList list;
    MerchantRecipe rec;
    ItemStack buy;
    ItemStack buySecond;
    ItemStack sell;
    String disabled, m;
    ArrayList<String> messages = new ArrayList<String>();
    boolean match = false;
    IMerchant v_merch;
    EntityLiving v_entity;
    for (int i = 0; i < villagers.size(); i++) {
      v_entity = villagers.get(i);
      v_merch = villagers.get(i);/// not null for sure based on how we
      /// constructed the list
      list = v_merch.getRecipes(p);
      for (int r = 0; r < list.size(); r++) {
        match = false;
        rec = (MerchantRecipe) list.get(r);
        disabled = (rec.isRecipeDisabled()) ? "[x] " : "";
        buy = rec.getItemToBuy();
        buySecond = rec.getSecondItemToBuy();
        sell = rec.getItemToSell();
        // match to any of the three possible items
        // only match quantity if they enter one
        if (buy.getDisplayName().toLowerCase().contains(searching)) {
          if (searchingQty < 0 || searchingQty == buy.getCount())
            match = true;
        }
        if (buySecond != null && buySecond.getDisplayName().contains(searching)) {
          if (searchingQty < 0 || searchingQty == buySecond.getCount())
            match = true;
        }
        if (sell.getDisplayName().contains(searching)) {
          if (searchingQty < 0 || searchingQty == sell.getCount())
            match = true;
        }
        if (match) {
          m = disabled + UtilChat.blockPosToString(v_entity.getPosition()) + " " + sell.getCount() + " " + sell.getDisplayName() + " :: " + buy.getCount() + " " + buy.getDisplayName();
          messages.add(m);
          /*
           * ModCommands.spawnParticlePacketByID(((Entity)villagers.get(i)).
           * getPosition() ,EnumParticleTypes.CRIT_MAGIC.getParticleID());
           */
        }
      }
    }
    for (int j = 0; j < messages.size(); j++) {
      UtilChat.addChatMessage(p, messages.get(j));
    }
    if (messages.size() == 0) {
      UtilChat.addChatMessage(p, UtilChat.lang("command.searchtrade.none") + " [" + range + "]");
    }
  }
}
