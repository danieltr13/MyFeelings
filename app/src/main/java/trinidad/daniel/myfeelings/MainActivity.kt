package trinidad.daniel.myfeelings

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import trinidad.daniel.myfeelings.utilities.CustomBarDrawable
import trinidad.daniel.myfeelings.utilities.CustomCircleDrawable
import trinidad.daniel.myfeelings.utilities.Feelings
import trinidad.daniel.myfeelings.utilities.JSONFile

class MainActivity : AppCompatActivity() {
    var jsonFile:JSONFile?=null
    var veryHappy = 0.0F
    var happy = 0.0F
    var neutral = 0.0F
    var sad = 0.0F
    var verySad = 0.0F
    var data: Boolean=false
    var lista= ArrayList<Feelings>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        jsonFile= JSONFile()
        fetchingData()
        if(!data){
            //var feelings=ArrayList
        }
    }

    fun fetchingData(){
        try{

            var json: String=jsonFile?.getData(this)?:""
            if(json!=""){
                this.data=true

                var jsonArray:JSONArray= JSONArray(json)
                this.lista=parseJson(jsonArray)

                for(i in lista){
                    when(i.name){
                        "Very happy"->veryHappy=i.total
                        "Happy"->happy=i.total
                        "Neutral"->neutral=i.total
                        "Sad"->sad=i.total
                        "Very sad"->verySad=i.total
                    }
                }
            }else{
                this.data=false
            }
        }catch (e:JSONException){
            e.printStackTrace()
        }
    }

    fun iconoMayoria() {
        if(happy>veryHappy && happy>neutral && happy>sad && happy>verySad){
            icon.setImageResource(R.drawable.ic_happy)
        }else if(veryHappy>happy && veryHappy>neutral && veryHappy>sad && veryHappy>verySad) {
            icon.setImageResource(R.drawable.ic_veryhappy)
        }else if(neutral>veryHappy && neutral>happy && neutral>sad && neutral>verySad){
            icon.setImageResource(R.drawable.ic_neutral)
        }else if(sad>veryHappy && sad>neutral && sad>happy &&sad> verySad){
            icon.setImageResource(R.drawable.ic_sad)
        }else if(verySad>veryHappy && verySad>neutral && verySad>sad && verySad>happy){
            icon.setImageResource(R.drawable.ic_verysad)
        }
    }

    fun actualizarGrafica(){
        val total=veryHappy+happy+neutral+sad+verySad

        var pVH: Float=((veryHappy*100)/total).toFloat()
        var pH: Float=((happy*100)/total).toFloat()
        var pN: Float=((neutral*100)/total).toFloat()
        var pS: Float=((sad*100)/total).toFloat()
        var pVS: Float=((verySad*100)/total).toFloat()

        Log.d("percentage", "Very happy"+pVH)
        Log.d("percentage", "Happy"+pH)
        Log.d("percentage", "Neutral"+pN)
        Log.d("percentage", "Sad"+pS)
        Log.d("percentage", "Very sad"+pVS)

        lista.clear()

        lista.add(Feelings("Very happy", pVH, R.color.mustard, veryHappy))
        lista.add(Feelings("Happy", pH, R.color.orange, happy))
        lista.add(Feelings("Neutral", pN, R.color.greenie, neutral))
        lista.add(Feelings("Sad", pS, R.color.blue, sad))
        lista.add(Feelings("Very sad", pVS, R.color.deepBlue, verySad))

        val fondo=CustomCircleDrawable(this, lista)

        graphVeryHappy.background = CustomBarDrawable(this, Feelings("Very happy", pVH, R.color.mustard, veryHappy))
        graphHappy.background = CustomBarDrawable(this, Feelings("Happy", pH, R.color.orange, happy))
        graphNeutral.background = CustomBarDrawable(this, Feelings("Neutral", pN, R.color.greenie, neutral))
        graphSad.background = CustomBarDrawable(this, Feelings("Sad", pS, R.color.blue, sad))
        graphVerySad.background = CustomBarDrawable(this, Feelings("Very sad", pVS, R.color.deepBlue, verySad))
        graph.background = fondo

    }

    fun parseJson(jsonArray:JSONArray):ArrayList<Feelings>{
        var lista=ArrayList<Feelings>()

        for(i in 0..jsonArray.length()){
            try{
                val name=jsonArray.getJSONObject(i).getString("name")
                val percentage=jsonArray.getJSONObject(i).getDouble("percentage")
                val color=jsonArray.getJSONObject(i).getInt("color")
                val total=jsonArray.getJSONObject(i).getDouble("total")
                val feeling=Feelings(name,percentage.toFloat(),color,total.toFloat())
                lista.add(feeling)

            }catch (e: JSONException){
                e.printStackTrace()
            }
        }
        return lista
    }

    fun guardar() {

        var jsonArray = JSONArray()
        var o : Int = 0
        for(i in lista) {
            Log.d("objetos",i.toString())
            var j: JSONObject = JSONObject()
            j.put("name",i.name)
            j.put("percentage",i.percentage)
            j.put("color", i.color)
            j.put("total", i.total)

            jsonArray.put(o,j)
            o++
        }

        jsonFile?.saveData(this,jsonArray.toString())

        Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show()
    }
}