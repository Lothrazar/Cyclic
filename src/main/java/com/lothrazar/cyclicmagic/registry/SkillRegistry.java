package com.lothrazar.cyclicmagic.registry;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.skill.ISkill;

public class SkillRegistry {

  private static List<ISkill> skills = new ArrayList<>();

  public static void register() {
    //    registerSkill(new StepupSkill());
  }

  private static void registerSkill(ISkill skill) {
    skills.add(skill);
  }

  public static List<ISkill> getSkills() {
    return skills;
  }

}
