package net.velleagle.civilian_aviation.item;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.velleagle.civilian_aviation.CivilianAviation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * 機体ひとつの色バリアントを表すレコード。
 *
 * @param entityId     バリアントのエンティティ登録名 (例: "skyhawk_pr_red")
 * @param displayName  GUI に表示する翻訳キー
 * @param previewColor GUI でのプレビュー色 (0xRRGGBB)
 * @param item         対応するアイテムの Supplier
 */
public record AircraftVariant(
        String entityId,
        String displayName,
        int previewColor,
        Supplier<Item> item
) {
    public Component getDisplayComponent() {
        return Component.translatable(displayName);
    }

    public ResourceLocation getTextureId() {
        return CivilianAviation.locate(entityId);
    }

    // -------------------------------------------------------
    // バリアントグループ レジストリ
    //
    // 同じ機体フレームで色だけ異なるバリアントをひとつのグループにまとめる。
    // CivilianAviation.init() で registerGroup() を呼んで登録する。
    //
    // 新しい色を追加する → 対応するグループの List に AircraftVariant を追加
    // 新しい機体フレームを追加する → 新しいグループを registerGroup() で追加
    // -------------------------------------------------------

    public static final List<List<AircraftVariant>> GROUPS = new ArrayList<>();

    public static void registerGroup(List<AircraftVariant> variants) {
        GROUPS.add(Collections.unmodifiableList(new ArrayList<>(variants)));
    }

    /**
     * 指定した entityId が属するグループを返す。見つからなければ空リスト。
     */
    public static List<AircraftVariant> findGroup(String entityId) {
        for (List<AircraftVariant> group : GROUPS) {
            for (AircraftVariant v : group) {
                if (v.entityId().equals(entityId)) {
                    return group;
                }
            }
        }
        return Collections.emptyList();
    }
}
