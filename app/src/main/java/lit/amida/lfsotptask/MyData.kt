package lit.amida.lfsotptask

import android.graphics.Bitmap
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

open class MyData(
    @PrimaryKey open var created: String = SimpleDateFormat("yyyy/MM/dd-HH:mm:ss", Locale.JAPANESE).format(Date(System.currentTimeMillis())),
    open var image: String = "",
    open var sizeX: Int = 0,
    open var sizeY: Int = 0
): RealmObject()