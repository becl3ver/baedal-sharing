package com.fourfifths.android.baedalsharing.utils

import com.google.firebase.firestore.FirebaseFirestore

class FirestoreRef {
    companion object {
        val boardsRef = FirebaseFirestore.getInstance().document("Boards")
        val userRef = FirebaseFirestore.getInstance().document("UserInfo")
    }
}