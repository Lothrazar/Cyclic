package com.lothrazar.cyclic.util;

import net.minecraft.core.HolderLookup;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.function.Supplier;

/**
 *
 * Client setup populates {@link #CLIENT_LOOKUP} via FMLClientSetupEvent so this class
 * never directly references any client-only types, keeping it safe to load on dedicated server.
 */
public class RegistryHolder {

  // Set by the client during FMLClientSetupEvent. Returns null on server.
  public static volatile Supplier<HolderLookup.Provider> CLIENT_LOOKUP = () -> null;


  /**
   *
   * Provides a HolderLookup.Provider that works on both sides:
   * - Server: ServerLifecycleHooks.getCurrentServer().registryAccess()
   * - Client (including dedicated-server clients): supplied by a client-side init hook
   */
  public static HolderLookup.Provider get() {
    var server = ServerLifecycleHooks.getCurrentServer();
    if (server != null) {
      return server.registryAccess();
    }
    return CLIENT_LOOKUP.get();
  }

}
