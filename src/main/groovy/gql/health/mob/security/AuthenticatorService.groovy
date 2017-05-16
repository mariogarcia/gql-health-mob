package gql.health.mob.security

import android.accounts.AbstractAccountAuthenticator
import android.app.Service
import android.content.Intent
import android.os.IBinder

class AuthenticatorService extends Service {
    @Override
    IBinder onBind(Intent intent) {
        AbstractAccountAuthenticator authenticator = new Authenticator(this)
        return authenticator.IBinder
    }
}