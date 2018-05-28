using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using OneStore;

public class Onestore_IapUi : MonoBehaviour
{
	private string inapp_pId1 = "p5000";
	//inapp default Test item
	private string inapp_pId2 = "p10000";
	private string inapp_pId3 = "p50000";
	private string auto_pId1 = "a100000";
	//auto default Test item

	private string inapp_type = "inapp";
	private string auto_type = "auto";

	private string devPayload = "this is test payload!";
	private string base64EncodedPublicKey = "input your public key";

	void Start ()
	{

	}

	void Update ()
	{
		if (Input.GetKeyDown (KeyCode.Escape)) {
			Application.Quit ();
		}
	}

	public void connectService ()
	{
		Debug.Log ("IapUi connectService");
		base64EncodedPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC195Gq6htuAeoJT/yy1fZvHXOAGGYYqfnoGUCyjtoscoMHBEfobaTSL5QjSKu0ghXbqNWGJMNOsah9l71xM4LbCUdMv8fi4/SvzVBCd3SifS4euf/gJJSwGyVHmrknLSRu9sexBr/LCbpQ+USqQGgErSt/gPPg/GZ65FEhD2Zj1QIDAQAB";
		Onestore_IapCallManager.connectService (base64EncodedPublicKey);
	}

	public void isBillingSupported ()
	{
		Onestore_IapCallManager.isBillingSupported ();
	}

	public void getPurchases ()
	{
		Onestore_IapCallManager.getPurchases ();
	}

	public void getProductDetails ()
	{
		var inapp_products = new string[] {	inapp_pId1, inapp_pId2, inapp_pId3 };
		var auto_products = new string[] { auto_pId1 };

		Onestore_IapCallManager.getProductDetails (inapp_products, inapp_type);
		Onestore_IapCallManager.getProductDetails (auto_products, auto_type);
	}

	public void buyProductInapp ()
	{
		Onestore_IapCallManager.buyProduct (inapp_pId1, inapp_type, devPayload);
	}

	public void consume ()
	{
		string inapp_json = PlayerPrefs.GetString (inapp_pId1);
		if (inapp_json.Length > 0) {
			Onestore_IapCallManager.consume (inapp_json);
		} else {
			AndroidNative.showMessage ("error", "no data to consume", "ok");
		}
	}

	public void buyProductAuto ()
	{
		Onestore_IapCallManager.buyProduct (auto_pId1, auto_type, devPayload);
	}

	public void cancelAutoItem ()
	{
		string auto_json = PlayerPrefs.GetString (auto_pId1);
		PurchaseData response = JsonUtility.FromJson<PurchaseData> (auto_json);

		if (auto_json.Length > 0) {
			string command = "";
			if (response.recurringState == 0) {
				command = "cancel"; 
			} else if (response.recurringState == 1) {
				command = "reactivate";
			}
			Onestore_IapCallManager.manageRecurringAuto (auto_json, command);
		} else {
			AndroidNative.showMessage ("Warning!!", "no data for manageRecurringAuto", "ok");
		}
	}

	public void login ()
	{
		Onestore_IapCallManager.login ();
	}

	public void destroy ()
	{
		Onestore_IapCallManager.destroy ();
	}
}
