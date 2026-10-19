# Kebijakan Keamanan

Kami memperlakukan keamanan sebagai prioritas tertinggi.

## Melaporkan Kerentanan

Jangan membuat issue publik untuk kerentanan keamanan. Laporkan secara privat
melalui GitHub Security Advisory pada repositori ini, atau hubungi pemelihara
proyek langsung.

Sertakan dalam laporan:

- Deskripsi kerentanan dan komponen yang terdampak
- Langkah reproduksi atau proof of concept
- Dampak potensial bagi pengguna
- Usulan mitigasi bila ada

## Waktu Respons yang Kami Targetkan

| Tingkat | Respon awal | Mitigasi |
| --- | --- | --- |
| Critical | 24 jam | Segera |
| High | 72 jam | Segera setelah triase |
| Medium | 1 minggu | Direncanakan |
| Low | 1 bulan | Backlog |

## Ruang Lingkup

Teyvat Hub adalah aplikasi fan project yang sebagian besar berjalan offline.
Fokus keamanan utama adalah integritas repositori, kerawanan dependensi,
keamanan data pengguna di perangkat, dan keaslian artefak rilis.

## Praktik Wajib

- Tidak ada kredensial, token, API key, atau secret yang di-commit ke repositori.
- Semua dependensi dipin pada versi eksak dan diperiksa terhadap advisory.
- Validasi input dengan allowlist, bukan deny-list.
- Data pengguna hanya disimpan lokal di perangkat dengan metode resmi Android.
- Entry point yang terekspos wajib diproteksi sesuai kebutuhan.

## Versi yang Didukung

Hanya rilis terbaru pada branch `main` yang mendapatkan perbaikan keamanan.
