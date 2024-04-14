import android.content.Context
import android.content.SharedPreferences
import com.example.smartpark.util.Constants

class PreferencesManager private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)

    fun saveAccessToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString(Constants.ACCESS_TOKEN_KEY, token)
        editor.apply()
    }

    fun getAccessToken(): String {
        return sharedPreferences.getString(Constants.ACCESS_TOKEN_KEY, "") ?: ""
    }

    companion object {
        @Volatile
        private var INSTANCE: PreferencesManager? = null

        fun getInstance(context: Context): PreferencesManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: PreferencesManager(context).also { INSTANCE = it }
            }
        }
    }
}
