using UnityEngine;
using System.Collections;

public class AndroidNative
{
    public static void CallStatic(string methodName, params object[] args)
    {
        #if UNITY_ANDROID && !UNITY_EDITOR
        try
        {
		    string CLASS_NAME = "com.onestore.iap.sdk.unity.PopupManager";
            AndroidJavaObject bridge = new AndroidJavaObject(CLASS_NAME);

            AndroidJavaClass jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer"); 
            AndroidJavaObject act = jc.GetStatic<AndroidJavaObject>("currentActivity"); 
            
            act.Call("runOnUiThread", new AndroidJavaRunnable(() =>
            {
                bridge.CallStatic(methodName, args);
            }));

        } catch (System.Exception ex)
        {
            Debug.LogWarning(ex.Message);
        }
        #endif
    }
		

    public static void showMessage(string title, string message, string ok)
    {
        CallStatic("ShowMessagePopup", title, message, ok);
    }

  
}