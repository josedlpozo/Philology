package com.jcminarro.philology.sample

import android.app.Application
import com.jcminarro.philology.Philology
import com.jcminarro.philology.PhilologyInterceptor
import com.jcminarro.philology.PhilologyRepository
import com.jcminarro.philology.PhilologyRepositoryFactory
import com.jcminarro.philology.PluralQuantity.Few
import com.jcminarro.philology.PluralQuantity.Many
import com.jcminarro.philology.PluralQuantity.One
import com.jcminarro.philology.PluralQuantity.Other
import com.jcminarro.philology.PluralQuantity.Two
import com.jcminarro.philology.PluralQuantity.Zero
import com.jcminarro.philology.Resource
import com.jcminarro.philology.Resource.Plural
import com.jcminarro.philology.Resource.Text
import io.github.inflationx.viewpump.ViewPump
import java.util.Locale

class App : Application() {

    override fun onCreate() {
        super.onCreate()
// Init Philology with our PhilologyRepositoryFactory
        Philology.init(MyPhilologyRepositoryFactory)
// Add PhilologyInterceptor to ViewPump
// If you are already using Calligraphy you can add both interceptors, there is no problem
        ViewPump.init(ViewPump.builder().addInterceptor(PhilologyInterceptor).build())
    }
}

object MyPhilologyRepositoryFactory : PhilologyRepositoryFactory {
    override fun getPhilologyRepository(locale: Locale): PhilologyRepository? = when {
        Locale.ENGLISH.language == locale.language -> EnglishPhilologyRepository
        Locale("es", "ES").language == locale.language -> SpanishPhilologyRepository
// If we don't support a language we could return null as PhilologyRepository and
// values from the strings resources file will be used
        else -> null
    }
}

object EnglishPhilologyRepository : PhilologyRepository {
    override fun get(resource: Resource): CharSequence? = when (resource) {
        is Text -> when(resource.key) {
            "label" -> "New value for the `label` key, it could be fetched from a database or an external API server"
            else -> null
        }
        is Plural -> when (resource.key) {
            "plurals_sample" -> when (resource.quantity) {
                One -> "Plural test 1"
                Other -> "Plural test other"
                Zero -> "Plural test zero"
                Two -> "Plural test 2"
                Few -> "Plural test few"
                Many -> "Plural test many"
            }
            "plurals_sample_format" -> "Plural test %s"
            else -> null
        }
// If we don't want reword an strings we could return null and the value from the string resources file will be used
        else -> null
    }
}

object SpanishPhilologyRepository : PhilologyRepository {
    override fun get(resource: Resource): CharSequence? = when (resource) {
        is Text -> when (resource.key) {
            "label" -> "Nuevo valor para la clave `label`, puede ser obtenida de una base de datos o un servidor externo"
            else -> null
        }
        is Plural -> when (resource.key) {
            "plurals_sample" -> when (resource.quantity) {
                One -> "Plural prueba 1"
                Other -> "Plural prueba other"
                Zero -> "Plural prueba zero"
                Two -> "Plural prueba 2"
                Few -> "Plural prueba few"
                Many -> "Plural prueba many"
            }
            "plurals_sample_format" -> "Plural prueba %s"
            else -> null
        }
        else -> null
    }

}