package lit.amida.lfsotptask

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.card.view.*
import java.nio.ByteBuffer

class MyAdapter(private val context: Context, private var list: OrderedRealmCollection<MyData>, private var listener:OnItemClickListener, private val autoUpdate: Boolean)
    :RealmRecyclerViewAdapter<MyData, MyAdapter.ViewHolder>(list, autoUpdate) {

    override fun getItemCount(): Int {
        return super.getItemCount()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position] ?: return

        val bitmap = Bitmap.createBitmap(data.sizeX, data.sizeY, Bitmap.Config.ARGB_8888)
        bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(Base64.decode(data.image, Base64.DEFAULT)))

        holder.cardView.setOnClickListener {
            listener.onItemClick(data)
        }

        holder.imageView.setImageBitmap(bitmap)
        holder.dateText.text = data.created

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.card, viewGroup, false)
        return ViewHolder(v)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val cardView = v.cardView
        val imageView = v.imageView
        val dateText = v.textView
    }

    interface OnItemClickListener {
        fun onItemClick(item: MyData)
    }
}