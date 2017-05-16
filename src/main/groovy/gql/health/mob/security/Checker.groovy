package gql.health.mob.security

import android.accounts.Account
import android.accounts.AccountManager
import android.app.Activity
import gql.health.mob.R
import groovy.transform.CompileStatic

@CompileStatic
class Checker {
    static String checkCredentials(Activity activity) {
        AccountManager accountManager = AccountManager.get(activity)

        String accountType = activity.getString(R.string.account_type)
        String tokenType = AuthenticatorActivity.ARG_FULL_TOKEN_TYPE
        Account[] accounts = accountManager.getAccountsByType(accountType)
        Account theAccount = accounts ? accounts.first() : null

        if (!theAccount) {
            accountManager.addAccount(accountType, tokenType, null, null, activity, null, null).result
            accounts = accountManager.getAccountsByType(accountType)
            theAccount = accounts ? accounts.first() : null
        }

        String token = accountManager
                .getAuthToken(theAccount, tokenType, null, activity, null, null)
                .result
                .getString(AccountManager.KEY_AUTHTOKEN)

        return token
    }
}