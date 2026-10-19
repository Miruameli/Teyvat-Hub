# ADR 0002: Arsitektur Modular Berlapis

- **Status**: Accepted
- **Tanggal**: 2026-10-18
- **Penulis**: Engineering Lead
- **Supersedes**: —

## Konteks

Teyvat-Hub adalah aplikasi kompanion Genshin Impact untuk pasar Indonesia yang
mencakup enam domain fungsional per PRODUk.md: onboarding, database karakter &
build, team builder, checklist farming, kalender event/banner, dan pengaturan.
Tim pengembangan berskala mulai dari satu engineer, jadi struktur harus mampu
dikembangkan mandiri tanpa koordinasi besar, sekaligus menjaga kualitas agar
layak dipelihara enam bulan ke depan.

Dua alternatif struktur dipikirkan: satu modul tunggal dengan pemisahan paket
saja, atau multi-modul Gradle penuh.

## Keputusan

Kami memakai **satu modul Gradle `:app` dengan pemisahan paket berlapis tegas**,
di dalam `com.teyvat.hub`. Lapisan-lapisannya:

| Paket | Tanggung jawab |
| --- | --- |
| `domain` | Model murni dan logika bisnis: entitas, aturan sinergi tim, reset checklist, peringkat pencarian |
| `data` | Sumber data lokal: entitas Room, DAO, implementasi repository, dataset seed |
| `application` | Orkestrasi: use case yang menghubungkan domain dan data |
| `presentation` | UI Compose: tema, komponen, layar, ViewModel |
| `injection` | Konfigurasi Hilt: modul, `@HiltAndroidApp` |
| `shared` | Utilitas bersama lintas lapisan: konstanta, helper format |

Aturan ketergantungan: `presentation` → `application` → `domain` ← `data`.
Domain tidak bergantung pada Android framework maupun Room sehingga teruji
murni pada JVM tanpa emulator.

Pemisahan modul Gradle penuh sengaja **tidak** dilakukan sekarang karena
semua kode hidup dalam satu artefak APK dengan satu basis kode. Multi-modul
akan diadopsi bila muncul kebutuhan yang jelas seperti reuse lintas platform
atau batas kepemilikan tim.

## Konsekuensi

Positif:

- Pengujian domain cepat dan deterministik tanpa dependensi Android.
- Batas kepemilikan jelas; perubahan UI tidak menyentuh aturan bisnis.
- Build tetap sederhana dengan satu modul.

Negatif:

- Batas antar-lapis bersifat konvensi, bukan aksesibilitas Kompilator, sehingga
  pelanggaran harus dijaga lewat review dan pemeriksaan berkala.

## Alternatif yang Dipertimbangkan

| Alternatif | Alasan ditolak |
| --- | --- |
| Satu modul Gradle terpisah per lapisan | Overhead konfigurasi dan siklus build bagi basis kode satu APK; ditunda sampai ada reuse nyata. |
| Tanpa pemisahan lapisan (semua di paket tunggal) | Tidak memenuhi kebutuhan keterpeliharaan jangka menengah dan menyulitkan pengujian pure-JVM. |

## Verifikasi

Struktur dibuat pada repositori dan dikonfirmasi terbaca melalui pemeriksaan
struktur direktori. Pengujian domain akan divalidasi pada tahap pengujian dengan
`./gradlew :app:testDebugUnitTest`.

## Referensi

- https://developer.android.com/topic/architecture
- PRODUk.md
