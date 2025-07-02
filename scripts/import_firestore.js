// Node.js script to import Firestore collections from JSON files
const admin = require('firebase-admin');
const path = require('path');

// Ganti path berikut dengan lokasi file service account key Anda
const serviceAccount = require(path.resolve(__dirname, '../serviceAccountKey.json'));

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

const db = admin.firestore();

// Daftar koleksi dan file JSON
const collections = [
  { name: 'catering_items', file: 'catering_items.json', key: 'catering_items' },
  { name: 'cart', file: 'cart.json', key: 'cart' },
  { name: 'chat', file: 'chat.json', key: 'chat' },
  { name: 'profile', file: 'profile.json', key: 'profile' }
];

async function importCollection(collectionName, objects) {
  for (const [docId, docData] of Object.entries(objects)) {
    await db.collection(collectionName).doc(docId).set(docData);
    console.log(`Imported ${docId} to ${collectionName}`);
  }
}

(async () => {
  for (const col of collections) {
    const data = require(path.resolve(__dirname, 'collection', col.file));
    await importCollection(col.name, data[col.key]);
  }
  console.log('All collections imported!');
  process.exit(0);
})(); 