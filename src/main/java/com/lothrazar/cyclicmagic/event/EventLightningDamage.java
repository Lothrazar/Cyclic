package com.lothrazar.cyclicmagic.event;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.SkeletonType;
import net.minecraft.entity.monster.ZombieType;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventLightningDamage implements IHasConfig {
  private boolean skelEnabled;
  private boolean zombEnabled;
  @SuppressWarnings("deprecation")
  @SubscribeEvent
  public void onEntityStruckByLightning(EntityStruckByLightningEvent event) {
    if (skelEnabled && event.getEntity() instanceof EntitySkeleton &&
        event.getLightning() != null) {
      EntitySkeleton skel = (EntitySkeleton) event.getEntity();
      if (skel.func_189771_df() == SkeletonType.NORMAL) {
        SkeletonType newType = skel.worldObj.rand.nextDouble() > 0.5 ? SkeletonType.WITHER : SkeletonType.STRAY;
        skel.func_189768_a(newType);
      }
    }
    if (zombEnabled && event.getEntity() instanceof EntityZombie &&
        event.getLightning() != null) {
      EntityZombie skel = (EntityZombie) event.getEntity();
      //it says  //Do not use, Replacement TBD
      //but , if there is no replacement, why is it deprecated? makes no sense i say!
      if (skel.func_189777_di() == ZombieType.NORMAL) {
        skel.func_189778_a(ZombieType.HUSK);
      }
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    skelEnabled = config.getBoolean("LightningSkeletonMutate", Const.ConfigCategory.mobs, true, "Skeletons turn into either a Stray or a Wither Skeleton when hit by lightning");
    zombEnabled = config.getBoolean("LightningZombieMutate", Const.ConfigCategory.mobs, true, "Zombies turn into a Husk when hit by lightning");
  }
}
