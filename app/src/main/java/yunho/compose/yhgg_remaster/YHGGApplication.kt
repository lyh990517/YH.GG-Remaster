package yunho.compose.yhgg_remaster

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class YHGGApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}