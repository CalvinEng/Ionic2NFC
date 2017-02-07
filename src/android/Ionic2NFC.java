package com.dnrps.ionic2nfc;

import com.nxp.nfclib.CardType;
import com.nxp.nfclib.NxpNfcLib;
import com.nxp.nfclib.ndef.INdefMessage;
import com.nxp.nfclib.ntag.INTag213215216;
import com.nxp.nfclib.ntag.NTagFactory;
import com.nxp.nfclib.ultralight.Ultralight;
import com.nxp.nfclib.ultralight.UltralightFactory;

import android.widget.Toast;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class Ionic2NFC extends CordovaPlugin {
    public static final String TAG = MainActivity.class.getSimpleName();

    private static String m_StrPackageKey = "417926a7a8219dd849faed9a46a133f5";

    private CardType m_cardType = null;
    private Ultralight m_objUltraLight = null;
    private INTag213215216 m_objNTag216 = null;

    private NxpNfcLib m_libInstance = null;
    
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        System.out.println("Start.");
        if (action.equals("coolMethod")) {
            String message = args.getString(0);
            this.coolMethod(message, callbackContext);
            return true;
        }
        else if (action.equals("startNfc")) {
            this.intializeLibrary(callbackContext);
            return true;
        }
        
        return false;
    }

    private void coolMethod(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
    
    private void intializeLibrary() {
        m_libInstance = NxpNfcLib.getInstance();
        m_libInstance.registerActivity(this, m_StrPackageKey);
        
        callbackContext.success();
    }

    @Override
    public void onNewIntent(final Intent intent)
    {
        super.onNewIntent(intent);

        m_cardType = m_libInstance.getCardType(intent);

        Log.d(TAG, m_cardType.toString());

        switch(m_cardType)
        {
            case Ultralight:
                m_objUltraLight = UltralightFactory.getInstance().getUltralight(m_libInstance.getCustomModules());

                try
                {
                    m_objUltraLight.getReader().connect();

                    INdefMessage message = m_objUltraLight.readNDEF();

                    Toast toast = Toast.makeText(getApplicationContext(), readINDefMessage(message), Toast.LENGTH_SHORT);
                    toast.show();

                    m_objUltraLight.getReader().close();
                }
                catch (Throwable t)
                {
                    t.printStackTrace();
                }

                break;
            case NTag216:
                m_objNTag216 = NTagFactory.getInstance().getNTAG216(m_libInstance.getCustomModules());

                try
                {
                    m_objNTag216.getReader().connect();

                    INdefMessage message = m_objNTag216.readNDEF();

                    Toast toast = Toast.makeText(getApplicationContext(), readINDefMessage(message), Toast.LENGTH_SHORT);
                    toast.show();

                    m_objNTag216.getReader().close();
                }
                catch (Throwable t)
                {
                    t.printStackTrace();
                }

                break;
            default:
                Toast toast = Toast.makeText(getApplicationContext(), "Unknown", Toast.LENGTH_SHORT);
                toast.show();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        m_libInstance.startForeGroundDispatch();

    }

    @Override
    protected void onPause() {
        super.onPause();
        m_libInstance.stopForeGroundDispatch();
    }
}
