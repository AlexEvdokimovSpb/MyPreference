package gb.myhomework.mypreference.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import gb.myhomework.mypreference.Constants
import gb.myhomework.mypreference.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val TAG = "HW " + MainActivity::class.java.simpleName
    lateinit var ui: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityMainBinding.inflate(layoutInflater)
        setContentView(ui.root)

        ui.button.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
            if (Constants.DEBUG) {
                Log.v(TAG, "go for history")
            }
        }
    }
}