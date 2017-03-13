package com.lothrazar.cyclicmagic.enchantment;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantXpBoost extends EnchantBase {
  private static final int XP_PER_LVL = 4;
  public EnchantXpBoost() {
    super(Rarity.VERY_RARE, EnumEnchantmentType.DIGGER, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND });
    this.setName("expboost");
  }
  @Override
  public int getMaxLevel() {
    return 3;
  }
  @SubscribeEvent
  public void onEntityKill(LivingDeathEvent event) {

    if(event.getSource() == null){return;}
    if (event.getSource().getSourceOfDamage() instanceof EntityPlayer && event.getEntity() instanceof EntityLivingBase) {
      EntityPlayer attacker = (EntityPlayer) event.getSource().getSourceOfDamage();
      int level = getCurrentLevelTool(attacker);
      if (level <= 0) { return; }
      EntityLivingBase target = (EntityLivingBase) event.getEntity();
      World world = attacker.getEntityWorld();
      BlockPos pos = target.getPosition();
      dropExp(world, pos, XP_PER_LVL * XP_PER_LVL * level);
    }
  }
  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void onBreakEvent(BreakEvent event) {
    World world = event.getWorld();
    EntityPlayer player = event.getPlayer();
    if(player == null){return;}
    BlockPos pos = event.getPos();
    int level = this.getCurrentLevelTool(player);
    if (level <= 0) { return; }
    Block block = event.getState().getBlock();
    int xpDropped = block.getExpDrop(event.getState(), world, pos, 0);
    dropExp(world, pos, xpDropped * XP_PER_LVL * level);
  }
  private void dropExp(World world, BlockPos pos, int xp) {
    if (world.isRemote == false) {
      EntityXPOrb orb = new EntityXPOrb(world);
      orb.setPositionAndUpdate(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
      orb.xpValue = xp;
      world.spawnEntityInWorld(orb);
    }
  }
}
