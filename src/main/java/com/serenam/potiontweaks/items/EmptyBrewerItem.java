package com.serenam.potiontweaks.items;

import com.serenam.potiontweaks.registry.ModBlocks;
import com.serenam.potiontweaks.registry.ModItems;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EmptyBrewerItem extends BlockItem
{
    public EmptyBrewerItem()
    {
        super(ModBlocks.EMPTY_BREWER, new FabricItemSettings().group(ItemGroup.MATERIALS));
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand)
    {
        if (entity.getType() != EntityType.VILLAGER)
        {
            return ActionResult.PASS;
        }

        if (user.world.isClient)
        {
            for (int i = 0; i < 40; i++)
            {
                Vec3d motion = new Vec3d(
                        (user.world.random.nextFloat() - .5f) * 2f,
                        (user.world.random.nextFloat() - .5f) * 2f,
                        (user.world.random.nextFloat() - .5f) * 2f);

                user.world.addParticle(ParticleTypes.HAPPY_VILLAGER, entity.getEyePos().x, entity.getEyeY(), entity.getEyePos().z, motion.x, motion.y, motion.z);
            }

            return ActionResult.FAIL;
        }

        user.world.playSound(null, new BlockPos(entity.getEyePos()), SoundEvents.ENTITY_VILLAGER_CELEBRATE, SoundCategory.NEUTRAL, .5f, 1f);

        ItemStack brewer = ModItems.BETTER_BREWER.getDefaultStack();
        if (!user.isCreative())
        {
            user.getStackInHand(hand).decrement(1);
        }

        if (user.getStackInHand(hand).isEmpty())
        {
            user.setStackInHand(hand, brewer);
        }
        else
        {
            user.getInventory().insertStack(brewer);
        }

        entity.discard();
        return ActionResult.FAIL;
    }
}
