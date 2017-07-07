package com.lothrazar.cyclicmagic.module;
import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.command.CommandEnderChest;
import com.lothrazar.cyclicmagic.command.CommandGetHome;
import com.lothrazar.cyclicmagic.command.CommandHeal;
import com.lothrazar.cyclicmagic.command.CommandHearts;
import com.lothrazar.cyclicmagic.command.CommandHome;
import com.lothrazar.cyclicmagic.command.CommandPing;
import com.lothrazar.cyclicmagic.command.CommandSearchItem;
import com.lothrazar.cyclicmagic.command.CommandSearchSpawner;
import com.lothrazar.cyclicmagic.command.CommandSearchTrades;
import com.lothrazar.cyclicmagic.command.CommandTodoList;
import com.lothrazar.cyclicmagic.command.CommandVillageInfo;
import com.lothrazar.cyclicmagic.command.CommandWorldHome;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry.IPlayerExtendedProperties;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CommandModule extends BaseModule implements IHasConfig {
  private static Map<String, Boolean> configToggle = new HashMap<String, Boolean>();
  private static Map<String, Boolean> commandNeedsOp = new HashMap<String, Boolean>();
  private static String category;
  @Override
  public void onPreInit() {
    if (configToggle.get(CommandTodoList.name)) {
      ModCyclic.instance.events.register(this);
    }
  }
  @Override
  public void onServerStarting(FMLServerStartingEvent event) {
    if (configToggle.get(CommandEnderChest.name)) {
      event.registerServerCommand(new CommandEnderChest(commandNeedsOp.get(CommandEnderChest.name)));
    }
    if (configToggle.get(CommandGetHome.name)) {
      event.registerServerCommand(new CommandGetHome(commandNeedsOp.get(CommandGetHome.name)));
    }
    if (configToggle.get(CommandHeal.name)) {
      event.registerServerCommand(new CommandHeal(commandNeedsOp.get(CommandHeal.name)));
    }
    if (configToggle.get(CommandHearts.name)) {
      event.registerServerCommand(new CommandHearts(commandNeedsOp.get(CommandHearts.name)));
    }
    if (configToggle.get(CommandHome.name)) {
      event.registerServerCommand(new CommandHome(commandNeedsOp.get(CommandHome.name)));
    }
    if (configToggle.get(CommandPing.name)) {
      event.registerServerCommand(new CommandPing(commandNeedsOp.get(CommandPing.name)));
    }
    if (configToggle.get(CommandSearchItem.name)) {
      event.registerServerCommand(new CommandSearchItem(commandNeedsOp.get(CommandSearchItem.name)));
    }
    if (configToggle.get(CommandSearchSpawner.name)) {
      event.registerServerCommand(new CommandSearchSpawner(commandNeedsOp.get(CommandSearchSpawner.name)));
    }
    if (configToggle.get(CommandSearchTrades.name)) {
      event.registerServerCommand(new CommandSearchTrades(commandNeedsOp.get(CommandSearchTrades.name)));
    }
    if (configToggle.get(CommandTodoList.name)) {
      event.registerServerCommand(new CommandTodoList(commandNeedsOp.get(CommandTodoList.name)));
    }
    if (configToggle.get(CommandVillageInfo.name)) {
      event.registerServerCommand(new CommandVillageInfo(commandNeedsOp.get(CommandVillageInfo.name)));
    }
    if (configToggle.get(CommandWorldHome.name)) {
      event.registerServerCommand(new CommandWorldHome(commandNeedsOp.get(CommandWorldHome.name)));
    }
  }
  private void syncCommandConfig(Configuration config, String name, boolean defaultNeedsOp, String comment) {
    Property prop = config.get(category, name, true, comment);
    prop.setRequiresMcRestart(true);
    configToggle.put(name, prop.getBoolean());
    prop = config.get(category, name + ".NeedsOP", defaultNeedsOp, "If true, only server OPs can run this command (or cheats enabled in single player)");
    prop.setRequiresMcRestart(true);
    commandNeedsOp.put(name, prop.getBoolean());
  }
  @Override
  public void syncConfig(Configuration config) {
    category = Const.ConfigCategory.commands;
    config.setCategoryComment(category, "Disable any command that was added");
    syncCommandConfig(config, CommandEnderChest.name, true, "Opens your ender chest");
    syncCommandConfig(config, CommandGetHome.name, false, "Get where your current spawn is set (by a bed)");
    syncCommandConfig(config, CommandHeal.name, true, "Heal yourself (or a target player) to full");
    syncCommandConfig(config, CommandHearts.name, true, "Increase the maximum hearts of a target player (lasts until death)");
    syncCommandConfig(config, CommandHome.name, true, "Teleport you to your current spawn (if its set)");
    syncCommandConfig(config, CommandPing.name, false, "Display your current coordinates");
    //    syncCommandConfig(config, CommandRecipe.name, false, "Find recipes for an item");
    syncCommandConfig(config, CommandSearchItem.name, false, "Search for an item in nearby containers");
    syncCommandConfig(config, CommandSearchSpawner.name, true, "Search the world nearby for spawners (dungeons, etc)");
    syncCommandConfig(config, CommandSearchTrades.name, false, "Search nearby villagers for trades based on item names");
    syncCommandConfig(config, CommandTodoList.name, false, "Set reminders on screen for yourself");
    //    syncCommandConfig(config, CommandUses.name, false, "Find how an item is used in other recipes");
    syncCommandConfig(config, CommandVillageInfo.name, false, "Get the stats on the nearest village (if any)");
    syncCommandConfig(config, CommandWorldHome.name, true, "Teleport to true worldspawn");
  }
  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onRenderTextOverlay(RenderGameOverlayEvent.Text event) {
    IPlayerExtendedProperties props = CapabilityRegistry.getPlayerProperties(Minecraft.getMinecraft().player);
    if (props != null && props.getTODO() != null && props.getTODO().length() > 0) {
      event.getRight().add(props.getTODO());
    }
  }
}
