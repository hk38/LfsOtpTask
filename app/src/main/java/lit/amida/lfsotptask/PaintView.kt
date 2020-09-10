package lit.amida.lfsotptask

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.PixelCopy
import android.view.View

class PaintView(context: Context, attrs: AttributeSet? = null):View(context, attrs) {
    val gridPaint = Paint()
    val paint = Paint()
    val path = Path()
    var canvas = Canvas()
    var sizeX: Int? = null
    var sizeY: Int? = null
    var gridGuid: BitmapDrawable? = null
    var bmp: Bitmap? = null

    init {
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeWidth = 5f

        gridPaint.color = Color.LTGRAY
        gridPaint.style = Paint.Style.STROKE
        gridPaint.strokeJoin = Paint.Join.ROUND
        gridPaint.strokeCap = Paint.Cap.ROUND
        gridPaint.strokeWidth = 10f
    }

    fun setSize(x:Int, y:Int){
        sizeX = x
        sizeY = y

        val gridBmp = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888)
        val gridCanvas = Canvas(gridBmp)
        gridCanvas.drawRect(0f, 0f, x.toFloat(), y.toFloat(), gridPaint)

        gridPaint.strokeWidth = 2f
        var lineX = 0f
        var lineY = 0f
        val width = x / 10f
        val height = y / 10f

        for(i in 1..9){
            lineX += height
            lineY += width
            gridCanvas.drawLine(0f, lineX, x.toFloat(), lineX, gridPaint)
            gridCanvas.drawLine(lineY, 0f, lineY, y.toFloat(), gridPaint)
        }

        gridGuid = BitmapDrawable(resources, gridBmp)
        this.foreground = gridGuid

        bmp = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888)
        canvas.setBitmap(bmp)
        canvas.drawColor(Color.WHITE)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event == null) return true
        val x = event.x
        val y  = event.y

        when(event.action){
            MotionEvent.ACTION_DOWN ->{
                path.reset()
                path.moveTo(x, y)
            }
            MotionEvent.ACTION_MOVE ->{
                path.lineTo(x, y)
            }
            MotionEvent.ACTION_UP ->{
                path.lineTo(x, y)
            }
        }

        draw()
        return true
    }

    private fun draw(){
        canvas.drawPath(path, paint)
        this.background = BitmapDrawable(resources, bmp)
    }

    fun setPathColor(color:Int){
        paint.color = color
    }

    fun setPathWidth(width:Float){
        paint.strokeWidth = width
    }

    fun getImage():Bitmap?{
        return bmp
    }

    fun setImage(bitmap: Bitmap){
        canvas.setBitmap(bitmap)
        bmp = bitmap
        this.background = BitmapDrawable(resources, bmp)
    }
}