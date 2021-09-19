package com.alurwa.data.firebase

import com.google.firebase.firestore.FirebaseFirestore

fun FirebaseFirestore.userCol() = collection("users")

fun FirebaseFirestore.userDoc(id: String) = userCol().document(id)