package gql.health.mob.security

import static android.accounts.AccountManager.KEY_BOOLEAN_RESULT

import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.util.Log
import groovy.transform.CompileStatic
import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.NetworkErrorException
import android.os.Bundle

@CompileStatic
class Authenticator extends AbstractAccountAuthenticator {

    private String TAG = "HelthyxAuthenticator";
    private final Context mContext;

    public Authenticator(Context context) {
        super(context);

        // I hate you! Google - set mContext as protected!
        this.mContext = context;
    }

    @Override
    Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        Log.d("healthyx", TAG + "> addAccount")

        final Intent intent = new Intent(mContext, AuthenticatorActivity.class)

        intent.with {
            putExtra(AuthenticatorActivity.ARG_ACCOUNT_TYPE, accountType)
            putExtra(AuthenticatorActivity.ARG_AUTH_TYPE, authTokenType)
            putExtra(AuthenticatorActivity.ARG_IS_ADDING_NEW_ACCOUNT, true)
            putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
        }

        final Bundle bundle = new Bundle()
        bundle.putParcelable(AccountManager.KEY_INTENT, intent)

        return bundle
    }

    @Override
    Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        if (!authTokenType == Constants.AUTHTOKEN_TYPE_READ_ONLY && !authTokenType == Constants.AUTHTOKEN_TYPE_FULL_ACCESS) {
            final Bundle result = new Bundle()
            result.putString(AccountManager.KEY_ERROR_MESSAGE, "invalid authTokenType")
            return result
        }

        final AccountManager am = AccountManager.get(mContext)

        String authToken = am.peekAuthToken(account, authTokenType)

        Log.d("healthyx", TAG + "> peekAuthToken returned - " + authToken)

        // Lets give another try to authenticate the user
        if (!authToken) {
            final String password = am.getPassword(account)
            if (password) {
                try {
                    Log.d("healthyx", TAG + "> re-authenticating with the existing password")
                    authToken = "TOKEN" // TODO get from remote service
                } catch (Exception e) {
                    e.printStackTrace()
                }
            }
        }

        if (authToken) {
            final Bundle result = new Bundle()
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name)
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type)
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken)
            return result
        }

        final Intent intent = new Intent(mContext, AuthenticatorActivity)

        intent.with {
            putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
            putExtra(AuthenticatorActivity.ARG_ACCOUNT_TYPE, account.type)
            putExtra(AuthenticatorActivity.ARG_AUTH_TYPE, authTokenType)
            putExtra(AuthenticatorActivity.ARG_ACCOUNT_NAME, account.name)
        }

        final Bundle bundle = new Bundle()
        bundle.putParcelable(AccountManager.KEY_INTENT, intent)

        return bundle
    }

    @Override
    String getAuthTokenLabel(String authTokenType) {
        switch(authTokenType) {
            case Constants.AUTHTOKEN_TYPE_FULL_ACCESS:
                return Constants.AUTHTOKEN_TYPE_FULL_ACCESS_LABEL

            case Constants.AUTHTOKEN_TYPE_READ_ONLY:
                return Constants.AUTHTOKEN_TYPE_READ_ONLY_LABEL

            default:
            return "$authTokenType (Label)"
        }
    }

    @Override
    Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        final Bundle result = new Bundle()
        result.putBoolean(KEY_BOOLEAN_RESULT, false)
        return result
    }

    @Override
    Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null
    }

    @Override
    Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null
    }

    @Override
    Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null
    }
}