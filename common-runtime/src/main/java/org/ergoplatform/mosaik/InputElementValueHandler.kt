package org.ergoplatform.mosaik

import org.ergoplatform.mosaik.model.ui.ViewElement
import org.ergoplatform.mosaik.model.ui.input.*

const val scaleErg = 9

abstract class InputElementValueHandler<T> {
    abstract val keyboardType: KeyboardType
    abstract fun isValueValid(value: Any?): Boolean
    abstract fun valueFromStringInput(value: String?): T?
    open fun getAsString(currentValue: Any?): String = currentValue?.toString() ?: ""

    companion object {
        fun getForElement(
            element: ViewElement,
            mosaikRuntime: MosaikRuntime
        ): InputElementValueHandler<*>? =
            when (element) {
                is StringTextField -> StringInputHandler(element)
                is ErgAmountInputField -> DecimalInputHandler(element, scaleErg)
                is DecimalInputField -> DecimalInputHandler(element, element.scale)
                is LongTextField -> IntegerInputHandler(element)
                is ErgoAddressChooseButton -> ErgoAddressChooserInputHandler(element, mosaikRuntime)
                is DropDownList -> DropDownListInputHandler(element, mosaikRuntime)
                is InputElement<*> -> OtherInputHandler(element)
                else -> null
            }
    }
}

class StringInputHandler(private val element: StringTextField) :
    InputElementValueHandler<String>() {
    override fun isValueValid(value: Any?): Boolean {
        val length = (value as? String)?.length ?: 0
        return length >= element.minValue && length <= element.maxValue
    }

    override fun valueFromStringInput(value: String?): String? {
        return value
    }

    override val keyboardType: KeyboardType
        get() = when (element) {
            is PasswordInputField -> KeyboardType.Password
            else -> KeyboardType.Password
        }
}

class IntegerInputHandler(private val element: LongTextField) : InputElementValueHandler<Long>() {
    override fun isValueValid(value: Any?): Boolean {
        return value is Long && value >= element.minValue && value <= element.maxValue
    }

    override fun valueFromStringInput(value: String?): Long? {
        return value?.toLong()
    }

    override val keyboardType: KeyboardType
        get() = KeyboardType.Number
}

class DecimalInputHandler(private val element: LongTextField, val scale: Int) :
    InputElementValueHandler<Long>() {
    override fun isValueValid(value: Any?): Boolean {
        return value is Long && value >= element.minValue && value <= element.maxValue
    }

    override fun valueFromStringInput(value: String?): Long? {
        return if (value.isNullOrBlank()) null
        else value.toBigDecimal().movePointRight(scale).longValueExact()
    }

    override val keyboardType: KeyboardType
        get() = KeyboardType.NumberDecimal
}

open class OtherInputHandler(private val element: InputElement<*>) :
    InputElementValueHandler<Any>() {
    override fun isValueValid(value: Any?): Boolean {
        return if (element is OptionalInputElement<*>) !element.isMandatory || value != null else true
    }

    override fun valueFromStringInput(value: String?): Any? {
        return value
    }

    override val keyboardType: KeyboardType
        get() = KeyboardType.Text
}

class DropDownListInputHandler(
    private val element: DropDownList,
    private val mosaikRuntime: MosaikRuntime
) : InputElementValueHandler<String>() {
    override fun isValueValid(value: Any?): Boolean {
        return !element.isMandatory || element.entries.containsKey(value)
    }

    override fun valueFromStringInput(value: String?): String? {
        return value
    }

    override fun getAsString(currentValue: Any?): String {
        return element.entries[currentValue]
            ?: mosaikRuntime.formatString(StringConstant.PleaseChoose)
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