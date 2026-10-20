# Glosarium

Daftar istilah yang dipakai di Teyvat Hub beserta artinya dalam konteks kode.
Komunitas Genshin Impact memakai istilah baku ini, jadi kode dan UI memakainya
sebagaimana adanya untuk menghindari terjemahan yang membingungkan.

## Istilah Permainan

| Istilah | Arti |
| --- | --- |
| **Banner** | Periode wish banner berbatas waktu untuk karakter atau senjata tertentu; memakai aturan tampilan yang sama dengan event |
| **Artifak** | Perlengkapan lima slot: Bunga Bulu, Ujung Ekor, Topi Waktu, Piala Cahaya, Jam Pasir |
| **Build** | Kombinasi senjata, artifak, dan prioritas talenta yang direkomendasikan untuk satu karakter |
| **Talent** | Kemampuan karakter: biasa, skill, burst, serta alternatif sprint/explorasi (constelasi) |
| **Sinergi** | Efek saling menguat antar karakter dalam satu tim, misalnya reaksi elemental |
| **Reset harian** | Batas harian yang direset server setiap pukul 04.00 UTC+8 (waktu server resmi) |
| **Farm** | Aktivitas mengumpulkan sumber daya berulang (domain, resin, boss) |
| **Event** | Periode event dalam game dengan tanggal mulai dan, bila diumumkan, tanggal berakhir |
| **Party** | Susunan karakter yang sedang dimainkan, maksimal empat anggota |
| **Berlangsung** | Status event yang sudah mulai dan belum melewati tanggal berakhirnya |
| **Akan datang** | Status event yang tanggal mulainya belum tercapai |

## Istilah Arsitektur

| Istilah | Arti |
| --- | --- |
| **Domain** | Lapisan model murni dan aturan bisnis; tidak bergantung Android |
| **Application** | Lapisan use case yang mengorkestrasi domain dan data |
| **Data** | Lapisan Room, DAO, repository, dan dataset seed |
| **Presentation** | Lapisan Jetpack Compose: tema, komponen, layar, ViewModel |
| **Injection** | Konfigurasi Hilt untuk dependency graph |
| **DAO** | Data Access Object, antarmuka Room untuk query database lokal |
| **Use case** | Satu operasi bisnis tunggal, diberi nama memakai verba |

## Istilah Keamanan

| Istilah | Arti |
| --- | --- |
| **Allowlist** | Daftar nilai yang diizinkan; hanya nilai di dalamnya diterima |
| **Secret** | Kredensial rahasia yang tidak boleh muncul di repositori mana pun |
| **SBOM** | Software Bill of Materials, daftar komponen dan versi dependensi |

## Konvensi Nama di Kode

- Paket memakai `kebab-case-free` berlapis (`domain`, `data`, `presentation`).
- Nama file dan kelas memakai `PascalCase`; fungsi dan properti `camelCase`.
- Satu file satu tanggung jawab; nama deskriptif, tanpa singkatan tidak lazim.
