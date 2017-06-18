package com.lothrazar.cyclicmagic.enchantment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantBeheading extends EnchantBase {
  private Map<String, String> mapClassToSkin;
  private Map<String, NBTTagCompound> mapClassToTag;
  public EnchantBeheading() {
    super("beheading", Rarity.RARE, EnumEnchantmentType.DIGGER, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND });
    GuideRegistry.register(this, new ArrayList<String>());
    buildDefaultHeadList();
  }
  private void buildDefaultHeadList() {
    mapClassToSkin = new HashMap<String, String>();
    mapClassToTag = new HashMap<String, NBTTagCompound>();
    //mhf https://twitter.com/Marc_IRL/status/542330244473311232  https://pastebin.com/5mug6EBu
    mapClassToSkin.put("net.minecraft.entity.monster.EntityBlaze", "MHF_Blaze");
    mapClassToSkin.put("net.minecraft.entity.monster.EntityCaveSpider", "MHF_CaveSpider");
    mapClassToSkin.put("net.minecraft.entity.passive.EntityChicken", "MHF_Chicken");
    mapClassToSkin.put("net.minecraft.entity.passive.EntityCow", "MHF_Cow");
    mapClassToSkin.put("net.minecraft.entity.monster.EntityEnderman", "MHF_Enderman");
    mapClassToSkin.put("net.minecraft.entity.monster.EntityGhast", "MHF_Ghast");
    mapClassToSkin.put("net.minecraft.entity.monster.EntityIronGolem", "MHF_Golem");
    mapClassToSkin.put("net.minecraft.entity.monster.EntitySlime", "MHF_LavaSlime");
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
    mapClassToSkin.put("net.minecraft.entity.monster.EntityWitch", "MHF_Witch");//not in list but working
//    mapClassToSkin.put("net.minecraft.entity.passive.EntityBat", "MHF_Bat");//??? might be missing
    //other https://www.planetminecraft.com/blog/minecraft-playerheads-2579899/
    mapClassToSkin.put("net.minecraft.entity.boss.EntityGuardian", "Guardian");
    mapClassToSkin.put("net.minecraft.entity.boss.EntityElderGuardian", "Guardian");
    //husk silverfish snowman shulker polar bear
    //llama, mule, horses, illager Vinidcator, vex, stray,    silverfish
    //TODO: LOAD IN CONFIGS or modded monsters with this format (class-playername)
    //use examples for like quark stuff-  vazkii
    //NBT image data from  http://www.minecraft-heads.com/custom/heads/animals/6746-llama
    //EntityLlama
    mapClassToTag.put("net.minecraft.entity.passive.EntityBat", UtilNBT.buildCustomSkull("LLama", "e2d4c388-42d5-4a96-b4c9-623df7f5e026", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGNmMWIzYjNmNTM5ZDJmNjNjMTcyZTk0Y2FjZmFhMzkxZThiMzg1Y2RkNjMzZjNiOTkxYzc0ZTQ0YjI4In19fQ=="));
  }
  @Override
  public int getMaxLevel() {
    return 1;
  }
  @SubscribeEvent
  public void onEntityKill(LivingDeathEvent event) {
    //ONLY FOR DEBUGGING IS THIS HERE    buildDefaultHeadList();
    if (event.getSource().getSourceOfDamage() instanceof EntityPlayer && event.getEntity() instanceof EntityLivingBase) {
      EntityPlayer attacker = (EntityPlayer) event.getSource().getSourceOfDamage();
      EntityLivingBase target = (EntityLivingBase) event.getEntity();
      int level = getCurrentLevelTool(attacker);
      if (level > 0) {
        World world = attacker.world;
        BlockPos pos = target.getPosition();
        String key = target.getClass().getName();
        //http://minecraft.gamepedia.com/Player.dat_format#Player_Heads
        //first the hardcoded supported ones
        if (target instanceof EntityCreeper) {//4
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
        else if (mapClassToSkin.containsKey(key)) {
          UtilItemStack.dropItemStackInWorld(world, pos, UtilNBT.buildNamedPlayerSkull(mapClassToSkin.get(key)));
        }
        else if (mapClassToTag.containsKey(key)) {
          
          UtilItemStack.dropItemStackInWorld(world, pos,UtilNBT.buildSkullFromTag(mapClassToTag.get(key)) );
        }
        else {
          ModCyclic.logger.info("beheading NOT FOUND " + target.getClass().getName());
        }
        //we have
        //noooooooooooop didntt work 
        //UtilItemStack.dropItemStackInWorld(attacker.world, target.getPosition(), skull);
      }
    }
  }
}
