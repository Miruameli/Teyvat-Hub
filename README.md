# Teyvat Hub

Aplikasi kompanion Android untuk pemain Genshin Impact di Indonesia: panduan
build karakter, builder tim, checklist farming harian/mingguan, kalender event
dan banner, semuanya berjalan **offline-first**.

> Fan project tidak resmi, tidak berafiliasi dengan HoYoverse.
> Konten diperbarui manual sesuai perubahan versi game.

## Fitur

- **Onboarding** — pilih karakter dan fokus bermain untuk mengisi beranda.
- **Database Karakter & Build** — senjata, artefak, prioritas talenta, tips
  pemakaian, disertai pencarian dan favorit.
- **Team Builder** — susun hingga empat karakter dengan catatan sinergi dan
  template tim siap pakai.
- **Checklist** — tugas harian dan mingguan dengan reset otomatis sesuai jadwal
  server, plus item buatan sendiri.
- **Event & Banner** — kalender event dan jadwal banner yang dapat difilter
  berstatus aktif, akan datang, atau sudah berakhir.
- **Pengaturan** — pilihan bahasa, atribusi sumber data, disclaimer, versi.

## Arsitektur

Satu modul Gradle `:app` dengan pemisahan paket berlapis:

```
com.teyvat.hub
├── domain        model murni dan aturan bisnis (teruji di JVM, tanpa Android)
├── data          Room (entitas, DAO), repository, dataset seed
├── application   use case penggabung domain dan data
├── presentation  Jetpack Compose: tema, komponen, layar, ViewModel
├── injection     konfigurasi Hilt
└── shared        konstanta dan helper bersama
```

Aliran dependensi: `presentation` → `application` → `domain`, dan
`data` → `domain`. Detail keputusan tercatat di [docs/adr](docs/adr).

## Teknologi

- **AGP 9.1.1** dengan built-in Kotlin (lihat
  [ADR 0001](docs/adr/0001-agp9-built-in-kotlin.md))
- Kotlin 2.3.20, Jetpack Compose, Material 3
- Room untuk penyimpanan lokal, Hilt untuk dependency injection
- Coil 3 untuk aset digital Genshin (lihat
  [ADR 0003](docs/adr/0003-optimized-genshin-digital-assets.md))
- DataStore untuk preferensi pengguna
- KSP untuk annotation processing

## Build dan Jalankan

Prasyarat: JDK 17, Android SDK platform 36, Build Tools 36.0.0, Gradle 9.3.1.

```bash
./gradlew :app:assembleDebug        # build APK debug
./gradlew :app:testDebugUnitTest    # unit test JVM
./gradlew :app:lintDebug            # lint statis
```

Hasil build berada di `app/build/outputs/apk/debug/app-debug.apk`. Untuk
menyalinnya ke root project guna mudah dipindahkan ke penyimpanan perangkat:

```bash
./scripts/copy-apk-to-storage.sh    # menghasilkan ./TeyvatHub-debug.apk
```

Alternatif tanpa build lokal: CI juga membangun dan mengunggah APK. Pada
halaman Actions, ambil artifact `TeyvatHub-debug-apk`; isinya sudah bernama
`TeyvatHub-debug.apk` dan bisa langsung disalin ke perangkat. Rinciannya ada
di [docs/runbook.md](docs/runbook.md).

> Catatan lingkungan: beberapa host (PRoot) tidak mengekspos `/sdcard`,
> sehingga salinan langsung ke penyimpanan bersama harus dijalankan dari
> lingkungan Android/Termux.

## Struktur Direktori

```
app/src/main/java/com/teyvat/hub/
├── MainActivity.kt              aktivitas Compose host
├── TeyvatHubApplication.kt       entry point: Hilt, seeding, Coil loader
├── domain/
│   ├── model/                   entitas murni (Character, Weapon, Team, ...)
│   └── service/                 aturan bisnis (reset checklist, pencarian)
├── data/
│   ├── local/<domain>/          entitas Room + DAO per domain
│   ├── remote/                  URL aset digital Genshin
│   ├── DatabaseSeeder.kt        seed konten saat pertama install
│   └── TeyvatHubDatabase.kt     database Room
├── application/                 repository (antarmuka + implementasi)
├── presentation/                layar Compose dan tema
└── injection/                   modul Hilt
```


## Pengujian

Unit test ditulis untuk semua aturan pada lapisan `domain` — reset checklist,
sinergi tim, peringkat pencarian, validasi tim — karena itu bagian yang paling
mungkin berubah sekaligus paling mudah divalidasi tanpa perangkat.

Lapisan `presentation` juga diuji di JVM: state builder event dipisah dari
composable sebagai fungsi murni, dan jamnya di-inject, sehingga aturan
"event yang sedang berjalan" bisa diverifikasi tanpa perangkat.

> Penting: CI yang hijau tidak menjamin sebuah layar punya isi. Konten seed
> pernah membuat semua event berstatus "selesai" tanpa gagalkan satu pun test.
> Karena itu ada test yang memeriksa seed mengisi tiap bagian layar.

```bash
./gradlew :app:testDebugUnitTest
```

## Kontribusi

Lihat [CONTRIBUTING.md](CONTRIBUTING.md). Hal wajib: satu file satu tanggung
jawab, maksimal 150 baris per file, maksimal lima file langsung per folder,
tanpa circular dependency, dan setiap perubahan logika disertai test.

## Lisensi

Proyek ini berlisensi MIT — lihat [LICENSE](LICENSE).

## Disclaimer

Konten permainan bersumber dari materi publik Genshin Impact. Teyvat Hub bukan
produk resmi HoYoverse dan tidak didukung oleh HoYoverse.
