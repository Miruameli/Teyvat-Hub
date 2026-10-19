Versi: 0.1 — Draft
Owner: Mysam (Miruameli)
Produk: Teyvat-Hub
Kategori: Fan-made companion guide untuk Genshin Impact
Platform: Mobile-first — Android & iOS
Bahasa: Indonesia
Disclaimer: Teyvat-Hub adalah proyek fan-made, tidak berafiliasi dengan HoYoverse.

---

1. Deskripsi Produk

Teyvat-Hub adalah aplikasi pendamping untuk pemain Genshin Impact yang menyediakan panduan ringkas, cepat dicari, dan mudah dipakai—mulai dari build karakter, rekomendasi senjata dan artefak, contoh komposisi tim, checklist farming harian/mingguan, sampai kalender event dan banner.

Teyvat-Hub bukan wiki raksasa. Fokusnya adalah menjadi “jawaban cepat” untuk kebutuhan harian pemain:

· “Build karakter ini gimana?”
· “Artefak dan stat priority-nya apa?”
· “Hari ini harus farming apa?”
· “Tim yang cocok buat karakter ini siapa?”
· “Event atau banner berikutnya kapan?”

Target awalnya adalah pemain Indonesia, mobile-first, dari pemain baru sampai mid-core.

---

2. Masalah yang Diselesaikan

Pemain Genshin Impact sering harus membuka banyak sumber terpisah: wiki, YouTube, Reddit, Discord, situs build, spreadsheet, dan aplikasi lain. Akibatnya:

1. Informasi tersebar dan tidak konsisten.
2. Banyak panduan berbahasa Inggris atau terlalu panjang.
3. Build bisa outdated setelah patch baru.
4. Pemain baru dan returning player bingung harus mulai dari mana.
5. Checklist farming masih manual.
6. Aplikasi existing terasa berat atau terlalu kompleks.

Teyvat-Hub hadir untuk merapikan itu menjadi satu pengalaman yang ringkas, cepat, dan berbahasa Indonesia.

---

3. Target Pengguna

Pemain Baru

AR 1–30, bingung prioritas farming, build, dan tim. Butuh panduan dasar yang tidak overwhelming.

Returning Player

Pernah berhenti dan balik lagi. Butuh catch-up cepat soal karakter baru, artefak baru, dan event aktif.

Mid-core Player

AR 45+, sudah paham dasar, butuh optimasi build, tim, dan efisiensi resin.

Content Creator / Helper

Sering bantu teman atau komunitas. Butuh referensi cepat yang mudah dibagikan.

---

4. Value Proposition

Teyvat-Hub membantu pemain Genshin Impact menemukan panduan build, tim, dan checklist farming dalam hitungan detik—dalam Bahasa Indonesia, ringkas, dan mobile-friendly.

Diferensiasi:

1. Indonesia-first.
2. Ringkas dan actionable.
3. Personalisasi sederhana: pilih karakter yang dimiliki, lalu dapat rekomendasi relevan.
4. Checklist harian/mingguan.
5. Offline-friendly untuk data inti.
6. Transparan soal sumber, tanggal update, dan versi patch.

---

5. Prinsip Desain Produk

1. Cepat ketemu jawaban — maksimal 3 tap dari Home ke info utama.
2. Ringkas tapi akurat — tampilkan rekomendasi utama dulu, detail belakangan.
3. Transparan — cantumkan sumber, tanggal update, dan versi patch.
4. Mobile-first — desain untuk satu tangan dan koneksi tidak stabil.
5. Tidak memaksa login — core experience bisa dipakai tanpa akun.
6. Fan-made yang bertanggung jawab — hormati IP, aset, dan komunitas.

---

6. Scope Fitur MVP

Area Fitur
Onboarding Pilih karakter yang dimiliki, pilih fokus pemain
Karakter Database karakter, role, build rekomendasi, stat priority, talent priority
Senjata Rekomendasi senjata per karakter
Artefak Set rekomendasi, main stat, substat priority
Tim Team builder sederhana dan contoh komposisi
Checklist Daily/weekly checklist manual
Event Kalender event dan banner sederhana
Pencarian Search global + filter
Favorit Simpan karakter/guide
Settings Bahasa, disclaimer, sumber data

Di luar MVP: sinkronisasi akun, API real-time, damage calculator advanced, social feed, UGC penuh, marketplace, aplikasi desktop.

---

7. User Journey Utama

First-time user

Install → onboarding → pilih karakter → Home personal → buka build karakter → favorit → buka checklist.

Returning player

Buka app → lihat event/banner aktif → buka checklist catch-up → cek karakter baru → simpan guide.

Mid-core player

