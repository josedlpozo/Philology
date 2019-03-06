package com.jcminarro.philology.sample

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.jcminarro.philology.Philology
import com.jcminarro.sample.R
import io.github.inflationx.viewpump.ViewPumpContextWrapper

class MainActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(Philology.wrap(newBase)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        findViewById<TextView>(R.id.tvPlural).text = resources.getQuantityString(R.plurals.plurals_sample, 1)
        findViewById<TextView>(R.id.tvPluralFormat).text = resources.getQuantityString(R.plurals.plurals_sample_format, 1, "Hola")
        Handler().postDelayed({
                                  findViewById<TextView>(R.id.tvPlural).text = resources.getQuantityString(R.plurals.plurals_sample, 54)
                                  findViewById<TextView>(R.id.tvPluralFormat).text = resources.getQuantityString(R.plurals.plurals_sample_format, 54, "Adios")
                              }, 10000)
    }
}
