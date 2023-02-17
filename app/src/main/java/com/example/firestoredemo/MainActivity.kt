package com.example.firestoredemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.firestoredemo.databinding.ActivityMainBinding
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.toObject


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = FirebaseFirestore.getInstance()
//        db = Firebase.firestore

        val cities = db.collection("cities")

        val tokioRef = cities.document("TOK")
        Log.d("TAG", "onCreate: $tokioRef")

//        tokioRef.update("capital",true)
//            .addOnSuccessListener {
//                Log.d("TAG", "Update erfolgreich ")
//            }
//            .addOnFailureListener {
//                Log.d("TAG", "Update fehlgeschlagen")
//            }

        tokioRef.delete()
            .addOnSuccessListener {
                Log.d("TAG", "onCreate: Tokio wurde gelöscht")
            }
            .addOnFailureListener {
                Log.d("TAG", "onCreate:Löschen felgeschlagen")
            }

//        val docRef = cities.get()
//            .addOnSuccessListener {
//                for (city in it.documents) {
//                    if (city.get("population").toString().toInt() > 1000000) {
//                        Log.d("TAG", "${city.get("name")} hat ${city.get("population")} Einwohner")
//                    }
//                }
//            }

        val docRef2 = cities.whereGreaterThan("population", 1000000).get(Source.SERVER)
            .addOnSuccessListener {
                for(city in it.documents){
                    Log.d("TAG", "${city.get("name")} hat ${city.get("population")} Einwohner")
                }
            }

    }

    private fun createCities(cities : CollectionReference) {


        val data1 = hashMapOf(
            "name" to "San Francisco",
            "state" to "CA",
            "country" to "USA",
            "capital" to true,
            "population" to 860001,
            "regions" to listOf("west_coast", "socal")
        )

        cities.document("SF").set(data1)

        val data2 = hashMapOf(
            "name" to "Los Angeles",
            "state" to "CA",
            "country" to "USA",
            "capital" to false,
            "population" to 3900000,
            "regions" to listOf("west_coast", "norcal")
        )
        cities.document("LA").set(data2)

        val data3 = hashMapOf(
            "name" to "Washington D.C",
            "state" to null,
            "country" to "USA",
            "capital" to true,
            "population" to 680000,
            "regions" to listOf("east_coast")
        )
        cities.document("DC").set(data3)

        val data4 = hashMapOf(
            "name" to "Tokyo",
            "state" to null,
            "country" to "Japan",
            "capital" to true,
            "population" to 9000000,
            "regions" to listOf("kanto", "honshu")
        )
        cities.document("TOK").set(data4)

        val data5 = hashMapOf(
            "name" to "Beijing",
            "state" to "nothing",
            "country" to "China",
            "capital" to true,
            "population" to 21500000,
            "regions" to listOf("jingjinji", "hebei")
        )
        cities.document("BJ").set(data5)
    }

    private fun userDemo() {
        db.collection("users")

        var user = hashMapOf(
            "first" to "Frank"
        )
        db.collection("users")
            .add(user)
            .addOnSuccessListener {
                Log.d("TAG", "onCreate: user wurde gespeichert")
            }
            .addOnFailureListener {
                Log.d("TAG", "onCreate: Error ")
            }

        var user2 = hashMapOf(
            "first" to "Hans",
            "last" to "Müller",
            "city" to "Bonn",
            "age" to 23
        )
        db.collection("users")
            .add(user2)
            .addOnSuccessListener {
                Log.d("TAG", "onCreate: user wurde gespeichert")
            }
            .addOnFailureListener {
                Log.d("TAG", "onCreate: Error ")
            }

        // Auslesen des user2 und verwenden
        db.collection("users")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        Log.d(
                            "TAG",
                            "User: ${document.id}, ${document.data} name: ${document.get("first")} "
                        )
                    }
                }
            }
        val u1 = User("Frank", "Neumann", "Berlin", 53)
        val u2 = User("Hans", "Schmidt", "Hamburg", 543)
        val u3 = User("Frank", "Meier", "Berlin", 3)
        val u4 = User("Peter", "Klaus", "Bonn", 5)

        val map = mapOf<String, User>(
            "1" to u1,
            "2" to u2,
            "3" to u3,
            "4" to u4,
        )

        db.collection("users").apply {
            map.forEach { (key, user) ->
                document(key).set(user)
            }
        }

        db.collection("users").whereEqualTo("age", 53).get()
            .addOnSuccessListener {
                val userFromDb = it.documents[0].toObject<User>()
                binding.tvOutput.text = userFromDb.toString()
            }
    }
}

data class User(val first: String, val last: String, val city: String, val age: Int) {
    constructor() : this("", "", "", 0)
}