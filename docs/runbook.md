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
| `TeyvatHub-debug.apk.sig` | tanda tangan GPG terpisah, hanya bila kunci sudah dikonfigurasi |

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

## Verifikasi Paket Rilis

Setiap paket rilis memuat APK, checksum SHA-256, SBOM, dan — kalau kunci GPG
sudah dikonfigurasi — tanda tangan terpisah. Verifikasi checksum terlebih
dahulu, lalu tanda tangannya:

```bash
sha256sum -c TeyvatHub-debug.apk.sha256
gpg --verify TeyvatHub-debug.apk.sig TeyvatHub-debug.apk
```

Kalau kunci rilis belum dikonfigurasi, berkas `.sig` tidak dibuat dan CI
memberi `::notice::`, bukan tanda tangan palsu. Rilis yang demikian hanya
dapat diverifikasi lewat checksum.

Mengonfigurasi kunci:

1. Buat kunci rilis di luar repositori, lalu ekspor kunci privatnya:

   ```bash
   gpg --full-generate-key          # RSA 3072, tanpa expiry, atau sesuai kebijakan
   gpg --armor --export-secret-keys <KEY_ID>    # isi untuk secret teks
   ```

2. Simpan dua secret: `TEYVAT_GPG_PRIVATE_KEY` berisi keluaran ekspor di
   atas, dan `TEYVAT_GPG_KEY_ID` berisi id panjang kunci:

   ```bash
   gh secret set TEYVAT_GPG_PRIVATE_KEY   # tempel keluaran ekspor
   gh secret set TEYVAT_GPG_KEY_ID -b"0123456789ABCDEF0123456789ABCDEF01234567"
   ```

3. Publikasikan kunci **publik** kembali supaya pengguna dapat memverifikasi.
   Tanpa itu, tanda tangan tidak membuktikan apa-apa: gpg hanya menyatakan
   bahwa berkas ditandatangani oleh kunci yang fingerprint-nya tidak dikenal.

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

Karakter dan senjata dibaca dari bundel JSON, bukan dari seed di kode:

- `app/src/main/assets/characters/characters.json`
- `app/src/main/assets/weapons/weapons.json`
- `app/src/main/assets/elements/elements.json`
- `app/src/main/assets/regions/regions.json`

`DatabaseSeeder` menyusunnya ke Room saat aplikasi pertama berjalan, dan
hanya menambah baris yang belum ada. Efeknya: mengubah isi bundel adalah diff
konten, bukan kompilasi, dan riwayat pengguna — termasuk favorit — tetap utuh.
Baris dengan key yang tidak dikenal **dibuang**, bukan ditulis di bawah nilai
fallback, karena karakter dengan elemen yang salah adalah cacat yang terlihat
pemain.

Checklist, event, dan template tim tetap seed hardcoded di
`app/src/main/java/com/teyvat/hub/data/content/seeding/`, karena tidak punya
padanan JSON. Template tim wajib memakai id yang benar-benar ada di bundel;
member yang merujuk karakter tidak ada akan tampil sebagai slot rusak.

## Menandatangani Rilis APK

Build release memakai keystore yang disimiap sebagai rahasia. Keystore dan kata
kuncinya **tidak boleh** masuk repositori.

Input yang dibaca `app/build.gradle.kts`, dari environment atau properti Gradle:

| Nama | Environment | Properti Gradle |
| --- | --- | --- |
| Jalur keystore | `TEYVAT_KEYSTORE_PATH` | `teyvat.keystore.path` |
| Kata kunci store | `TEYVAT_KEYSTORE_PASSWORD` | `teyvat.keystore.password` |
| Alias key | `TEYVAT_KEY_ALIAS` | `teyvat.key.alias` |
| Kata kunci key | `TEYVAT_KEY_PASSWORD` | `teyvat.key.password` |

Keempatnya wajib ada bersamaan. Kalau ada yang kosong, build release tetap
berjalan tetapi **tetap tanpa tanda tangan** — tidak ada fallback ke key debug,
karena artefak yang terlihat seperti rilis tetapi tidak dapat diverifikasi lebih
berbahaya daripada artefak yang jujur belum ditandatangani.

Untuk CI, simpan keempat nilai di GitHub Secrets:

```bash
gh secret set TEYVAT_KEYSTORE_PATH -b"app/release.keystore"
gh secret set TEYVAT_KEYSTORE_PASSWORD        # isi diminta interaktif
gh secret set TEYVAT_KEY_ALIAS -b"teyvat-release"
gh secret set TEYVAT_KEY_PASSWORD             # isi diminta interaktif
```

Isi keystore-nya sendiri harus diunggah sebagai artefak rahasia terpisah
(atau ditulis ulang dari sumber pemilik repositori) karena secret teks tidak
cocok untuk berkas biner.

Memperbarui keystore:

1. Buat keystore baru di luar repo, misalnya `~/.keystores/teyvat-release.jks`:

   ```bash
   keytool -genkeypair -v -keystore ~/.keystores/teyvat-release.jks \
     -alias teyvat-release -keyalg RSA -keysize 2048 -validity 10000
   ```

2. Verifikasi tanda tangan hasil build sebelum dipublikasikan:

   ```bash
   apksigner verify --print-certs --verbose \
     app/build/outputs/apk/release/app-release.apk
   ```

3. Perbarui nilai secret yang berubah, lalu picu ulang run CI. APK rilis yang
   tanda tangannya tidak dapat diverifikasi tidak dipublikasikan.

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
