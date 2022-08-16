package com.serenam.potiontweaks.registry;

import com.serenam.potiontweaks.PotionTweaksMod;
import com.serenam.potiontweaks.gui.BetterBrewerScreenHandler;
import com.serenam.potiontweaks.gui.PotionCombinerScreenHandler;
import com.serenam.potiontweaks.gui.SingularityCrafterScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers
{
    public static ScreenHandlerType<BetterBrewerScreenHandler> BETTER_BREWER;
    public static ScreenHandlerType<PotionCombinerScreenHandler> POTION_COMBINER;
    public static ScreenHandlerType<SingularityCrafterScreenHandler> SINGULARITY_CRAFTER;

    public static void registerScreenHandlers()
    {
        BETTER_BREWER = ScreenHandlerRegistry.registerSimple(new Identifier(PotionTweaksMod.MOD_ID, "better_brewer"), BetterBrewerScreenHandler::new);
        POTION_COMBINER = ScreenHandlerRegistry.registerSimple(new Identifier(PotionTweaksMod.MOD_ID, "potion_combiner"), PotionCombinerScreenHandler::new);
        SINGULARITY_CRAFTER = ScreenHandlerRegistry.registerSimple(new Identifier(PotionTweaksMod.MOD_ID, "singularity_crafter"), SingularityCrafterScreenHandler::new);
    }
}
