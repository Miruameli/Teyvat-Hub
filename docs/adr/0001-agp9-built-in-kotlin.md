# ADR 0001: Migrasi ke AGP 9.x dengan Built-in Kotlin

- **Status**: Accepted
- **Tanggal**: 2026-10-18
- **Penulis**: Engineering Lead
- **Supersedes**: —

## Konteks

Proyek Teyvat-Hub awalnya dikonfigurasi dengan Android Gradle Plugin (AGP)
versi 9.0.0 sambil tetap mendeklarasikan plugin `org.jetbrains.kotlin.android`
versi 2.0.20 beserta `kotlinOptions {}` dan `composeOptions {}`.

Setelah migrasi build file ke Gradle Kotlin DSL, build gagal berurutan dengan
dua kegagalan yang saling terkait:

1. `Multiple 'from' invocations` — version catalog dideklarasikan ganda.
2. `Cannot add extension with name 'kotlin'` — plugin `kotlin-android` bentrok
   dengan dukungan Kotlin bawaan AGP 9.0.

Akar penyebabnya adalah perubahan perilaku besar pada AGP 9.0: AGP kini
menyertakan dukungan Kotlin secara built-in dan bergantung runtime pada
Kotlin Gradle Plugin (KGP). Plugin `org.jetbrains.kotlin.android` menjadi tidak
kompatibel, sementara `kotlinOptions` dan `composeOptions` juga digantikan DSL.

## Keputusan

Kami mengadopsi **built-in Kotlin** dari AGP 9.x sebagai satu-satunya cara
mengompilasi Kotlin di proyek ini.

1. **Hapus** deklarasi plugin `org.jetbrains.kotlin.android` dari seluruh build
   file, termasuk entri `kotlin-android` di `gradle/libs.versions.toml`.
2. **Ganti** blok `android.kotlinOptions {}` dengan `kotlin { compilerOptions {} }`
   sesuai DSL KGP modern.
3. **Ganti** `android.composeOptions {}` dengan plugin
   `org.jetbrains.kotlin.plugin.compose` yang versinya terikat otomatis pada
   versi KGP.
4. **Naikkan** versi toolchain ke baris yang terverifikasi kompatibel dengan
   AGP 9.x dan kompatibilitas plugin pihak ketiga:
   - AGP `9.1.1`
   - Kotlin Gradle Plugin `2.3.20` (melebihi minimum `2.2.10` AGP 9.1.1)
   - KSP `2.3.12` (versi 2.3.10+ mendukung built-in Kotlin AGP 9)
   - Hilt `2.60.1` (perbaikan dukungan AGP 9.0)
   - Room `2.8.5`
   - Compose BOM `2026.09.00`
   - Navigation `2.9.8`
   - Compose compiler `org.jetbrains.kotlin.plugin.compose`
5. **Naikkan** `compileSdk`/`targetSdk` ke `36` agar sesuai dengan SDK platform
   dan Build Tools `36.0.0` yang sudah terpasang, dan sesuai dengan rekomendasi
   AGP 9.x.
6. **Naikkan** Gradle wrapper ke **`9.3.1`**. AGP 9.1.1 menolak Gradle di bawah
   versi ini (`Minimum supported Gradle version is 9.3.1`), sehingga nilai
   `9.1.0` yang dipakai pada tahap migrasi awal tidak dapat dipakai.
7. **Gunakan** KSP versi 2.3.12, bukan 2.3.x lebih tua. KSP di bawah 2.3.10
   mendaftarkan direktori hasil generate melalui `kotlin.sourceSets`, yang
   dilarang built-in Kotlin dan membuat konfigurasi gagal dengan pesan
   `Using kotlin.sourceSets DSL to add Kotlin sources is not allowed with
   built-in Kotlin` (google/ksp#2729, diperbaiki oleh #3001 pada rilis 2.3.10).
   Alternatif berupa flag `android.disallowKotlinSourceSets=false` sengaja
   tidak dipakai karena menutupi masalah, bukan memperbaikinya.
7. **Bersihkan** `gradle.properties` dari properti yang sudah dihapus atau
   berubah default di AGP 9.x, tanpa menambahkan flag opt-out
   (`android.builtInKotlin=false`) agar tidak menambah utang migrasi.

## Konsekuensi

Positif:

- Build file lebih sederhana; satu sumber kebenaran versi Kotlin (KGP).
- Menghindari konflik ekstensi antar plugin AGP dan KGP.
- Peringatan deprecation pada DSL lama hilang.

Negatif:

- Mengunci versi Kotlin pada baris yang didukung AGP 9.x; menurunkan KGP di
  bawah `2.2.10` tidak lagi memungkinkan selama built-in Kotlin aktif.
- Contoh kode dan dokumentasi pihak ketiga untuk `kotlinOptions` maupun
  `composeOptions` tidak lagi berlaku.

## Alternatif yang Dipertimbangkan

| Alternatif | Alasan ditolak |
| --- | --- |
| Mempertahankan `kotlin-android` dan menambahkan `android.builtInKotlin=false` | Hanya solusi sementara; flag ini dihapus pada AGP 10.0 sehingga tetap membangun utang migrasi. |
| Downgrade AGP ke 8.x | Meninggalkan perbaikan bug AGP 9.x dan memaksa versi Kotlin lama. SDK local hanya menyediakan platform 36, tidak selaras dengan rekomendasi SDK build tools terbaru. |

## Verifikasi

Perubahan tervalidasi dengan menjalankan build sungguhan:

```bash
./gradlew --version          # Gradle 9.1.0
./gradlew help               # konfigurasi berhasil
./gradlew :app:assembleDebug # APK debug berhasil di-build
```

## Referensi

- https://developer.android.com/build/migrate-to-built-in-kotlin
- https://developer.android.com/build/releases/agp-9-0-0-release-notes
- https://agp-status.frybits.com/agp-9.0.0/
- https://github.com/JetBrains/skills/tree/main/kotlin-tooling-agp9-migration
