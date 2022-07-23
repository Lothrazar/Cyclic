package com.lothrazar.cyclic.command;

import java.util.Collection;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.registry.CommandRegistry;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Scoreboard;

public class CommandScoreboard {

  public static int scoreboardRngTest(CommandContext<CommandSourceStack> x, Collection<String> scoreHolderTargets, Objective objective) {
    Scoreboard scoreboard = x.getSource().getServer().getScoreboard();
    int i = 0;
    for (String s : scoreHolderTargets) {
      Score score = scoreboard.getOrCreatePlayerScore(s, objective);
      ModCyclic.LOGGER.error("[test cmd]" + score.getScore());
      i += score.getScore();
    }
    return i;
  }

  public static int scoreboardAdd(CommandContext<CommandSourceStack> x, Collection<String> scoreHolderTargets, Objective objective, int integer) {
    Scoreboard scoreboard = x.getSource().getServer().getScoreboard();
    int i = 0;
    for (String s : scoreHolderTargets) {
      Score score = scoreboard.getOrCreatePlayerScore(s, objective);
      score.add(integer);
      ModCyclic.LOGGER.info("objective add " + score.getScore());
      i += score.getScore();
    }
    return i;
  }

  public static int scoreboardRng(CommandContext<CommandSourceStack> x, Collection<String> scoreHolderTargets, Objective objective, int min, int max) {
    Scoreboard scoreboard = x.getSource().getServer().getScoreboard();
    int i = 0;
    for (String s : scoreHolderTargets) {
      Score score = scoreboard.getOrCreatePlayerScore(s, objective);
      if (min < max) {
        score.setScore(CommandRegistry.RAND.nextInt(min, max));
      }
      else {
        //either equal, or max is lower than min
        score.setScore(min);
      }
      ModCyclic.LOGGER.info("objective rng " + score.getScore());
      i += score.getScore();
    }
    return i;
  }
}
