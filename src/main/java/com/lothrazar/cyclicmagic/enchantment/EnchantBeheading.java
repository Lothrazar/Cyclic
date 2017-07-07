package com.lothrazar.cyclicmagic.enchantment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.registry.GuideRegistry;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantBeheading extends EnchantBase implements IHasConfig {
  private Map<String, String> mapClassToSkin;
  private Map<String, NBTTagCompound> mapClassToTag;
  private int percentDrop;
  public EnchantBeheading() {
    super("beheading", Rarity.VERY_RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND });
    GuideRegistry.register(this, new ArrayList<String>());
    buildDefaultHeadList();
  }
  private void buildDefaultHeadList() {
    mapClassToSkin = new HashMap<String, String>();
    mapClassToTag = new HashMap<String, NBTTagCompound>();
    //http://minecraft.gamepedia.com/Player.dat_format#Player_Heads
    //mhf https://twitter.com/Marc_IRL/status/542330244473311232  https://pastebin.com/5mug6EBu
    //other https://www.planetminecraft.com/blog/minecraft-playerheads-2579899/
    //NBT image data from  http://www.minecraft-heads.com/custom/heads/animals/6746-llama
    mapClassToSkin.put("net.minecraft.entity.monster.EntityBlaze", "MHF_Blaze");
    mapClassToSkin.put("net.minecraft.entity.monster.EntityCaveSpider", "MHF_CaveSpider");
    mapClassToSkin.put("net.minecraft.entity.passive.EntityChicken", "MHF_Chicken");
    mapClassToSkin.put("net.minecraft.entity.passive.EntityCow", "MHF_Cow");
    mapClassToSkin.put("net.minecraft.entity.monster.EntityEnderman", "MHF_Enderman");
    mapClassToSkin.put("net.minecraft.entity.monster.EntityGhast", "MHF_Ghast");
    mapClassToSkin.put("net.minecraft.entity.monster.EntityIronGolem", "MHF_Golem");
    mapClassToSkin.put("net.minecraft.entity.monster.EntityMagmaCube", "MHF_LavaSlime");
    mapClassToSkin.put("net.minecraft.entity.passive.EntityMooshroom", "MHF_MushroomCow");
    mapClassToSkin.put("net.minecraft.entity.passive.EntityOcelot", "MHF_Ocelot");
    mapClassToSkin.put("net.minecraft.entity.passive.EntityPig", "MHF_Pig");
    mapClassToSkin.put("net.minecraft.entity.monster.EntityPigZombie", "MHF_PigZombie");
    mapClassToSkin.put("net.minecraft.entity.passive.EntitySheep", "MHF_Sheep");
    mapClassToSkin.put("net.minecraft.entity.monster.EntitySlime", "MHF_Slime");
    mapClassToSkin.put("net.minecraft.entity.monster.EntitySpider", "MHF_Spider");
    mapClassToSkin.put("net.minecraft.entity.passive.EntitySquid", "MHF_Squid");
    mapClassToSkin.put("net.minecraft.entity.passive.EntityVillager", "MHF_Villager");
    mapClassToSkin.put("net.minecraft.entity.monster.EntityWitherSkeleton", "MHF_WSkeleton");
    mapClassToSkin.put("net.minecraft.entity.boss.EntityWither", "MHF_Wither");
    mapClassToSkin.put("net.minecraft.entity.monster.EntityWitch", "MHF_Witch");
    mapClassToSkin.put("net.minecraft.entity.passive.EntityWolf", "MHF_Wolf");
    mapClassToSkin.put("net.minecraft.entity.boss.EntityGuardian", "MHF_Guardian");
    mapClassToSkin.put("net.minecraft.entity.boss.EntityElderGuardian", "MHF_Guardian");
    mapClassToSkin.put("net.minecraft.entity.monster.EntitySnowman", "MHF_SnowGolem");
    mapClassToSkin.put("net.minecraft.entity.monster.EntitySilverfish", "MHF_Silverfish");
    mapClassToSkin.put("net.minecraft.entity.monster.EntityEndermite", "MHF_Endermite");
    mapClassToTag.put("net.minecraft.entity.monster.EntityZombieVillager", UtilNBT.buildCustomSkullTag("Zombie Villager Head", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2ZmMDQ4MmZkMzJmYWIyY2U5ZjVmYTJlMmQ5YjRkYzc1NjFkYTQyMjE1MmM5OWZjODA0YjkxMzljYWYyNTZiIn19fQ=="));
    mapClassToTag.put("net.minecraft.entity.monster.EntityVindicator", UtilNBT.buildCustomSkullTag("Vindicator Head", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTAwZDNmZmYxNmMyZGNhNTliOWM1OGYwOTY1MjVjODY5NzExNjZkYmFlMTMzYjFiMDUwZTVlZTcxNjQ0MyJ9fX0="));
    mapClassToTag.put("net.minecraft.entity.monster.EntityEvoker", UtilNBT.buildCustomSkullTag("Evoker Head", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTAwZDNmZmYxNmMyZGNhNTliOWM1OGYwOTY1MjVjODY5NzExNjZkYmFlMTMzYjFiMDUwZTVlZTcxNjQ0MyJ9fX0="));
    mapClassToTag.put("net.minecraft.entity.monster.EntityShulker", UtilNBT.buildCustomSkullTag("Shulker Head", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDhmYjk2YmY0YTlhMzFiMjU1MzhiNzEyMTdkYThiNjM0ZThjMDVkNGMzNWE2YWI4N2FjYjM3ZjkzYTZmMmMifX19"));
    mapClassToTag.put("net.minecraft.entity.passive.EntityRabbit", UtilNBT.buildCustomSkullTag("Rabbit Head", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGM3YTMxN2VjNWMxZWQ3Nzg4Zjg5ZTdmMWE2YWYzZDJlZWI5MmQxZTk4NzljMDUzNDNjNTdmOWQ4NjNkZTEzMCJ9fX0="));
    mapClassToTag.put("net.minecraft.entity.monster.EntityPolarBear", UtilNBT.buildCustomSkullTag("Polar Bear Head", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDQ2ZDIzZjA0ODQ2MzY5ZmEyYTM3MDJjMTBmNzU5MTAxYWY3YmZlODQxOTk2NjQyOTUzM2NkODFhMTFkMmIifX19"));
    mapClassToTag.put("net.minecraft.entity.passive.EntityLlama", UtilNBT.buildCustomSkullTag("Llama Head", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGNmMWIzYjNmNTM5ZDJmNjNjMTcyZTk0Y2FjZmFhMzkxZThiMzg1Y2RkNjMzZjNiOTkxYzc0ZTQ0YjI4In19fQ=="));
    mapClassToTag.put("net.minecraft.entity.passive.EntityBat", UtilNBT.buildCustomSkullTag("Bat Head", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzJiMWVjZmY3N2ZmZTNiNTAzYzMwYTU0OGViMjNhMWEwOGZhMjZmZDY3Y2RmZjM4OTg1NWQ3NDkyMTM2OCJ9fX0="));
    NBTTagCompound horseTag = UtilNBT.buildCustomSkullTag("Horse Head", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjE5MDI4OTgzMDg3MzBjNDc0NzI5OWNiNWE1ZGE5YzI1ODM4YjFkMDU5ZmU0NmZjMzY4OTZmZWU2NjI3MjkifX19");
    mapClassToTag.put("net.minecraft.entity.passive.AbstractHorse", horseTag);
    mapClassToTag.put("net.minecraft.entity.passive.EntityHorse", horseTag);
    mapClassToTag.put("net.minecraft.entity.passive.EntitySkeletonHorse", horseTag);
    mapClassToTag.put("net.minecraft.entity.passive.EntityZombieHorse", horseTag);
  }
  @Override
  public int getMaxLevel() {
    return 1;
  }
  @SubscribeEvent
  public void onEntityKill(LivingDeathEvent event) {
    if (event.getSource().getSourceOfDamage() instanceof EntityPlayer && event.getEntity() instanceof EntityLivingBase) {
      EntityPlayer attacker = (EntityPlayer) event.getSource().getSourceOfDamage();
      EntityLivingBase target = (EntityLivingBase) event.getEntity();
      int level = getCurrentLevelTool(attacker);
      if (level < 0) { return; }
      World world = attacker.world;
      if (MathHelper.getInt(world.rand, 0, 100) > this.percentDrop) { return; }
      //else the random number was less than 10, so it passed the 10% chance req
      BlockPos pos = target.getPosition();
      String key = target.getClass().getName();
      ////we allow all these, which include config, to override the vanilla skulls below
      if (mapClassToSkin.containsKey(key)) {
        UtilItemStack.dropItemStackInWorld(world, pos, UtilNBT.buildNamedPlayerSkull(mapClassToSkin.get(key)));
      }
      else if (mapClassToTag.containsKey(key)) {
        UtilItemStack.dropItemStackInWorld(world, pos, UtilNBT.buildSkullFromTag(mapClassToTag.get(key)));
      }
      else if (target instanceof EntityCreeper) {//4
        UtilItemStack.dropItemStackInWorld(world, pos, new ItemStack(Items.SKULL, 1, Const.skull_creeper));
      }
      else if (target instanceof EntityZombie) {//2
        UtilItemStack.dropItemStackInWorld(world, pos, new ItemStack(Items.SKULL, 1, Const.skull_zombie));
      }
      else if (target instanceof EntitySkeleton) {//0
        UtilItemStack.dropItemStackInWorld(world, pos, new ItemStack(Items.SKULL, 1, Const.skull_skeleton));
      }
      else if (target instanceof EntityWitherSkeleton) {//1
        UtilItemStack.dropItemStackInWorld(world, pos, new ItemStack(Items.SKULL, 1, Const.skull_wither));
      }
      else if (target instanceof EntityDragon) {//5
        UtilItemStack.dropItemStackInWorld(world, pos, new ItemStack(Items.SKULL, 1, Const.skull_dragon));
      }
      else if (target instanceof EntityPlayer) {//player name
        UtilItemStack.dropItemStackInWorld(world, pos, UtilNBT.buildNamedPlayerSkull((EntityPlayer) target));
      }
      else {
        ModCyclic.logger.info("beheading mob not found " + target.getClass().getName());
        ModCyclic.logger.info("beheading mob not found " + target.getClass().getName());
        ModCyclic.logger.info("beheading mob not found " + target.getClass().getName());
        ModCyclic.logger.info("beheading mob not found " + target.getClass().getName());
        ModCyclic.logger.info("beheading mob not found " + target.getClass().getName());
      }
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    this.percentDrop = config.getInt("BeheadingPercent", Const.ConfigCategory.modpackMisc, 10, 1, 100, "Percent chance that the beheading enchant will actually drop a head.");
    String[] defaultConf = new String[] {
        "net.minecraft.entity.monster.EntityVex-Vazkii", "elucent.roots.entity.EntitySprite-Darkosto" };
    String[] mappings = config.getStringList("BeheadingExtraMobs", Const.ConfigCategory.modpackMisc, defaultConf, "By default Beheading works on vanilla mobs and player heads.  Add creatures from any other mod here along with a player name to act as the skin for the dropped head.  Format is: classpath-player");
    for (String s : mappings) {
      if (s.contains("-")) {
        String[] spl = s.split("-");
        if (spl.length == 2) {
          mapClassToSkin.put(spl[0], spl[1]);
        }
      }
    }
  }
}
