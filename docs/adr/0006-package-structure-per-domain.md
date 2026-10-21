# ADR 0006: Struktur Paket Per Domain Fungsional

- **Status**: Accepted
- **Tanggal**: 2026-10-21
- **Penulis**: Engineering Lead
- **Supersedes**: —

## Konteks

ADR 0002 menetapkan satu modul Gradle dengan lapisan paket `domain`, `data`,
`application`, `presentation`, dan `injection`. Struktur itu benar pada
lapisan, tetapi di dalam lapisan domain dan presentation pembagiannya hanya
mengikuti tipe file, bukan domain yang diwakili.

Akibatnya `domain/model/` menampung tujuh file dari lima domain berbeda dan
`domain/service/` menjadi tempat semua aturan dikumpulkan. Folder
`presentation/` tumbuh satu folder per layar, sehingga strukturnya berubah
setiap kali ada layar baru, dan paket `shared` muncul hanya untuk satu
berkas konstanta.

Pemeriksaan quality gate pada 2026-10-21 menemukan tiga pelanggaran batas
lima entri langsung, dan satu import dari `presentation` ke `data` yang
melanggar arah ketergantungan.

## Keputusan

Pembagian paket di dalam lapisan mengikuti **domain fungsional**, bukan tipe
file, dan jumlah entri per folder dibatasi lima.

| Lapisan | Pembagian |
| --- | --- |
| `domain/` | satu folder per domain: `character`, `equipment`, `team`, `event`, `checklist` |
| `data/` | `local/` untuk tabel Room, `content/` untuk pembaca aset dan dataset seed, `mapper/`, `remote/` |
| `presentation/` | `feature/` untuk satu folder per layar, `core/` untuk navigasi dan tema |
| `application/` | repository dan pembentuk URL aset yang dipakai lintas lapisan |

Paket `shared` dihapus. Konstantanya pindah ke `data/`, karena isinya
menggambarkan konfigurasi cache gambar.

Pembentuk URL aset juga pindah dari `data/remote/` ke `application/`, karena
layer presentation memerlukannya dan presentation tidak boleh menyentuh
lapisan data secara langsung.

## Konsekuensi

Positif:

- Membuka `domain/` langsung menunjukkan domain apa saja yang ada.
- Layar berikutnya masuk ke `presentation/feature/` dan tidak mengubah
  bentuk lapisan.
- Arah ketergantungan antar lapisan dapat diperiksa dengan skrip, dan
  pemeriksaan itu melaporkan nol pelanggaran.

Negatif:

- Tipe yang dipakai bersama lebih dari satu domain, seperti `Element`, harus
  punya satu rumah yang jelas, di sini `domain/character/`. Penempatan itu
  pilihan, bukan hasil otomatis.
- Pembagian paket berubah lagi ketika ada domain baru, dan setiap perubahan
  menyentuh banyak import. Pemeriksaan pola diperlukan, bukan sekadar
  menghitung baris yang berubah pada diff.

## Alternatif yang Dipertimbangkan

| Alternatif | Alasan ditolak |
| --- | --- |
| Satu paket besar per lapisan | Persis kondisi sebelumnya; folder membengkak tanpa batas |
| Package per tipe file seperti sebelumnya | File dari domain berbeda bercampur, dan pembaca tidak bisa menebak domain mana yang seekinggi |
| Folder `shared` untuk semua yang lintas lapisan | Melanggar aturan batas entri, dan hanya berisi satu berkas |

## Verifikasi

Skrip pemeriksaan membaca setiap import di `app/src/main` dan memastikan tidak
ada lapisan yang mengimpor lapisan yang tidak boleh disentuh: hasilnya nol
pelanggaran. Pemeriksaan lain memastikan setiap deklarasi `package` cocok
dengan lokasi foldernya, dan hasilnya juga nol selisih.

Issue #15 merangkum ketiga PR yang menjalankan perubahan ini: #27 untuk
domain, #33 untuk data, dan #34 untuk presentation. Issue #36 untuk batas
lapisan yang terungkap saat audit.

## Referensi

- docs/adr/0002-modular-layered-architecture.md
- issue #15
- issue #36
