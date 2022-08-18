package org.ergoplatform.mosaik

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import org.ergoplatform.mosaik.model.ui.MarkDown
import org.ergoplatform.mosaik.model.ui.layout.HAlignment
import org.ergoplatform.mosaik.model.ui.text.LabelStyle
import org.intellij.markdown.IElementType
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.flavours.gfm.GFMTokenTypes
import org.intellij.markdown.parser.MarkdownParser

/**
 * Mostly copied from https://github.com/mikepenz/multiplatform-markdown-renderer
 * Needed to be because some stuff was not adjustable
 *
 * - Removed images support
 * - Links not bold, but colorized
 * - use contentAlignment
 * - no fillMaxWidth()
 */
@Composable
fun MosaikMarkDown(treeElement: TreeElement, modifier: Modifier) {
    val element = treeElement.element as MarkDown
    val content = element.content

    CompositionLocalProvider(LocalUriHandler provides object : UriHandler {
        override fun openUri(uri: String) {
            treeElement.viewTree.mosaikRuntime.openBrowser(uri)
        }
    }) {
        Column(
            modifier,
            horizontalAlignment = when (element.contentAlignment) {
                HAlignment.START -> Alignment.Start
                HAlignment.CENTER -> Alignment.CenterHorizontally
                HAlignment.END -> Alignment.End
                HAlignment.JUSTIFY -> Alignment.Start
            }
        ) {
            val parsedTree = remember(treeElement.createdAtContentVersion) {
                MarkdownParser(GFMFlavourDescriptor()).buildMarkdownTreeFromString(content)
            }

            val textAlign = element.contentAlignment.toTextAlign()

            CompositionLocalProvider(LocalReferenceLinkHandler provides ReferenceLinkHandlerImpl()) {
                parsedTree.children.forEach { node ->
                    if (!node.handleElement(content, textAlign)) {
                        node.children.forEach { child ->
                            child.handleElement(content, textAlign)
                        }
                    }
                }
            }

        }
    }
}

@Composable
private fun ASTNode.handleElement(content: String, textAlign: TextAlign): Boolean {
    var handled = true
    when (type) {
        MarkdownTokenTypes.TEXT -> Text(
            getTextInNode(content).toString(),
            color = MosaikStyleConfig.defaultLabelColor,
            textAlign = textAlign,
            style = labelStyle(LabelStyle.BODY1)
        )
        MarkdownTokenTypes.EOL -> Spacer(Modifier.padding(4.dp))
        // MarkdownElementTypes.CODE_FENCE -> MarkdownCodeFence(content, this, colors = colors)
        // MarkdownElementTypes.CODE_BLOCK -> MarkdownCodeBlock(content, this, colors = colors)
        MarkdownElementTypes.ATX_1 -> MarkdownHeader(
            content,
            this,
            labelStyle(LabelStyle.HEADLINE1),
            textAlign
        )
        MarkdownElementTypes.ATX_2 -> MarkdownHeader(
            content,
            this,
            labelStyle(LabelStyle.HEADLINE2),
            textAlign
        )
        MarkdownElementTypes.ATX_3 -> MarkdownHeader(
            content,
            this,
            labelStyle(LabelStyle.BODY1BOLD),
            textAlign
        )
        MarkdownElementTypes.ATX_4 -> MarkdownHeader(
            content,
            this,
            labelStyle(LabelStyle.BODY2BOLD),
            textAlign
        )
        MarkdownElementTypes.ATX_5 -> MarkdownHeader(
            content,
            this,
            labelStyle(LabelStyle.BODY1),
            textAlign
        )
        MarkdownElementTypes.ATX_6 -> MarkdownHeader(
            content,
            this,
            labelStyle(LabelStyle.BODY1),
            textAlign
        )
        //MarkdownElementTypes.BLOCK_QUOTE -> MarkdownBlockQuote(content, this, style = typography.body1, color = colors.textColorByType(
        //    MarkdownElementTypes.BLOCK_QUOTE
        //))
        MarkdownElementTypes.PARAGRAPH -> MarkdownParagraph(content, this, textAlign)
        MarkdownElementTypes.ORDERED_LIST -> Column(
            modifier = Modifier.padding(
                top = 8.dp,
                bottom = 8.dp
            )
        ) {
            MarkdownOrderedList(
                content,
                this@handleElement,
                style = labelStyle(LabelStyle.BODY1),
                textAlign = textAlign
            )
        }
        MarkdownElementTypes.UNORDERED_LIST -> Column(
            modifier = Modifier.padding(
                top = 8.dp,
                bottom = 8.dp
            )
        ) {
            MarkdownBulletList(
                content,
                this@handleElement,
                style = labelStyle(LabelStyle.BODY1),
                textAlign = textAlign
            )
        }
        // MarkdownElementTypes.IMAGE -> MarkdownImage(content, this)
        MarkdownElementTypes.LINK_DEFINITION -> {
            val linkLabel =
                findChildOfType(MarkdownElementTypes.LINK_LABEL)?.getTextInNode(content)?.toString()
            if (linkLabel != null) {
                val destination =
                    findChildOfType(MarkdownElementTypes.LINK_DESTINATION)?.getTextInNode(content)
                        ?.toString()
                LocalReferenceLinkHandler.current.store(linkLabel, destination)
            }
        }
        else -> handled = false
    }
    return handled
}

