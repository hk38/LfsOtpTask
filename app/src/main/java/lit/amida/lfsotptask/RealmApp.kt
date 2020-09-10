package lit.amida.lfsotptask

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class RealmApp: Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val realmConf = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()

        Realm.setDefaultConfiguration(realmConf)
    }
}