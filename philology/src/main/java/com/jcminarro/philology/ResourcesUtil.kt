package com.jcminarro.philology

import android.content.res.Resources
import android.icu.text.PluralRules
import android.os.Build
import java.util.Locale

internal class ResourcesUtil(private val baseResources: Resources) {
    private val repository: PhilologyRepository by lazy {
        Philology.getPhilologyRepository(baseResources.currentLocale())
    }

    @Throws(Resources.NotFoundException::class)
    fun getText(id: Int): CharSequence = repository.get(Resource.Text(baseResources.getResourceEntryName(id))) ?: baseResources.getText(id)

    @Throws(Resources.NotFoundException::class)
    fun getString(id: Int): String = getText(id).toString()

    @Throws(Resources.NotFoundException::class)
    fun getQuantityText(id: Int, quantity: Int) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        val plural = PluralRules.forLocale(baseResources.currentLocale()).select(quantity.toDouble())
        repository.get(Resource.Plural(baseResources.getResourceEntryName(id), PluralQuantity.from(plural))) ?: baseResources.getQuantityText(id, quantity)
    } else throw Resources.NotFoundException()

    @Throws(Resources.NotFoundException::class)
    fun getQuantityString(id: Int, quantity: Int) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        getQuantityText(id, quantity).toString()
    } else throw Resources.NotFoundException()

    @Throws(Resources.NotFoundException::class)
    fun getQuantityString(id: Int, quantity: Int, vararg formatArgs: Any?): String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        String.format(getQuantityString(id, quantity), *formatArgs)
    } else throw Resources.NotFoundException()
}

interface PhilologyRepository {
    fun get(resource: Resource): CharSequence?
}

sealed class Resource(open val key: String) {
    data class Text(override val key: String): Resource(key)
    data class Plural(override val key: String, val quantity: PluralQuantity): Resource(key)
}

sealed class PluralQuantity(val name: String) {
    object Zero: PluralQuantity("zero")
    object One: PluralQuantity("one")
    object Two: PluralQuantity("two")
    object Few: PluralQuantity("few")
    object Many: PluralQuantity("many")
    object Other: PluralQuantity("other")

    companion object {
        fun from(name: String) = when(name) {
            "zero" -> Zero
            "one" -> One
            "two" -> Two
            "few" -> Few
            "many" -> Many
            "other" -> Other
            else -> Other
        }
    }
}

@SuppressWarnings("NewApi")
private fun Resources.currentLocale(): Locale = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
    @Suppress("DEPRECATION")
    configuration.locale
} else {
    configuration.locales[0]
}