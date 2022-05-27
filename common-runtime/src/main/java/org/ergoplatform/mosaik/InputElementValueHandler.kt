package org.ergoplatform.mosaik

import org.ergoplatform.mosaik.model.ui.ViewElement
import org.ergoplatform.mosaik.model.ui.input.InputElement
import org.ergoplatform.mosaik.model.ui.input.LongTextField
import org.ergoplatform.mosaik.model.ui.input.StringTextField

abstract class InputElementValueHandler<T> {
    abstract val keyboardType: KeyboardType
    abstract fun isValueValid(value: Any?): Boolean
    abstract fun valueFromStringInput(value: String?): T?

    companion object {
        fun getForElement(element: ViewElement): InputElementValueHandler<*>? =
            when (element) {
                is StringTextField -> StringInputHandler(element)
                is LongTextField -> LongInputHandler(element)
                is InputElement<*> -> OtherInputHandler(element)
                else -> null
            }
    }
}

class StringInputHandler(val element: StringTextField) : InputElementValueHandler<String>() {
    override fun isValueValid(value: Any?): Boolean {
        val length = (value as? String)?.length ?: 0
        return length >= element.minValue && length <= element.maxValue
    }

    override fun valueFromStringInput(value: String?): String? {
        return value
    }

    override val keyboardType: KeyboardType
        get() = KeyboardType.Text // TODO might be password or email
}

class LongInputHandler(val element: LongTextField) : InputElementValueHandler<Long>() {
    override fun isValueValid(value: Any?): Boolean {
        // TODO check
        return value is Long && value >= element.minValue && value <= element.maxValue
    }

    override fun valueFromStringInput(value: String?): Long? {
        return value?.toLong()
    }

    override val keyboardType: KeyboardType
        get() = KeyboardType.Number
}

class OtherInputHandler(val element: InputElement<*>) : InputElementValueHandler<Any>() {
    override fun isValueValid(value: Any?): Boolean {
        // TODO mandatory elements should return false
        return true
    }

    override fun valueFromStringInput(value: String?): Any? {
        return value
    }

    override val keyboardType: KeyboardType
        get() = KeyboardType.Text
}

enum class KeyboardType {
    Text,
    Number,
    NumberDecimal,
    Email,
    Password
}