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
fun <G : ViewGroup> G.ergAddress(
    address: String,
    style: LabelStyle? = null,
    textAlignment: HAlignment? = null,
    textColor: ForegroundColor? = null,
    expand: Boolean = true,
    init: (@MosaikDsl ErgoAddressLabel).() -> Unit = {}
): ErgoAddressLabel =
    viewElement(ErgoAddressLabel().apply {
        this.text = address
        style?.let { this.style = style }
        textAlignment?.let { this.textAlignment = textAlignment }
        textColor?.let { this.textColor = textColor }
        this.isExpandOnClick = expand
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
fun <G : ViewGroup> G.tokenLabel(
    tokenId: String,
    fallbackName: String?,
    amount: Long? = null,
    decimals: Int = 0,
    style: LabelStyle? = null,
    textColor: ForegroundColor? = null,
    decorated: Boolean = true,
    init: (@MosaikDsl TokenLabel).() -> Unit = {}
): TokenLabel =
    viewElement(TokenLabel().apply {
        this.setTokenId(tokenId)
        this.tokenName = fallbackName
        this.amount = amount
        this.decimals = decimals
        this.isDecorated = decorated
        style?.let { this.style = style }
        textColor?.let { this.textColor = textColor }
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
fun <G : ViewGroup> G.qrCode(
    content: String,
    init: (@MosaikDsl QrCode).() -> Unit = {}
): QrCode =
    viewElement(QrCode().apply {
        this.content = content
    }, init)

@MosaikDsl
fun <G : ViewGroup> G.markDown(
    content: String,
    alignment: HAlignment? = null,
    init: (@MosaikDsl MarkDown).() -> Unit = {}
): MarkDown =
    viewElement(MarkDown().apply {
        this.content = content
        alignment?.let { this.contentAlignment = alignment }
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
fun <G : ViewGroup> G.checkboxLabel(
    id: String,
    text: String,
    initialValue: Boolean? = false,
    style: LabelStyle? = null,
    textAlignment: HAlignment? = null,
    textColor: ForegroundColor? = null,
    init: (@MosaikDsl CheckboxLabel).() -> Unit = {}
): Label =
    viewElement(CheckboxLabel().apply {
        this.id = id
        this.text = text
        this.value = initialValue
        style?.let { this.style = style }
        textAlignment?.let { this.textAlignment = textAlignment }
        textColor?.let { this.textColor = textColor }
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
        this.entries = if (entries is LinkedHashMap) entries else LinkedHashMap(entries)
        this.value = initialValue
    }, init)

@MosaikDsl
fun <G : ViewGroup> G.ergoAddressChooser(
    id: String,
    style: StyleableInputButton.InputButtonStyle? = null,
    init: (@MosaikDsl ErgoAddressChooseButton).() -> Unit = {}
): ErgoAddressChooseButton =
    viewElement(ErgoAddressChooseButton().apply {
        this.id = id
        style?.let { this.style = style }
    }, init)

@MosaikDsl
fun <G : ViewGroup> G.ergoWalletChooser(
    id: String,
    style: StyleableInputButton.InputButtonStyle? = null,
    init: (@MosaikDsl WalletChooseButton).() -> Unit = {}
): WalletChooseButton =
    viewElement(WalletChooseButton().apply {
        this.id = id
        style?.let { this.style = style }
    }, init)

@MosaikDsl
fun <G : ViewGroup> G.loadingIndicator(
    size: LoadingIndicator.Size? = null,
    init: (@MosaikDsl LoadingIndicator).() -> Unit = {}
): LoadingIndicator =
    viewElement(LoadingIndicator().apply {
        size?.let { this.size = size }
    }, init)

