package com.lothrazar.cyclicmagic.spell;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilSound;
import com.lothrazar.cyclicmagic.util.UtilSpellCaster;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BaseSpell implements ISpell {
  private ResourceLocation icon;
  private int ID;
  private String name;
  protected void init(int id, String n) {
    ID = id;
    name = n;
    icon = new ResourceLocation(Const.MODID, "textures/spells/" + name + ".png");
  }
  public String getName() {
    return UtilChat.lang("spell." + name + ".name");
  }
  public String getUnlocalizedName() {
    return name;
  }
  public String getInfo() {
    return UtilChat.lang("spell." + name + ".info");
  }
  @Override
  public void onCastFailure(World world, EntityPlayer player, BlockPos pos) {
    UtilSound.playSound(player, pos, SoundRegistry.buzzp);
  }
  @Override
  public int getID() {
    return ID;
  }
  @Override
  public boolean canPlayerCast(World world, EntityPlayer player, BlockPos pos) {
    if (player.capabilities.isCreativeMode) { return true; }
    ItemStack wand = UtilSpellCaster.getPlayerWandIfHeld(player);
    if (wand == null) { return false; }
    return true;
  }
  @Override
  public ResourceLocation getIconDisplay() {
    return icon;
  }
}
