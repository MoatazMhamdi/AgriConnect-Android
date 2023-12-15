package tn.sim5.agriconnect

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.loader.content.CursorLoader
import tn.sim5.agriconnect.models.Blog

import tn.sim5.agriconnect.utils.RetrofitInstance
import com.google.android.material.textfield.TextInputEditText
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.Calendar
import android.webkit.MimeTypeMap
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import tn.sim5.agriconnect.utils.BlogApi

class BlogDetailActivity : AppCompatActivity() {

    private lateinit var textInputEditTextTitre: EditText
    private lateinit var textInputEditTextDescription: EditText
    private lateinit var textInputEditTextLieu: EditText
    private lateinit var textInputEditTextPrix: EditText
    private lateinit var textInputEditTextDate: EditText
    private lateinit var imageViewBlogDetail: ImageView
    private lateinit var buttonOpenGallery: Button

    private var selectedImageUri: Uri? = null

    private lateinit var blogApi: BlogApi

    object RealPathUtil {
        @SuppressLint("ObsoleteSdkInt")
        fun getRealPath(context: Context, uri: Uri): String? {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            var cursor: Cursor? = null
            try {
                cursor = if (android.os.Build.VERSION.SDK_INT > 19) {
                    val loader = CursorLoader(context, uri, projection, null, null, null)
                    loader.loadInBackground()
                } else {
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
        setContentView(R.layout.add_blog)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        textInputEditTextTitre = findViewById(R.id.editTextTitre)
        textInputEditTextDescription = findViewById(R.id.editTextDescription)
        textInputEditTextLieu = findViewById(R.id.editTextLieu)
        textInputEditTextPrix = findViewById(R.id.editTextPrix)
        textInputEditTextDate = findViewById(R.id.editTextDate)
        imageViewBlogDetail = findViewById(R.id.imageViewLogo)

        buttonOpenGallery = findViewById(R.id.buttonImportImage)
        buttonOpenGallery.setOnClickListener {
            openGallery()
        }

        if (intent.hasExtra("blogId")) {
            val blogId = intent.getStringExtra("blogId") ?: ""
            getBlogDetails(blogId)
        }

        val buttonAjouter = findViewById<Button>(R.id.buttonAjouter)
        buttonAjouter.setOnClickListener {
            addBlogg()
        }

        blogApi = RetrofitInstance.apiBlog
    }

    private fun addBlogg() {
        val titre = textInputEditTextTitre.text.toString()
        val description = textInputEditTextDescription.text.toString()
        val lieu = textInputEditTextLieu.text.toString()

        val date = textInputEditTextDate.text.toString()
        val prix = textInputEditTextPrix.text.toString()


        if (titre.isEmpty() || description.isEmpty() || lieu.isEmpty() || prix.isEmpty() || date.isEmpty() || selectedImageUri == null) {
            Log.d("BlogDetailActivity", "Veuillez remplir tous les champs et sélectionner une image")
            Toast.makeText(this, "Veuillez remplir tous les champs et sélectionner une image", Toast.LENGTH_SHORT).show()
            return
        }

        val imageFile = File(RealPathUtil.getRealPath(this, selectedImageUri!!)!!)
        val titreRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), titre)
        val descriptionRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), description)
        val lieuRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), lieu)
        val prixRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), prix)
        val dateRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), date)
      //  val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, RequestBody.create(MediaType.parse(getMimeType(imageFile)), imageFile))
        val imagePart = MultipartBody.Part.createFormData(
            "image", imageFile.name,
            RequestBody.create(getMimeType(imageFile).toMediaTypeOrNull(), imageFile)
        )
        // Update this line to specify the media type based on the file extension

        Log.d("BlogDetailActivity", "Données à envoyer - Titre: $titre, Description: $description, Lieu: $lieu, Prix: $prix, Date: $date, Image URL: ${imageFile.absolutePath}")
        Log.d("BlogDetailActivity", "Selected Image MIME Type: " + getMimeType(imageFile));

        RetrofitInstance.apiBlog.addBlog(titreRequestBody, descriptionRequestBody, lieuRequestBody, dateRequestBody, prixRequestBody, imagePart)
            .enqueue(object : Callback<Blog> {
                override fun onResponse(call: Call<Blog>, response: Response<Blog>) {
                    if (response.isSuccessful) {
                        val addedBlog = response.body()
                        Log.d("BlogDetailActivity", "Blog ajouté avec succès. ID: ${addedBlog?._id}")
                        Toast.makeText(this@BlogDetailActivity, "Blog ajouté avec succès.", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e("BlogDetailActivity", "Échec de l'ajout du blog. Réponse du serveur: ${response.message()}")
                        Toast.makeText(this@BlogDetailActivity, "Échec de l'ajout du blog. Réponse du serveur: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Blog>, t: Throwable) {
                    Log.e("BlogDetailActivity", "Erreur lors de l'envoi de la requête", t)
                    Toast.makeText(this@BlogDetailActivity, "Erreur lors de l'envoi de la requête", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun openGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSION_CODE)
        } else {
            // Specify the desired directory URI
            val directoryUri = Uri.parse("content://com.android.externalstorage.documents/document/primary:Downloads")

            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"

            // Optionally, set the initial directory
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, directoryUri)

            startActivityForResult(intent, GALLERY_REQUEST_CODE)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            Log.d("BlogDetailActivity", "Selected Image URI: $selectedImageUri")
            imageViewBlogDetail.setImageURI(selectedImageUri)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery()
                } else {
                    Toast.makeText(this, "Permission de stockage refusée. Vous ne pourrez pas sélectionner d'image.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getBlogDetails(blogId: String) {
        // Logique pour obtenir les détails du blog avec l'ID donné
        // Mettez à jour l'interface utilisateur avec les détails du blog
    }

    fun showDatePickerDialog(view: View) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                val editTextDate = findViewById<TextInputEditText>(R.id.editTextDate)
                editTextDate.setText(selectedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 100
        private const val REQUEST_PERMISSION_CODE = 101
    }

    private fun getMimeType(file: File): String {
        val extension = MimeTypeMap.getFileExtensionFromUrl(file.absolutePath)
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: "application/octet-stream"
    }
}
