package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.command.CommandGetHome;
import com.lothrazar.cyclic.command.CommandHome;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommandRegistry {

  public enum CyclicCommands {

    HOME, GETHOME, HEALTH, HUNGER, NBT, TODO;

    @Override
    public String toString() {
      return this.name().toLowerCase();
    }
  }

  @SubscribeEvent
  public void onRegisterCommandsEvent(RegisterCommandsEvent event) {
    //
    CommandDispatcher<CommandSource> r = event.getDispatcher();
    r.register(LiteralArgumentBuilder.<CommandSource> literal(ModCyclic.MODID)
        .then(Commands.literal(CyclicCommands.HOME.toString())
            .executes(x -> {
              return CommandHome.execute(x);
            }))
        .then(Commands.literal(CyclicCommands.GETHOME.toString())
            .executes(x -> {
              return CommandGetHome.execute(x);
            }))
    //
    );
    //        .then(Commands.argument("arguments", StringArgumentType.greedyString()).executes(this::execute))
    //        .executes(this::execute));
  }
  //  private int execute(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
  //    ServerPlayerEntity player = ctx.getSource().asPlayer();
  //    List<String> arguments = Arrays.asList(ctx.getInput().split("\\s+"));
  //    if (arguments.size() < 2) {
  //      badCommandMsg(player);
  //      return 0;
  //    }
  //    String sub = arguments.get(1);
  //    //loop on all registered commands
  //    for (ICyclicCommand cmd : COMMANDS) {
  //      if (sub.equalsIgnoreCase(cmd.getName())) {
  //        //do i need op
  //        if (cmd.needsOp()) {
  //          //ok check me
  //          boolean isOp = ctx.getSource().hasPermissionLevel(1);
  //          if (!isOp) {
  //            //player needs op but does not have it
  //            //            player.getDisplayName()
  //            ModCyclic.LOGGER.info("Player [" + player.getUniqueID() + "," + player.getDisplayName() + "] attempted /cyclic command "
  //                + sub + " but does not have the required permissions");
  //            UtilChat.sendFeedback(ctx, "commands.help.failed");
  //            return 1;
  //          }
  //        }
  //        return cmd.execute(ctx, arguments.subList(2, arguments.size()), player);
  //      }
  //    }
  //    badCommandMsg(player);
  //    return 0;
  //  }
  //
  //  private void badCommandMsg(ServerPlayerEntity player) {
  //    UtilChat.addServerChatMessage(player,
  //        UtilChat.lang("command.cyclic.arguments.null")
  //            + "[" + String.join(", ", SUBCOMMANDS) + "]");
  //  }
}
