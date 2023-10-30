import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.hataru.R
import com.example.hataru.presentation.forMap.flat

class ClusterView(context: Context) : LinearLayout(context) {

    private val flatsLayout by lazy { findViewById<View>(R.id.layout_flats_group) }
    private val flatsCountText by lazy { findViewById<TextView>(R.id.text_flats_count) }
    private val flatsMinCost by lazy { findViewById<TextView>(R.id.textMinValue) }
    private val flatsMaxCost by lazy { findViewById<TextView>(R.id.textMaxValue) }

    init {
        inflate(context, R.layout.cluster_view, this)
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        orientation = HORIZONTAL
        setBackgroundResource(R.drawable.cluster_view_background)
    }

    fun setData(size : Int,minCost : Double,maxCost : Double) {
        flatsCountText.text = size.toString()
        flatsLayout.isVisible = size != 0
        flatsMinCost.text = minCost.toString()
        flatsMaxCost.text = maxCost.toString()
    }

}