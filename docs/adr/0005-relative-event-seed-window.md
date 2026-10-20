# ADR 0005: Jendela Event di-Seed Relatif terhadap Waktu Aplikasi

- **Status**: Accepted
- **Tanggal**: 2026-10-20
- **Penulis**: Engineering Lead
- **Supersedes**: —

## Konteks

Layar kalender event menampilkan tiga bagian: sedang berlangsung, akan
datang, dan sudah selesai. Status tiap baris dihitung dari `startsAt` dan
`endsAt` yang disimpan di Room, dibandingkan terhadap jam perangkat.

Konten awal event mulanya ditulis sebagai tanggal absolut. Nilai anchor yang
tertulis di komentar kode menyebut 2026-10-18, sementara nilai yang sebenarnya
ternilai 2026-09-28 — selisih dua puluh hari yang tidak terdeteksi karena
komentar dan nilai tidak pernah dibandingkan.

Pada 2026-10-20 seluruh baris seed sudah melewati tanggal berakhirnya. Bagian
"sedang berlangsung" dan "akan datang" kosong, dan pengguna hanya melihat
empat baris tanpa judul bagian. Build, lint, dan seluruh unit test tetap
hijau: tidak ada test yang memeriksa apakah seed mengisi setiap bagian layar.

Perbaikan dengan menggeser tanggal anchor hanya memindahkan masalah: jendela
seed akan habis lagi beberapa minggu kemudian, dan setiap kali itu perlu
perbaikan manual.

## Keputusan

Jendela event **dihitung relatif terhadap waktu saat basis data diisi**,
bukan dari tanggal absolut.

1. `EventSeedContent.all(now)` membangun seluruh baris dari instant yang
   diberikan, dengan jarak yang tetap: satu baris berjalan, satu banner akan
   datang, satu banner sudah selesai, dan satu event sudah selesai.
2. `DatabaseSeeder` menulis ulang baris event pada setiap kali aplikasi dibuka.
   Event tidak menyimpan state pengguna seperti item checklist, sehingga
   menimpa baris tersebut tidak menghilangkan apa pun.
3. `EventSeedContent.REFERENCE_NOW` tetap ada sebagai titik acuan yang
   didokumentasikan, dipakai test untuk memeriksa komposisi hasil seed tanpa
   bergantung pada jam mesin.

Test ditambahkan pada dua lapisan: satu memeriksa komposisi seed langsung,
satu lagi memeriksa state kalender yang dibangun dari baris seed tersebut
dan memastikan ketiga bagian terisi.

## Konsekuensi

Positif:

- Kalender selalu memuat event berjalan, akan datang, dan selesai, berapa pun
  tanggal aplikasi dijalankan.
- Tidak ada tanggal seed yang perlu digeser secara manual.
- Kegagalan seperti ini tertangkap test, bukan oleh pemeriksaan manual.

Negatif:

- Baris event berubah setiap aplikasi dibuka, sehingga isi tabel tidak
  identik antar waktu. Ini tidak berpengaruh karena event tidak menyimpan
  state pengguna.
- Tampilan "berakhir" tidak bisa merujuk pada tanggal historis yang tetap.

## Alternatif yang Dipertimbangkan

| Alternatif | Alasan ditolak |
| --- | --- |
| Menggeser tanggal anchor ke masa depan | Memindahkan masalah beberapa minggu ke depan; masalah yang sama berulang tanpa akhir |
| Membiarkan tanggal absolut dan memperbarui manual tiap bulan | Membutuhkan pemeliharaan berkala dan kembali ke keadaan yang sama saat deadline terlewat |
| Membuang baris yang sudah berakhir saat seed | Menghapus riwayat event dari pengguna; pengguna tidak bisa melihat event yang baru saja selesai |

## Verifikasi

`EventSeedContentTest` memeriksa bahwa seed menghasilkan baris pada setiap
status dan bahwa komposisi tetap sama walau seed dijalankan satu tahun kemudian.
`EventCalendarSeedMixTest` memeriksa hal yang sama pada jalur lengkap dari
baris seed sampai bagian layar, 즉 kondisi yang dulu lolos tanpa terdeteksi.

## Referensi

- Issue #2
- app/src/main/java/com/teyvat/hub/data/seeding/EventSeedContent.kt
- docs/runbook.md bagian "Update Konten Game"
