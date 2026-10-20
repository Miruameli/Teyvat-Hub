# Runbook

Prosedur operasional umum untuk Teyvat Hub.

## Build APK Debug

```bash
./gradlew :app:assembleDebug
```

Keluarannya: `app/build/outputs/apk/debug/app-debug.apk`.

Untuk menempatkan APK di root project guna siap dipindahkan ke penyimpanan
perangkat:

```bash
./scripts/copy-apk-to-storage.sh     # menghasilkan ./TeyvatHub-debug.apk
```

Catatan: `/sdcard` hanya dapat diakses ketika build dijalankan dari lingkungan
Android/Termux. Di lingkungan PRoot shared storage tidak di-mount, jadi gunakan
salinan di root project lalu copy manual.

## Mengambil APK dari CI

Build lokal tidak menjadi jalur distribusi; CI yang membangun dan mengunggah
APK. Pada halaman Actions, pilih run terbaru untuk branch yang diinginkan,
buka job **Build Debug APK**, lalu unduh artifact:

| Artifact | Isi | Dipakai untuk |
| --- | --- | --- |
| `TeyvatHub-debug-apk` | `TeyvatHub-debug.apk` (sudah di root project) | disalin ke perangkat |
| `app-debug-apk` | `app-debug.apk` (output mentah Gradle) | pemeriksaan teknis |

Artifact `TeyvatHub-debug-apk` memakai nama file yang sama dengan hasil
`scripts/copy-apk-to-storage.sh`, jadi langkah penisipannya ke perangkat
identik. File di root project tidak ikut ter-commit karena `.gitignore`
mengecualikan `*.apk`.

## Paket Rilis

Job **Release Package** menyusun satu paket berisi APK, checksum, dan SBOM:

| File | Isi |
| --- | --- |
| `TeyvatHub-debug.apk` | binary debug |
| `TeyvatHub-debug.apk.sha256` | checksum SHA-256 untuk verifikasi unduhan |
| `TeyvatHub-debug.apk.sbom.json` | SBOM CycloneDX 1.5 |

SBOM dibuat oleh `scripts/generate-sbom.mjs` dari `gradle/libs.versions.toml`,
yang sudah mem-pin setiap versi secara eksak. Karena sumbernya file di repo,
hasilnya deterministik dan dapat diverifikasi dengan diff. Komponen yang
versinya dikelola BOM platform tidak diklaim versinya; komponen tersebut
ditandai dengan `bom-ref` berakhiran `?version-managed` beserta properti
yang menjelaskan asalnya.

Untuk memperbarui SBOM secara lokal:

```bash
node scripts/generate-sbom.mjs ./sbom.json
```

## Logo Aplikasi

Emblem launcher berasal dari satu sumber: `docs/branding/ic_launcher_source.svg`.
Berkas SVG **tidak boleh** diletakkan di `app/src/main/res` karena AAPT2
menolak SVG dan build akan gagal.

```bash
scripts/render-launcher-icons.sh    # menghasilkan ulang PNG tiap density
```

API 26 ke atas memakai adaptive icon dari `mipmap-anydpi-v26`, sedangkan API
24 dan 25 memakai PNG hasil render. Keduanya memakai warna latar yang sama
(`#16233F`), jadi ikon tidak berbeda antar versi Android.

## Environment Terbatas RAM

Jika build mati mendadak (proses Java dibunuh tanpa pesan error Kotlin):

1. Cek memori: `free -m`.
2. Hentikan daemon Gradle: `./gradlew --stop`.
3. Jalankan satu worker saja (sudah disetel di `gradle.properties`):
   `org.gradle.workers.max=1`, `org.gradle.parallel=false`.
4. Gunakan `--no-daemon` agar tidak ada dua JVM Gradle berjalan bersamaan:
   `./gradlew :app:assembleDebug --no-daemon`.

## Upgrading AGP atau Kotlin

AGP 9.x memasang rapat versi minimum berikut:

| Komponen | Batas |
| --- | --- |
| AGP | 9.1.1 |
| Gradle | 9.3.1 |
| Kotlin Gradle Plugin | 2.2.10 (bundled; kita pakai 2.3.20) |
| KSP | 2.3.10 minimal untuk dukungan built-in Kotlin |
| compileSdk | 36 (versi library lebih baru meminta 37) |

Saat menaikkan Compose BOM, periksa versi `androidx.compose.ui` yang diikat.
BOM `2026.06.01` mengikat `ui 1.11.4` yang masih cocok dengan SDK 36; BOM lebih
baru mengikat versi yang menuntut compileSdk 37 dan build akan gagal.

## Update Konten Game

Konten disimpan sebagai seed di `app/src/main/java/com/teyvat/hub/data/`:

- `CharacterSeedContent.kt`
- `ChecklistSeedContent.kt`
- `EventSeedContent.kt`

Perubahan konten dilakukan di file-file tersebut agar tiap update berupa diff
yang mudah direview. Id baris bersifat stabil sehingga riwayat selesai pengguna
tidak hilang saat konten diperbarui.

## Migrasi Skema Database

Saat struktur tabel berubah:

1. Naikkan `version` di `TeyvatHubDatabase.kt`.
2. Tambahkan `Migration` dan daftarkan di `injection/DatabaseModule.kt`.
3. Aktifkan `exportSchema = true` agar skema ikut terverifikasi.

Jangan menghapus database pengguna untuk melewati migrasi.

## Aset Tidak Muncul di Layar

Jika kartu karakter tampil kosong:

1. Pastikan perangkat punya koneksi saat pemuatan pertama; Coil menyimpan hasil
   unduhan di disk cache 64 MB.
2. Cek nama karakter di seed cocok dengan slug Enka; alias yang belum terdaftar
   akan resolve ke `null` dan UI sengaja menampilkan fallback, bukan gambar rusak.
3. Ukuran varian aset mengikuti ADR 0003; jangan mengganti ke
   `UI_Gacha_AvatarImg_` (1.5 MB) karena menyebabkan OOM bitmap.
