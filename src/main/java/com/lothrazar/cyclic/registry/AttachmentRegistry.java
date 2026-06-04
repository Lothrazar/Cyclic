package com.lothrazar.cyclic.registry;

import java.util.function.Supplier;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.capabilities.livingentity.LivingEntityCapabilityStorage;
import com.lothrazar.cyclic.capabilities.player.PlayerCyclicAttachment;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class AttachmentRegistry {

  public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
      DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, ModCyclic.MODID);

  public static final Supplier<AttachmentType<LivingEntityCapabilityStorage>> ANTI_BEACON_TARGET =
      ATTACHMENT_TYPES.register("anti_beacon_target",
          () -> AttachmentType.builder(LivingEntityCapabilityStorage::new).build());

  public static final Supplier<AttachmentType<PlayerCyclicAttachment>> CYCLIC_PLAYER =
      ATTACHMENT_TYPES.register("cyclic_player",
          () -> AttachmentType.builder(PlayerCyclicAttachment::new)
              .serialize(PlayerCyclicAttachment.CODEC)
              .copyOnDeath()
              .build());
}
