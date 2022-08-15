package com.serenam.potiontweaks;

import com.serenam.potiontweaks.gui.BetterBrewerScreen;
import com.serenam.potiontweaks.registry.ModBlocks;
import com.serenam.potiontweaks.registry.ModScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class PotionTweaksModClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BETTER_BREWER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.EMPTY_BREWER, RenderLayer.getCutout());
        ScreenRegistry.register(ModScreenHandlers.BETTER_BREWER, BetterBrewerScreen::new);
    }
}
