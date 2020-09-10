package lit.amida.lfsotptask

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import io.realm.Realm
import io.realm.Sort
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.nio.ByteBuffer

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val realm = Realm.getDefaultInstance()

        val intent = Intent(applicationContext, EditActivity::class.java)

        fab.setOnClickListener{
            startActivity(intent)
        }

        val adapter = MyAdapter(this, realm.where(MyData::class.java).findAll().sort(
            "created",
            Sort.ASCENDING
        ), object : MyAdapter.OnItemClickListener {
            override fun onItemClick(item: MyData) {
                val bitmap = Bitmap.createBitmap(item.sizeX, item.sizeY, Bitmap.Config.ARGB_8888)
                bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(Base64.decode(item.image, Base64.DEFAULT)))

                val shareFile = File("${File(applicationContext.filesDir.toString() + "/images")}/share_file")
                val uri = FileProvider.getUriForFile(applicationContext, BuildConfig.APPLICATION_ID + ".fileprovider", shareFile)

                val fileName = "share.png"

                val shareIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, uri)
                    type = "image/jpeg"
                }
                startActivity(Intent.createChooser(shareIntent, "共有"))
            }
        }, true
        )
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
    }
}