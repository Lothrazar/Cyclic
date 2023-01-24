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

  // orandom for darkosto
  // /cyclic scoreboard orandom @p testmin testmax abc 
  // use scoreboard setup and /cyclic scoreboard test to debug
  public static int scoreboardObjectiveRng(CommandContext<CommandSourceStack> x, Collection<String> scoreHolderTargets, Objective objective, Objective omin, Objective omax) {
    Scoreboard scoreboard = x.getSource().getServer().getScoreboard();
    int i = 0;
    for (String s : scoreHolderTargets) {
      Score score = scoreboard.getOrCreatePlayerScore(s, objective);
      Score scoreMin = scoreboard.getOrCreatePlayerScore(s, omin);
      Score scoreMax = scoreboard.getOrCreatePlayerScore(s, omax);
      int min = scoreMin.getScore();
      int max = scoreMax.getScore();
      ModCyclic.LOGGER.info("objective dependency detected: " + min + " ?<? " + max);
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
