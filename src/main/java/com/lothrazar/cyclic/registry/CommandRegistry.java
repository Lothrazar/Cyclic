package com.lothrazar.cyclic.registry;

import java.util.Collection;
import java.util.Random;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.command.CommandGetHome;
import com.lothrazar.cyclic.command.CommandHealth;
import com.lothrazar.cyclic.command.CommandHome;
import com.lothrazar.cyclic.command.CommandHunger;
import com.lothrazar.cyclic.command.CommandNbt;
import com.lothrazar.cyclic.command.CommandNetherping;
import com.lothrazar.cyclic.command.CommandTask;
import com.lothrazar.cyclic.util.AttributesUtil;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ObjectiveArgument;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.commands.arguments.ScoreHolderArgument;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Scoreboard;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommandRegistry {

  private static final String ARG_MIN = "min";
  private static final String ARG_MAX = "max";
  private static final String ARG_VALUE = "value";
  private static final String ARG_PLAYER = "player";

  public enum CyclicCommands {

    HOME, GETHOME, HEALTH, HUNGER, DEV, PING, TODO, HEARTS, GAMEMODE, GRAVITY, GLOWING, SCOREBOARD, ATTRIBUTE;

    @Override
    public String toString() {
      return this.name().toLowerCase();
    }
  }

  public static BooleanValue COMMANDDEV;
  public static BooleanValue COMMANDGETHOME;
  public static BooleanValue COMMANDHEALTH;
  public static BooleanValue COMMANDHOME;
  public static BooleanValue COMMANDHUNGER;
  public static BooleanValue COMMANDPING;

  @SubscribeEvent
  public void onRegisterCommandsEvent(RegisterCommandsEvent event) {
    CommandDispatcher<CommandSourceStack> r = event.getDispatcher();
    r.register(LiteralArgumentBuilder.<CommandSourceStack> literal(ModCyclic.MODID)
        .then(Commands.literal(CyclicCommands.HOME.toString())
            .requires((p) -> {
              return p.hasPermission(COMMANDHOME.get() ? 3 : 0);
            })
            .executes(x -> {
              return CommandHome.execute(x);
            }))
        .then(Commands.literal(CyclicCommands.GETHOME.toString())
            .requires((p) -> {
              return p.hasPermission(COMMANDGETHOME.get() ? 3 : 0);
            })
            .executes(x -> {
              return CommandGetHome.execute(x);
            }))
        .then(Commands.literal(CyclicCommands.HEALTH.toString())
            .requires((p) -> {
              return p.hasPermission(COMMANDHEALTH.get() ? 3 : 0);
            })
            .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                .then(Commands.argument(ARG_VALUE, FloatArgumentType.floatArg(0, 100F))
                    .executes(x -> {
                      return CommandHealth.executeHealth(x, EntityArgument.getPlayers(x, ARG_PLAYER), FloatArgumentType.getFloat(x, ARG_VALUE));
                    }))))
        .then(Commands.literal(CyclicCommands.HEARTS.toString())
            .requires((p) -> {
              return p.hasPermission(COMMANDHEALTH.get() ? 3 : 0);
            })
            //reverted to old way. deprecate in future
            .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                .then(Commands.argument(ARG_VALUE, IntegerArgumentType.integer(1, 100))
                    .executes(x -> {
                      return AttributesUtil.setHearts(EntityArgument.getPlayers(x, ARG_PLAYER), IntegerArgumentType.getInteger(x, ARG_VALUE));
                    }))))
        //cyclic scoreboard rng @p <objective> <min> <max>
        .then(Commands.literal(CyclicCommands.SCOREBOARD.toString())
            .requires((p) -> {
              return p.hasPermission(0); // 3 for
            })
            .then(Commands.literal("rng")
                .then(Commands.argument("targets", ScoreHolderArgument.scoreHolders())
                    .then(Commands.argument(ARG_MIN, IntegerArgumentType.integer(1, 100))
                        .then(Commands.argument(ARG_MAX, IntegerArgumentType.integer(1, 100))
                            .then(Commands.argument("objective", StringArgumentType.greedyString())
                                .executes(x -> {
                                  return CommandRegistry.executeRng(x, ScoreHolderArgument.getNamesWithDefaultWildcard(x, "targets"),
                                      ObjectiveArgument.getObjective(x, "objective"),
                                      IntegerArgumentType.getInteger(x, ARG_MIN),
                                      IntegerArgumentType.getInteger(x, ARG_MAX));
                                }))))))
            //cyclic scoreboard test @p <objective>
            .then(Commands.literal("test")
                .then(Commands.argument("targets", ScoreHolderArgument.scoreHolders())
                    .then(Commands.argument("objective", ObjectiveArgument.objective())
                        .executes(x -> {
                          return CommandRegistry.executeRngTest(x, ScoreHolderArgument.getNamesWithDefaultWildcard(x, "targets"),
                              ObjectiveArgument.getObjective(x, "objective"));
                        })))))
        // /cyclic attributes reach_dist add 3
        .then(Commands.literal(CyclicCommands.ATTRIBUTE.toString()) //same as hearts but subcommand again instead of just number
            .requires((p) -> {
              return p.hasPermission(2);
            })
            .then(Commands.argument("attribute", ResourceKeyArgument.key(Registry.ATTRIBUTE_REGISTRY))
                .then(Commands.literal("add")
                    .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                        .then(Commands.argument(ARG_VALUE, IntegerArgumentType.integer(-10000, 10000))
                            .executes(x -> {
                              return AttributesUtil.add(ResourceKeyArgument.getAttribute(x, "attribute"), EntityArgument.getPlayers(x, ARG_PLAYER), IntegerArgumentType.getInteger(x, ARG_VALUE));
                            }))))
                .then(Commands.literal("factor")
                    .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                        .then(Commands.argument(ARG_VALUE, DoubleArgumentType.doubleArg(0, 100))
                            .executes(x -> {
                              return AttributesUtil.multiply(ResourceKeyArgument.getAttribute(x, "attribute"), EntityArgument.getPlayers(x, ARG_PLAYER), DoubleArgumentType.getDouble(x, ARG_VALUE));
                            }))))
                .then(Commands.literal("reset")
                    .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                        .executes(x -> {
                          return AttributesUtil.reset(ResourceKeyArgument.getAttribute(x, "attribute"), EntityArgument.getPlayers(x, ARG_PLAYER));
                        })))))
        .then(Commands.literal(CyclicCommands.GAMEMODE.toString())
            .requires((p) -> {
              return p.hasPermission(3); // 3 for gamemode
            })
            .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                .then(Commands.argument(ARG_VALUE, IntegerArgumentType.integer(0, 3))
                    .executes(x -> {
                      return CommandRegistry.executeGamemode(x, EntityArgument.getPlayers(x, ARG_PLAYER), IntegerArgumentType.getInteger(x, ARG_VALUE));
                    }))))
        .then(Commands.literal(CyclicCommands.GRAVITY.toString())
            .requires((p) -> {
              return p.hasPermission(3); // 3 for
            })
            .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                .then(Commands.argument(ARG_VALUE, BoolArgumentType.bool())
                    .executes(x -> {
                      return CommandRegistry.executeGravity(x, EntityArgument.getPlayers(x, ARG_PLAYER), BoolArgumentType.getBool(x, ARG_VALUE));
                    }))))
        .then(Commands.literal(CyclicCommands.GLOWING.toString())
            .requires((p) -> {
              return p.hasPermission(3); // 3 for
            })
            .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                .then(Commands.argument(ARG_VALUE, BoolArgumentType.bool())
                    .executes(x -> {
                      return CommandRegistry.executeGlowing(x, EntityArgument.getPlayers(x, ARG_PLAYER), BoolArgumentType.getBool(x, ARG_VALUE));
                    }))))
        .then(Commands.literal(CyclicCommands.HUNGER.toString())
            .requires((p) -> {
              return p.hasPermission(COMMANDHUNGER.get() ? 3 : 0);
            })
            .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                .then(Commands.argument(ARG_VALUE, IntegerArgumentType.integer(0, 20))
                    .executes(x -> {
                      return CommandHunger.execute(x, EntityArgument.getPlayers(x, ARG_PLAYER), IntegerArgumentType.getInteger(x, ARG_VALUE));
                    }))))
        .then(Commands.literal(CyclicCommands.DEV.toString())
            .requires((p) -> {
              return p.hasPermission(COMMANDDEV.get() ? 3 : 0);
            })
            .then(Commands.literal("nbt")
                .executes(x -> {
                  return CommandNbt.executePrintNbt(x);
                }))
            .then(Commands.literal("tags")
                .executes(x -> {
                  return CommandNbt.executePrintTags(x);
                })))
        .then(Commands.literal(CyclicCommands.PING.toString())
            .requires((p) -> {
              return p.hasPermission(COMMANDPING.get() ? 3 : 0);
            })
            .then(Commands.literal("nether")
                .executes(x -> {
                  return CommandNetherping.exeNether(x);
                }))
            .then(Commands.literal("here")
                .executes(x -> {
                  return CommandNetherping.execute(x);
                })))
        .then(Commands.literal(CyclicCommands.TODO.toString())
            .requires((p) -> {
              return p.hasPermission(0);
            })
            .then(Commands.literal("add")
                .then(Commands.argument("arguments", StringArgumentType.greedyString())
                    .executes(x -> {
                      return CommandTask.add(x, StringArgumentType.getString(x, "arguments"));
                    })))
            .then(Commands.literal("remove")
                .then(Commands.argument(ARG_VALUE, IntegerArgumentType.integer(0, 20))
                    .executes(x -> {
                      return CommandTask.remove(x, IntegerArgumentType.getInteger(x, ARG_VALUE));
                    })))
            .then(Commands.literal("list")
                .executes(x -> {
                  return CommandTask.list(x);
                })))
    //
    );
  }

  private static int executeRngTest(CommandContext<CommandSourceStack> x, Collection<String> scoreHolderTargets, Objective objective) {
    Scoreboard scoreboard = x.getSource().getServer().getScoreboard();
    Random rand = new Random();
    for (String s : scoreHolderTargets) {
      Score score = scoreboard.getOrCreatePlayerScore(s, objective);
      ModCyclic.LOGGER.error("score test " + score.getScore());
    }
    return 0;
  }

  private static int executeRng(CommandContext<CommandSourceStack> x, Collection<String> scoreHolderTargets, Objective objective, int min, int max) {
    Scoreboard scoreboard = x.getSource().getServer().getScoreboard();
    int i = 0;
    Random rand = new Random();
    for (String s : scoreHolderTargets) {
      Score score = scoreboard.getOrCreatePlayerScore(s, objective);
      score.setScore(rand.nextInt(min, max));
      ModCyclic.LOGGER.info("objective rng " + score.getScore());
      i += score.getScore();
    }
    return i;
  }

  private static int executeGlowing(CommandContext<CommandSourceStack> x, Collection<ServerPlayer> players, boolean bool) {
    for (ServerPlayer p : players) {
      p.setGlowingTag(bool);
    }
    return 0;
  }

  private static int executeGravity(CommandContext<CommandSourceStack> x, Collection<ServerPlayer> players, boolean bool) {
    for (ServerPlayer p : players) {
      p.setNoGravity(bool);
    }
    return 0;
  }

  private static int executeGamemode(CommandContext<CommandSourceStack> x, Collection<ServerPlayer> players, int integer) {
    for (ServerPlayer p : players) {
      switch (integer) {
        case 0:
          p.setGameMode(GameType.SURVIVAL);
        break;
        case 1:
          p.setGameMode(GameType.CREATIVE);
        break;
        case 2:
          p.setGameMode(GameType.ADVENTURE);
        break;
        case 3:
          p.setGameMode(GameType.SPECTATOR);
        break;
        default:
          UtilChat.sendFeedback(x, integer + " = ?!");
        break;
      }
    }
    return 0;
  }
}
