package tn.sim5.agriconnect.ViewModels


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tn.sim5.agriconnect.models.Equipment
import tn.sim5.agriconnect.utils.RetrofitInstance
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.sim5.agriconnect.R
import tn.sim5.agriconnect.models.Users

class EquipmentListActivity : AppCompatActivity() {
    private lateinit var equipments: ArrayList<Equipment>
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: EquipmentAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    val staticUser = Users(id = "655f76d027ec50c1a8f8cebc", role = "farmer")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipment_list)
        Log.d("EquipmentAdapter", "Utilisateur Statique: ID = ${staticUser.id}, role = ${staticUser.role}")

        equipments = ArrayList<Equipment>()

        viewManager = LinearLayoutManager(this)

        viewAdapter = EquipmentAdapter(equipments, this)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerViewEquipments)
        recyclerView.layoutManager = viewManager
        recyclerView.adapter = viewAdapter


        if (staticUser.role == "client") {
            getAllEquipments()
        } else {
            getEquipmentByUserId(staticUser.id)
        }

        val fab: FloatingActionButton = findViewById(R.id.floatingActionButton)
        if (staticUser.role == "client") {
            fab.visibility = View.GONE // Masquer le bouton pour les clients
        } else {
            fab.setOnClickListener {
                val intent = Intent(this, EquipmentDetailActivity::class.java)
                intent.putExtra("state", "Adding")
                intent.putExtra("userId", staticUser.id)
                startActivity(intent)
            }
        }
    }

    private fun getAllEquipments() {
        RetrofitInstance.api.getAllEquipments().enqueue(object : Callback<List<Equipment>> {
            override fun onResponse(call: Call<List<Equipment>>, response: Response<List<Equipment>>) {
                if (response.isSuccessful) {
                    response.body()?.let { equipmentList ->
                        Log.d("EquipmentListActivity", "Données reçues : $equipmentList")
                        viewAdapter.equipmentList = ArrayList(equipmentList)
                        viewAdapter.notifyDataSetChanged()
                    } ?: Log.d("EquipmentListActivity", "Réponse réussie, mais le corps de la réponse est null")
                } else {
                    Log.e("EquipmentListActivity", "Réponse non réussie: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Equipment>>, t: Throwable) {
                Log.e("EquipmentListActivity", "Échec de l'appel: ${t.message}")
            }
        })
    }

    private fun getEquipmentByUserId(userId: String) {
        RetrofitInstance.api.getEquipmentByUserId(userId).enqueue(object : Callback<List<Equipment>> {
            override fun onResponse(call: Call<List<Equipment>>, response: Response<List<Equipment>>) {
                if (response.isSuccessful) {
                    response.body()?.let { equipmentList ->
                        Log.d("EquipmentListActivity", "Données reçues pour l'utilisateur $userId : $equipmentList")
                        viewAdapter.equipmentList = ArrayList(equipmentList)
                        viewAdapter.notifyDataSetChanged()
                    } ?: Log.d("EquipmentListActivity", "Réponse réussie, mais le corps de la réponse est null")
                } else {
                    Log.e("EquipmentListActivity", "Réponse non réussie: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Equipment>>, t: Throwable) {
                Log.e("EquipmentListActivity", "Échec de l'appel: ${t.message}")
            }
        })
    }


}
