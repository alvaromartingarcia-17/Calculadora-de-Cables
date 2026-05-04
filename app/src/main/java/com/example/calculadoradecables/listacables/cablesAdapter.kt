package com.example.calculadoradecables.listacables

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.calculadoradecables.MainActivity
import com.example.calculadoradecables.R
import com.example.calculadoradecables.RetrofitClient
import com.example.calculadoradecables.modelosDataClass.Cable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class cablesAdapter(private val listaCables: MutableList<Cable>) :
    RecyclerView.Adapter<cablesAdapter.UsuarioViewHolder>() {

    class UsuarioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.textViewNombre)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listado_cables, parent, false)

        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {

        val posicion = holder.bindingAdapterPosition
        val cable = listaCables[posicion]

        holder.nombre.text = cable.nombre

        holder.nombre.setOnClickListener {
            //Aqui hago para que me mande a los fragmento que muestran datos del cable con el cable y pongo los datos automaticamente
        }
    }

    override fun getItemCount(): Int {
        return listaCables.size
    }
}