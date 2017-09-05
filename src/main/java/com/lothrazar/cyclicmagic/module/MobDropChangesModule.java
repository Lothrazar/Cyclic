package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MobDropChangesModule extends BaseEventModule implements IHasConfig {
  private boolean endermanDrop;
  private boolean nameTagDeath;
  @SubscribeEvent
  public void onLivingDropsEvent(LivingDropsEvent event) {
    Entity entity = event.getEntity();
    World world = entity.getEntityWorld();
    if (nameTagDeath) {
      if (entity.getCustomNameTag() != null && entity.getCustomNameTag() != "") {
        // item stack NBT needs the name enchanted onto it
        if (world.isRemote == false) {
          ItemStack nameTag = UtilNBT.buildEnchantedNametag(entity.getCustomNameTag());
          UtilItemStack.dropItemStackInWorld(world, entity.getPosition(), nameTag);
        }
      }
    }
    if (endermanDrop && entity instanceof EntityEnderman) {
      EntityEnderman mob = (EntityEnderman) entity;
      IBlockState bs = mob.getHeldBlockState();// mob.func_175489_ck();
      if (bs != null && bs.getBlock() != null && world.isRemote == false) {
        UtilItemStack.dropItemStackInWorld(world, mob.getPosition(), bs.getBlock());
      }
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.mobs;
    config.addCustomCategoryComment(category, "Changes to vanilla mobs");
    nameTagDeath = config.getBoolean("Name Tag Death", category, true,
        "When an entity dies that is named with a tag, it drops the nametag");
    endermanDrop = config.getBoolean("Enderman Block", category, true,
        "Enderman will always drop block they are carrying 100%");
  }
}
