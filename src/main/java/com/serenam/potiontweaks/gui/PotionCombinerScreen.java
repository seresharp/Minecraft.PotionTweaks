package com.serenam.potiontweaks.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.serenam.potiontweaks.PotionTweaksMod;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class PotionCombinerScreen extends HandledScreen<PotionCombinerScreenHandler>
{
    private static final Identifier TEXTURE = new Identifier(PotionTweaksMod.MOD_ID, "textures/gui/container/potion_combiner.png");

    public PotionCombinerScreen(PotionCombinerScreenHandler handler, PlayerInventory inventory, Text title)
    {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY)
    {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);

        // Draw background
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);

        // Draw arrow
        int brewTime = handler.getBrewTime();
        if (brewTime > 0)
        {
            int brewTimeHeight = (int)(28f * (1f - (brewTime / 200f)));
            if (brewTimeHeight > 0)
            {
                drawTexture(matrices, x + 134, y + 37, 176, 0, 9, brewTimeHeight);
            }
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
    {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void init()
    {
        super.init();
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }
}
