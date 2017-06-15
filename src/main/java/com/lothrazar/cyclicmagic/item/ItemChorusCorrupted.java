package com.lothrazar.cyclicmagic.item;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry.IPlayerExtendedProperties;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemChorusCorrupted extends ItemFood implements IHasRecipe, IHasConfig {
  //revived from https://github.com/PrinceOfAmber/Cyclic/blob/d2f91d1f97b9cfba47786a30b427fbfdcd714212/src/main/java/com/lothrazar/cyclicmagic/spell/SpellGhost.java
  public static int GHOST_SECONDS = 5;
  public static int POTION_SECONDS = 20;
  private static final int numFood = 2;
  public ItemChorusCorrupted() {
    super(numFood, false);
    this.setAlwaysEdible();
  }
  @Override
  protected void onFoodEaten(ItemStack par1ItemStack, World world, EntityPlayer player) {
    setPlayerGhostMode(player, world);
  }
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltips, boolean advanced) {
    tooltips.add(UtilChat.lang(this.getUnlocalizedName() + ".tooltip"));
  }
  @Override
  public IRecipe addRecipe() {
    return GameRegistry.addShapedRecipe(new ItemStack(this, 3),
        "lal", "lal", "lal",
        'l', Items.FERMENTED_SPIDER_EYE,
        'a', Items.CHORUS_FRUIT);
  }
  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.modpackMisc;
    GHOST_SECONDS = config.getInt("CorruptedChorusSeconds", category, 5, 1, 60, "How long you can noclip after eating corrupted chorus");
    POTION_SECONDS = config.getInt("CorruptedChorusPotions", category, 10, 1, 60, "How long the negative potion effects last after a corrupted chorus teleports you");
  }
  private void setPlayerGhostMode(EntityPlayer player, World par2World) {
    if (par2World.isRemote == false) {
      player.setGameType(GameType.SPECTATOR);
      IPlayerExtendedProperties props = CapabilityRegistry.getPlayerProperties(player);
      props.setChorusTimer(GHOST_SECONDS * Const.TICKS_PER_SEC);
      props.setChorusOn(true);
      props.setChorusStart(player.getPosition());
      props.setChorusDim(player.dimension);
      //      UtilNBT.incrementPlayerIntegerNBT(player, KEY_TIMER, GHOST_SECONDS * Const.TICKS_PER_SEC);
      //      player.getEntityData().setBoolean(KEY_BOOLEAN, true);
      //      player.getEntityData().setString(KEY_EATLOC, UtilNBT.posToStringCSV(player.getPosition()));
      //      player.getEntityData().setInteger(KEY_EATDIM, player.dimension);
    }
  }
  @SubscribeEvent
  public void onPlayerUpdate(LivingUpdateEvent event) {
    if (event.getEntityLiving() instanceof EntityPlayer == false) { return; }
    EntityPlayer player = (EntityPlayer) event.getEntityLiving();
    World world = player.getEntityWorld();
    IPlayerExtendedProperties props = CapabilityRegistry.getPlayerProperties(player);
    if (props.getChorusOn()) {
      int playerGhost = props.getChorusTimer();
      if (playerGhost > 0) {
        if (playerGhost % Const.TICKS_PER_SEC == 0) {
          int secs = playerGhost / Const.TICKS_PER_SEC;
          UtilChat.addChatMessage(player, "" + secs);
        }
        props.setChorusTimer(playerGhost - 1);
      }
      else {
        //times up!
        props.setChorusOn(false);
        //        player.getEntityData().setBoolean(KEY_BOOLEAN, false);
        if (props.getChorusDim() != player.dimension) {
          // if the player changed dimension while a ghost, thats not
          // allowed. dont tp them back
          player.setGameType(GameType.SURVIVAL);
          player.attackEntityFrom(DamageSource.MAGIC, 50);
        }
        else {
          // : teleport back to source
          //          String posCSV = player.getEntityData().getString(KEY_EATLOC);
          //          String[] p = posCSV.split(",");
          BlockPos currentPos = player.getPosition();
          BlockPos sourcePos = props.getChorusStart();//new BlockPos(Double.parseDouble(p[0]), Double.parseDouble(p[1]), Double.parseDouble(p[2]));
          if (world.isAirBlock(currentPos) && world.isAirBlock(currentPos.up())) {
            //then we can stay, but add nausea
            player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, Const.TICKS_PER_SEC * POTION_SECONDS));
            player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, Const.TICKS_PER_SEC * POTION_SECONDS));
          }
          else {
            //teleport back home	
            UtilEntity.teleportWallSafe(player, world, sourcePos);
            //player.setPositionAndUpdate(Double.parseDouble(p[0]), Double.parseDouble(p[1]), Double.parseDouble(p[2]));
          }
          player.fallDistance = 0.0F;
          player.setGameType(GameType.SURVIVAL);
        }
      }
    }
  }
}
