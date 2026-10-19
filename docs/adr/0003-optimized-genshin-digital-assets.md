# ADR 0003: Aset Digital Genshin yang Dioptimasi

- **Status**: Accepted
- **Tanggal**: 2026-10-18
- **Penulis**: Engineering Lead
- **Supersedes**: —

## Konteks

Pemain perlu mengenali karakter dari visual asli, bukan ikon buatan sendiri.
Sumber aset resmi permainan tersedia secara publik lewat CDN komunitas, terutama
Enka Network (`enka.network/ui/`) yang menyajikan berkas persis dari paket game.

Kendala nyata dari lingkungan pengembangan adalah **RAM sangat terbatas**. Host
hanya menyediakan sekitar 5.8 GB total dengan >=1.4 GB sudah dipakai runtime
environment agent, sehingga sekitar 2 GB yang tersedia; build pun pernah mati
karena OOM. Karena itu setiap kilobyte bitmap yang masuk ke heap sangat berarti.
Aplikasi juga *offline-first*, jadi aset wajib tetap tampil setelah perangkat
kehilangan jaringan.

Hasil pengukuran nyata terhadap tiga varian avatar yang sama:

| Berkas (Enka) | Ukuran | Catatan |
| --- | --- | --- |
| `UI_AvatarIcon_Side_<Nama>.png` | **12.7 KB** | Half-body, cocok untuk baris list |
| `UI_AvatarIcon_<Nama>.png` | **34.3 KB** | Potret, cocok untuk kartu/detail |
| `UI_Gacha_AvatarImg_<Nama>.png` | **1.52 MB** (2048x1024) | Terlalu besar untuk heap perangkat kelas bawah |

## Keputusan

1. **Sumber aset**: Enka Network CDN (`https://enka.network/ui/`), karena
   deterministik, terukur cepat (HTTP 206 partial content), dan berisi aset asli
   permainan.
2. **Pilih varian sesuai peran tampilan**, jangan gunakan satu ukuran untuk semua:
   - Baris daftar karakter/elemen: `UI_AvatarIcon_Side_<Nama>.png` (12.7 KB).
   - Kartu dan halaman detail: `UI_AvatarIcon_<Nama>.png` (34.3 KB).
   - `UI_Gacha_AvatarImg_` **tidak dipakai** karena 1.52 MB PNG akan menjadi
     sumber utama OOM bitmap di perangkat kelas bawah.
3. **Nama berkas dari `name` karakter**: aset di-resolve lewat helper yang
   menormalkan nama menjadi form Enka (mis. `"Raiden Shogun"` → `"Raiden"`,
   `"Kaedehara Kazuha"` → `"Kazuha"`), bukan kolom database baru. Nama panggilan
   berbeda dari nama lengkap ditangani melalui peta alias eksplisit.
4. **Coil 3 dengan batas memori eksplisit**: cache disk terbatas 64 MB untuk
   aset karakter, cache maksimum perangkat, dan ukuran bitmap dibatasi agar
   dekoder tidak lagi memuat resolusi penuh ke heap.
5. **Fallback aman**: selalu ada placeholder berbasis warna elemen bila jaringan
   dan cache dua-satunya tidak memiliki berkas; aplikasi tidak pernah menampilkan
   layar kosong.
6. **Lisensi dan atribusi**: aset adalah milik HoYoverse, dipakai sebagai fan
   project. README, SECURITY, dan layar Pengaturan menyebutkan atribusi dan
   status non-resmi agar kewajiban legal terpenuhi.

## Konsekuensi

Positif:

- Ukuran bitmap terkendali, risiko OOM turun drastis dari 1.52 MB ke 12.7 KB
  per baris list.
- Waktu pemuatan dan konsumsi data lebih rendah di jaringan seluler Indonesia.
- Tampak asli dan otentik bagi pemain.

Negatif:

- Bergantung pada CDN pihak ketiga untuk konten yang bukan bagian APK. Selama
  CDN gagal, UI memakai fallback berbasis elemen.
- Perlu pemeliharaan peta alias bila nama aset Enka berubah atau karakter baru
  memakai form nama yang tidak terduga.

## Alternatif yang Dipertimbangkan

| Alternatif | Alasan ditolak |
| --- | --- |
| Bundling semua aset ke APK | Menaikkan ukuran APK puluhan MB; tidak mungkin di lingkungan RAM terbatas |
| Mengunduh semua aset sekali lalu simpan | 1.52 MB per karakter tetap keliru; tetap tinggi ukuran cache |
| Menggambar ikon sendiri | Kehilangan pengenalan visual yang menjadi alasan pemain memakai aplikasi |

## Verifikasi

Ketersediaan dan ukuran setiap varian sudah diverifikasi langsung terhadap CDN
dengan permintaan range (HTTP 206) dan pengukuran byte. Pemakaian wire Coil
divalidasi pada layar daftar karakter dengan mengurangi ukuran dekode bitmap.

## Referensi

- https://enka.network/
- PRODUk.md
