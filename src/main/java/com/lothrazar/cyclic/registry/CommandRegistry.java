package com.lothrazar.cyclic.registry;

import java.util.Random;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.command.CommandGamemode;
import com.lothrazar.cyclic.command.CommandGetHome;
import com.lothrazar.cyclic.command.CommandGlowing;
import com.lothrazar.cyclic.command.CommandGravity;
import com.lothrazar.cyclic.command.CommandHealth;
import com.lothrazar.cyclic.command.CommandHome;
import com.lothrazar.cyclic.command.CommandHunger;
import com.lothrazar.cyclic.command.CommandNbt;
import com.lothrazar.cyclic.command.CommandNetherping;
import com.lothrazar.cyclic.command.CommandScoreboard;
import com.lothrazar.cyclic.command.CommandTask;
import com.lothrazar.cyclic.util.AttributesUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ObjectiveArgument;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.commands.arguments.ScoreHolderArgument;
import net.minecraft.core.Registry;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommandRegistry {

  public static final Random RAND = new Random();
  private static final String FORK_RESET = "reset";
  private static final String FORK_FACTOR = "factor";
  private static final String FORK_ADD = "add";
  private static final String FORK_SET = "set";
  private static final String FORK_TOGGLE = "toggle";
  //  ?? set
  //      ?? toggle
  private static final String FORK_RANDOM = "random";
  private static final String ARG_OBJECTIVE = "objective";
  private static final String ARG_TARGETS = "targets";
  private static final String ARG_ATTR = "attribute";
  private static final String ARG_MIN = "min";
  private static final String ARG_MAX = "max";
  private static final String ARG_VALUE = "value";
  private static final String ARG_PLAYER = "player";
  private static final int PERM_EVERYONE = 0; // no restrictions
  private static final int PERM_ELEVATED = 2; // player with perms/creative OR function OR command block

  public enum CyclicCommands {

    HOME, GETHOME, HEALTH, HUNGER, DEV, PING, TODO, HEARTS, GAMEMODE, GRAVITY, GLOWING, SCOREBOARD, ATTRIBUTE, STEPHEIGHT;
    //step height
    //does the file format thing, same as DatFile

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
        // cyclic home
        .then(Commands.literal(CyclicCommands.HOME.toString())
            .requires((p) -> {
              return p.hasPermission(COMMANDHOME.get() ? PERM_ELEVATED : PERM_EVERYONE);
            })
            .executes(x -> {
              return CommandHome.execute(x);
            }))
        //  /cyclic gethome
        .then(Commands.literal(CyclicCommands.GETHOME.toString())
            .requires((p) -> {
              return p.hasPermission(COMMANDGETHOME.get() ? PERM_ELEVATED : PERM_EVERYONE);
            })
            .executes(x -> {
              return CommandGetHome.execute(x);
            }))
        //     /cyclic health 
        .then(Commands.literal(CyclicCommands.HEALTH.toString())
            .requires((p) -> {
              return p.hasPermission(COMMANDHEALTH.get() ? PERM_ELEVATED : PERM_EVERYONE);
            })
            .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                .then(Commands.argument(ARG_VALUE, FloatArgumentType.floatArg(0, 100F))
                    .executes(x -> {
                      return CommandHealth.executeHealth(x, EntityArgument.getPlayers(x, ARG_PLAYER), FloatArgumentType.getFloat(x, ARG_VALUE));
                    }))))
        // cyclic hearts
        .then(Commands.literal(CyclicCommands.HEARTS.toString())
            .requires((p) -> {
              return p.hasPermission(COMMANDHEALTH.get() ? PERM_ELEVATED : PERM_EVERYONE);
            })
            //reverted to old way. deprecate in future
            .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                .then(Commands.argument(ARG_VALUE, IntegerArgumentType.integer())
                    .executes(x -> {
                      return AttributesUtil.setHearts(EntityArgument.getPlayers(x, ARG_PLAYER), IntegerArgumentType.getInteger(x, ARG_VALUE));
                    }))))
        //cyclic scoreboard rng @p <objective> <min> <max>
        .then(Commands.literal(CyclicCommands.SCOREBOARD.toString())
            .requires((p) -> {
              return p.hasPermission(PERM_ELEVATED);
            })
            .then(Commands.literal(FORK_RANDOM)
                .then(Commands.argument(ARG_TARGETS, ScoreHolderArgument.scoreHolders())
                    .then(Commands.argument(ARG_MIN, IntegerArgumentType.integer())
                        .then(Commands.argument(ARG_MAX, IntegerArgumentType.integer())
                            .then(Commands.argument(ARG_OBJECTIVE, StringArgumentType.greedyString())
                                .executes(x -> {
                                  return CommandScoreboard.scoreboardRng(x, ScoreHolderArgument.getNamesWithDefaultWildcard(x, ARG_TARGETS),
                                      ObjectiveArgument.getObjective(x, ARG_OBJECTIVE),
                                      IntegerArgumentType.getInteger(x, ARG_MIN),
                                      IntegerArgumentType.getInteger(x, ARG_MAX));
                                }))))))
            //cyclic scoreboard test @p <objective>
            .then(Commands.literal(FORK_ADD)
                .then(Commands.argument(ARG_TARGETS, ScoreHolderArgument.scoreHolders())
                    .then(Commands.argument(ARG_VALUE, IntegerArgumentType.integer())
                        .then(Commands.argument(ARG_OBJECTIVE, ObjectiveArgument.objective())
                            .executes(x -> {
                              return CommandScoreboard.scoreboardAdd(x, ScoreHolderArgument.getNamesWithDefaultWildcard(x, ARG_TARGETS),
                                  ObjectiveArgument.getObjective(x, ARG_OBJECTIVE), IntegerArgumentType.getInteger(x, ARG_VALUE));
                            })))))
            .then(Commands.literal("test")
                .then(Commands.argument(ARG_TARGETS, ScoreHolderArgument.scoreHolders())
                    .then(Commands.argument(ARG_OBJECTIVE, ObjectiveArgument.objective())
                        .executes(x -> {
                          return CommandScoreboard.scoreboardRngTest(x, ScoreHolderArgument.getNamesWithDefaultWildcard(x, ARG_TARGETS),
                              ObjectiveArgument.getObjective(x, ARG_OBJECTIVE));
                        })))))
        // /cyclic attributes reach_dist add 3
        .then(Commands.literal(CyclicCommands.ATTRIBUTE.toString()) //same as hearts but subcommand again instead of just number
            .requires((p) -> {
              return p.hasPermission(PERM_ELEVATED);
            })
            .then(Commands.argument(ARG_ATTR, ResourceKeyArgument.key(Registry.ATTRIBUTE_REGISTRY))
                .then(Commands.literal(FORK_ADD)
                    .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                        .then(Commands.argument(ARG_VALUE, IntegerArgumentType.integer(-10000, 10000))
                            .executes(x -> {
                              return AttributesUtil.add(ResourceKeyArgument.getAttribute(x, ARG_ATTR), EntityArgument.getPlayers(x, ARG_PLAYER), IntegerArgumentType.getInteger(x, ARG_VALUE));
                            }))))
                .then(Commands.literal(FORK_RANDOM)
                    .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                        .then(Commands.argument(ARG_MIN, IntegerArgumentType.integer(-10000, 10000))
                            .then(Commands.argument(ARG_MAX, IntegerArgumentType.integer(-10000, 10000))
                                .executes(x -> {
                                  return AttributesUtil.addRandom(ResourceKeyArgument.getAttribute(x, ARG_ATTR), EntityArgument.getPlayers(x, ARG_PLAYER), IntegerArgumentType.getInteger(x, ARG_MIN), IntegerArgumentType.getInteger(x, ARG_MAX));
                                })))))
                .then(Commands.literal(FORK_FACTOR)
                    .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                        .then(Commands.argument(ARG_VALUE, DoubleArgumentType.doubleArg(0, 100))
                            .executes(x -> {
                              return AttributesUtil.multiply(ResourceKeyArgument.getAttribute(x, ARG_ATTR), EntityArgument.getPlayers(x, ARG_PLAYER), DoubleArgumentType.getDouble(x, ARG_VALUE));
                            }))))
                .then(Commands.literal(FORK_RESET)
                    .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                        .executes(x -> {
                          return AttributesUtil.reset(ResourceKeyArgument.getAttribute(x, ARG_ATTR), EntityArgument.getPlayers(x, ARG_PLAYER));
                        })))))
        .then(Commands.literal(CyclicCommands.GAMEMODE.toString())
            .requires((p) -> {
              return p.hasPermission(PERM_ELEVATED);
            })
            .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                .then(Commands.argument(ARG_VALUE, IntegerArgumentType.integer(0, 3))
                    .executes(x -> {
                      return CommandGamemode.executeGamemode(x, EntityArgument.getPlayers(x, ARG_PLAYER), IntegerArgumentType.getInteger(x, ARG_VALUE));
                    }))))
        .then(Commands.literal(CyclicCommands.GRAVITY.toString())
            .requires((p) -> {
              return p.hasPermission(PERM_ELEVATED);
            })
            .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                .then(Commands.argument(ARG_VALUE, BoolArgumentType.bool())
                    .executes(x -> {
                      return CommandGravity.executeGravity(x, EntityArgument.getPlayers(x, ARG_PLAYER), BoolArgumentType.getBool(x, ARG_VALUE));
                    }))))
        //       /cyclic glowing @p random
        .then(Commands.literal(CyclicCommands.GLOWING.toString())
            .requires((p) -> {
              return p.hasPermission(PERM_ELEVATED);
            })
            .then(Commands.literal(FORK_SET)
                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                    .then(Commands.argument(ARG_VALUE, BoolArgumentType.bool())
                        .executes(x -> {
                          return CommandGlowing.executeGlowing(x, EntityArgument.getPlayers(x, ARG_PLAYER), BoolArgumentType.getBool(x, ARG_VALUE));
                        })))))
        //   /cyclic hunger @p add -4
        .then(Commands.literal(CyclicCommands.HUNGER.toString())
            .requires((p) -> {
              return p.hasPermission(COMMANDHUNGER.get() ? PERM_ELEVATED : PERM_EVERYONE);
            })
            .then(Commands.literal(FORK_FACTOR)
                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                    .then(Commands.argument(ARG_VALUE, DoubleArgumentType.doubleArg(0, 10))
                        .executes(x -> {
                          return CommandHunger.executeFactor(x, EntityArgument.getPlayers(x, ARG_PLAYER), DoubleArgumentType.getDouble(x, ARG_VALUE));
                        }))))
            .then(Commands.literal(FORK_ADD)
                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                    .then(Commands.argument(ARG_VALUE, IntegerArgumentType.integer(0, 20))
                        .executes(x -> {
                          return CommandHunger.executeAdd(x, EntityArgument.getPlayers(x, ARG_PLAYER), IntegerArgumentType.getInteger(x, ARG_VALUE));
                        }))))
            .then(Commands.literal(FORK_SET)
                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                    .then(Commands.argument(ARG_VALUE, IntegerArgumentType.integer(0, 20))
                        .executes(x -> {
                          return CommandHunger.executeSet(x, EntityArgument.getPlayers(x, ARG_PLAYER), IntegerArgumentType.getInteger(x, ARG_VALUE));
                        }))))
            .then(Commands.literal(FORK_RANDOM)
                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                    .then(Commands.argument(ARG_MIN, IntegerArgumentType.integer(0, 20))
                        .then(Commands.argument(ARG_MAX, IntegerArgumentType.integer(0, 20))
                            .executes(x -> {
                              return CommandHunger.executeRandom(x, EntityArgument.getPlayers(x, ARG_PLAYER), IntegerArgumentType.getInteger(x, ARG_MIN), IntegerArgumentType.getInteger(x, ARG_MAX));
                            }))))))
        .then(Commands.literal(CyclicCommands.DEV.toString())
            .requires((p) -> {
              return p.hasPermission(COMMANDDEV.get() ? PERM_ELEVATED : PERM_EVERYONE);
            })
            .then(Commands.literal("nbt")
                .executes(x -> {
                  return CommandNbt.executePrintNbt(x);
                }))
            .then(Commands.literal("tags")
                .executes(x -> {
                  return CommandNbt.executePrintTags(x);
                }))
            .then(Commands.literal(FORK_RANDOM)
                .then(Commands.argument(ARG_MIN, IntegerArgumentType.integer())
                    .then(Commands.argument(ARG_MAX, IntegerArgumentType.integer())
                        .executes(x -> {
                          return CommandRegistry.returnIntRng(
                              IntegerArgumentType.getInteger(x, ARG_MIN),
                              IntegerArgumentType.getInteger(x, ARG_MAX));
                        })))))
        .then(Commands.literal(CyclicCommands.PING.toString())
            .requires((p) -> {
              return p.hasPermission(COMMANDPING.get() ? PERM_ELEVATED : PERM_EVERYONE);
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
              return p.hasPermission(PERM_EVERYONE);
            })
            .then(Commands.literal(FORK_ADD)
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
    //new commands here
    );
  }

  private static int returnIntRng(int min, int max) {
    return RAND.nextInt(min, max);
  }
}
