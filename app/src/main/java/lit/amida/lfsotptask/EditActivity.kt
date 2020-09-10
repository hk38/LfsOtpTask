package lit.amida.lfsotptask

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.SeekBar
import androidx.core.widget.addTextChangedListener
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_edit.*
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*

class EditActivity : AppCompatActivity() {
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        paintView.setSize(paintView.width, paintView.height)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        val realm = Realm.getDefaultInstance()

        setColorBars()
        setWidthbar()

        fab.setOnClickListener{
            val temp = paintView.getImage() ?: return@setOnClickListener
            val byteBuffer = ByteBuffer.allocate(temp.byteCount)
            temp.copyPixelsToBuffer(byteBuffer)
            realm.executeTransaction{
                val data = it.createObject(MyData::class.java, SimpleDateFormat("yyyy/MM/dd-HH:mm:ss", Locale.JAPANESE).format( Date( System.currentTimeMillis() ) ) )
                data.image = Base64.encodeToString(byteBuffer.array(), Base64.DEFAULT)
                data.sizeX = temp.width
                data.sizeY = temp.height

                Log.d("height", data.sizeY.toString())
            }

            finish()
        }

    }

    private fun setColorBars() {
        val seekBarList = listOf(seekBarR, seekBarG, seekBarB)
        val editList = listOf(editR, editG, editB)

        for (i in seekBarList.indices) {
            seekBarList[i].setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, formUser: Boolean) {
                    editList[i].setText(progress.toString())
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })

            editList[i].addTextChangedListener {
                when {
                    editList[i].text.isEmpty() -> editList[i].setText(0.toString())
                    Integer.parseInt(editList[i].text.toString()) > 255 -> editList[i].setText(255.toString())
                    Integer.parseInt(editList[i].text.toString()) < 0 -> editList[i].setText(0.toString())
                }
                editList[i].setSelection(editList[i].text.length)
                seekBarList[i].progress = editList[i].text.toString().toInt()

                paintView.setPathColor(Color.rgb(
                    editR.text.toString().toInt(),
                    editG.text.toString().toInt(),
                    editB.text.toString().toInt(),
                ))
            }
        }
    }

    private fun setWidthbar(){
        barWidth.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, formUser: Boolean) {
                editWidth.setText(progress.toString())
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        editWidth.addTextChangedListener {
            when {
                editWidth.text.isEmpty() -> editWidth.setText(0.toString())
                Integer.parseInt(editWidth.text.toString()) > 100 -> editWidth.setText(100.toString())
                Integer.parseInt(editWidth.text.toString()) < 0 -> editWidth.setText(0.toString())
            }
            editWidth.setSelection(editWidth.text.length)
            barWidth.progress = editWidth.text.toString().toInt()

            paintView.setPathWidth(editWidth.text.toString().toFloat())
        }
    }
}