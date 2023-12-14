package com.example.bicycles

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.loader.content.CursorLoader
import com.example.bicycles.models.Equipment
import com.example.bicycles.service.RetrofitInstance
import com.squareup.picasso.Picasso
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import android.Manifest



class EquipmentDetailActivity : AppCompatActivity() {
    private lateinit var state: String
    private lateinit var userId: String
    private lateinit var textInputEditTextName: EditText
    private lateinit var textInputEditTextDescription: EditText
    private lateinit var textInputEditTextCategorie: EditText
    private lateinit var textInputEditTextEtat: EditText
    private lateinit var imageViewEquipmentDetail: ImageView
    private lateinit var buttonEdit: Button
    private lateinit var buttonDelete: Button
    private lateinit var equipmentId: String
    private lateinit var buttonOpenGallery: Button
    private var selectedImageUri: Uri? = null
    private lateinit var buttonMaintenance: Button



    object RealPathUtil {

        @SuppressLint("ObsoleteSdkInt")
        fun getRealPath(context: Context, uri: Uri): String? {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            var cursor: Cursor? = null
            try {
                cursor = if (android.os.Build.VERSION.SDK_INT > 19) {
                    // Recent versions of Android use the Document API
                    val loader = CursorLoader(context, uri, projection, null, null, null)
                    loader.loadInBackground()
                } else {
                    // Old versions use the deprecated managedQuery
                    val columnIndex = projection.indexOf(MediaStore.Images.Media.DATA)
                    context.contentResolver.query(uri, projection, null, null, null)
                }

                cursor?.let {
                    if (it.moveToFirst()) {
                        val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                        return columnIndex.let { it1 -> it.getString(it1) }
                    }
                }
            } finally {
                cursor?.close()
            }
            return null
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipment_detail)




        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        state = this.intent.getStringExtra("state").toString()
        equipmentId = this.intent.getStringExtra("equipmentId") ?: ""
        userId = intent.getStringExtra("userId").toString()
        Log.d("EquipmentDetailActivity", "UserId: $userId")



        // Initialize all EditTexts and ImageView
        textInputEditTextName = findViewById(R.id.TextInputEditTextName)
        textInputEditTextDescription = findViewById(R.id.TextInputEditTextDescription)
        textInputEditTextCategorie = findViewById(R.id.TextInputEditTextCategorie)
        textInputEditTextEtat = findViewById(R.id.TextInputEditTextEtat)
        imageViewEquipmentDetail = findViewById(R.id.imageViewEquipmentDetail)

        buttonDelete = findViewById(R.id.buttonDelete)
        buttonDelete.setOnClickListener {
            deleteEquipment(equipmentId)
        }
        buttonOpenGallery = findViewById(R.id.buttonOpenGallery)
        buttonOpenGallery.setOnClickListener {
            openGallery()
        }
        buttonMaintenance = findViewById(R.id.buttonMaintenance)
        buttonMaintenance.setOnClickListener {
            // Intent pour démarrer l'activité de maintenance
            val intent = Intent(this, MaintenanceActivity::class.java)
            intent.putExtra("equipmentId", equipmentId)
            Log.d("EquipmentAdapter", "Élément sélectionné: ID = ${equipmentId}")
            startActivity(intent)
        }





        if (state == "Editing") getEquipment(equipmentId)





        buttonEdit = findViewById(R.id.buttonEdit)

        val userRole = intent.getStringExtra("userRole")
        adjustButtonVisibility(userRole)


        buttonEdit.setOnClickListener {
            when (state) {
                "Editing" -> {
                    Log.d("EquipmentDetailActivity", "Editing")
                    updateEquipment(userId)
                    // Mise à jour de l'équipement
                }
                "Adding" -> {

                    Log.d("EquipmentDetailActivity", "UserId: $userId")
                    Log.d("EquipmentDetailActivity", "Ajout d'un nouvel équipement")
                    addEquipment(userId)
                }
            }
        }



    }

