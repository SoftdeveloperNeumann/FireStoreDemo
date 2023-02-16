package com.example.firestoredemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.firestoredemo.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = FirebaseFirestore.getInstance()
//        db = Firebase.firestore
        db.collection("users")

        var user = hashMapOf(
            "first" to "Frank"
        )
        db.collection("users")
            .add(user)
            .addOnSuccessListener {
                Log.d("TAG", "onCreate: user wurde gespeichert")
            }
            .addOnFailureListener{
                Log.d("TAG", "onCreate: Error ")
            }

        var user2 = hashMapOf(
            "first" to "Hans",
            "last"  to "Müller",
            "city" to "Bonn",
            "age" to 23
        )
        db.collection("users")
            .add(user2)
            .addOnSuccessListener {
                Log.d("TAG", "onCreate: user wurde gespeichert")
            }
            .addOnFailureListener{
                Log.d("TAG", "onCreate: Error ")
            }

        // Auslesen des user2 und verwenden
        db.collection("users")
            .get()
            .addOnCompleteListener {task ->
                if(task.isSuccessful){
                    for(document in task.result){
                        Log.d("TAG", "User: ${document.id}, ${document.data} name: ${document.get("first")} ")
                    }
                }
            }
        val u1 = User("Frank","Neumann", "Berlin", 53)
        val u2 = User("Hans","Schmidt", "Hamburg", 543)
        val u3 = User("Frank","Meier", "Berlin", 3)
        val u4 = User("Peter","Klaus", "Bonn", 5)

        val map = mapOf<String,User>(
            "1" to u1,
            "2" to u2,
            "3" to u3,
            "4" to u4,
        )

        db.collection("users").apply {
            map.forEach{ (key, user) ->
                document(key).set(user)
            }
        }

        db.collection("users").whereEqualTo("age",53).get()
            .addOnSuccessListener {
                val userFromDb = it.documents[0].toObject<User>()
                binding.tvOutput.text = userFromDb.toString()
            }
    }
}

data class User(val first:String, val last: String, val city: String, val age: Int){
    constructor():this("","","",0)
}