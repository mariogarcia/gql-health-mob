package gql.health.mob.security

import android.accounts.Account
import android.accounts.AccountAuthenticatorActivity
import android.accounts.AccountManager
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.arasthel.swissknife.annotations.OnBackground
import com.arasthel.swissknife.annotations.OnClick
import com.arasthel.swissknife.annotations.OnUIThread
import gql.health.mob.R
import groovy.transform.CompileStatic

@CompileStatic
class AuthenticatorActivity extends AccountAuthenticatorActivity {

    static final String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE"
    static final String ARG_AUTH_TYPE = "AUTH_TYPE"
    static final String ARG_ACCOUNT_NAME = "ACCOUNT_NAME"
    static final String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT"
    static final String ARG_FULL_TOKEN_TYPE = 'FULL_TOKEN_TYPE'
    static final String ARG_AUTH_TOKEN_TYPE = 'AUTH_TOKEN_TYPE'

    @InjectView(R.id.username)
    TextView username

    @InjectView(R.id.password)
    TextView password

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle)
        setContentView(R.layout.login)
        SwissKnife.inject(this)
    }

    @OnClick(R.id.save_credentials_button)
    void login() {
        saveCredentials()
    }

    @OnBackground
    void saveCredentials() {
        AccountManager accountManager = AccountManager.get(baseContext)
        Intent authIntent = resolveCredentials()

        if (authIntent.getStringExtra(AccountManager.KEY_ERROR_MESSAGE)) {
            showMessage(stringFrom(authIntent, AccountManager.KEY_ERROR_MESSAGE))
            return
        }

        String accountName   = stringFrom(authIntent, AccountManager.KEY_ACCOUNT_NAME)
        String password      = stringFrom(authIntent, AccountManager.KEY_PASSWORD)
        Account account      = new Account(accountName, stringFrom(authIntent, AccountManager.KEY_ACCOUNT_TYPE))
        String authToken     = stringFrom(authIntent, AccountManager.KEY_AUTHTOKEN)
        String authTokenType = stringFrom(authIntent, ARG_AUTH_TOKEN_TYPE)

        if (authIntent.getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {
            accountManager.addAccountExplicitly(account, password, null)
            accountManager.setAuthToken(account, authTokenType, authToken)
        } else {
            accountManager.setPassword(account, password)
        }

        accountAuthenticatorResult = authIntent.extras
        setResult(RESULT_OK, authIntent)
        finish()
    }

    Intent resolveCredentials() {
        String accountType = intent.getStringExtra(ARG_ACCOUNT_TYPE)
        Intent intent = new Intent()
        try {
            String usernameText = "${username.text}"
            String passwordText = "${password.text}"
            String token = findTokenByEmailAndPassword(usernameText, passwordText)

            intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, usernameText)
            intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType)
            intent.putExtra(AccountManager.KEY_AUTHTOKEN, token)
            intent.putExtra(AccountManager.KEY_PASSWORD, passwordText)
            intent.putExtra(ARG_IS_ADDING_NEW_ACCOUNT, true)
            intent.putExtra(ARG_AUTH_TOKEN_TYPE, 'FULL_TOKEN_TYPE')

        } catch (e) {
            intent.putExtra(
                    AccountManager.KEY_ERROR_MESSAGE,
                    "Login error"
            )
        }
        return intent
    }

    String findTokenByEmailAndPassword(usernameText, passwordText) {
        return "token"
    }

    @OnUIThread
    void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    String stringFrom(Intent intent, String key) {
        intent.getStringExtra(key)
    }
}