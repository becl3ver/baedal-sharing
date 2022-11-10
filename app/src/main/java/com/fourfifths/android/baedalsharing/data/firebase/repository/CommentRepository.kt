package com.fourfifths.android.baedalsharing.data.firebase.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fourfifths.android.baedalsharing.data.firebase.model.community.Comment
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

class CommentRepository() {
    private val TAG = "CommentRepository"

    fun getComments(comments: MutableLiveData<MutableList<Comment>>, postId: String) {
        val query = FirebaseFirestore.getInstance().collection("Boards")
            .document(postId).collection("Comments").orderBy("timestamp").get()

        query.addOnSuccessListener { value ->
            val items = ArrayList<Comment>()

            for (snapshot in value) {
                items.add(convertComment(snapshot))
            }

            comments.value = items
        }.addOnFailureListener {
            Log.d(TAG, it.message.toString())
        }
    }

    private fun convertComment(snapshot: QueryDocumentSnapshot): Comment {
        return Comment(
            snapshot.id,
            snapshot.get("uid") as String,
            snapshot.get("nickname") as String,
            snapshot.get("timestamp") as Timestamp,
            snapshot.get("content") as String
        )
    }
}