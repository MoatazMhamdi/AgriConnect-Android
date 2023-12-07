package com.example.bicycles

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.bicycles.R
import java.text.SimpleDateFormat
import java.util.*

class MaintenanceActivity : AppCompatActivity() {

    private lateinit var editTextDateMaintenance: EditText
    private lateinit var spinnerTypeMaintenance: Spinner
    private lateinit var editTextDescription: EditText
    private lateinit var editTextCoutMaintenance: EditText
    private lateinit var buttonSaveMaintenance: Button
    private lateinit var buttonCancelMaintenance: Button
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maintenance)

        // Initialisation des composants
        editTextDateMaintenance = findViewById(R.id.editTextDateMaintenance)
        spinnerTypeMaintenance = findViewById(R.id.spinnerTypeMaintenance)
        editTextDescription = findViewById(R.id.editTextDescription)
        editTextCoutMaintenance = findViewById(R.id.editTextCoutMaintenance)
        buttonSaveMaintenance = findViewById(R.id.buttonSaveMaintenance)
        buttonCancelMaintenance = findViewById(R.id.buttonCancelMaintenance)

        // Setup DatePicker for editTextDateMaintenance
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }

        editTextDateMaintenance.setOnClickListener {
            DatePickerDialog(
                this@MaintenanceActivity,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Setup Spinner for Type_Maintenance
        ArrayAdapter.createFromResource(
            this,
            R.array.type_maintenance_array, // Add this array to your strings.xml
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerTypeMaintenance.adapter = adapter
        }

        // Bouton pour enregistrer la maintenance
        buttonSaveMaintenance.setOnClickListener {
            // Logique pour enregistrer la maintenance
            // Ici, vous pouvez appeler votre API pour sauvegarder les données
        }

        // Bouton pour annuler
        buttonCancelMaintenance.setOnClickListener {
            // Fermer l'activité
            finish()
        }
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy" // Format de la date
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        editTextDateMaintenance.setText(sdf.format(calendar.time))
    }
}