    private fun updateEquipment(userId: String) {
        val name = textInputEditTextName.text.toString()
        val description = textInputEditTextDescription.text.toString()
        val categorie = textInputEditTextCategorie.text.toString()
        val etat = textInputEditTextEtat.text.toString()

        if (name.isEmpty() || description.isEmpty() || categorie.isEmpty() || etat.isEmpty()) {
            Log.e("UpdateEquipment", "Incomplete or missing data. Aborting equipment update.")
            return
        }

        // ID of the equipment to be updated
        val equipmentId = equipmentId

        // Initialize image as null
        var imageRequestBody: RequestBody? = null
        var newImagePart: MultipartBody.Part? = null

        // Check if a new image is selected
        if (selectedImageUri != null) {
            // Convert the URI of the new image to a file
            val newImageFile = File(RealPathUtil.getRealPath(this, selectedImageUri!!)!!)

            // Create MultipartBody.Part for the new image
            imageRequestBody = RequestBody.create(MediaType.parse("image/*"), newImageFile)
            newImagePart = MultipartBody.Part.createFormData("image", newImageFile.name, imageRequestBody)
        }

        val userIdRequestBody = RequestBody.create(MediaType.parse("text/plain"), userId)
        val nameRequestBody = RequestBody.create(MediaType.parse("text/plain"), name)
        val descriptionRequestBody = RequestBody.create(MediaType.parse("text/plain"), description)
        val categorieRequestBody = RequestBody.create(MediaType.parse("text/plain"), categorie)
        val etatRequestBody = RequestBody.create(MediaType.parse("text/plain"), etat)
        Log.d("UpdateEquipment", "UserID: $userId, Name: $name, Description: $description, Categorie: $categorie, Etat: $etat")

        // Use Retrofit to make the network request
        if (newImagePart != null) {
            // Update with image
            RetrofitInstance.api.updateEquipmentWithImage(
                equipmentId,
                nameRequestBody,
                descriptionRequestBody,
                categorieRequestBody,
                etatRequestBody,
                userIdRequestBody,
                newImagePart
            ).enqueue(object : Callback<Equipment> {
                override fun onResponse(call: Call<Equipment>, response: Response<Equipment>) {
                    if (response.isSuccessful) {
                        val updatedEquipment = response.body()
                        navigateToList()
                        Log.d("UpdateEquipment", "Equipment updated successfully.")
                    } else {
                        Log.e("UpdateEquipment", "Failed to update equipment. Server response: ${response.message()}")
                        // Additional logging or error handling if needed
                    }
                }

                override fun onFailure(call: Call<Equipment>, t: Throwable) {
                    Log.e("UpdateEquipment", "API call failed. Error: ${t.message}")
                    // Additional logging or error handling if needed
                }
            })
        } else {
            // Update without image
            Log.d("UpdateEquipment", "Updating without image - UserID: $userId, Name: $name, Description: $description, Categorie: $categorie, Etat: $etat")

            RetrofitInstance.api.updateEquipmentWithOutImage(
                equipmentId,
                nameRequestBody,
                descriptionRequestBody,
                categorieRequestBody,
                etatRequestBody,
                userIdRequestBody
            ).enqueue(object : Callback<Equipment> {
                override fun onResponse(call: Call<Equipment>, response: Response<Equipment>) {
                    if (response.isSuccessful) {
                        val updatedEquipment = response.body()
                        navigateToList()
                        Log.d("UpdateEquipment", "Equipment updated successfully.")
                    } else {
                        Log.e("UpdateEquipment", "Failed to update equipment. Server response: ${response.message()}")
                        // Additional logging or error handling if needed
                    }
                }

                override fun onFailure(call: Call<Equipment>, t: Throwable) {
                    Log.e("UpdateEquipment", "API call failed. Error: ${t.message}")
                    // Additional logging or error handling if needed
                }
            })
        }

    }





    private fun addEquipment(userId: String) {
        val name = textInputEditTextName.text.toString()
        val description = textInputEditTextDescription.text.toString()
        val categorie = textInputEditTextCategorie.text.toString()
        val etat = textInputEditTextEtat.text.toString()

        Log.d("AddEquipment", "UserID: $userId, Name: $name, Description: $description, Categorie: $categorie, Etat: $etat")

        if (name.isEmpty() || description.isEmpty() || categorie.isEmpty() || etat.isEmpty() || selectedImageUri == null) {
            Log.e("AddEquipment", "Incomplete or missing data. Aborting equipment addition.")
            return
        }

        // Convert the URI of the image to a file
        val imageFile = File(RealPathUtil.getRealPath(this, selectedImageUri!!)!!)

        // Create RequestBody instances for each field
        val userIdRequestBody = RequestBody.create(MediaType.parse("text/plain"), userId)
        val nameRequestBody = RequestBody.create(MediaType.parse("text/plain"), name)
        val descriptionRequestBody = RequestBody.create(MediaType.parse("text/plain"), description)
        val categorieRequestBody = RequestBody.create(MediaType.parse("text/plain"), categorie)
        val etatRequestBody = RequestBody.create(MediaType.parse("text/plain"), etat)

        // Create MultipartBody.Part for the image
        val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, RequestBody.create(MediaType.parse("image/*"), imageFile))

