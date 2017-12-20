package ru.radiationx.anilibria.ui.fragments.article

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.radiationx.anilibria.data.api.models.ArticleItem
import ru.radiationx.anilibria.data.repository.ArticlesRepository
import ru.radiationx.anilibria.utils.mvp.BasePresenter
import ru.terrakok.cicerone.Router

/**
 * Created by radiationx on 20.12.17.
 */
@InjectViewState
class ArticlePresenter(private val articlesRepository: ArticlesRepository,
                       private val router: Router) : BasePresenter<ArticleView>(router) {

    fun setDataFromItem(item: ArticleItem) {
        item.run {
            viewState.preShow(title, userNick, commentsCount, viewsCount)
        }
    }

    fun loadArticle(articleId: String) {
        Log.e("SUKA", "loadArticle")
        viewState.setRefreshing(true)
        val disposable = articlesRepository.getArticle(articleId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ article ->
                    viewState.setRefreshing(false)
                    viewState.showArticle(article)
                }) { throwable ->
                    viewState.setRefreshing(false)
                    Log.d("SUKA", "SAS")
                    throwable.printStackTrace()
                }
        addDisposable(disposable)
    }
}