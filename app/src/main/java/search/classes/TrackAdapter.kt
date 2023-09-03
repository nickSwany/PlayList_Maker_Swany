package search.classes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pl_market.R
import com.example.pl_market.Track

class TrackAdapter(private val trackList: List<Track>): RecyclerView.Adapter<TrackViewHolder> (){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return TrackViewHolder(view)
    }
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
    }
    override fun getItemCount(): Int {
        return trackList.size
    }
}
