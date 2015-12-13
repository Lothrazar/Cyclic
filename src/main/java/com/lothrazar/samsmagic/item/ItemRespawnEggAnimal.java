package com.lothrazar.samsmagic.item;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityList.EntityEggInfo;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item; 
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRespawnEggAnimal extends Item  //mimic/mirror of ItemMonsterPlacer
{
	public ItemRespawnEggAnimal()
    { 
        this.setHasSubtypes(true); 
    }

    public String getItemStackDisplayName(ItemStack stack)
    {
        String s = ("" + StatCollector.translateToLocal(this.getUnlocalizedName() + ".name")).trim();
        String s1 = EntityList.getStringFromID(stack.getMetadata());

        if (s1 != null)
        {
            s = s + " " + StatCollector.translateToLocal("entity." + s1 + ".name");
        }

        return s;
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int renderPass)
    {
        EntityList.EntityEggInfo entityegginfo = (EntityList.EntityEggInfo)EntityList.entityEggs.get(Integer.valueOf(stack.getMetadata()));
   
        int c = entityegginfo != null ? (renderPass == 0 ? entityegginfo.primaryColor : entityegginfo.secondaryColor) : 16777215;
         
        return c;
    }

    /**
     * Called when a Block is right-clicked with this Item
     *  
     * @param pos The block being right-clicked
     * @param side The side being right-clicked
     */
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else if (!playerIn.canPlayerEdit(pos.offset(side), side, stack))
        {
            return false;
        }
        else
        {
            IBlockState iblockstate = worldIn.getBlockState(pos);
            //this is where we disabled the interaction with monster spawners
            pos = pos.offset(side);
            double offsetY = 0.0D;

            if (side == EnumFacing.UP && iblockstate instanceof BlockFence)
            {
                offsetY = 0.5D;
            }
 
            Entity entity = spawnCreature(worldIn, stack.getMetadata(), (double)pos.getX() + 0.5D, (double)pos.getY() + offsetY, (double)pos.getZ() + 0.5D);

            if (entity != null)
            {
                if (entity instanceof EntityLivingBase && stack.hasDisplayName())
                {
                    entity.setCustomNameTag(stack.getDisplayName());
                }

                if (!playerIn.capabilities.isCreativeMode)
                {
                    --stack.stackSize;
                }
            }

            return true;
        }
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
        if (worldIn.isRemote)
        {
            return itemStackIn;
        }
        else
        {
            MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(worldIn, playerIn, true);

            if (movingobjectposition == null)
            {
                return itemStackIn;
            }
            else
            {
                if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
                {
                    BlockPos blockpos = movingobjectposition.getBlockPos();

                    if (!worldIn.isBlockModifiable(playerIn, blockpos))
                    {
                        return itemStackIn;
                    }

                    if (!playerIn.canPlayerEdit(blockpos, movingobjectposition.sideHit, itemStackIn))
                    {
                        return itemStackIn;
                    }

                    if (worldIn.getBlockState(blockpos).getBlock() instanceof BlockLiquid)
                    {
                        Entity entity = spawnCreature(worldIn, itemStackIn.getMetadata(), (double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.5D, (double)blockpos.getZ() + 0.5D);

                        if (entity != null)
                        {
                            if (entity instanceof EntityLivingBase && itemStackIn.hasDisplayName())
                            {
                                ((EntityLiving)entity).setCustomNameTag(itemStackIn.getDisplayName());
                            }

                            if (!playerIn.capabilities.isCreativeMode)
                            {
                                --itemStackIn.stackSize;
                            }

                          //  playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
                        }
                    }
                }

                return itemStackIn;
            }
        }
    }

    /**
     * Spawns the creature specified by the egg's type in the location specified by the last three parameters.
     * Parameters: world, entityID, x, y, z.
     */
    public static Entity spawnCreature(World worldIn, int entityID, double x, double y, double z)
    {
        if (!EntityList.entityEggs.containsKey(Integer.valueOf(entityID)))
        {
            return null;
        }
        else
        {
            Entity entity = null;

            for (int j = 0; j < 1; ++j)
            {
                entity = EntityList.createEntityByID(entityID, worldIn);

                if (entity instanceof EntityLivingBase)
                {
                    EntityLiving entityliving = (EntityLiving)entity;
                    entity.setLocationAndAngles(x, y, z, MathHelper.wrapAngleTo180_float(worldIn.rand.nextFloat() * 360.0F), 0.0F);
                    entityliving.rotationYawHead = entityliving.rotationYaw;
                    entityliving.renderYawOffset = entityliving.rotationYaw;
                   
                    entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityliving)), (IEntityLivingData)null);
                    worldIn.spawnEntityInWorld(entity);
                    entityliving.playLivingSound();
                }
            }

            return entity;
        }
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     *  
     * @param subItems The List of sub-items. This is a List of ItemStacks.
     */
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
    {
        Iterator<EntityEggInfo> iterator = EntityList.entityEggs.values().iterator();

        while (iterator.hasNext())
        {
        	//TODO: how to fix id??
            EntityList.EntityEggInfo entityegginfo = (EntityList.EntityEggInfo)iterator.next();
            subItems.add(new ItemStack(itemIn, 1, entityegginfo.spawnedID));
        }
        
    } 
}