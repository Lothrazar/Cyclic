package com.lothrazar.cyclicmagic.enchantment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.registry.GuideRegistry;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantBeheading extends EnchantBase {
  private Map<String, String> mapMHF = new HashMap<String, String>();
  public EnchantBeheading() {
    super("beheading", Rarity.RARE, EnumEnchantmentType.DIGGER, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND });
    GuideRegistry.register(this, new ArrayList<String>());
    mapMHF.put("net.minecraft.entity.monster.EntityBlaze", "MHF_Blaze");
    mapMHF.put("net.minecraft.entity.monster.EntityCaveSpider", "MHF_CaveSpider");
    mapMHF.put("net.minecraft.entity.passive.EntityChicken", "MHF_Chicken");
    mapMHF.put("net.minecraft.entity.passive.EntityCow", "MHF_Cow");
    mapMHF.put("net.minecraft.entity.monster.EntityEnderman", "MHF_Enderman");
    mapMHF.put("net.minecraft.entity.monster.EntityGhast", "MHF_Ghast");
    mapMHF.put("net.minecraft.entity.monster.EntityIronGolem", "MHF_Golem");
    mapMHF.put("net.minecraft.entity.monster.EntitySlime", "MHF_LavaSlime");
    mapMHF.put("net.minecraft.entity.passive.EntityMooshroom", "MHF_MushroomCow");
    mapMHF.put("net.minecraft.entity.passive.EntityOcelot", "MHF_Ocelot");
    mapMHF.put("net.minecraft.entity.passive.EntityPig", "MHF_Pig");
    mapMHF.put("net.minecraft.entity.monster.EntityPigZombie", "MHF_PigZombie");
    mapMHF.put("net.minecraft.entity.passive.EntitySheep", "MHF_Sheep");
    mapMHF.put("net.minecraft.entity.monster.EntitySlime", "MHF_Slime");
    mapMHF.put("net.minecraft.entity.monster.EntitySpider", "MHF_Spider");
    mapMHF.put("net.minecraft.entity.passive.EntitySquid", "MHF_Squid");
    mapMHF.put("net.minecraft.entity.passive.EntityVillager", "MHF_Villager");
    mapMHF.put("net.minecraft.entity.monster.EntityWitherSkeleton", "MHF_WSkeleton");
//  mapMHF.put("", "MHF_Creeper");
//  mapMHF.put("", "MHF_Herobrine");
    //    mapMHF.put("", "MHF_Skeleton");
    //    mapMHF.put("", "MHF_Skeleton");
    //    mapMHF.put("net.minecraft.entity.monster.EntityZombie", "MHF_Zombie");
    mapMHF.put("net.minecraft.entity.monster.EntityBat", "MHF_Bat");//??? might be missing
    mapMHF.put("net.minecraft.entity.monster.EntityWitch", "MHF_Witch");//also might be missing
    //TODO: LOAD IN CONFIGS or modded monsters with this format (class-playername)
    //use examples for like quark stuff-  vazkii
    //llama, mule, horses, illager and friends,  guardian, guardian boss, shulker, silverfish
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
      if (level > 0) {
        World world = attacker.world;
        BlockPos pos = target.getPosition();
        //http://minecraft.gamepedia.com/Player.dat_format#Player_Heads
        //first the hardcoded supported ones
        if (target instanceof EntityCreeper) {//4
          UtilItemStack.dropItemStackInWorld(world, pos, new ItemStack(Items.SKULL, 1, Const.skull_creeper));
          return;
        }
        else if (target instanceof EntityZombie) {//2
          UtilItemStack.dropItemStackInWorld(world, pos, new ItemStack(Items.SKULL, 1, Const.skull_zombie));
          return;
        }
        else if (target instanceof EntitySkeleton) {//0
          UtilItemStack.dropItemStackInWorld(world, pos, new ItemStack(Items.SKULL, 1, Const.skull_skeleton));
          return;
        }
        else if (target instanceof EntityWitherSkeleton) {//1
          UtilItemStack.dropItemStackInWorld(world, pos, new ItemStack(Items.SKULL, 1, Const.skull_wither));
          return;
        }
        else if (target instanceof EntityDragon) {//5
          UtilItemStack.dropItemStackInWorld(world, pos, new ItemStack(Items.SKULL, 1, Const.skull_dragon));
          return;
        }
        else if (target instanceof EntityPlayer) {//player name
          UtilItemStack.dropItemStackInWorld(world, pos, UtilNBT.buildNamedPlayerSkull((EntityPlayer) target));
          return;
        }
        String key = target.getClass().getName();
        if (mapMHF.containsKey(key)) {
          UtilItemStack.dropItemStackInWorld(world, pos, UtilNBT.buildNamedPlayerSkull(mapMHF.get(key)));
        }
        else {
          
          ModCyclic.logger.info("beheading NOT FOUND " + target.getClass().getName());
        }
        //we have
        //noooooooooooop didntt work 
        //        ItemStack skull = new ItemStack(Items.SKULL, 1, Const.skull_player);
        ////        if (skull.getTagCompound() == null) {
        ////          skull.setTagCompound(new NBTTagCompound());
        ////        }
        //        NBTTagCompound base =     new NBTTagCompound();
        //        NBTTagCompound skullOwner =     new NBTTagCompound();
        //        skullOwner.setString("Id", "e2d4c388-42d5-4a96-b4c9-623df7f5e026");
        //        skullOwner.setString("LocName", "Bat Head");
        //        base.setTag(Const.SkullOwner, skullOwner);
        //        skull.setTagCompound(base);
        //        
        //UtilItemStack.dropItemStackInWorld(attacker.world, target.getPosition(), skull);
 
      }
    }
  }
}