@Composable
private fun MarkdownHeader(
    content: String,
    node: ASTNode,
    style: TextStyle = LocalTextStyle.current,
    textAlign: TextAlign,
) {
    node.findChildOfType(MarkdownTokenTypes.ATX_CONTENT)?.let {
        Text(
            it.getTextInNode(content).trim().toString(),
            modifier = Modifier
                .padding(top = 16.dp),
            style = style,
            textAlign = textAlign,
            color = MosaikStyleConfig.defaultLabelColor
        )
    }
}

@Composable
private fun MarkdownParagraph(
    content: String,
    node: ASTNode,
    textAlign: TextAlign,
    style: TextStyle = labelStyle(LabelStyle.BODY1),
) {
    val styledText = buildAnnotatedString {
        pushStyle(style.toSpanStyle())
        buildMarkdownAnnotatedString(content, node)
        pop()
    }

    MarkdownText(
        styledText,
        style = style,
        color = MosaikStyleConfig.defaultLabelColor,
        textAlign = textAlign
    )
}

@Composable
private fun MarkdownText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign,
    style: TextStyle = LocalTextStyle.current
) {
    val uriHandler = LocalUriHandler.current
    val referenceLinkHandler = LocalReferenceLinkHandler.current
    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }

    val hasUrl = text.getStringAnnotations(TAG_URL, 0, text.length).any()
    val textModifier = if (hasUrl) modifier.pointerInput(Unit) {
        detectTapGestures { pos ->
            layoutResult.value?.let { layoutResult ->
                val position = layoutResult.getOffsetForPosition(pos)
                text.getStringAnnotations(TAG_URL, position, position)
                    .firstOrNull()
                    ?.let { a ->
                        uriHandler.openUri(referenceLinkHandler.find(a.item))
                    }
            }
        }
    } else modifier

    Text(
        text = text,
        modifier = textModifier,
        style = style,
        color = color,
        textAlign = textAlign,
        onTextLayout = { layoutResult.value = it }
    )
}

@Composable
private fun AnnotatedString.Builder.appendMarkdownLink(content: String, node: ASTNode) {
    val linkText =
        node.findChildOfType(MarkdownElementTypes.LINK_TEXT)?.children?.innerList() ?: return
    val destination =
        node.findChildOfType(MarkdownElementTypes.LINK_DESTINATION)?.getTextInNode(content)
            ?.toString()
    val linkLabel =
        node.findChildOfType(MarkdownElementTypes.LINK_LABEL)?.getTextInNode(content)?.toString()
    (destination ?: linkLabel)?.let { pushStringAnnotation(TAG_URL, it) }
    pushStyle(
        SpanStyle(
            textDecoration = TextDecoration.Underline,
            color = MosaikStyleConfig.primaryLabelColor
        )
    )
    buildMarkdownAnnotatedString(content, linkText)
    pop()
}

private fun AnnotatedString.Builder.appendAutoLink(content: String, node: ASTNode) {
    val destination = node.getTextInNode(content).toString()
    pushStringAnnotation(TAG_URL, (destination))
    pushStyle(
        SpanStyle(
            textDecoration = TextDecoration.Underline,
            color = MosaikStyleConfig.primaryLabelColor
        )
    )
    append(destination)
    pop()
}

@Composable
private fun AnnotatedString.Builder.buildMarkdownAnnotatedString(content: String, node: ASTNode) {
    buildMarkdownAnnotatedString(content, node.children)
}

