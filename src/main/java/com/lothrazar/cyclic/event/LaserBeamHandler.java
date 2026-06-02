package com.lothrazar.cyclic.event;

import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.cyclic.item.LaserItem;
import com.lothrazar.library.render.RenderEntityToBlockLaser;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class LaserBeamHandler {
    @SubscribeEvent
    public void onRenderBeam(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) {
            return;
        }

        ItemStack stack = LaserItem.getIfHeld(player);
        if (!stack.isEmpty() && player.isUsingItem()) {
            // Check energy
            IEnergyStorage storage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
            if (storage == null || storage.getEnergyStored() < ConfigRegistry.LaserItemEnergy.get()) {
                return;
            }

            float partialTick = event.getPartialTick().getGameTimeDeltaTicks();

            // Close-range
            if (mc.crosshairPickEntity != null) {
                RenderEntityToBlockLaser.renderLaser(event, player, partialTick, stack, InteractionHand.MAIN_HAND, 18, -0.02F);
            }
            // Long-range
            else {
                boolean shouldRenderMiss = ConfigRegistry.LaserRenderMisses.get();
                boolean hitLongRange = false;

                // Determine where the laser should hit
                double laserRange = ConfigRegistry.LaserItemRange.get();
                Vec3 eyePos = player.getEyePosition(partialTick);
                Vec3 view = player.getViewVector(partialTick);
                Vec3 end = eyePos.add(view.scale(laserRange));
                AABB aabb = player.getBoundingBox().expandTowards(view.scale(laserRange)).inflate(1.0D);

                EntityHitResult ehr = ProjectileUtil.getEntityHitResult(player, eyePos, end, aabb, ent -> ent.isAttackable() && ent.isAlive(), 0);

                if (ehr != null) {
                    Vec3 hitLoc = ehr.getLocation();
                    BlockHitResult miss = player.level().clip(new ClipContext(eyePos, hitLoc, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, player));
                    if (miss.getType() != HitResult.Type.BLOCK) {
                        hitLongRange = true;
                    }
                }

                if (hitLongRange || shouldRenderMiss) {
                    RenderEntityToBlockLaser.renderLaser(event, player, partialTick, stack, InteractionHand.MAIN_HAND);
                }
            }
        }
    }
}
