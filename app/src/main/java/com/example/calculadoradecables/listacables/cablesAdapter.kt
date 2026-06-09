package com.example.calculadoradecables.listacables

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.calculadoradecables.R
import com.example.calculadoradecables.modelosDataClass.Cable

class cablesAdapter(private val listaCables: MutableList<Cable>) :
    RecyclerView.Adapter<cablesAdapter.cablesAdapter>() {

    var onItemClick: ((Cable) -> Unit)? = null

    class cablesAdapter(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.textViewNombre)
        val potencia:  TextView = view.findViewById(R.id.textViewPotencia)
        val proteccionSelecc:  TextView = view.findViewById(R.id.textViewProteccionSelec)
        val numeroCables :  TextView = view.findViewById(R.id.textViewNumeroCables)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cablesAdapter {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listado_cables, parent, false)

        return cablesAdapter(view)
    }

    override fun onBindViewHolder(holder: cablesAdapter, position: Int) {

        val posicion = holder.bindingAdapterPosition
        val cable = listaCables[posicion]

        holder.nombre.text = "Nombre del circuito: ${cable.nombre}"
        holder.potencia.text = "Potencia (W): ${cable.potencia} W"
        holder.proteccionSelecc.text = "Protección seleccionada (A): ${cable.proteccionseleccionada}"
        holder.numeroCables.text = "Número de cables necesarios: ${cable.numerocable}"


        holder.itemView.setOnClickListener {
            onItemClick?.invoke(cable)
        }
    }

    override fun getItemCount(): Int {
        return listaCables.size
    }
}