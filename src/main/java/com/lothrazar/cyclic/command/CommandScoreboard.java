package com.lothrazar.cyclic.command;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.registry.CommandRegistry;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.ScoreAccess;
import net.minecraft.world.scores.ScoreHolder;
import net.minecraft.world.scores.Scoreboard;

import java.util.Collection;

public class CommandScoreboard {

  public static int scoreboardRngTest(CommandContext<CommandSourceStack> x, Collection<ScoreHolder> scoreHolderTargets, Objective objective) {
    Scoreboard scoreboard = x.getSource().getServer().getScoreboard();
    int i = 0;
    for (ScoreHolder holder : scoreHolderTargets) {
      ScoreAccess score = scoreboard.getOrCreatePlayerScore(holder, objective);
      ModCyclic.LOGGER.error("[test cmd]" + score.get());
      i += score.get();
    }
    return i;
  }

  public static int scoreboardAdd(CommandContext<CommandSourceStack> x, Collection<ScoreHolder> scoreHolderTargets, Objective objective, int integer) {
    Scoreboard scoreboard = x.getSource().getServer().getScoreboard();
    int i = 0;
    for (ScoreHolder holder : scoreHolderTargets) {
      ScoreAccess score = scoreboard.getOrCreatePlayerScore(holder, objective);
      score.set(score.get() + integer);
      i += score.get();
    }
    return i;
  }

  // orandom for darkosto
  public static int scoreboardObjectiveRng(CommandContext<CommandSourceStack> x, Collection<ScoreHolder> scoreHolderTargets, Objective objective, Objective omin, Objective omax) {
    Scoreboard scoreboard = x.getSource().getServer().getScoreboard();
    int i = 0;
    for (ScoreHolder holder : scoreHolderTargets) {
      ScoreAccess score = scoreboard.getOrCreatePlayerScore(holder, objective);
      ScoreAccess scoreMin = scoreboard.getOrCreatePlayerScore(holder, omin);
      ScoreAccess scoreMax = scoreboard.getOrCreatePlayerScore(holder, omax);
      int min = scoreMin.get();
      int max = scoreMax.get();
      if (min < max) {
        score.set(CommandRegistry.RAND.nextInt(min, max));
      }
      else {
        score.set(min);
      }
      i += score.get();
    }
    return i;
  }

  public static int scoreboardRng(CommandContext<CommandSourceStack> x, Collection<ScoreHolder> scoreHolderTargets, Objective objective, int min, int max) {
    Scoreboard scoreboard = x.getSource().getServer().getScoreboard();
    int i = 0;
    for (ScoreHolder holder : scoreHolderTargets) {
      ScoreAccess score = scoreboard.getOrCreatePlayerScore(holder, objective);
      if (min < max) {
        score.set(CommandRegistry.RAND.nextInt(min, max));
      }
      else {
        score.set(min);
      }
      i += score.get();
    }
    return i;
  }
}
