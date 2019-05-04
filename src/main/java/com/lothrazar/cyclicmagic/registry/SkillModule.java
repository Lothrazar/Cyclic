package com.lothrazar.cyclicmagic.registry;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.playerupgrade.skill.ISkill;
import com.lothrazar.cyclicmagic.playerupgrade.skill.StepupSkill;

public class SkillModule {

  private static List<ISkill> skills = new ArrayList<>();

  public static void onPostInit() {
    registerSkill(new StepupSkill());
  }

  private static void registerSkill(ISkill skill) {
    skills.add(skill);
  }

  public static List<ISkill> getSkills() {
    return skills;
  }
}
