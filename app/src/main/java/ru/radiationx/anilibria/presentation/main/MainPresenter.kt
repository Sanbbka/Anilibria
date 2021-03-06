package ru.radiationx.anilibria.presentation.main

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import ru.radiationx.anilibria.Screens
import ru.radiationx.anilibria.entity.common.AuthState
import ru.radiationx.anilibria.model.repository.AuthRepository
import ru.radiationx.anilibria.utils.mvp.BasePresenter
import ru.terrakok.cicerone.Router

/**
 * Created by radiationx on 17.12.17.
 */
@InjectViewState
class MainPresenter(
        private val router: Router,
        private val authRepository: AuthRepository
) : BasePresenter<MainView>(router) {

    var defaultScreen = Screens.MAIN_RELEASES

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        Log.e("S_DEF_LOG", "main onFirstViewAttach " + authRepository.getAuthState().toString())
        if (authRepository.getAuthState() == AuthState.NO_AUTH) {
            router.navigateTo(Screens.AUTH)
        }

        selectTab(defaultScreen)
        authRepository
                .observeUser()
                .subscribe {
                    viewState.updateTabs()
                }
    }

    fun getAuthState() = authRepository.getAuthState()

    fun selectTab(screenKey: String) {
        Log.e("S_DEF_LOG", "presenter selectTab " + screenKey)
        viewState.highlightTab(screenKey)
        router.replaceScreen(screenKey)
    }

}
