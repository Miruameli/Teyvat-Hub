# ADR 0004: Pin pada compileSdk 36 dan Versi Dependensi yang Kompatibel

- **Status**: Accepted
- **Tanggal**: 2026-10-19
- **Penulis**: Engineering Lead
- **Supersedes**: —

## Konteks

Proyek mengunci `compileSdk = 36` karena lingkungan pengembangan hanya
menyediakan Android SDK platform 36 dan Build Tools 36.0.0, tanpa `sdkmanager`
untuk memasang platform 37. Emulator PRoot ini juga kehabisan RAM, sehingga
memasang SDK tambahan bukan pilihan realistis.

Setelah membangun beberapa fitur, build gagal berulang kali dengan pesan yang
menuntut `compileSdk 37`. Investigasi menunjukkan beberapa dependensi terbaru
secara transitif menarik `androidx.lifecycle` 2.11.0, yang mensyaratkan
compileSdk 37:

| Dependensi | Efek transitif |
| --- | --- |
| `androidx.hilt:hilt-navigation-compose:1.4.0` | `lifecycle-runtime-compose 2.11.0` |
| Compose BOM `2026.09.00` | `compose-ui 1.12.1` (butuh SDK 37) |
| `activity-compose` terbaru | ikut menaikkan baris lifecycle |

Menaikkan `compileSdk` tidak memungkinkan di lingkungan ini, sementara
menurunkan versi tidak sesuai keinginan user yang minta fitur berjalan.

## Keputusan

Kami **mempertahankan `compileSdk = 36`** dan menyelaraskan versi dependensi
agar seluruh graf sesuai dengan SDK tersebut:

1. `androidx.hilt:hilt-navigation-compose` dipatok di **1.3.0**, bukan 1.4.0,
   karena hanya menarik `lifecycle-viewmodel-compose 2.9.1`.
2. Compose BOM dipatok di **2026.06.01** (`compose-ui 1.11.4`), bukan
   2026.09.00 (`compose-ui 1.12.1` yang menuntut SDK 37).
3. `androidx.lifecycle` tetap pada **2.10.0**, versi terbaru yang tidak
   menuntut compileSdk 37.
4. Versi-versi ini dicatat di `gradle/libs.versions.toml` beserta komentar
   penyebabnya, supaya penegas berikutnya tidak mengangkatnya tanpa ikuti
   peningkatan compileSdk.

Constraint Gradle (`constraints {}`) dicoba lebih dulu tetapi tidak cukup karena
AGP 9 menggunakan konfigurasi dependensi tersendiri; menaikkan/menurunkan versi
pada akar rantai lebih tepat.

## Konsekuensi

Positif:

- Build hijau di lingkungan yang hanya punya SDK 36.
- Tidak ada dependensi yang bergantung pada API yang tidak tersedia, sehingga
  risiko runtime `NoSuchMethodError` kecil.
- Setiap pengecualian versi terdokumentasi beserta alasannya.

Negatif:

- Beberapa fitur terbaru AndroidX (khusus Android 17/SDK 37) belum dapat
  dipakai sampai toolchain dinaikkan.
- Perlu audit ulang versi saat project berpindah ke lingkungan dengan SDK 37.

## Alternatif yang Dipertimbangkan

| Alternatif | Alasan ditolak |
| --- | --- |
| Menaikkan compileSdk ke 37 | Tidak ada platform 37 terpasang dan `sdkmanager` tidak tersedia; memaksa environment baru untuk fitur yang belum dibutuhkan |
| Menggunakan `android.newDsl=false` dan legacy dependency | Hanya menunda masalah dan membuka kembali utang migrasi ADR 0001 |
| Membiarkan Gradle menaikkan versi otomatis | Build gagal karena tuntutan compileSdk 37; ini yang sedang terjadi |

## Verifikasi

Hasil tracing dependensi (`./gradlew :app:dependencyInsight`) menunjukkan
`hilt-navigation-compose:1.4.0` menarik `lifecycle-common:2.11.0`. Setelah versi
diturunkan ke 1.3.0 dan BOM diturunkan ke 2026.06.01, build kembali berhasil.

## Referensi

- https://developer.android.com/build/releases/agp-9-0-0-release-notes
- docs/runbook.md bagian "Upgrading AGP atau Kotlin"
