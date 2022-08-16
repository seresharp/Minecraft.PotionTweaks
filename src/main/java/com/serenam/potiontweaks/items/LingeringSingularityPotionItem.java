package com.serenam.potiontweaks.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.LingeringPotionItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class LingeringSingularityPotionItem extends LingeringPotionItem
{
    public LingeringSingularityPotionItem()
    {
        super(new FabricItemSettings()
                .group(ItemGroup.BREWING)
                .maxDamageIfAbsent(1000));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
    {
        ItemStack itemStack = user.getStackInHand(hand);
        if (itemStack.isDamaged())
        {
            return TypedActionResult.pass(itemStack);
        }

        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_LINGERING_POTION_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));

        if (!world.isClient)
        {
            ItemStack linger = new ItemStack(Items.LINGERING_POTION);
            linger.setNbt(itemStack.getNbt());

            PotionEntity potionEntity = new PotionEntity(world, user);
            potionEntity.setItem(linger);
            potionEntity.setVelocity(user, user.getPitch(), user.getYaw(), -20.0F, 0.5F, 1.0F);
            world.spawnEntity(potionEntity);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));

        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public String getTranslationKey(ItemStack stack)
    {
        return getTranslationKey();
    }
}
