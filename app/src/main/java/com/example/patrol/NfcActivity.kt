package com.example.patrol

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.os.Parcelable
import android.os.StrictMode
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.patrol.component.drawer.NavFrame

class NfcActivity : ComponentActivity() {
    private var nfcAdapter: NfcAdapter? = null
    private var pendingIntent: PendingIntent? = null

    //Intialize attributes
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i("intent from class", intent.getComponent()?.getClassName().toString())

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this@NfcActivity, this.javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            , FLAG_MUTABLE
        )

        setContent {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painterResource(id = R.drawable.outline_nfc_24),
                    contentDescription = "Scan NFC",
                    modifier = Modifier.size(192.dp).padding(bottom = 20.dp)
                )
                Text(text = "Scan NFC Tag", fontSize = 24.sp)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if(nfcAdapter != null) {
            nfcAdapter?.enableForegroundDispatch(this, pendingIntent, null, null)
        }
    }

    override fun onPause() {
        super.onPause()
        if(nfcAdapter != null) {
            nfcAdapter?.disableForegroundDispatch(this)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)

        var action = intent.action

        if(NfcAdapter.ACTION_TAG_DISCOVERED == action) {
            var tag = (intent.getParcelableExtra<Parcelable>(NfcAdapter.EXTRA_TAG) as Tag?)!!
            Log.i("NfcActivity", "Tag: " + tag.id.toString())
            val data = Intent().apply {
                putExtra("tagId", tag.id.toString())
                putExtra("type", "NFC")
            }
            setResult(RESULT_OK, data);
            finish();
        }
    }
}