@Composable
private fun AnnotatedString.Builder.buildMarkdownAnnotatedString(
    content: String,
    children: List<ASTNode>
) {
    children.forEach { child ->
        when (child.type) {
            MarkdownElementTypes.PARAGRAPH -> buildMarkdownAnnotatedString(content, child)
            MarkdownElementTypes.IMAGE -> child.findChildOfTypeRecursive(MarkdownElementTypes.LINK_DESTINATION)
                ?.let {
                    appendInlineContent(TAG_IMAGE_URL, it.getTextInNode(content).toString())
                }
            MarkdownElementTypes.EMPH -> {
                pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                buildMarkdownAnnotatedString(content, child)
                pop()
            }
            MarkdownElementTypes.STRONG -> {
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                buildMarkdownAnnotatedString(content, child)
                pop()
            }
            MarkdownElementTypes.CODE_SPAN -> {
                pushStyle(
                    SpanStyle(
                        fontFamily = FontFamily.Monospace,
                        background = MaterialTheme.colors.surface
                    )
                )
                append(' ')
                buildMarkdownAnnotatedString(content, child.children.innerList())
                append(' ')
                pop()
            }
            MarkdownElementTypes.AUTOLINK -> appendAutoLink(content, child)
            MarkdownElementTypes.INLINE_LINK -> appendMarkdownLink(content, child)
            MarkdownElementTypes.SHORT_REFERENCE_LINK -> appendMarkdownLink(content, child)
            MarkdownElementTypes.FULL_REFERENCE_LINK -> appendMarkdownLink(content, child)
            MarkdownTokenTypes.TEXT -> append(child.getTextInNode(content).toString())
            GFMTokenTypes.GFM_AUTOLINK -> if (child.parent == MarkdownElementTypes.LINK_TEXT) {
                append(child.getTextInNode(content).toString())
            } else appendAutoLink(content, child)
            MarkdownTokenTypes.SINGLE_QUOTE -> append('\'')
            MarkdownTokenTypes.DOUBLE_QUOTE -> append('\"')
            MarkdownTokenTypes.LPAREN -> append('(')
            MarkdownTokenTypes.RPAREN -> append(')')
            MarkdownTokenTypes.LBRACKET -> append('[')
            MarkdownTokenTypes.RBRACKET -> append(']')
            MarkdownTokenTypes.LT -> append('<')
            MarkdownTokenTypes.GT -> append('>')
            MarkdownTokenTypes.COLON -> append(':')
            MarkdownTokenTypes.EXCLAMATION_MARK -> append('!')
            MarkdownTokenTypes.BACKTICK -> append('`')
            MarkdownTokenTypes.HARD_LINE_BREAK -> append("\n\n")
            MarkdownTokenTypes.EOL -> append('\n')
            MarkdownTokenTypes.WHITE_SPACE -> append(' ')
        }
    }
}

@Composable
private fun MarkdownBulletList(
    content: String,
    node: ASTNode,
    modifier: Modifier = Modifier,
    textAlign: TextAlign,
    style: TextStyle = LocalTextStyle.current,
    level: Int = 0
) {
    val bulletHandler = LocalBulletListHandler.current
    MarkdownListItems(content, node, modifier, style, textAlign, level) { child ->
        Row(Modifier) {
            Text(
                bulletHandler.transform(
                    child.findChildOfType(MarkdownTokenTypes.LIST_BULLET)?.getTextInNode(content)
                ),
                style = style,
                textAlign = textAlign,
                color = MosaikStyleConfig.defaultLabelColor
            )
            val text = buildAnnotatedString {
                pushStyle(style.toSpanStyle())
                buildMarkdownAnnotatedString(content, child.children.filterNonListTypes())
                pop()
            }
            MarkdownText(
                text,
                modifier.padding(bottom = 4.dp),
                style = style,
                textAlign = textAlign
            )
        }
    }
}

@Composable
private fun MarkdownOrderedList(
    content: String,
    node: ASTNode,
    modifier: Modifier = Modifier,
    textAlign: TextAlign,
    style: TextStyle = LocalTextStyle.current,
    level: Int = 0,
) {
    val orderedListHandler = LocalOrderedListHandler.current
    MarkdownListItems(content, node, modifier, style, textAlign, level) { child ->
        Row(Modifier) {
            Text(
                orderedListHandler.transform(
                    child.findChildOfType(MarkdownTokenTypes.LIST_NUMBER)?.getTextInNode(content)
                ),
                style = style,
                textAlign = textAlign,
                color = MosaikStyleConfig.defaultLabelColor
            )
            val text = buildAnnotatedString {
                pushStyle(style.toSpanStyle())
                buildMarkdownAnnotatedString(content, child.children.filterNonListTypes())
                pop()
            }
            MarkdownText(
                text,
                modifier.padding(bottom = 4.dp),
                style = style,
                textAlign = textAlign
            )
        }
    }
}

