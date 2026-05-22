package net.velleagle.civilian_aviation.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import immersive_aircraft.entity.AircraftEntity;
import immersive_aircraft.Main;
import immersive_aircraft.cobalt.network.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.velleagle.civilian_aviation.entity.CivilianAircraftEntity;
import net.velleagle.civilian_aviation.entity.HelicopterEntity;
import net.velleagle.civilian_aviation.item.AircraftVariant;
import net.velleagle.civilian_aviation.network.c2s.PaintMessage;

import java.util.List;

/**
 * ペイントスプレーの色選択 GUI。
 *
 * レイアウト:
 *   ┌──────────────────────────────────────┐
 *   │  タイトル（機体名）                     │
 *   │  ─────────────────────────────────  │
 *   │  [機体アイテムアイコン]  機体名・色名    │
 *   │  ─────────────────────────────────  │
 *   │  ██ ██ ██ ██ ██ ...（スウォッチ行）     │
 *   └──────────────────────────────────────┘
 */
public class PaintSprayScreen extends Screen {

    // IA 共有テクスチャ
    private static final ResourceLocation TEXTURE =
            Main.locate("textures/gui/container/inventory.png");

    // スロット枠 UV
    private static final int SLOT_TEX_U         = 262;
    private static final int SLOT_TEX_V_NORMAL  = 0;
    private static final int SLOT_TEX_V_CURRENT = 22 * 4;
    private static final int SLOT_SIZE          = 22;
    private static final int SLOT_INNER         = 16;
    private static final int SLOT_OFFSET        = 3;

    // レイアウト定数
    private static final int TITLE_H       = 10;
    private static final int PADDING       = 8;
    private static final int SLOT_GAP      = 4;
    private static final int MAX_COLS      = 8;
    private static final int PREVIEW_SIZE  = 32;   // アイコン描画サイズ（2倍スケール）
    private static final int PREVIEW_PAD   = 6;
    private static final int SEPARATOR_H   = 1;
    private static final int MIN_PANEL_W   = 180;

    // カラー定数
    private static final int COLOR_TITLE      = 0x222222;
    private static final int COLOR_LABEL      = 0x606060;
    private static final int COLOR_LABEL_SEL  = 0xFFFFFF;
    private static final int COLOR_SEPARATOR  = 0x80909090;
    private static final int COLOR_CHECK      = 0xFFFFFFFF;
    private static final int COLOR_HOVER_OVL  = 0x40FFFFFF;
    private static final int COLOR_HOVER_BDR  = 0xCCFFFFFF;

    private final AircraftEntity aircraft;
    private final List<AircraftVariant>  variants;

    // レイアウト計算済み変数
    private int panelX, panelY, panelW, panelH;
    private int cols, rows;
    private int gridX, gridY;
    private int previewX, previewY;

    // ホバー追跡
    private int hoveredIndex = -1;

    private PaintSprayScreen(AircraftEntity aircraft, List<AircraftVariant> variants) {
        super(Component.translatable("gui.civilian_aviation.paint_spray"));
        this.aircraft = aircraft;
        this.variants = variants;
    }

    public static void open(AircraftEntity aircraft, List<AircraftVariant> variants) {
        Minecraft.getInstance().setScreen(new PaintSprayScreen(aircraft, variants));
    }

    /** aircraft の種別に応じて variantEntityId を返すヘルパー */
    private String getVariantEntityId() {
        if (aircraft instanceof CivilianAircraftEntity ca) return ca.getVariantEntityId();
        if (aircraft instanceof HelicopterEntity he)      return he.getVariantEntityId();
        return "";
    }

    // -------------------------------------------------------
    // init（レイアウト計算）
    // -------------------------------------------------------
    @Override
    protected void init() {
        super.init();

        int count = variants.size();
        cols = Math.min(count, MAX_COLS);
        rows = (count + cols - 1) / cols;

        // グリッド幅
        int gridW = cols * SLOT_INNER + (cols - 1) * SLOT_GAP;

        // プレビューエリア幅（アイコン + テキスト余白）
        int previewAreaW = PREVIEW_SIZE + PREVIEW_PAD * 2 + 80;

        panelW = Math.max(MIN_PANEL_W,
                Math.max(gridW, previewAreaW) + (PADDING + SLOT_OFFSET) * 2);

        // 高さ計算：
        //   タイトル + 区切り + プレビューエリア + 区切り + グリッド行数 + 余白
        int previewAreaH = PREVIEW_SIZE + PREVIEW_PAD * 2;
        int gridAreaH    = rows * SLOT_INNER + (rows - 1) * SLOT_GAP;
        panelH = TITLE_H * 2
                + SEPARATOR_H + PADDING
                + previewAreaH + PADDING
                + SEPARATOR_H + PADDING
                + gridAreaH
                + PADDING;

        panelX = (width  - panelW) / 2;
        panelY = (height - panelH) / 2;

        previewX = panelX + PADDING + SLOT_OFFSET;
        previewY = panelY + TITLE_H * 2 + SEPARATOR_H + PADDING;

        gridX = panelX + (panelW - gridW) / 2;
        gridY = previewY + previewAreaH + PADDING + SEPARATOR_H + PADDING;
    }

