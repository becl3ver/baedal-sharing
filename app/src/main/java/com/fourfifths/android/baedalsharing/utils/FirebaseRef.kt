package com.fourfifths.android.baedalsharing.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseRef {
    companion object {
        private val database = Firebase.database
        val boardsRef = database.getReference("boards")
    }
}