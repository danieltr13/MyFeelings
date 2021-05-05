package trinidad.daniel.myfeelings.utilities

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.content.contentValuesOf
import trinidad.daniel.myfeelings.R

class CustomBarDrawable: Drawable {
    var coordenadas: RectF?=null
    var context:Context?=null
    var feeling:Feelings?=null

    constructor(context: Context, feeling:Feelings){
        this.context=context
        this.feeling=feeling
    }

    override fun draw(canvas: Canvas) {
        val fondo:Paint= Paint()
        fondo.style=Paint.Style.FILL
        fondo.isAntiAlias=true
        fondo.color= context?.resources?.getColor(R.color.gray)?:R.color.gray
        val ancho:Float=(canvas.width-10).toFloat()
        val alto:Float=(canvas.height-10).toFloat()

        coordenadas= RectF(0.0F, 0.0F, ancho, alto)

        canvas.drawRect(coordenadas!!, fondo)

        if(this.feeling!=null){
            val percentage:Float=this.feeling!!.percentage*(canvas.width-10)/100
            var coordenadas2 = RectF(0.0F, 0.0F, percentage, alto)

            var seccion: Paint= Paint()
            seccion.style=Paint.Style.FILL
            seccion.isAntiAlias=true
            seccion.color=ContextCompat.getColor(this.context!!,feeling!!.color)

            canvas.drawRect(coordenadas2!!, seccion)
        }
    }

    override fun setAlpha(alpha: Int) {

    }

    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }
}