package dyachenko.kotlinbeginnerfilms.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dyachenko.kotlinbeginnerfilms.R
import dyachenko.kotlinbeginnerfilms.view.main.FilmsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, FilmsFragment.newInstance())
                .commitNow()
        }
    }
}