    // -------------------------------------------------------
    // IA互換描画ヘルパー
    // -------------------------------------------------------

    private void drawRectangle(GuiGraphics g, int x, int y, int h, int w) {
        g.blit(TEXTURE, x,          y,          176,      0,  16, 16, 512, 256);
        g.blit(TEXTURE, x + w - 16, y,          176 + 32, 0,  16, 16, 512, 256);
        g.blit(TEXTURE, x + w - 16, y + h - 16, 176 + 32, 32, 16, 16, 512, 256);
        g.blit(TEXTURE, x,          y + h - 16, 176,      32, 16, 16, 512, 256);
        g.blit(TEXTURE, x + 16,     y,          w - 32, 16,     176 + 16, 0,  16, 16, 512, 256);
        g.blit(TEXTURE, x + 16,     y + h - 16, w - 32, 16,     176 + 16, 32, 16, 16, 512, 256);
        g.blit(TEXTURE, x,          y + 16,     16,     h - 32, 176,      16, 16, 16, 512, 256);
        g.blit(TEXTURE, x + w - 16, y + 16,     16,     h - 32, 176 + 32, 16, 16, 16, 512, 256);
        g.blit(TEXTURE, x + 16, y + 16, w - 32, h - 32, 176 + 16, 16, 16, 16, 512, 256);
    }

    private void drawImage(GuiGraphics g, int x, int y, int u, int v, int w, int h) {
        g.blit(TEXTURE, x, y, u, v, w, h, 512, 256);
    }

    /** 横区切り線を描画 */
    private void drawSeparator(GuiGraphics g, int y) {
        g.fill(panelX + PADDING, y, panelX + panelW - PADDING, y + SEPARATOR_H, COLOR_SEPARATOR);
    }

    // -------------------------------------------------------
    // render
    // -------------------------------------------------------
    // 1.21.1 の Screen.renderBackground() はブラーシェーダーを適用するため呼ばない。
    // ワールドを透過させたままパネルだけ描画する（インゲームGUI）。
    @Override
    public void renderBackground(GuiGraphics g, int mouseX, int mouseY, float delta) {
        // 何もしない（ブラー抑制）
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float delta) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        // パネル背景
        drawRectangle(g, panelX, panelY, panelH, panelW);

        // ── タイトル ──────────────────────────────────────
        int titleW = font.width(title);
        g.drawString(font, title, panelX + panelW / 2 - titleW / 2, panelY + TITLE_H, COLOR_TITLE, false);

        // 区切り線①（タイトル下）
        drawSeparator(g, panelY + TITLE_H * 2);

        // ── 現在選択中のプレビュー（アイテムアイコン + 名前）────────
        renderCurrentPreview(g);

        // 区切り線②（プレビューエリア下）
        int sep2Y = previewY + PREVIEW_SIZE + PREVIEW_PAD * 2 + PADDING;
        drawSeparator(g, sep2Y);

        // ── スウォッチグリッド ───────────────────────────────
        hoveredIndex = -1;
        String currentId = getVariantEntityId();
        for (int i = 0; i < variants.size(); i++) {
            AircraftVariant variant = variants.get(i);
            int col = i % cols;
            int row = i / cols;
            int sx  = gridX + col * (SLOT_INNER + SLOT_GAP);
            int sy  = gridY + row * (SLOT_INNER + SLOT_GAP);

            boolean isCurrent = variant.entityId().equals(currentId);
            boolean hovered   = isInside(mouseX, mouseY, sx - SLOT_OFFSET, sy - SLOT_OFFSET, SLOT_SIZE, SLOT_SIZE);
            if (hovered) hoveredIndex = i;

            renderSwatch(g, variant, sx, sy, isCurrent, hovered);
        }

        super.render(g, mouseX, mouseY, delta);

