# 全エンジンサウンド統合ガイド

## 新規ファイル一覧

```
sound/
  BristolMercurySounds.java        Bristol Mercury SoundEvent登録
  BristolMercurySoundManager.java  Bristol Mercury サウンド制御
  Allison250Sounds.java            Allison 250 SoundEvent登録
  Allison250SoundManager.java      Allison 250 サウンド制御
  Franklin0335Sounds.java          Franklin O-335 SoundEvent登録
  Franklin0335SoundManager.java    Franklin O-335 サウンド制御
  CivilianSoundHandler.java        全機体統合ハンドラ（SkyhawkSoundHandlerを統合）

resources/
  sounds_additional.json           sounds.jsonに追記する内容
```

## Step 1 — .ogg ファイルの配置

各zipのファイルを `assets/civilian_aviation/sounds/engine/` 以下に配置。

**Bristol Mercury** (enginebristolmercury.zip より):
- enginebristolmercury_starting/stopping/sputter/cranking.ogg
- enginebristolmercury_running_800rpm.ogg / _inside / _distant / _moredistant
- enginebristolmercury_running_1300rpm.ogg / _inside / _distant / _moredistant
- enginebristolmercury_running_1800rpm.ogg / _inside / _distant / _moredistant
- enginebristolmercury_running_2300rpm.ogg / _inside / _distant / _moredistant

**Allison 250** (engineallison250.zip より):
- engineallison250_crankingintro.ogg / _inside
- engineallison250_running_turbine.ogg / _inside
- engineallison250_running.ogg / _inside
- engineallison250_running_distant.ogg
- engineallison250_running_moredistant.ogg

**Franklin O-335** (enginefranklin0335.zip より):
- enginefranklin0335_starting/stopping/sputter.ogg
- enginefranklin0335_crankingintro.ogg / crankingloop.ogg
- enginefranklin0335_running.ogg / _inside
- enginefranklin0335_distant.ogg / more_distant.ogg

## Step 2 — sounds.json に追記

`sounds_additional.json` の内容を既存の `sounds.json` にマージ。
（コメント行 `"// Bristol Mercury": "==="` は削除してからマージすること）

## Step 3 — CivilianAviation.init() に追記

```java
// 既存
SkyhawkEngineSounds.register();
// 追加
BristolMercurySounds.register();
Allison250Sounds.register();
Franklin0335Sounds.register();
```

## Step 4 — CIvilianAviationForgeClient.java の変更

`SkyhawkSoundHandler` の呼び出しを `CivilianSoundHandler` に置き換える:

```java
// 変更前
SkyhawkSoundHandler.INSTANCE.tick();

// 変更後
CivilianSoundHandler.INSTANCE.tick();
```

stopAll も同様に変更（CLIENT_STOPPINGイベント等）。

## Step 5 — HelicopterEntity の getEngineVolume() オーバーライド

Bell47g と Bell206Blackstripe の基底クラス `HelicopterEntity` に追加:

```java
@Override
protected float getEngineVolume() {
    return -1.0f;  // IAデフォルト音を完全無効化
}
```

---

## 機体→エンジン対応表

| 機体 | エンジン | interior | 基底クラス |
|------|---------|---------|-----------|
| Skyhawk系（全バリアント） | Lycoming O-360 | ✓ | CivilianAircraftEntity |
| Comanche系（全バリアント） | Lycoming O-360 | ✓ | CivilianAircraftEntity |
| VulcanairRed | Lycoming O-360 | ✓ | CivilianAircraftEntity |
| TrimotorBlue | Bristol Mercury | ✓ | CivilianAircraftEntity |
| Pzl37Los | Bristol Mercury | ✓ | CivilianAircraftEntity |
| Pzlp11 | Bristol Mercury | ✗（オープンコックピット） | CivilianAircraftEntity |
| Bell206Blackstripe | Allison 250 | ✓ | HelicopterEntity |
| Bell47g | Franklin O-335 | ✗（オープンコックピット） | HelicopterEntity |

## CivilianSoundHandler の機体判定ロジック

```
path.startsWith("skyhawk")  → LYCOMING
path.startsWith("comanche") → LYCOMING
path == "vulcanair_red"     → LYCOMING
path == "trimotor_blue"     → BRISTOL_INTERIOR
path == "pzl37los"          → BRISTOL_INTERIOR
path == "pzlp11"            → BRISTOL_NO_INTERIOR
path == "bell206_blackstripe" → ALLISON250
path == "bell47g"           → FRANKLIN
```
