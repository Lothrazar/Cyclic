package com.lothrazar.cyclic.capabilities;
/**
 * MASHUP of 'Mana' chunk stuff and Playermana
 * 
 * @author lothr
 *
 */
public class ClientDataManager {

  //TODO: objects
  private static int playerMana;
  private static int chunkMana;

  public static void set(int playerMana, int chunkMana) {
    ClientDataManager.playerMana = playerMana;
    ClientDataManager.chunkMana = chunkMana;
  }

  public static int getPlayerMana() {
    return playerMana;
  }

  public static int getChunkMana() {
    return chunkMana;
  }
}