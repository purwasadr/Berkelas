package com.alurwa.data.firebase

import com.google.firebase.firestore.FirebaseFirestore

fun FirebaseFirestore.userCol() = collection("users")

fun FirebaseFirestore.userDoc(id: String) = userCol().document(id)

fun FirebaseFirestore.subjectCol() = collection("subject")

private fun FirebaseFirestore.subjectSharedDoc(roomId: String) =
    subjectCol()
        .document("shared")
        .collection("room")
        .document(roomId)

fun FirebaseFirestore.subjectSharedCol(roomId: String) =
    subjectSharedDoc(roomId)
        .collection("subjectData")

fun FirebaseFirestore.roomCol() = collection("room")

fun FirebaseFirestore.roomDoc(id: String) = roomCol().document(id)