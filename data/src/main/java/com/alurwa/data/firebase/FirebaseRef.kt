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

fun FirebaseFirestore.cashRootCol() = collection("cash")

fun FirebaseFirestore.cashDataCol(roomId: String) =
    cashRootCol()
        .document(roomId)
        .collection("cashData")

fun FirebaseFirestore.cashDataDoc(roomId: String, id: String) =
    cashDataCol(roomId)
        .document(id)

fun FirebaseFirestore.picketRootCol() = collection("picket")

fun FirebaseFirestore.picketCol(roomId: String) =
    picketRootCol().document(roomId)
        .collection("picketData")

fun FirebaseFirestore.picketDoc(roomId: String, picketId: String) =
    picketCol(roomId)
        .document(picketId)

fun FirebaseFirestore.attendanceRootCol() = collection("attendance")

fun FirebaseFirestore.attendanceCol(roomId: String) =
    attendanceRootCol().document(roomId)
        .collection("attendanceData")

fun FirebaseFirestore.attendanceDoc(roomId: String, attendanceId: String) =
    attendanceCol(roomId)
        .document(attendanceId)