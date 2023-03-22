package yunho.compose.presentation

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class YHGGApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}