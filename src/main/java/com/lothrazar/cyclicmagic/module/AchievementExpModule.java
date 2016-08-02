package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilExperience;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.AchievementEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AchievementExpModule extends BaseEventModule {
  private boolean isEnabled;
  @SubscribeEvent
  public void onAchievement(AchievementEvent event) {
    //something changed where now i catch this event even if the player MAYBE DID NOT
    //get the ach : https://github.com/PrinceOfAmber/Cyclic/issues/60
    //so i have to check 2 
    // 1 do i already have it
    //and 2 do i not have it but i dont have the parent one 
    if (isEnabled) {
      EntityPlayer p = event.getEntityPlayer();
      //am i getting an achievement that i have already? 
      if (event.getAchievement().isAchievement() && p.hasAchievement(event.getAchievement()) == false) {
        if (event.getAchievement().parentAchievement == null
            || p.hasAchievement(event.getAchievement().parentAchievement) == true) {
          //either the parent ach is null, so we get in.
          //OR parent is not null, but we have it (true) so is fine
          int mult = (event.getAchievement().getSpecial()) ? 100 : 1;
          int amt = mult * (p.worldObj.rand.nextInt(30) + 20);// between 20,30
          UtilExperience.setXp(p, (int) UtilExperience.getExpTotal(p) + amt);
          UtilSound.playSound(p, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP);
        }
      }
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    //new in 1.4.5 defaults to false
    isEnabled = config.getBoolean("Exp Achievements", Const.ConfigCategory.player, false, "Get experience from achievements (random 20-30 exp, multiplied by 100 if it is flagged as special)");
  }
}
