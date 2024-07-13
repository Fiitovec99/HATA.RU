import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hataru.R
import com.example.hataru.domain.entity.FAQItem

class FAQAdapter(private val faqList: List<FAQItem>) :
    RecyclerView.Adapter<FAQAdapter.FAQViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_faq, parent, false)
        return FAQViewHolder(view)
    }

    override fun onBindViewHolder(holder: FAQViewHolder, position: Int) {
        val faqItem = faqList[position]
        holder.bind(faqItem)
    }

    override fun getItemCount(): Int {
        return faqList.size
    }

    inner class FAQViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val questionContainer: RelativeLayout = itemView.findViewById(R.id.questionContainer)
        private val questionTextView: TextView = itemView.findViewById(R.id.questionTextView)
        private val answerTextView: TextView = itemView.findViewById(R.id.answerTextView)
        private val arrowImageView: ImageView = itemView.findViewById(R.id.more)

        init {
            questionContainer.setOnClickListener {
                val faqItem = faqList[adapterPosition]
                faqItem.isExpanded = !faqItem.isExpanded
                notifyItemChanged(adapterPosition)
            }
        }

        fun bind(faqItem: FAQItem) {
            questionTextView.text = faqItem.question
            answerTextView.text = faqItem.answer
            answerTextView.visibility = if (faqItem.isExpanded) View.VISIBLE else View.GONE

            val rotationAngle = if (faqItem.isExpanded) 180f else 0f
            arrowImageView.animate().rotation(rotationAngle).setDuration(300).start()
        }
    }
}
