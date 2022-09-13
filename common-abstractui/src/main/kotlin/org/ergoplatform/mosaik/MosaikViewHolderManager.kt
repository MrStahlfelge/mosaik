package org.ergoplatform.mosaik

import java.util.*

/**
 * This class holds the UiViewHolder and its children and manages updates to it and its children.
 *
 * When creating the ViewHolderManager for the root element, do it like follows:
 *
 *      val rootElement = mosaikRuntime.viewTree.content
 *      val rootManager = MosaikViewHolderManager<MyUiViewHolder>(rootElement, ::buildMyUiViewHolder)
 *      setRootView(rootManager.mosaikViewHolder) // <- your code to add the view element to your UI
 *      rootManager.updateChildren(true) // call this to add all the children
 *
 *  For any change to the ViewTree, call
 *
 *      rootManager.updateView(rootElement, ::replaceElement)
 *
 */
class MosaikViewHolderManager<A : MosaikViewHolder>(
    /**
     * Initial tree element this ViewHolderManager is created for. This tree element can change
     * during the life time of this manager (by calling [updateView])
     */
    treeElement: TreeElement,
    /**
     * Builder/factory method to build new View Holders for tree elements. Used for initial setup
     * as well as updating the uiViewHolder when needed.
     */
    private val viewHolderBuilder: (TreeElement) -> A
) {

    var treeElement = treeElement
        private set
    var mosaikViewHolder: A = viewHolderBuilder(treeElement)
        private set
    private val children: MutableList<MosaikViewHolderManager<A>> =
        LinkedList<MosaikViewHolderManager<A>>()

    /**
     * Call this for the [ViewTree] root element on any update of the [ViewTree]. It will update
     * this and all child ViewHolderManagers and ViewHolders when needed.
     *
     * replaceOnParent method to replace the UI element on your UI framework's root view. Note that
     * for the first time, you need to add the [mosaikViewHolder] yourself to your root view and call
     * [updateChildren](true)
     */
    fun updateView(
        newTreeElement: TreeElement,
        replaceOnParent: (oldView: A, newView: A) -> Unit,
    ) {
        val elementChanged =
            if (newTreeElement.createdAtContentVersion > treeElement.createdAtContentVersion) {
                treeElement = newTreeElement

                // if the element changed, remove it from parent and readd the new one
                val newViewHolder = viewHolderBuilder(treeElement)
                replaceOnParent(mosaikViewHolder, newViewHolder)
                removeAllChildren()
                mosaikViewHolder.onRemovedFromSuperview()
                newViewHolder.onAddedToSuperview()

                mosaikViewHolder = newViewHolder
                true
            } else false

        // resource bytes might have an update
        treeElement.getResourceBytes?.let {
            mosaikViewHolder.resourceBytesAvailable(it)
        }

        updateChildren(elementChanged)
    }

    fun updateChildren(parentChanged: Boolean) {
        val mosaikViewGroupHolder = mosaikViewHolder as? MosaikViewGroupHolder<A> ?: return
        val newChildren = treeElement.children

        // check if a children are to be added or removed
        // this is the case when the parent was changed or children aren't initialized yet
        val childAddedOrRemoved = parentChanged || newChildren.size != children.size

        if (childAddedOrRemoved) {
            // remove all children
            removeAllChildren()

            // then add the new elements
            newChildren.forEach {
                val newElem = MosaikViewHolderManager(it, viewHolderBuilder)
                mosaikViewGroupHolder.addSubView(newElem.mosaikViewHolder)
                newElem.mosaikViewHolder.onAddedToSuperview()
                children.add(newElem)
            }
        }

        children.zip(newChildren).forEach { (childViewHolder, newChild) ->
            childViewHolder.updateView(
                newChild, // might be the old child, updateview will check
                mosaikViewGroupHolder::replaceSubView
            )
        }
    }

    fun removeAllChildren() {
        (mosaikViewHolder as? MosaikViewGroupHolder<*>)?.removeAllChildren()
        children.forEach {
            it.removeAllChildren()
            it.mosaikViewHolder.onRemovedFromSuperview()
        }
        children.clear()
    }

}

/**
 * Interface your UI framework specific UiViewHolders must implement.
 */
interface MosaikViewHolder {
    /**
     * Called after this view was added to its parent view by [MosaikViewGroupHolder.addSubView]
     * or [MosaikViewGroupHolder.replaceSubView], but before its child elements are added
     */
    fun onAddedToSuperview()

    /**
     * Called after this view was removed from its parent view. Child elements are removed first
     */
    fun onRemovedFromSuperview()

    /**
     * Called when downloadable resources are available. Might be called multiple times.
     */
    fun resourceBytesAvailable(bytes: ByteArray)
}

/**
 * Interface your UI framework specific UiViewHolders for all [ViewGroup] elements must implement.
 */
interface MosaikViewGroupHolder<VH : MosaikViewHolder> {

    /**
     * Called when all child views should be removed
     */
    fun removeAllChildren()

    /**
     * Called when the subviewHolder element should be added to this holder's view group
     */
    fun addSubView(subviewHolder: VH)

    /**
     * Called when the newView holder's element should replace oldView holder's element in this
     * holder's view group
     */
    fun replaceSubView(oldView: VH, newView: VH)
}