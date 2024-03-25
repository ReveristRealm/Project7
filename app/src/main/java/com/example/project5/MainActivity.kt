package com.example.project5

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val client = AsyncHttpClient()
        val params = RequestParams()
        val picture = findViewById<ImageView>(R.id.imageView)
        val button = findViewById<Button>(R.id.button)
        val name = findViewById<TextView>(R.id.name)
        val weight = findViewById<TextView>(R.id.weight)
        val atkmove = findViewById<TextView>(R.id.atkmove)
        val pokemon  = findViewById<EditText>(R.id.searchthis)

        button.setOnClickListener{
            getPokemon(client, params, pokemon, name, weight,atkmove,picture)
        }


}
    fun getPokemon(client: AsyncHttpClient, params: RequestParams, pokemon: EditText, name :TextView, weightofpokemon:  TextView , atkmove:TextView, pic: ImageView){
        params["limit"] = "10000"
        params["offset"] = "0"
        val move ="lightning-rod"
        val pokemonn = pokemon.text.toString()
        client["https://pokeapi.co/api/v2/pokemon/$pokemonn", params, object :
            JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Toast.makeText(applicationContext, "failed", Toast.LENGTH_SHORT).show()
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON?) {
                //Parse the Json Data into a string
                val jsonString = json?.jsonObject

                name.setText(pokemon.text.toString())
                //Extract the Ability Array
                val abilitiesArray = jsonString?.getJSONArray("abilities")
                //Extract the Types Array
                val typesArray = jsonString?.getJSONArray("types")

                //Grab the weight
                val weight = jsonString?.getString("weight")
                Log.d("Pokemon weight", weight.toString())
                weightofpokemon.text = weight

                //Grab a picture of the pokemon
                val picture = jsonString?.getJSONObject("sprites")?.getString("front_default")
                Log.d("Pokemon picture", picture.toString())
                picture?.let {
                    Glide.with(this@MainActivity)
                        .load(it)
                        .placeholder(R.mipmap.pokemon_round)
                        .error(R.mipmap.pokemon_round)
                        .into(pic)
                }
                var typeNameInArray: String
                //Search for the ability with the specified type
                for (i in 0 until typesArray?.length()!!) {
                    val typeObject = typesArray.getJSONObject(i)
                    if (typeObject.getInt("slot").toString() == "1") {
                        typeNameInArray = typeObject.getJSONObject("type").getString("name")
                        Log.d("Pokemon type", typeNameInArray)
                        atkmove.setText(typeNameInArray)
                    }
                }
                //Search for the ability with the specified name
                for (i in 0 until abilitiesArray?.length()!!) {
                    val abilityObject = abilitiesArray.getJSONObject(i)
                    val abilityNameInArray =
                        abilityObject.getJSONObject("ability").getString("name")
                    Log.d("Pokemon object", abilityNameInArray)
                }
            }
        }]
    }
    @GlideModule
    class MyAppGlideModule : AppGlideModule(){

    }
}