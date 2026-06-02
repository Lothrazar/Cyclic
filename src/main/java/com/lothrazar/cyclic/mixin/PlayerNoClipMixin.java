package com.lothrazar.cyclic.mixin;

import com.lothrazar.cyclic.registry.PotionEffectRegistry;
import net.minecraft.world.entity.player.Player;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Makes the cyclic NoClip effect actually phase the player through walls.
 *
 * <p>{@code Player#tick()} contains {@code this.noPhysics = this.isSpectator();} near its top
 * (verified via javap on the NeoForge-patched Player.class). That assignment clobbers any noPhysics
 * we'd set from a NeoForge event, and since super.tick() → LivingEntity.tick() → aiStep() →
 * Entity.move() runs later in the same call, an EntityTickEvent.Pre re-assertion is too late.
 *
 * <p>We redirect the PUTFIELD on {@code Player.noPhysics} inside {@code Player#tick} to OR-in
 * "player has the NoClip effect", so the assignment becomes
 * {@code noPhysics = isSpectator() || hasEffect(NOCLIP)}. That's the only viable intercept point —
 * there's no NeoForge event between this assignment and the eventual {@code Entity#move()} call.
 *
 * <p>Vanilla auto-syncs MobEffectInstance to the client, so both server-side and client-side
 * {@code Player#tick} read the same effect state — the mixin works on both sides without any
 * custom packet for noPhysics.
 *
 * <p>NoClip is intentionally NOT paired with auto-flight at this layer; the effect alone will drop
 * the player through bedrock (the "curse" mechanic). Players are expected to bring their own flight
 * source (Flight potion, creative, etc.) if they want to phase safely.
 *
 * <p>Note: javac emits the PUTFIELD against the static receiver type (Player), even though
 * {@code noPhysics} is declared on Entity. Mixin matches the constant pool entry literally, so the
 * {@code @At} target owner must be Player — using Entity gives "Scanned 0 target(s)" at apply time.
 */
@Mixin(Player.class)
public abstract class PlayerNoClipMixin {

  @Redirect(
      method = "tick()V",
      at = @At(
          value = "FIELD",
          target = "Lnet/minecraft/world/entity/player/Player;noPhysics:Z",
          opcode = Opcodes.PUTFIELD))
  private void cyclic$keepNoPhysicsForNoClip(Player self, boolean value) {
    self.noPhysics = value || self.hasEffect(PotionEffectRegistry.NOCLIP);
  }
}
