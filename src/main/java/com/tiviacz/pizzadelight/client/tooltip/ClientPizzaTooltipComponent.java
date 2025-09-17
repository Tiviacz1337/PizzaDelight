package com.tiviacz.pizzadelight.client.tooltip;

import com.tiviacz.pizzadelight.util.Utils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ClientPizzaTooltipComponent implements ClientTooltipComponent {
    private static final ResourceLocation BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/background");
    private final List<ItemStack> items;

    public ClientPizzaTooltipComponent(PizzaTooltipComponent pizzaTooltip) {
        this.items = pizzaTooltip.ingredients;
    }

    @Override
    public int getHeight() {
        if(!Utils.isShiftPressed()) return 0;
        return this.gridSizeY() * 20 + 2 + 4;
    }

    @Override
    public int getWidth(Font pFont) {
        if(!Utils.isShiftPressed()) return 0;
        return this.gridSizeX() * 18 + 2;
    }

    @Override
    public void renderImage(Font pFont, int pX, int pY, GuiGraphics pGuiGraphics) {
        if(!Utils.isShiftPressed()) return;

        int i = this.gridSizeX();
        int j = this.gridSizeY();
        pGuiGraphics.blitSprite(BACKGROUND_SPRITE, pX, pY, this.gridSizeX() * 18 + 2, this.gridSizeY() * 20 + 2);
        int k = 0;

        for(int l = 0; l < j; ++l) {
            for(int i1 = 0; i1 < i; ++i1) {
                int j1 = pX + i1 * 18 + 1;
                int k1 = pY + l * 20 + 1;
                this.renderSlot(j1, k1, k++, pGuiGraphics, pFont);
            }
        }
    }

    private void renderSlot(int x, int y, int itemIndex, GuiGraphics guiGraphics, Font font) {
        if(itemIndex >= this.items.size()) {
            this.blit(guiGraphics, x, y, Texture.BLOCKED_SLOT);
        } else {
            ItemStack itemstack = this.items.get(itemIndex);
            this.blit(guiGraphics, x, y, ClientPizzaTooltipComponent.Texture.SLOT);
            guiGraphics.renderItem(itemstack, x + 1, y + 1, itemIndex);
            guiGraphics.renderItemDecorations(font, itemstack, x + 1, y + 1);
        }

    }

    private void blit(GuiGraphics guiGraphics, int x, int y, ClientPizzaTooltipComponent.Texture texture) {
        guiGraphics.blitSprite(texture.sprite, x, y, 0, texture.w, texture.h);
    }

    private int gridSizeX() {
        int x = Math.max(2, (int)Math.ceil(Math.sqrt((double)this.items.size() + 1.0D)));
        return Math.min(x, 3);
    }

    private int gridSizeY() {
        int y = (int)Math.ceil(((double)this.items.size() + 1.0D) / (double)this.gridSizeX());
        return Math.min(y, 3);
    }

    @OnlyIn(Dist.CLIENT)
    enum Texture {
        BLOCKED_SLOT(ResourceLocation.withDefaultNamespace("container/bundle/blocked_slot"), 18, 20),
        SLOT(ResourceLocation.withDefaultNamespace("container/bundle/slot"), 18, 20);

        public final ResourceLocation sprite;
        public final int w;
        public final int h;

        Texture(ResourceLocation sprite, int w, int h) {
            this.sprite = sprite;
            this.w = w;
            this.h = h;
        }
    }
}