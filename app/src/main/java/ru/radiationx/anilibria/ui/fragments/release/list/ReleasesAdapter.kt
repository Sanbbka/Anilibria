package ru.radiationx.anilibria.ui.fragments.release.list

import android.util.Log
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import ru.radiationx.anilibria.entity.app.release.ReleaseItem
import ru.radiationx.anilibria.entity.app.vital.VitalItem
import ru.radiationx.anilibria.ui.adapters.*
import ru.radiationx.anilibria.ui.adapters.global.LoadMoreDelegate
import ru.radiationx.anilibria.ui.adapters.release.list.ReleaseItemDelegate
import java.util.*

/* Created by radiationx on 31.10.17. */

open class ReleasesAdapter(
        var listener: ItemListener,
        private val placeHolder: PlaceholderListItem
) : ListDelegationAdapter<MutableList<ListItem>>() {

    private val vitalItems = mutableListOf<VitalItem>()
    private val random = Random()

    var endless: Boolean = false
        set(enable) {
            field = enable
            removeLoadMore()
            addLoadMore()
            notifyDataSetChanged()
        }

    init {
        items = mutableListOf()
        delegatesManager.run {
            addDelegate(ReleaseItemDelegate(listener))
            addDelegate(LoadMoreDelegate(listener))
            addDelegate(PlaceholderDelegate())
            addDelegate(VitalWebItemDelegate())
            addDelegate(VitalNativeItemDelegate())
        }
    }

    protected fun updatePlaceholder(condition: Boolean = items.isEmpty()) {
        if (condition) {
            items.add(placeHolder)
        } else {
            items.removeAll { it is PlaceholderListItem }
        }
    }

    private fun rand(from: Int, to: Int): Int {
        return random.nextInt(to - from) + from
    }

    fun setVitals(vitals: List<VitalItem>) {
        vitalItems.clear()
        vitalItems.addAll(vitals)
    }

    private fun removeLoadMore() {
        this.items.removeAll { it is LoadMoreListItem }
    }

    protected fun addLoadMore() {
        if (endless) {
            this.items.add(LoadMoreListItem())
        }
    }

    protected fun randomInsertVitals() {
        if (vitalItems.isNotEmpty() && items.isNotEmpty()) {
            val randomIndex = rand(0, Math.min(8, items.size))
            if (randomIndex < 6) {
                val randomVital = if (vitalItems.size > 1) rand(0, vitalItems.size) else 0
                val listItem = getVitalListItem(vitalItems[randomVital])
                this.items.add(items.lastIndex - randomIndex, listItem)
            }
        }
    }

    protected fun getVitalListItem(item: VitalItem) = when (item.contentType) {
        VitalItem.ContentType.WEB -> VitalWebListItem(item)
        else -> VitalNativeListItem(item)
    }

    fun insertMore(newItems: List<ReleaseItem>) {
        val prevItems = itemCount
        removeLoadMore()
        this.items.addAll(newItems.map { ReleaseListItem(it) })
        randomInsertVitals()
        addLoadMore()
        notifyItemRangeInserted(prevItems, itemCount)
    }

    open fun bindItems(newItems: List<ReleaseItem>) {
        this.items.clear()
        this.items.addAll(newItems.map { ReleaseListItem(it) })
        updatePlaceholder()
        randomInsertVitals()
        addLoadMore()
        notifyDataSetChanged()
    }

    interface ItemListener : LoadMoreDelegate.Listener, ReleaseItemDelegate.Listener
}
