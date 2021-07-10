package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ConfigRegistry;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.command.CommandGetHome;
import com.lothrazar.cyclic.command.CommandHealth;
import com.lothrazar.cyclic.command.CommandHome;
import com.lothrazar.cyclic.command.CommandHunger;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
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
    CommandDispatcher<CommandSource> r = event.getDispatcher();
    r.register(LiteralArgumentBuilder.<CommandSource> literal(ModCyclic.MODID)
        .then(Commands.literal(CyclicCommands.HOME.toString())
            .requires((p) -> {
              return p.hasPermissionLevel(ConfigRegistry.COMMANDHOME.get() ? 3 : 0);
            })
            .executes(x -> {
              return CommandHome.execute(x);
            }))
        .then(Commands.literal(CyclicCommands.GETHOME.toString())
            .requires((p) -> {
              return p.hasPermissionLevel(ConfigRegistry.COMMANDGETHOME.get() ? 3 : 0);
            })
            .executes(x -> {
              return CommandGetHome.execute(x);
            }))
        .then(Commands.literal(CyclicCommands.HEALTH.toString())
            .requires((p) -> {
              return p.hasPermissionLevel(ConfigRegistry.COMMANDHEALTH.get() ? 3 : 0);
            })
            .then(Commands.argument("player", EntityArgument.players())
                .then(Commands.argument("value", FloatArgumentType.floatArg(0, 100F))
                    .executes(x -> {
                      return CommandHealth.execute(x, EntityArgument.getPlayers(x, "player"), FloatArgumentType.getFloat(x, "value"));
                    }))))
        .then(Commands.literal(CyclicCommands.HUNGER.toString())
            .requires((p) -> {
              return p.hasPermissionLevel(ConfigRegistry.COMMANDHUNGER.get() ? 3 : 0);
            })
            .then(Commands.argument("player", EntityArgument.players())
                .then(Commands.argument("value", IntegerArgumentType.integer(0, 20))
                    .executes(x -> {
                      return CommandHunger.execute(x, EntityArgument.getPlayers(x, "player"), IntegerArgumentType.getInteger(x, "value"));
                    }))))
    //
    );
  }
}