        // Use Retrofit to make the network request
        RetrofitInstance.api.createEquipmentWithImage(nameRequestBody, descriptionRequestBody, categorieRequestBody, etatRequestBody, userIdRequestBody, imagePart)
            .enqueue(object : Callback<Equipment> {
                override fun onResponse(call: Call<Equipment>, response: Response<Equipment>) {
                    if (response.isSuccessful) {
                        val addedEquipment = response.body()
                        navigateToList()
                        Log.d("AddEquipment", "Equipment added successfully.")
                    } else {
                        Log.e("AddEquipment", "Failed to add equipment. Server response: ${response.message()}")
                        // Additional logging or error handling if needed
                    }
                }

                override fun onFailure(call: Call<Equipment>, t: Throwable) {
                    Log.e("AddEquipment", "API call failed. Error: ${t.message}")
                    // Additional logging or error handling if needed
                }
            })
    }







    private fun changeButtonsToAdding() {
        // Similar implementation to BicycleDetailActivity
    }

    private fun changeButtonsToShowing(equipmentId: String) {
        // Similar implementation to BicycleDetailActivity
    }

    private fun changeButtonsToEditing() {
        // Similar implementation to BicycleDetailActivity
    }

    private fun getEquipment(equipmentId: String) {
        Log.d("EquipmentDetail", "Début de getEquipment avec l'ID: $equipmentId")

        RetrofitInstance.api.getEquipmentById(equipmentId).enqueue(object : Callback<Equipment> {
            override fun onResponse(call: Call<Equipment>, response: Response<Equipment>) {
                Log.d("EquipmentDetail", "onResponse appelé")

                if (response.isSuccessful) {
                    response.body()?.let { equipment ->
                        // Mettre à jour l'interface utilisateur avec les détails de l'équipement
                        textInputEditTextName.setText(equipment.name)
                        textInputEditTextDescription.setText(equipment.description)
                        textInputEditTextCategorie.setText(equipment.categorie)
                        textInputEditTextEtat.setText(equipment.etat)

                        val imageUrl = "http://10.0.2.2:9090/" + equipment.image // URL de l'image
                        Picasso.get().load(imageUrl).into(imageViewEquipmentDetail)

                        Log.d("EquipmentDetail", "Équipement reçu: $equipment")
                    } ?: Log.d("EquipmentDetail", "La réponse est réussie, mais le corps est null")
                } else {
                    Log.e("EquipmentDetail", "Réponse non réussie: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<Equipment>, t: Throwable) {
                Log.e("EquipmentDetail", "Échec de l'appel: ${t.message}")
            }
        })
    }



    private fun openGallery() {
        // Vérifiez si la permission est accordée
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Si la permission n'est pas accordée, demandez-la à l'utilisateur
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_PERMISSION_CODE
            )
        } else {
            // Si la permission est déjà accordée, ouvrez la galerie
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, GALLERY_REQUEST_CODE)
        }
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 100
        private const val REQUEST_PERMISSION_CODE = 101
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            // Log l'URI réel de l'image
            Log.d("EquipmentDetailActivity", "Selected Image URI: $selectedImageUri")
            // Affichez l'image dans votre ImageView si nécessaire
            imageViewEquipmentDetail.setImageURI(selectedImageUri)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // La permission a été accordée, ouvrez la galerie
                    openGallery()
                } else {
                    // La permission a été refusée, vous pouvez informer l'utilisateur ou prendre d'autres mesures
                    Toast.makeText(
                        this,
                        "Permission de stockage refusée. Vous ne pourrez pas sélectionner d'image.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }






    private fun deleteEquipment(equipmentId: String) {
        RetrofitInstance.api.deleteEquipment(equipmentId).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    // Suppression réussie
                    Toast.makeText(
                        this@EquipmentDetailActivity,
                        "Équipement supprimé avec succès",
                        Toast.LENGTH_SHORT
                    ).show()
                    navigateToList() // Naviguer ou mettre à jour l'interface utilisateur
                } else {
                    // Gérer le cas d'erreur
                    Toast.makeText(
                        this@EquipmentDetailActivity,
                        "Erreur lors de la suppression de l'équipement",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                // Gérer l'échec de l'appel
                Toast.makeText(
                    this@EquipmentDetailActivity,
                    "Erreur lors de la suppression de l'équipement : ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }








    private fun navigateToList() {
        val intent = Intent(this, EquipmentListActivity::class.java)
        startActivity(intent)
    }
    private fun adjustButtonVisibility(userRole: String?) {
        if (userRole == "client") {
            buttonEdit.visibility = View.GONE
            buttonDelete.visibility = View.GONE
            buttonOpenGallery.visibility = View.GONE
        } else {

            buttonEdit.visibility = View.VISIBLE
            buttonDelete.visibility = View.VISIBLE
            buttonOpenGallery.visibility = View.VISIBLE
        }
    }

    private fun getRealPathFromURI(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        val path = columnIndex?.let { cursor.getString(it) }
        cursor?.close()
        return path
    }


}
