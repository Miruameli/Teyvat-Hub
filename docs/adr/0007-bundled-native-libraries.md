# 7. Dua pustaka native dibundel tanpa di-strip

## Status

Diterima.

## Konteks

Build release menampilkan peringatan berikut setiap kali:

```
Unable to strip the following libraries, packaging them as they are:
libandroidx.graphics.path.so, libdatastore_shared_counter.so
Run with --info option to learn more.
```

Peringatan itu bukan kegagalan, sehingga mudah diabaikan. Tapi diam-diam
membiarkan dua berkas `.so` masuk APK tanpa preprocessing berarti debug info
ikut terbawa, dan ukurannya lebih besar dari yang seharusnya.

Issue #35 mengusulkan langkah pertama: "periksa apakah `datastore-preferences`
benar belum dipakai, dan kalau ya hapus dependensinya." Usulan itu diuji, dan
jawabannya bukan usulan itu.

## Keputusan

Kedua pustaka tetap, dan peringatan diterima sebagai keadaan yang dicatat
alih-alih dianggap utang yang harus dibuang.

### `libdatastore_shared_counter.so` tetap dipakai

Pemeriksaan langsung atas `app/src` menemukan `datastore-preferences` dipakai:
`application/language/LanguagePreference.kt` menyimpan bahasa yang dipilih
pemain lewat `preferencesDataStore`, dan itu sudah ikut pada PR #38. Klaim di
issue #35 — "DataStore saat ini belum dipakai fitur apa pun" — benar pada saat
issue itu ditulis dan **tidak lagi benar sekarang**. Karena itu dependensinya
tidak dapat dihapus, dan `.so` ini adalah bagian dari pustaka itu.

Satu-satunya cara menghilangkan pustaka ini adalah mengganti penyimpanan
bahasa, misalnya kembali ke SharedPreferences. Pertukaran itu menukar pustaka
native yang kecil untuk API yang tidak menghiburkan, dan itu bukan pekerjaan
yang layak ditukar.

### `libandroidx.graphics.path.so` adalah dependensi transitif

Pustaka ini tidak dideklarasikan di `gradle/libs.versions.toml`. Ia masuk
melalui `androidx.compose.ui:ui-graphics`, yang memakainya untuk `Path` dan
bentuk membulat di seluruh UI. Menghilangkannya berarti mengurangi Compose
bukan berarti menghapus sebuah dependensi.

## Dampak

- Ukuran APK sedikit lebih besar daripada ideal karena debug info di dua
  pustaka tersebut tidak di-strip.
- Angka pastinya belum diukur di lingkungan ini. Lingkungan kerja ini tidak
  punya Android SDK, jadi Gradle tidak dapat dijalankan dan APK tidak dapat
  dibangun atau diukur secara lokal. Ukuran perlu dibaca dari APK rilis hasil
  CI, dan angka itu sengaja tidak ditebak di sini.
- Peringatan akan tetap muncul di log build. Ia kini punya alasan tertulis,
  sehingga tidak perlu diselidiki ulang setiap kali muncul.

## Alternatif yang ditolak

| Alternatif | Alasan ditolak |
| --- | --- |
| Menghapus `datastore-preferences` | Pustaka itu dipakai; klaim yang mengatakan sebaliknya sudah usang. |
| Menghapus `ui-graphics` | Menghapus Compose bukan menutup satu pustaka. |
| Memfilter `.so` dari paket | Menghilangkan pustaka yang dibutuhkan saat runtime akan membuat aplikasi gagal dimuat. |
| Menambah `packagingOptions.jniLibs` untuk memperbaiki strip | Peringatannya berasal dari NDK strip yang tidak tersedia di runner CI, bukan dari konfigurasi proyek. Memaksa strip `false` hanya menyembunyikan masalah. |
