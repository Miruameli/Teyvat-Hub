# Changelog

Semua perubahan penting pada proyek ini dicatat di berkas ini.
Format mengikuti [Keep a Changelog](https://keepachangelog.com/) dan
[Semantic Versioning](https://semver.org/).

## [Belum dirilis]

### Added
- Konfigurasi build migrasi ke AGP 9.1.1 dengan built-in Kotlin
  (lihat `docs/adr/0001-agp9-built-in-kotlin.md`).
- Arsitektur berlapis `domain`, `data`, `application`, `presentation`,
  `injection` dalam satu modul Gradle
  (`docs/adr/0002-modular-layered-architecture.md`).
- Basis data Room untuk karakter, senjata, tim, checklist, dan event beserta
  DAO reaktif berbasis Flow.
- Aset digital Genshin dari CDN Enka dalam varian teroptimasi
  (`docs/adr/0003-optimized-genshin-digital-assets.md`).
- Penggabungan konten awal saat pertama install melalui `DatabaseSeeder`.
- Layar daftar karakter dengan pencarian, filter favorit, dan avatar asli.
- Layar kalender event dan banner yang membaca baris Room, memisahkan
  event berlangsung, akan datang, dan selesai berdasarkan jam perangkat
  (`EventRepository`, `EventCalendarViewModel`).
- Layar team builder untuk memilih maksimal empat karakter, membaca catatan
  sinergi, dan menyimpan tim secara lokal (`TeamRepository`,
  `TeamBuilderViewModel`).
- Aturan domain yang teruji: reset checklist harian/mingguan, peringkat
  pencarian, validasi tim, dan analisis sinergi tim.
- Dokumentasi: README, CONTRIBUTING, SECURITY, LICENSE, CODE_OF_CONDUCT,
  glosarium, runbook, dan tiga ADR.

### Changed
- `MainActivity` dimigrasikan dari Java/AppCompat ke Kotlin + Jetpack Compose.
- Batas Java/Kotlin diseragamkan ke 17 dan `compileSdk` dinaikkan ke 36.
- Jendela event dan banner di-seed relatif terhadap waktu saat aplikasi
  dibuka, bukan tanggal absolut, sehingga kalender selalu memuat event
  berjalan, akan datang, dan selesai.

### Security
- Tidak ada kredensial maupun secret dalam repositori; aset dimuat dari CDN
  publik di luar APK sehingga ukuran artefak tetap kecil.

[Belum dirilis]: https://github.com/example/teyvat-hub
