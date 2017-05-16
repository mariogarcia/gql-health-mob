package gql.health.mob.init

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.arasthel.swissknife.annotations.OnBackground
import gql.health.mob.R
import gql.health.mob.meal.MealListActivity
import gql.health.mob.security.Checker

class SplashScreenActivity extends Activity {

    static final long DELAY = 3000

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        showMeals()
    }

    @OnBackground
    void showMeals() {
        Checker.checkCredentials(this)
        Thread.sleep(DELAY)
        startActivity(new Intent(this, MealListActivity))
        finish()
    }
}
