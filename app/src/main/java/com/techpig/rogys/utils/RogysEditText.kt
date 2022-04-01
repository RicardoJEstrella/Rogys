package com.techpig.rogys.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class RogysEditText(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {
    init {
        applyFont()
    }

    private fun applyFont() {
        //This is used to get the file from the assets folder and set it to the title TextView.
        val typeface: Typeface = Typeface.createFromAsset(context.assets, "Montserrat-Regular.ttf")
        setTypeface(typeface)
    }


}