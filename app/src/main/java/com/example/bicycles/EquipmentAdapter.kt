import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bicycles.EquipmentDetailActivity
import com.squareup.picasso.Picasso
import com.example.bicycles.models.Equipment
import com.example.bicycles.service.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.bicycles.R
import com.example.bicycles.models.User


class EquipmentAdapter(var equipmentList: ArrayList<Equipment>, private val context: Context) : RecyclerView.Adapter<EquipmentAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipmentAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.equipment_list_row, parent, false)

        return ViewHolder(itemView)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(equipmentList[position], context)

    }

    override fun getItemCount(): Int = equipmentList.size

    fun updateEquipmentList() {
        RetrofitInstance.api.getAllEquipments().enqueue(object : Callback<List<Equipment>> {
            override fun onResponse(call: Call<List<Equipment>>, response: Response<List<Equipment>>) {
                if (response.isSuccessful) {
                    equipmentList.clear()
                    equipmentList.addAll(response.body()!!)
                    notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<Equipment>>, t: Throwable) {
                // Gérez l'échec ici
            }
        })
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(e: Equipment, context: Context) {
            val txt_name: TextView = itemView.findViewById(R.id.textViewName)
            val txt_description: TextView = itemView.findViewById(R.id.textViewDescription)
            val img: ImageView = itemView.findViewById(R.id.imageViewEquipment)


            txt_name.text = e.name
            txt_description.text = e.description

            val imageUrl = "http://10.0.2.2:9090/" + e.image
            Picasso.with(context).load(imageUrl).into(img)
            val staticUser = User(id = "655f76d027ec50c1a8f8cebc", role = "farmer")
            itemView.setOnClickListener {
                Log.d("EquipmentAdapter", "Élément sélectionné: ID = ${e._id}")
                val intent = Intent(context, EquipmentDetailActivity::class.java)
                intent.putExtra("state", "Editing")
                intent.putExtra("equipmentId", e._id)
                intent.putExtra("userRole", staticUser.role)
                intent.putExtra("userId", staticUser.id)
                context.startActivity(intent)
            }


        }
    }
}
