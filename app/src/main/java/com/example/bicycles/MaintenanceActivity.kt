package com.example.bicycles

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.bicycles.R
import com.example.bicycles.models.Maintenance
import com.example.bicycles.service.MaintenanceApiService
import com.example.bicycles.service.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import android.widget.TextView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet


class MaintenanceActivity : AppCompatActivity() {

    private lateinit var textViewDateMaintenance: TextView
    private lateinit var textViewTypeMaintenance: TextView
    private lateinit var textViewDescription: TextView
    private lateinit var textViewCoutMaintenance: TextView
    private lateinit var equipmentId: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maintenance)
        equipmentId = intent.getStringExtra("equipmentId").toString()
        Log.d("MaintenanceActivity", "equipmentId: $equipmentId")
        getMaintenanceByEquipmentId(equipmentId)




        // Initialisation des composants
        textViewDateMaintenance = findViewById(R.id.textViewDateMaintenance)
        textViewTypeMaintenance = findViewById(R.id.textViewTypeMaintenance)
        textViewDescription = findViewById(R.id.textViewDescription)
        textViewCoutMaintenance = findViewById(R.id.textViewCoutMaintenance)

        val lineChart = findViewById<LineChart>(R.id.lineChart)

        // Créez une liste d'entrées (x, y)
        val entries = ArrayList<Entry>()
        entries.add(Entry(1f, 50f))
        entries.add(Entry(2f, 80f))
        entries.add(Entry(3f, 60f))
        entries.add(Entry(4f, 40f))
        entries.add(Entry(5f, 70f))

        // Créez un ensemble de données avec les entrées
        val dataSet = LineDataSet(entries, "Label du graphique")

        // Ajoutez le jeu de données à l'ensemble de données
        val dataSets: ArrayList<ILineDataSet> = ArrayList()
        dataSets.add(dataSet)

        // Créez un objet LineData à partir de l'ensemble de données
        val lineData = LineData(dataSets)

        // Configurez l'axe X
        val xAxis: XAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        // Configurez l'axe Y (l'axe gauche par défaut)
        val yAxis: YAxis = lineChart.axisLeft

        // Ajoutez des propriétés supplémentaires selon vos besoins

        // Ajoutez les données au graphique
        lineChart.data = lineData

        // Rafraîchissez le graphique
        lineChart.invalidate()







    }
    private fun getMaintenanceByEquipmentId(equipmentId: String) {
        Log.d("MaintenanceActivity", "Début de getMaintenanceByEquipmentId avec l'ID: $equipmentId")

        RetrofitInstance.apiM.getMaintenanceByEquipmentId(equipmentId)
            .enqueue(object : Callback<List<Maintenance>> {
                override fun onResponse(call: Call<List<Maintenance>>, response: Response<List<Maintenance>>) {
                    Log.d("MaintenanceActivity", "onResponse appelé")

                    // À l'intérieur de onResponse après avoir vérifié que la liste n'est pas nulle
                    if (response.isSuccessful) {
                        response.body()?.let { maintenanceList ->
                            if (maintenanceList.isNotEmpty()) {
                                // Prenez la première maintenance de la liste
                                val maintenance = maintenanceList[0]

                                // Mettez à jour les TextView avec les valeurs de la maintenance
                                textViewDateMaintenance.text = "Date de maintenance : ${maintenance.Date_Maintenance}"
                                textViewTypeMaintenance.text = "Type de maintenance : ${maintenance.Type_Maintenance}"
                                textViewDescription.text = "Description : ${maintenance.Description}"
                                textViewCoutMaintenance.text = "Coût de maintenance : ${maintenance.Coût_Maintenance}"
                            } else {
                                Log.d("MaintenanceActivity", "La liste de maintenance est vide.")
                            }
                        } ?: Log.d("MaintenanceActivity", "La réponse est réussie, mais le corps est null")
                    } else {
                        Log.e("MaintenanceActivity", "Réponse non réussie: ${response.errorBody()}")
                    }

                }

                override fun onFailure(call: Call<List<Maintenance>>, t: Throwable) {
                    Log.e("MaintenanceActivity", "Échec de l'appel: ${t.message}")
                }
            })
    }



}
