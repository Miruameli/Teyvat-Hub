# Changelog

Semua perubahan penting pada proyek ini dicatat di berkas ini.
Format mengikuti [Keep a Changelog](https://keepachangelog.com/) dan
[Semantic Versioning](https://semver.org/).

## [1.0.0] - 2026-10-20

Rilis pertama Teyvat Hub. Aplikasi kompanion offline-first untuk pemain
Genshin Impact berbahasa Indonesia: daftar karakter, checklist farming,
team builder, kalender event, dan pengaturan.

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
- Layar pengaturan berisi atribusi sumber data, kebijakan privasi, versi
  aplikasi, dan disclaimer.
- Aturan domain yang teruji: reset checklist harian/mingguan, peringkat
  pencarian, validasi tim, dan analisis sinergi tim.
- Ikon launcher berupa emblem Anemo, sebagai adaptive icon (API 26+), varian
  bertema (API 33+), dan PNG hasil render (API 24-25).
- Paket rilis berisi binary APK, checksum SHA-256, dan SBOM CycloneDX 1.5.
- Dokumentasi: README, CONTRIBUTING, SECURITY, LICENSE, CODE_OF_CONDUCT,
  glosarium, runbook, dan lima ADR.

### Changed
- `MainActivity` dimigrasikan dari Java/AppCompat ke Kotlin + Jetpack Compose.
- Batas Java/Kotlin diseragamkan ke 17 dan `compileSdk` dinaikkan ke 36.
- Jendela event dan banner di-seed relatif terhadap waktu saat aplikasi
  dibuka, bukan tanggal absolut, sehingga kalender selalu memuat event
  berjalan, akan datang, dan selesai.
- Nama elemen dan peran yang tampil di layar memakai string resource
  berbahasa Indonesia, bukan kunci internal seperti `pyro` atau `main-dps`.

### Fixed
- Batas party size empat karakter ditegakkan saat seleksi, bukan hanya
  ketika menyimpan.
- Tim yang tersimpan dapat dibaca kembali dan dihapus dari layar; sebelumnya
  tabel `teams` tidak pernah diisi dan tidak ada yang bisa dilihat.
- 741 berkas hasil build yang pernah ter-track di git sekarang tidak lagi
  mengikuti repositori, sehingga `git status` bersih setelah build.

### Security
- Tidak ada kredensial maupun secret dalam repositori; aset dimuat dari CDN
  publik di luar APK sehingga ukuran artefak tetap kecil.
- Ikon dan konten aplikasi tidak pernah memanggil kode dinamis; seluruh
  input ke Room memakai kueri berparameter.
- Rilis v1.0.0 **belum ditandatangani** dengan GPG. Kunci signing adalah
  aset kredensial yang harus datang dari pemilik repositori, lihat issue #17.

[1.0.0]: https://github.com/Miruameli/Teyvat-Hub/releases/tag/v1.0.0
