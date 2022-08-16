package com.serenam.potiontweaks.util;

import com.serenam.potiontweaks.registry.ModItems;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomPotionUtil
{
    public static ItemStack CombinePotions(ItemStack potion1, ItemStack potion2)
    {
        if (!IsNormalPotion(potion1) || !IsNormalPotion(potion2) || potion1.getItem() != potion2.getItem())
        {
            return ItemStack.EMPTY;
        }

        // Combine effects and remove duplicates, prioritizing higher amplifier
        List<StatusEffectInstance> effects = PotionUtil.getPotionEffects(potion1);
        for (StatusEffectInstance status : PotionUtil.getPotionEffects(potion2))
        {
            Optional<StatusEffectInstance> status2 = effects.stream()
                    .filter(e -> e.getEffectType() == status.getEffectType())
                    .findFirst();

            if (status2.isEmpty())
            {
                effects.add(status);
            }
            else
            {
                int amp1 = status.getAmplifier();
                int amp2 = status2.get().getAmplifier();

                if (amp1 > amp2)
                {
                    effects.remove(status2.get());
                    effects.add(status);
                }
                else if (amp1 == amp2)
                {
                    effects.remove(status2.get());
                    effects.add(new StatusEffectInstance(
                            status.getEffectType(),
                            status.getDuration() + status2.get().getDuration(),
                            Math.max(amp1, amp2),
                            status.isAmbient(),
                            status.shouldShowParticles(),
                            status.shouldShowIcon(),
                            null,
                            status.getFactorCalculationData()));
                }
            }
        }

        // Reduce the durations for balance
        List<StatusEffectInstance> effectsReduced = new ArrayList<>();
        for (StatusEffectInstance status : effects)
        {
            effectsReduced.add(new StatusEffectInstance(
                    status.getEffectType(),
                    (int)(status.getDuration() * .8f),
                    status.getAmplifier(),
                    status.isAmbient(),
                    status.shouldShowParticles(),
                    status.shouldShowIcon(),
                    null,
                    status.getFactorCalculationData()));
        }

        // Create and return the potion
        ItemStack potion = new ItemStack(potion1.getItem());
        PotionUtil.setCustomPotionEffects(potion, effectsReduced);

        potion.setCustomName(Text.translatable("item.potiontweaks.combo_potion"));

        return potion;
    }

    public static ItemStack ToSingularity(ItemStack potion)
    {
        if (!IsNormalPotion(potion))
        {
            return ItemStack.EMPTY;
        }

        ItemStack singularity;
        if (potion.isOf(Items.POTION))
        {
            singularity = new ItemStack(ModItems.SINGULARITY);
        }
        else if (potion.isOf(Items.SPLASH_POTION))
        {
            singularity = new ItemStack(ModItems.SPLASH_SINGULARITY);
        }
        else
        {
            singularity = new ItemStack(ModItems.LINGERING_SINGULARITY);
        }

        singularity.setNbt(potion.getNbt());
        singularity.setDamage(999);

        return singularity;
    }

    public static boolean IsNormalPotion(ItemStack potion)
    {
        return potion.isOf(Items.POTION) || potion.isOf(Items.SPLASH_POTION) || potion.isOf(Items.LINGERING_POTION);
    }

    public static boolean IsSingularity(ItemStack potion)
    {
        return potion.isOf(ModItems.SINGULARITY) || potion.isOf(ModItems.SPLASH_SINGULARITY) || potion.isOf(ModItems.LINGERING_SINGULARITY);
    }
}
