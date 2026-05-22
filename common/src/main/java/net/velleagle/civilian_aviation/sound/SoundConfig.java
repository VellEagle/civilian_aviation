package net.velleagle.civilian_aviation.sound;

/**
 * エンジンサウンドの音量設定。
 *
 * <p>各エンジン種別ごとに「外部音量」と「内部音量」を独立して調整できます。
 * 値は 0.0（無音）〜 2.0（最大2倍）で、デフォルトは 1.0 です。</p>
 *
 * <h2>調整対象</h2>
 * <ul>
 *   <li>LYCOMING    — Skyhawk / Comanche（レシプロ単発）</li>
 *   <li>BRISTOL     — Trimotor / Pzl37Los / Pzlp11（Bristol Mercury 星型）</li>
 *   <li>ALLISON250  — Bell206Blackstripe（タービンヘリ）</li>
 *   <li>FRANKLIN    — Bell47g（レシプロヘリ）</li>
 *   <li>PW610F      — E500（ジェット双発）</li>
 * </ul>
 *
 * <h2>変更方法</h2>
 * <p>下記の定数を直接編集してビルドし直すか、将来的な設定ファイル連携に
 * 差し替えてください。</p>
 */
public final class SoundConfig {

    private SoundConfig() {}

    // =========================================================================
    // Lycoming O-360 (Skyhawk / Comanche)
    // =========================================================================

    /** Skyhawk/Comanche 外部エンジン音の音量係数（三人称・機外視点） */
    public static float LYCOMING_EXTERIOR_VOLUME = 0.5f;

    /** Skyhawk/Comanche 内部エンジン音の音量係数（一人称・機内視点） */
    public static float LYCOMING_INTERIOR_VOLUME = 0.10f;  // 修正: 0.5→0.30 (内部音が大きすぎるため)

    // =========================================================================
    // Bristol Mercury (Trimotor / Pzl37Los / Pzlp11)
    // =========================================================================

    /** Bristol Mercury 外部エンジン音の音量係数 */
    public static float BRISTOL_EXTERIOR_VOLUME = 0.5f;

    /** Bristol Mercury 内部エンジン音の音量係数 */
    public static float BRISTOL_INTERIOR_VOLUME = 0.1f;

    // =========================================================================
    // Allison 250 (Bell206Blackstripe)
    // =========================================================================

    /** Bell206 外部エンジン音の音量係数 */
    public static float ALLISON250_EXTERIOR_VOLUME = 0.1f;  // 修正: 0.5→0.28 (全体的に音が大きすぎるため)

    /** Bell206 内部エンジン音の音量係数 */
    public static float ALLISON250_INTERIOR_VOLUME = 0.1f;  // 修正: 0.5→0.25

    // =========================================================================
    // Franklin O-335 (Bell47g)
    // =========================================================================

    /** Bell47g エンジン音の音量係数（オープンコックピットのため共通） */
    public static float FRANKLIN_VOLUME = 0.5f;

    // =========================================================================
    // PW610F (E500)
    // =========================================================================

    /** E500 外部エンジン音の音量係数 */
    public static float PW610F_EXTERIOR_VOLUME = 0.5f;

    /** E500 内部エンジン音の音量係数 */
    public static float PW610F_INTERIOR_VOLUME = 0.5f;

    /** E500 始動音 enginepw610f_starting（ワンショット）の音量 */
    public static float PW610F_STARTING_VOLUME = 1.0f;

    /** E500 停止音 enginepw610f_stopping（ワンショット）の音量 */
    public static float PW610F_STOPPING_VOLUME = 1.0f;

    /**
     * E500 クランキング音 enginepw610f_cranking（ワンショット + ループ）の音量。
     * 始動時に繰り返し鳴るジェット特有のウィーン音。
     * 元の値は 1.0（ワンショット）/ 0.8（ループ）。
     * 大きすぎる場合は 0.4〜0.6 程度に下げてください。
     */
    public static float PW610F_CRANKING_VOLUME = 0.2f;
}