Search karakter → bandingkan senjata/artefak → buat tim → simpan tim → gunakan checklist farming.

---

8. Information Architecture

Bottom navigation awal:

1. Home — rekomendasi, event aktif, checklist ringkas.
2. Karakter — database dan build.
3. Tim — team builder dan contoh komposisi.
4. Checklist — daily/weekly.
5. Lainnya — favorit, event, settings, disclaimer.

Global search tersedia di Home dan halaman database.

---

9. Desain Layar Utama

9.1 Onboarding

Tujuan: memberi personalisasi awal tanpa memaksa.

Isi layar:

· Sapaan singkat dan penjelasan produk.
· Pilih karakter yang dimiliki.
· Pilih fokus utama: farming, build, event, atau tim.
· Tombol “Lewati”.
· Tombol “Mulai”.

Prinsip: maksimal 2–3 langkah, bisa skip, pilihan bisa diubah di Settings.

9.2 Home

Tujuan: menjadi pusat aktivitas harian.

Isi layar:

· Search bar di atas.
· Kartu “Event Aktif”.
· Checklist ringkas hari ini.
· Rekomendasi karakter/build berdasarkan onboarding.
· Shortcut ke Karakter, Tim, dan Checklist.

Prinsip: informasi paling penting terlihat tanpa scroll berlebihan.

9.3 Halaman Karakter

Tujuan: menjawab “build karakter ini gimana?” secepat mungkin.

Isi layar:

· Header: nama, elemen, senjata, rarity, role.
· Build utama: senjata, artefak, main stat, substat priority.
· Talent priority.
· Contoh tim.
· Sumber, last_updated, dan versi patch.
· Tombol favorit.

Prinsip: rekomendasi utama dulu, detail belakangan.

9.4 Database Senjata & Artefak

Tujuan: memudahkan eksplorasi dan perbandingan.

Isi layar:

· Search dan filter.
· List senjata/artefak.
· Detail item.
· Karakter yang cocok.

9.5 Team Builder

Tujuan: membantu menyusun tim sederhana.

Isi layar:

· Pilih 4 karakter.
· Catatan sinergi dasar.
· Contoh tim rekomendasi.
· Simpan tim.

Prinsip: tidak mengklaim kalkulasi damage presisi.

9.6 Checklist

Tujuan: membantu rutinitas farming.

Isi layar:

· Daily checklist.
· Weekly checklist.
· Custom item.
· Reset otomatis harian/mingguan.

Prinsip: tersimpan lokal tanpa login.

9.7 Event & Banner

Tujuan: memberi informasi jadwal ringkas.

Isi layar:

· Event aktif, akan datang, selesai.
· Filter.
· Disclaimer bahwa jadwal bisa berubah.

9.8 Search

Tujuan: menemukan apa pun dengan cepat.

Isi layar:

· Search global.
· Filter kategori.
· Empty state yang membantu.
· Hasil untuk karakter, senjata, artefak, dan event.

9.9 Favorit & Settings

Favorit: simpan karakter, build, atau tim tanpa login.
Settings: bahasa, disclaimer, sumber data, kebijakan privasi, versi aplikasi.

---

10. Struktur Konten

Character

id, name, element, weapon_type, rarity, role, release_patch, build_recommendations, artifact_sets, stat_priority, talent_priority, team_examples, source, last_updated, patch_version

Weapon

id, name, type, rarity, passive_summary, recommended_characters, source, last_updated

Artifact Set

id, name, 2pc_effect, 4pc_effect, recommended_characters, source, last_updated

Team

id, name, characters, role_notes, synergy_notes, source, last_updated

Checklist Item

id, title, type_daily_weekly, reset_rule, is_custom

Event

id, title, start_date, end_date, type, summary, source, last_updated

Setiap guide wajib punya source, last_updated, patch_version, dan status draft / reviewed / outdated.

---

11. Arah Visual & UX

· Bersih dan ringan — tidak meniru UI game secara berlebihan.
· Fokus keterbacaan — kontras cukup, font scalable, tap target minimal 44px.
· Hierarki jelas — rekomendasi utama paling menonjol.
· Konsisten — kartu, label, dan warna elemen dipakai konsisten.
· Tidak mengganggu — iklan atau promosi tidak boleh memutus alur baca panduan.
· Mobile-first — navigasi mudah dijangkau satu tangan.

---

12. Disclaimer Produk

Teyvat-Hub adalah aplikasi fan-made yang dibuat oleh komunitas. Teyvat-Hub tidak berafiliasi dengan, disponsori, atau didukung oleh HoYoverse. Genshin Impact dan nama terkait adalah trademark milik pemiliknya masing-masing. Semua data panduan disediakan untuk tujuan informasional dan bisa berubah sesuai update game.
