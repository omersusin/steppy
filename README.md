# steppy

Android adım sayar uygulaması. Şu an yalnızca GitHub Actions üzerinde derlenen minimum
"iskelet" sürüm içerir; adım sayma servisi, harita ve istatistikler sonraki adımlarda
eklenecek.

## Derleme

Derleme GitHub Actions ile yapılır: `main` dalına her push'ta lint + debug APK üretilir ve
APK iş özetinden artefakt olarak indirilebilir. Yerelde derleme için JDK 17 ve Android SDK
(platform 35, build-tools 35.0.0) gerektirir: `gradle :app:assembleDebug`.

## Planlanan yapı

- Özet: günlük hedef ilerlemesi, tahmini mesafe/kalori, aktif süre, son antrenman, seri
- Harita: elle başlatılan antrenmanlar (yürüyüş, koşu, doğa yürüyüşü, bisiklet), serbest /
  mesafe / süre / adım hedefi, duraklat ve isteğe bağlı otomatik mola
- İstatistik ve ayarlar sekmeleri
