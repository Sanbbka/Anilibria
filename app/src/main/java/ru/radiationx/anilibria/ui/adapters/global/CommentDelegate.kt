package ru.radiationx.anilibria.ui.adapters.global

import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.item_comment.view.*
import ru.radiationx.anilibria.R
import ru.radiationx.anilibria.entity.app.release.Comment
import ru.radiationx.anilibria.ui.adapters.CommentListItem
import ru.radiationx.anilibria.ui.adapters.ListItem
import ru.radiationx.anilibria.utils.bbparser.BbParser

/**
 * Created by radiationx on 18.01.18.
 */
class CommentDelegate : AdapterDelegate<MutableList<ListItem>>() {
    override fun isForViewType(items: MutableList<ListItem>, position: Int): Boolean = items[position] is CommentListItem

    override fun onBindViewHolder(items: MutableList<ListItem>, position: Int, holder: RecyclerView.ViewHolder, payloads: MutableList<Any>) {
        val item = items[position] as CommentListItem
        (holder as ViewHolder).bind(item.item)
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
    )

    val parser = BbParser()

    private inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: Comment) {
            view.run {
                item_nick.text = Html.fromHtml(item.authorNick)
                item_date.text = item.date
                //println("BindSRC: '${item.message}'")
                item.message?.let {
                    item_content.setContent(parser.parse(it).toSequence())
                }
            }
        }
    }


}