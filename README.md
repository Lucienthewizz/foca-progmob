# Foca - Aplikasi Catering

## Integrasi dengan Firebase

Aplikasi ini telah diintegrasikan dengan Firebase untuk menyimpan dan menampilkan data catering. Berikut adalah langkah-langkah untuk menyelesaikan integrasi Firebase:

### 1. Buat Proyek Firebase

1. Buka [Firebase Console](https://console.firebase.google.com/)
2. Klik "Add project" dan buat proyek baru dengan nama "foca"
3. Ikuti langkah-langkah untuk menyelesaikan pembuatan proyek

### 2. Tambahkan Aplikasi Android ke Proyek Firebase

1. Di dashboard proyek Firebase, klik ikon Android untuk menambahkan aplikasi Android
2. Masukkan package name: `com.example.foca`
3. Klik "Register app"
4. Unduh file `google-services.json`
5. Tempatkan file `google-services.json` di direktori `app/` proyek Android Studio Anda (menggantikan file placeholder yang ada)

### 3. Siapkan Firestore Database

1. Di Firebase Console, buka "Firestore Database"
2. Klik "Create database"
3. Pilih mode "Start in test mode" untuk memulai (Anda dapat mengubahnya nanti)
4. Pilih lokasi server yang terdekat dengan pengguna Anda

### 4. Buat Koleksi dan Dokumen

1. Di Firestore, buat koleksi baru dengan nama `catering_items`
2. Tambahkan beberapa dokumen dengan struktur berikut:

```
title: "Nama Menu" (string)
imageUrl: "URL Gambar" (string)
category: "daily" atau "event" (string)
description: "Deskripsi menu" (string)
price: 100000 (number)
```

### 5. Siapkan Firebase Storage untuk Gambar

1. Di Firebase Console, buka "Storage"
2. Klik "Get started"
3. Pilih mode "Start in test mode" (Anda dapat mengubahnya nanti)
4. Unggah beberapa gambar ke Storage
5. Dapatkan URL download gambar dan gunakan URL tersebut di field `imageUrl` pada dokumen Firestore

## Struktur Aplikasi

Aplikasi ini menggunakan:

- **Jetpack Compose** untuk UI
- **Firebase Firestore** untuk database
- **Firebase Storage** untuk penyimpanan gambar
- **Glide** untuk memuat dan menampilkan gambar dari URL

## Fitur

- Menampilkan menu catering dari Firebase
- Pencarian menu
- Kategori catering (Daily dan Event)
- Rekomendasi menu