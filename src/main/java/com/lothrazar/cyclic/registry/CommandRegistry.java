package com.lothrazar.cyclic.registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.command.CommandGetHome;
import com.lothrazar.cyclic.command.CommandHealth;
import com.lothrazar.cyclic.command.CommandHome;
import com.lothrazar.cyclic.command.CommandHunger;
import com.lothrazar.cyclic.command.CommandNbt;
import com.lothrazar.cyclic.command.CommandNetherping;
import com.lothrazar.cyclic.command.CommandWorldspawn;
import com.lothrazar.cyclic.command.ICyclicCommand;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

public class CommandRegistry {

  List<String> subs = new ArrayList<>();
  public static final List<ICyclicCommand> COMMANDS = new ArrayList<>();

  @SubscribeEvent
  public void serverStarting(FMLServerStartingEvent event) {
    COMMANDS.add(new CommandGetHome());
    COMMANDS.add(new CommandHealth());
    COMMANDS.add(new CommandHome());
    COMMANDS.add(new CommandHunger());
    COMMANDS.add(new CommandNbt());
    COMMANDS.add(new CommandNetherping());
    COMMANDS.add(new CommandWorldspawn());
    for (ICyclicCommand cmd : COMMANDS) {
      subs.add(cmd.getName());
    }
    //
    CommandDispatcher<CommandSource> r = event.getCommandDispatcher();
    // what now lol
    r.register(LiteralArgumentBuilder.<CommandSource> literal(ModCyclic.MODID)
        .then(Commands.argument("arguments", StringArgumentType.greedyString())
            //            .then(Commands.argument("arguments", StringArgumentType.greedyString())
            //                
            .executes(this::execute))
        .executes(this::execute));
  }

  private int execute(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
    ServerPlayerEntity player = ctx.getSource().asPlayer();
    List<String> arguments = Arrays.asList(ctx.getInput().split("\\s+"));
    if (arguments.size() < 2) {
      badCommandMsg(player);
      return 0;
    }
    String sub = arguments.get(1);
    //loop on all registered commands
    for (ICyclicCommand cmd : COMMANDS) {
      if (sub.equalsIgnoreCase(cmd.getName())) {
        return cmd.execute(ctx, arguments.subList(2, arguments.size()), player);
      }
    }
    badCommandMsg(player);
    return 0;
  }

  private void badCommandMsg(ServerPlayerEntity player) {
    UtilChat.addServerChatMessage(player,
        UtilChat.lang("command.cyclic.arguments.null")
            + "[" + String.join(", ", subs) + "]");
  }
}
