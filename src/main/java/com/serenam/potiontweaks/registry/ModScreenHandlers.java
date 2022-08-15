package com.serenam.potiontweaks.registry;

import com.serenam.potiontweaks.PotionTweaksMod;
import com.serenam.potiontweaks.gui.BetterBrewerScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers
{
    public static ScreenHandlerType<BetterBrewerScreenHandler> BETTER_BREWER;

    public static void registerScreenHandlers()
    {
        BETTER_BREWER = ScreenHandlerRegistry.registerSimple(new Identifier(PotionTweaksMod.MOD_ID, "better_brewer"), BetterBrewerScreenHandler::new);
    }
}
