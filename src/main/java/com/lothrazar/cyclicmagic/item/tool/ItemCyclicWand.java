package com.lothrazar.cyclicmagic.item.tool;
import java.util.List;
import org.lwjgl.input.Keyboard;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.gui.wand.InventoryWand;
import com.lothrazar.cyclicmagic.registry.SpellRegistry;
import com.lothrazar.cyclicmagic.spell.BaseSpellRange;
import com.lothrazar.cyclicmagic.spell.ISpell;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilSpellCaster;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCyclicWand extends Item implements IHasRecipe, IHasConfig {
  private static final String NBT_SPELLCURRENT = "spell_id";
  private List<ISpell> spellbook;
  public ItemCyclicWand() {
    this.setMaxStackSize(1);
    this.setFull3D();
    this.setContainerItem(this);
  }
  public void setSpells(List<ISpell> spells) {
    this.spellbook = spells;
  }
  public List<ISpell> getSpells() {
    return this.spellbook;
  }
  @Override
  public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
    if (!slotChanged) { return false;// only item data has changed, so do notanimate
    }
    return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
  }
  @Override
  public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
    //Energy.rechargeBy(stack, Energy.START);
    Spells.setSpellCurrent(stack, SpellRegistry.getSpellbook(stack).get(0).getID());
    super.onCreated(stack, worldIn, playerIn);
  }
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    ISpell spell = SpellRegistry.getSpellFromID(Spells.getSpellIDCurrent(stack));
    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
      tooltip.add(TextFormatting.GREEN + spell.getName() + " "
          + "[" + UtilChat.lang(BuildType.getName(stack)) + "] ");
      tooltip.add(TextFormatting.DARK_GRAY + UtilChat.lang("item.cyclic_wand.tooltiprange") + BaseSpellRange.maxRange);
      tooltip.add(TextFormatting.DARK_GRAY + UtilChat.lang("item.cyclic_wand.shifting"));
    }
    else {
      tooltip.add(TextFormatting.DARK_GRAY + UtilChat.lang("item.shift"));
    }
    super.addInformation(stack, playerIn, tooltip, advanced);
  }
  @Override
  @SideOnly(Side.CLIENT)
  public EnumRarity getRarity(ItemStack par1ItemStack) {
    return EnumRarity.UNCOMMON;
  }
  @Override
  public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
      EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    // If onItemUse returns false onItemRightClick will be called.
    // http://www.minecraftforge.net/forum/index.php?topic=31966.0
    // so if this casts and succeeds, the right click is cancelled
    boolean success = UtilSpellCaster.tryCastCurrent(worldIn, playerIn, pos, side, stack, hand);
    return success ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
  }
  @Override
  public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
    // so this only happens IF either onItemUse did not fire at all, or it
    // fired and casting failed
    boolean success = UtilSpellCaster.tryCastCurrent(worldIn, playerIn, null, null, itemStackIn, hand);
    return success ? new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn)
        : new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStackIn);
  }
  @Override
  public int getMaxItemUseDuration(ItemStack stack) {
    return 1; // Without this method, your inventory will NOT work!!!
  }
  public static class Spells {
    public static int getSpellIDCurrent(ItemStack stack) {
      // workaround for default spell being replace. and oncrafting not
      if (UtilNBT.getItemStackNBT(stack).hasKey(NBT_SPELLCURRENT) == false) {
        // what is default spell for that then?
        return SpellRegistry.getSpellbook(stack).get(0).getID();
      }
      int c = UtilNBT.getItemStackNBT(stack).getInteger(NBT_SPELLCURRENT);
      return c;
    }
    public static ISpell getSpellCurrent(ItemStack stack) {
      int idCurrent = getSpellIDCurrent(stack);
      ISpell s = SpellRegistry.getSpellFromID(idCurrent);
      return s;
    }
    public static void setSpellCurrent(ItemStack stack, int spell_id) {
      NBTTagCompound tags = UtilNBT.getItemStackNBT(stack);
      tags.setInteger(NBT_SPELLCURRENT, spell_id);
      stack.setTagCompound(tags);
    }
  }
  public enum BuildType {
    FIRST, ROTATE, RANDOM;
    private final static String NBT = "build";
    private final static String NBT_SLOT = "buildslot";
    private final static String NBT_SIZE = "buildsize";
    public static String getName(ItemStack wand) {
      try {
        NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
        return "button.build." + BuildType.values()[tags.getInteger(NBT)].toString().toLowerCase();
      }
      catch (Exception e) {
        return "button.build." + FIRST.toString().toLowerCase();
      }
    }
    public static int get(ItemStack wand) {
      if (wand == null) { return 0; }
      NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
      return tags.getInteger(NBT);
    }
    public static void toggle(ItemStack wand) {
      NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
      int type = tags.getInteger(NBT);
      type++;
      if (type > RANDOM.ordinal()) {
        type = FIRST.ordinal();
      }
      tags.setInteger(NBT, type);
      wand.setTagCompound(tags);
      int slot = getSlot(wand);
      if (InventoryWand.getFromSlot(wand, slot) == null || InventoryWand.getToPlaceFromSlot(wand, slot) == null) {
        //try to move away from empty slot
        setNextSlot(wand);
      }
    }
    public static int getBuildSize(ItemStack wand) {
      NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
      int s = tags.getInteger(NBT_SIZE);
      return s;
    }
    public static void setBuildSize(ItemStack wand, int size) {
      NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
      tags.setInteger(NBT_SIZE, size);
      wand.setTagCompound(tags);
    }
    public static int getSlot(ItemStack wand) {
      NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
      if (!tags.hasKey(NBT_SLOT)) {
        resetSlot(wand);
        return 0;
      }
      return tags.getInteger(NBT_SLOT);
    }
    public static void resetSlot(ItemStack wand) {
      NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
      tags.setInteger(NBT_SLOT, 0);
    }
    public static void setNextSlot(ItemStack wand) {
      NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
      int prev = getSlot(wand);
      int next = InventoryWand.calculateSlotCurrent(wand);
      if (prev != next)
        tags.setInteger(NBT_SLOT, next);
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.items;
    SpellRegistry.renderOnLeft = config.getBoolean("Build Scepter HUD", category, true, "Cyclic Scepter: True for top left of the screen, false for top right");
    SpellRegistry.doParticles = config.getBoolean("Build Scepter Particles", category, false, "Cyclic Scepter: Set to false to disable particles");
    category = Const.ConfigCategory.modpackMisc;
    BaseSpellRange.maxRange = config.getInt("Build Scepter Max Range", category, 64, 8, 128, "Cyclic Scepter: Maximum range for all spells");
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this),
        "sds",
        " o ",
        "gog",
        'd', new ItemStack(Blocks.DIAMOND_BLOCK),
        'g', Blocks.QUARTZ_BLOCK,
        'o', Blocks.OBSIDIAN,
        's', Blocks.END_STONE);
  }
}