        // ホバーツールチップ（super.render の後）
        if (hoveredIndex >= 0) {
            g.renderTooltip(font,
                    variants.get(hoveredIndex).getDisplayComponent(),
                    mouseX, mouseY);
        }
    }

    /**
     * 現在選択中バリアントのプレビューエリアを描画。
     * アイテムアイコンを 2 倍スケールで描画し、右側に機体名+色名を表示する。
     */
    private void renderCurrentPreview(GuiGraphics g) {
        String currentId = getVariantEntityId();
        AircraftVariant current = variants.stream()
                .filter(v -> v.entityId().equals(currentId))
                .findFirst()
                .orElse(variants.isEmpty() ? null : variants.get(0));

        int px = previewX + PREVIEW_PAD;
        int py = previewY + PREVIEW_PAD;

        if (current != null) {
            // ── アイテムアイコンを 2 倍スケールで描画 ──
            ItemStack stack = new ItemStack(current.item().get());

            var pose = g.pose();
            pose.pushPose();
            pose.translate(px, py, 0);
            pose.scale(2.0F, 2.0F, 1.0F);
            g.renderItem(stack, 0, 0);
            pose.popPose();

            // ── テキスト（アイコン右側、縦中央揃え）──
            // テキスト描画エリアはパネル右端のパディングまで
            int tx = px + PREVIEW_SIZE + PREVIEW_PAD + 2;
            int ty = py + (PREVIEW_SIZE - font.lineHeight) / 2;
            int maxTextW = panelX + panelW - PADDING - tx;

            Component displayComp = current.getDisplayComponent();
            String displayStr = displayComp.getString();
            // パネル幅に収まるよう文字列を切り詰める
            if (font.width(displayStr) > maxTextW) {
                displayStr = font.plainSubstrByWidth(displayStr, maxTextW - font.width("…")) + "…";
            }
            g.drawString(font, displayStr, tx, ty, COLOR_LABEL_SEL, false);

        } else {
            // バリアントなし（通常は発生しない）
            g.fill(px, py, px + PREVIEW_SIZE, py + PREVIEW_SIZE, 0xFF444444);
            g.drawString(font, "---", px + PREVIEW_SIZE + PREVIEW_PAD, py + 4, COLOR_LABEL, false);
        }
    }

    /** 個別スウォッチ（カラー枠のみ）を描画 */
    private void renderSwatch(GuiGraphics g,
                              AircraftVariant variant,
                              int sx, int sy,
                              boolean isCurrent, boolean hovered) {

        // スロット枠テクスチャ
        int slotV = isCurrent ? SLOT_TEX_V_CURRENT : SLOT_TEX_V_NORMAL;
        drawImage(g, sx - SLOT_OFFSET, sy - SLOT_OFFSET, SLOT_TEX_U, slotV, SLOT_SIZE, SLOT_SIZE);

        // アイテムアイコン（16×16、スケール等倍）
        ItemStack stack = new ItemStack(variant.item().get());
        g.renderItem(stack, sx, sy);

        // ホバーオーバーレイ
        if (hovered && !isCurrent) {
            g.fill(sx, sy, sx + SLOT_INNER, sy + SLOT_INNER, COLOR_HOVER_OVL);
        }

        // ホバー時白枠ハイライト
        if (hovered) {
            g.fill(sx,              sy,
                    sx + SLOT_INNER, sy + 1,              COLOR_HOVER_BDR);
            g.fill(sx,              sy + SLOT_INNER - 1,
                    sx + SLOT_INNER, sy + SLOT_INNER,     COLOR_HOVER_BDR);
            g.fill(sx,              sy,
                    sx + 1,          sy + SLOT_INNER,     COLOR_HOVER_BDR);
            g.fill(sx + SLOT_INNER - 1, sy,
                    sx + SLOT_INNER, sy + SLOT_INNER,     COLOR_HOVER_BDR);
        }

        // 選択中チェックマーク
        if (isCurrent) {
            g.drawCenteredString(font, "✔",
                    sx + SLOT_INNER / 2, sy + SLOT_INNER / 2 - 4, COLOR_CHECK);
        }
    }

    // -------------------------------------------------------
    // 入力処理
    // -------------------------------------------------------
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return super.mouseClicked(mouseX, mouseY, button);

        String currentId = getVariantEntityId();
        for (int i = 0; i < variants.size(); i++) {
            AircraftVariant variant = variants.get(i);
            int col = i % cols;
            int row = i / cols;
            int sx  = gridX + col * (SLOT_INNER + SLOT_GAP);
            int sy  = gridY + row * (SLOT_INNER + SLOT_GAP);

            if (isInside((int) mouseX, (int) mouseY,
                    sx - SLOT_OFFSET, sy - SLOT_OFFSET, SLOT_SIZE, SLOT_SIZE)) {
                if (!variant.entityId().equals(currentId)) {
                    NetworkHandler.sendToServer(
                            new PaintMessage(aircraft.getId(), variant.entityId()));
                }
                onClose();
                return true;
            }
        }

        // パネル外クリックで閉じる
        if (mouseX < panelX || mouseX > panelX + panelW
                || mouseY < panelY || mouseY > panelY + panelH) {
            onClose();
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    // -------------------------------------------------------
    // ユーティリティ
    // -------------------------------------------------------

    private static boolean isInside(int mx, int my, int x, int y, int w, int h) {
        return mx >= x && mx < x + w && my >= y && my < y + h;
    }
}
