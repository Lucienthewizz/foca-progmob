// Debug script untuk testing admin account
// Gunakan ini di Firebase Console untuk set user sebagai admin

// 1. Buka Firebase Console -> Firestore Database
// 2. Cari collection "users"
// 3. Cari document dengan email "admin@foca.com"
// 4. Edit field "role" menjadi "admin"

// Alternatif: jalankan ini di Firebase console JavaScript:

/*
// Check existing admin user:
db.collection("users").where("email", "==", "admin@foca.com").get().then((querySnapshot) => {
    if (querySnapshot.empty) {
        console.log("No admin user found!");
    } else {
        querySnapshot.forEach((doc) => {
            console.log("Admin user found:", doc.data());
            // Update role to admin
            doc.ref.update({
                role: "admin"
            }).then(() => {
                console.log("Admin role updated successfully!");
            });
        });
    }
});

// Or create admin user manually:
db.collection("users").add({
    userId: "admin-001",
    firstName: "Admin",
    lastName: "Foca",
    name: "Admin Foca",
    email: "admin@foca.com",
    phone: "",
    birthDate: "",
    photoUrl: "",
    role: "admin",
    createdAt: firebase.firestore.Timestamp.now()
}).then((docRef) => {
    console.log("Admin user created with ID: ", docRef.id);
});
*/

// CARA TESTING ADMIN:
// 1. Daftar dengan email: admin@foca.com
// 2. Password: admin123 (atau password pilihan)
// 3. Role akan otomatis diset ke "admin"

// CARA TESTING ADMIN VIA GOOGLE:
// 1. Gunakan akun Google dengan email admin@foca.com
// 2. Login via Google Sign-In
// 3. Role akan otomatis diset ke "admin"

// TROUBLESHOOTING ADMIN REDIRECT:
// 1. Pastikan email ditulis persis: "admin@foca.com" (lowercase)
// 2. Cek di Firebase Console apakah field "role" sudah ada dan = "admin"
// 3. Hapus app data dan coba login ulang jika masih bermasalah
// 4. Pastikan "Remember Me" sudah di-clear sebelum testing
// 5. Periksa Logcat untuk debug info:
//    - Tag "LoadingScreen": cek role detection saat app start
//    - Tag "AppNavHost": cek navigation logic
//    - Tag "ProfileViewModel": cek profile loading
// 6. Jika masih bermasalah, force update role via Firestore console:
//    - Buka collection "users"
//    - Cari dokumen berdasarkan email "admin@foca.com"
//    - Pastikan field "role" = "admin" (bukan "user")

// TROUBLESHOOTING LOGOUT STUCK:
// 1. Pastikan onLogout dipanggil dengan benar
// 2. Cek Logcat untuk navigation debug info
// 3. Pastikan Firebase Auth logout berhasil
// 4. Pastikan SharedPreferences "remember_me" di-clear
// 5. Force close app dan buka ulang jika masih stuck

// CARA MANUAL SET ADMIN DI FIRESTORE:
// 1. Buka Firebase Console
// 2. Pilih project Foca
// 3. Masuk ke Firestore Database
// 4. Cari collection "users"
// 5. Cari dokumen dengan email admin@foca.com
// 6. Edit field "role" menjadi "admin"
// 7. Save changes
// 8. Force close app dan login ulang
