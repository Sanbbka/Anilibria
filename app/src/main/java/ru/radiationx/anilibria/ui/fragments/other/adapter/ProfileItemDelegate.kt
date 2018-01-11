package ru.radiationx.anilibria.ui.fragments.other.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import com.nostra13.universalimageloader.core.ImageLoader
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.item_other_profile.view.*
import ru.radiationx.anilibria.App
import ru.radiationx.anilibria.R
import ru.radiationx.anilibria.entity.app.other.ProfileItem
import ru.radiationx.anilibria.ui.common.ListItem
import ru.radiationx.anilibria.ui.common.ProfileListItem

class ProfileItemDelegate : AdapterDelegate<MutableList<ListItem>>() {
    private val dimensionsProvider = App.injections.dimensionsProvider
    private var compositeDisposable = CompositeDisposable()

    override fun isForViewType(items: MutableList<ListItem>, position: Int): Boolean
            = items[position] is ProfileListItem

    override fun onBindViewHolder(items: MutableList<ListItem>, position: Int, holder: RecyclerView.ViewHolder, payloads: MutableList<Any>) {
        val item = items[position] as ProfileListItem
        (holder as ViewHolder).bind(item.profileItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
            = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_other_profile, parent, false))

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder?) {
        super.onViewDetachedFromWindow(holder)
        compositeDisposable.dispose()
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        init {
            compositeDisposable.add(dimensionsProvider.dimensions().subscribe {
                view.setPadding(
                        view.paddingLeft,
                        it.statusBar,
                        view.paddingRight,
                        view.paddingBottom
                )
            })
        }

        fun bind(profileItem: ProfileItem) {
            view.run {
                ImageLoader.getInstance().displayImage(profileItem.avatarUrl, profileAvatar)
                profileNick.text = profileItem.nick
            }
        }
    }
}
