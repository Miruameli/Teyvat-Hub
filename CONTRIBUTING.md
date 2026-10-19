# Panduan Kontribusi

Terima kasih berkontribusi pada Teyvat Hub. Proyek ini diberlakukan dengan
standar perekayasa profesional: kualitas dan keamanan lebih diutamakan daripada
kecepatan.

## Aturan Wajib

- Tidak ada push langsung ke `main`. Semua perubahan lewat pull request.
- Setiap pull request harus menautkan satu issue.
- Setiap perubahan logika disertai test yang gagal sebelum perbaikan dan lulus
  sesudahnya.
- Keamanan mutlak: tidak ada secret, kredensial, atau data pengguna yang
  di-commit.

## Struktur Kode

- Satu file satu tanggung jawab.
- Maksimal **150 baris** per file; besarnya itu harus dipecah.
- Maksimal **5 file langsung** per folder dan maksimal **5 subfolder**.
- Tidak ada circular dependency antar-lapis.
- Baris kosong maksimal satu secara berurutan; tanpa trailing whitespace.
- Komentar menjelaskan **mengapa**, bukan memparafrasekan kode.
- Commit mengikuti Conventional Commit: subject imperatif maksimal 72 karakter.

## Alur Kerja

1. Ambil issue atau buat baru bila belum ada.
2. Buat branch dengan prefix: `feat/`, `fix/`, `docs/`, `refactor/`, `chore/`.
3. Implementasi perubahan beserta test.
4. Jalankan verifikasi lokal sebelum push:

```bash
./gradlew :app:testDebugUnitTest   # unit test
./gradlew :app:lintDebug           # lint
./gradlew :app:assembleDebug       # build
```

5. Buka pull request yang menautkan issue, lengkapi ringkasan, alasan, dan dampak.
6. Tunggu review dan CI hijau sebelum merge.

## Setup Lokal

Lihat [README.md](README.md) bagian "Build dan Jalankan". Waktu setup yang
diharapkan untuk kontributor baru adalah di bawah lima menit setelah
prasyarat JDK 17 dan Android SDK terpasang.

## Pelaporan Masalah

Laporkan bug melalui issue dengan melampirkan langkah reproduksi, perilaku yang
diharapkan, perilaku aktual, dan versi aplikasi. Jangan lampirkan data pribadi.

## Keamanan

Bila Anda menemukan celah keamanan, jangan membuat issue publik. Ikuti instruksi
di [SECURITY.md](SECURITY.md).

## Kode Etik

Semua partisipan wajib menjaga lingkungan yang hormat. Lihat
[CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md).