@Composable
private fun MarkdownListItems(
    content: String,
    node: ASTNode,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    textAlign: TextAlign,
    level: Int = 0,
    item: @Composable (child: ASTNode) -> Unit
) {
    Column(modifier = modifier.padding(start = (8.dp) * level)) {
        node.children.forEach { child ->
            when (child.type) {
                MarkdownElementTypes.LIST_ITEM -> {
                    item(child)
                    when (child.children.last().type) {
                        MarkdownElementTypes.ORDERED_LIST -> MarkdownOrderedList(
                            content,
                            child,
                            modifier,
                            textAlign,
                            style,
                            level + 1
                        )
                        MarkdownElementTypes.UNORDERED_LIST -> MarkdownBulletList(
                            content,
                            child,
                            modifier,
                            textAlign,
                            style,
                            level + 1
                        )
                    }
                }
                MarkdownElementTypes.ORDERED_LIST -> MarkdownOrderedList(
                    content,
                    child,
                    modifier,
                    textAlign,
                    style,
                    level + 1
                )
                MarkdownElementTypes.UNORDERED_LIST -> MarkdownBulletList(
                    content,
                    child,
                    modifier,
                    textAlign,
                    style,
                    level + 1
                )
            }
        }
    }
}

/**
 * Helper function to filter out items within a list of nodes, not of interest for the bullet list.
 */
internal fun List<ASTNode>.filterNonListTypes(): List<ASTNode> = this.filter { n ->
    n.type != MarkdownElementTypes.ORDERED_LIST && n.type != MarkdownElementTypes.UNORDERED_LIST && n.type != MarkdownTokenTypes.EOL
}

/**
 * Helper function to drop the first and last element in the children list.
 * E.g. we don't want to render the brackets of a link
 */
internal fun List<ASTNode>.innerList(): List<ASTNode> = this.subList(1, this.size - 1)


/**
 * Interface to describe the [ReferenceLinkHandler]
 */
private interface ReferenceLinkHandler {
    /** Keep the provided link */
    fun store(label: String, destination: String?)

    /** Returns the link for the provided label if it exists */
    fun find(label: String): String
}

/**
 * Local [ReferenceLinkHandler] provider
 */
private val LocalReferenceLinkHandler = staticCompositionLocalOf<ReferenceLinkHandler> {
    error("CompositionLocal ReferenceLinkHandler not present")
}

/**
 * Implementation for [ReferenceLinkHandler] to resolve referenced link within the Markdown
 */
private class ReferenceLinkHandlerImpl : ReferenceLinkHandler {
    private val stored = mutableMapOf<String, String?>()
    override fun store(label: String, destination: String?) {
        stored[label] = destination
    }

    override fun find(label: String): String {
        return stored[label] ?: label
    }
}


/**
 * Tag used to indicate an url for inline content. Required for click handling.
 */
private const val TAG_URL = "MARKDOWN_URL"

/**
 * Tag used to indicate an image url for inline content. Required for rendering.
 */
private const val TAG_IMAGE_URL = "MARKDOWN_IMAGE_URL"

/**
 * Find a child node recursive
 */
private fun ASTNode.findChildOfTypeRecursive(type: IElementType): ASTNode? {
    children.forEach {
        if (it.type == type) {
            return it
        } else {
            val found = it.findChildOfTypeRecursive(type)
            if (found != null) {
                return found
            }
        }
    }
    return null
}


/**
 * The CompositionLocal to provide functionality related to transforming the bullet of an ordered list
 */
private val LocalBulletListHandler = staticCompositionLocalOf {
    return@staticCompositionLocalOf BulletHandler { "• " }
}

/**
 * The CompositionLocal to provide functionality related to transforming the bullet of an ordered list
 */
private val LocalOrderedListHandler = staticCompositionLocalOf {
    return@staticCompositionLocalOf BulletHandler { "$it " }
}


/** An interface of providing use case specific un/ordered list handling.*/
private fun interface BulletHandler {
    /** Transforms the bullet icon */
    fun transform(bullet: CharSequence?): String
}