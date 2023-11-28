package hataru.presentation

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hataru.R

class ApartmentViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val twAddress = view.findViewById<TextView>(R.id.text_View_Address)
    val twArea = view.findViewById<TextView>(R.id.text_View_Area)
    val twGuests = view.findViewById<TextView>(R.id.text_View_Guests)
    val twPrice = view.findViewById<TextView>(R.id.text_View_Price)
//    val twDescription = view.findViewById<TextView>(R.id.text_View_Description)
    val buttonLike = view.findViewById<ImageButton>(R.id.button_Like)
}