package org.ergoplatform.mosaik

import org.ergoplatform.mosaik.model.ui.*
import org.ergoplatform.mosaik.model.ui.input.*
import org.ergoplatform.mosaik.model.ui.layout.HAlignment
import org.ergoplatform.mosaik.model.ui.layout.HorizontalRule
import org.ergoplatform.mosaik.model.ui.layout.Padding
import org.ergoplatform.mosaik.model.ui.text.*

@MosaikDsl
fun <G : ViewGroup> G.label(
    text: String,
    style: LabelStyle? = null,
    textAlignment: HAlignment? = null,
    textColor: ForegroundColor? = null,
    init: (@MosaikDsl Label).() -> Unit = {}
): Label =
    viewElement(Label().apply {
        this.text = text
        style?.let { this.style = style }
        textAlignment?.let { this.textAlignment = textAlignment }
        textColor?.let { this.textColor = textColor }
    }, init)

@MosaikDsl
fun <G : ViewGroup> G.ergAmount(
    nanoErg: Long,
    style: LabelStyle? = null,
    textAlignment: HAlignment? = null,
    textColor: ForegroundColor? = null,
    maxDecimals: Int = 4,
    withCurrency: Boolean = true,
    trimTrailingZero: Boolean = false,
    init: (@MosaikDsl ErgAmountLabel).() -> Unit = {}
): ErgAmountLabel =
    viewElement(ErgAmountLabel().apply {
        this.text = nanoErg
        style?.let { this.style = style }
        textAlignment?.let { this.textAlignment = textAlignment }
        textColor?.let { this.textColor = textColor }
        this.maxDecimals = maxDecimals
        this.isWithCurrencySymbol = withCurrency
        this.isTrimTrailingZero = trimTrailingZero
    }, init)

@MosaikDsl
fun <G : ViewGroup> G.fiatAmount(
    nanoErg: Long,
    style: LabelStyle? = null,
    textAlignment: HAlignment? = null,
    textColor: ForegroundColor? = null,
    fallbackToErg: Boolean = false,
    init: (@MosaikDsl FiatAmountLabel).() -> Unit = {}
): FiatAmountLabel =
    viewElement(FiatAmountLabel().apply {
        this.text = nanoErg
        style?.let { this.style = style }
        textAlignment?.let { this.textAlignment = textAlignment }
        textColor?.let { this.textColor = textColor }
        this.isFallbackToErg = fallbackToErg
    }, init)

@MosaikDsl
fun <G : ViewGroup> G.button(
    text: String,
    style: Button.ButtonStyle? = null,
    init: (@MosaikDsl Button).() -> Unit = {}
): Button =
    viewElement(Button().apply {
        this.text = text
        style?.let { this.style = style }
    }, init)

@MosaikDsl
fun <G : ViewGroup> G.hr(
    verticalPadding: Padding? = null,
    init: (@MosaikDsl HorizontalRule).() -> Unit = {}
): HorizontalRule =
    viewElement(HorizontalRule().apply {
        verticalPadding?.let { this.setvPadding(verticalPadding) }
    }, init)

@MosaikDsl
fun <G : ViewGroup> G.image(
    url: String,
    size: Image.Size? = null,
    init: (@MosaikDsl Image).() -> Unit = {}
): Image =
    viewElement(Image().apply {
        this.url = url
        size?.let { this.size = size }
    }, init)

@MosaikDsl
fun <G : ViewGroup> G.icon(
    iconType: IconType,
    size: Icon.Size? = null,
    tintColor: ForegroundColor? = null,
    init: (@MosaikDsl Icon).() -> Unit = {}
): Icon =
    viewElement(Icon().apply {
        this.iconType = iconType
        size?.let { this.iconSize = size }
        tintColor?.let { this.tintColor = tintColor }
    }, init)

@MosaikDsl
fun <G : ViewGroup> G.textInputField(
    id: String,
    placeholder: String? = null,
    initialValue: String? = null,
    init: (@MosaikDsl TextInputField).() -> Unit = {}
): TextInputField =
    viewElement(TextInputField().apply {
        this.id = id
        placeholder?.let { this.placeholder = placeholder }
        initialValue?.let { this.value = initialValue }
    }, init)

@MosaikDsl
fun <G : ViewGroup> G.passwordInputField(
    id: String,
    placeholder: String? = null,
    initialValue: String? = null,
    init: (@MosaikDsl PasswordInputField).() -> Unit = {}
): PasswordInputField =
    viewElement(PasswordInputField().apply {
        this.id = id
        placeholder?.let { this.placeholder = placeholder }
        initialValue?.let { this.value = initialValue }
    }, init)

@MosaikDsl
fun <G : ViewGroup> G.ergAddressInputField(
    id: String,
    placeholder: String? = null,
    initialValue: String? = null,
    mandatory: Boolean = false,
    init: (@MosaikDsl ErgAddressInputField).() -> Unit = {}
): ErgAddressInputField =
    viewElement(ErgAddressInputField().apply {
        this.id = id
        placeholder?.let { this.placeholder = placeholder }
        initialValue?.let { this.value = initialValue }
        if (mandatory) {
            minValue = 1
        }
    }, init)

@MosaikDsl
fun <G : ViewGroup> G.integerInputField(
    id: String,
    placeholder: String? = null,
    initialValue: Long? = null,
    init: (@MosaikDsl IntegerInputField).() -> Unit = {}
): IntegerInputField =
    viewElement(IntegerInputField().apply {
        this.id = id
        placeholder?.let { this.placeholder = placeholder }
        initialValue?.let { this.value = initialValue }
    }, init)

@MosaikDsl
fun <G : ViewGroup> G.decimalInputField(
    id: String,
    scale: Int,
    placeholder: String? = null,
    initialRawValue: Long? = null,
    init: (@MosaikDsl DecimalInputField).() -> Unit = {}
): DecimalInputField =
    viewElement(DecimalInputField().apply {
        this.id = id
        this.scale = scale
        placeholder?.let { this.placeholder = placeholder }
        initialRawValue?.let { this.value = initialRawValue }
    }, init)

@MosaikDsl
fun <G : ViewGroup> G.ergAmountInputField(
    id: String,
    placeholder: String? = null,
    initialValue: Long? = null,
    canUseFiatInput: Boolean = false,
    init: (@MosaikDsl ErgAmountInputField).() -> Unit = {}
): ErgAmountInputField =
    viewElement((if (canUseFiatInput) FiatOrErgAmountInputField() else ErgAmountInputField()).apply {
        this.id = id
        placeholder?.let { this.placeholder = placeholder }
        initialValue?.let { this.value = initialValue }
    }, init)

@MosaikDsl
fun <G : ViewGroup> G.dropDownList(
    id: String,
    entries: Map<String, String>,
    initialValue: String? = null,
    init: (@MosaikDsl DropDownList).() -> Unit = {}
): DropDownList =
    viewElement(DropDownList().apply {
        this.id = id
        this.entries = entries
        this.value = initialValue
    }, init)

@MosaikDsl
fun <G : ViewGroup> G.ergoAddressChooser(
    id: String,
    init: (@MosaikDsl ErgoAddressChooseButton).() -> Unit = {}
): ErgoAddressChooseButton =
    viewElement(ErgoAddressChooseButton().apply {
        this.id = id
    }, init)

@MosaikDsl
fun <G : ViewGroup> G.loadingIndicator(
    size: LoadingIndicator.Size? = null,
    init: (@MosaikDsl LoadingIndicator).() -> Unit = {}
): LoadingIndicator =
    viewElement(LoadingIndicator().apply {
        size?.let { this.size = size }
    }, init)

