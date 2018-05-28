using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System;
using System.Text;

namespace OneStore
{
	[Serializable]
	public class Onestore_PurchaseResponse
	{
		public string productType;
		//inapp or auto,
		//productType은 현재  getPurchase, getProductDetails와 같은 명령어를 통해서 확인할수 있고 getPurchaseIntent를 통해서는
		//현재는 알수가 없다. 단순히 "" 값이다. 굳이 필요없을 수도 있으나 개발자 편의를 위해서 getPurchase명령에서만 활용할수 있는 필드이다.
		public string purchaseData;
		//original purchase json
		public string signature;
		//signature

		public string ToString ()
		{
			StringBuilder sb = new StringBuilder ();
			sb.Append ("type: " + productType + "\n");
			sb.Append ("PurchaseData: " + purchaseData + "\n");
			sb.Append ("Signature: " + signature + "\n");
			return sb.ToString ();
		}
	}

	[Serializable] 
	public class Signature
	{
		public string signature;

		public string ToString ()
		{
			StringBuilder sb = new StringBuilder ();
			sb.Append (signature);
			return sb.ToString ();
		}
	}

	[Serializable]
	public class ProductDetail
	{
		public String productId;
		public String type;
		public String price;
		public String title;

		public string ToString ()
		{
			StringBuilder sb = new StringBuilder ();
			sb.Append ("productId: " + productId + "\n");
			sb.Append ("type: " + type + "\n");
			sb.Append ("price: " + price + "\n");
			sb.Append ("title: " + title + "\n");
			return sb.ToString ();
		}
	}

	[Serializable]
	public class PurchaseData
	{
		public string orderId;
		public string packageName;
		public string productId;
		public long purchaseTime;
		public string purchaseId;
		public string developerPayload;
		public int purchaseState;
		public int recurringState;


		public string ToString ()
		{
			StringBuilder sb = new StringBuilder ("[Product]\n");
			sb.Append ("orderId: " + orderId + "\n");
			sb.Append ("packageName: " + packageName + "\n");
			sb.Append ("productId: " + productId + "\n");
			sb.Append ("purchaseTime: " + purchaseTime + "\n");
			sb.Append ("purchaseId: " + purchaseId + "\n");
			sb.Append ("developerPayload: " + developerPayload + "\n");
			sb.Append ("purchaseState: " + purchaseState + "\n");
			sb.Append ("recurringState: " + recurringState + "\n");

			return sb.ToString ();
		}
	}
}


