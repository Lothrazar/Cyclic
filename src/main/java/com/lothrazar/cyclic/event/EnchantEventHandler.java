package com.lothrazar.cyclic.event;

import com.lothrazar.cyclic.enchant.MultiBowEnchant;
import com.lothrazar.cyclic.registry.EnchantRegistry;
import com.lothrazar.library.util.EnchantUtil;
import com.lothrazar.library.util.EntityUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.ArrowLooseEvent;

public class EnchantEventHandler {
  @SubscribeEvent
  public void onArrowLooseEvent(ArrowLooseEvent event) {
    if (!MultiBowEnchant.isEnabled()) {
      return;
    }
    ItemStack stackBow = event.getBow();
    Player player = event.getEntity();
    Level worldIn = player.level();
    if (!worldIn.isClientSide) {
      var holder = EnchantUtil.holder(EnchantRegistry.MULTIBOW, player);
      int level = EnchantUtil.getCurrentLevelTool(holder, stackBow);
      if (level <= 0) {
        return;
      }
      Vec3 playerDirection = EntityUtil.lookVector(player.getYRot(), player.getXRot());
      Vec3 left = playerDirection.cross(new Vec3(0, 1, 0));
      Vec3 right = playerDirection.cross(new Vec3(0, -1, 0));
      MultiBowEnchant.spawnArrow(worldIn, player, stackBow, event.getCharge(), left.normalize());
      MultiBowEnchant.spawnArrow(worldIn, player, stackBow, event.getCharge(), right.normalize());
    }
  }
}
