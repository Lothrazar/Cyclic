package com.lothrazar.cyclic.potion.effect;

import com.lothrazar.cyclic.potion.CyclicMobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/**
 * Phase-through-walls effect. Intentionally a curse — granting it does NOT also grant flight, so a player
 * with NoClip alone will free-fall through the world. The item tooltip on chorus_spectral warns about this;
 * the brewable Potion of NoClip is marked HARMFUL (red HUD tint) and applies a 2-tick Flight burst as a hint
 * that the player needs their own flight source if they want to phase without dying.
 *
 * <p>The {@code noPhysics} flag is managed by {@link com.lothrazar.cyclic.mixin.PlayerNoClipMixin}, which
 * redirects the assignment inside {@code Player#tick} so noPhysics stays true while this effect is present.
 * Vanilla auto-syncs MobEffectInstance to the client, so the mixin reads the effect off the player on both
 * sides without any custom packet.
 *
 * <p>This class itself is a no-op subclass — it exists so the mixin and the per-effect dispatcher in
 * {@code PotionEvents} can detect "the NoClip effect" via {@code instanceof NoClipEffect} or by identity.
 * No lifecycle hooks are overridden because there's no per-tick state to maintain on the cyclic side.
 */
public class NoClipEffect extends CyclicMobEffect {

  public NoClipEffect(MobEffectCategory typeIn, int liquidColorIn) {
    super(typeIn, liquidColorIn);
  }
}
