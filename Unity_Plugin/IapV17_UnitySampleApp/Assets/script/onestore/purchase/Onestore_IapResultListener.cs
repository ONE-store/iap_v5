using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace OneStore
{
	public class Onestore_IapResultListener : MonoBehaviour
	{
		void Awake ()
		{
			Onestore_IapCallbackManager.serviceConnectionEvent += serviceConnectionResult;
			Onestore_IapCallbackManager.isBillingSupportedEvent += isBillingSupportedResult;

			Onestore_IapCallbackManager.getPurchaseSuccessEvent += getPurchaseSuccessResult;
			Onestore_IapCallbackManager.getPurchaseErrorEvent += getPurchaseErrorResult;

			Onestore_IapCallbackManager.queryProductsSuccessEvent += queryProductsSuccessResult;
			Onestore_IapCallbackManager.queryProductsErrorEvent += queryProductsErrorResult;

			Onestore_IapCallbackManager.getPurchaseIntentSuccessEvent += getPurchaseIntentSuccessResult;
			Onestore_IapCallbackManager.getPurchaseIntentErrorEvent += getPurchaseIntentErrorResult;

			Onestore_IapCallbackManager.consumeSuccessEvent += consumeSuccessResult;
			Onestore_IapCallbackManager.consumeErrorEvent += consumeErrorResult;

			Onestore_IapCallbackManager.manageRecurringSuccessEvent += manageRecurringSuccessResult;
			Onestore_IapCallbackManager.manageRecurringErrorEvent += manageRecurringErrorResult;

			Onestore_IapCallbackManager.getLoginIntentEvent += getLoginIntentEvent;
		}

		void OnDestroy ()
		{
			Onestore_IapCallbackManager.serviceConnectionEvent -= serviceConnectionResult;
			Onestore_IapCallbackManager.isBillingSupportedEvent -= isBillingSupportedResult;

			Onestore_IapCallbackManager.getPurchaseSuccessEvent -= getPurchaseSuccessResult;
			Onestore_IapCallbackManager.getPurchaseErrorEvent -= getPurchaseErrorResult;

			Onestore_IapCallbackManager.queryProductsSuccessEvent -= queryProductsSuccessResult;
			Onestore_IapCallbackManager.queryProductsErrorEvent -= queryProductsErrorResult;

			Onestore_IapCallbackManager.getPurchaseIntentSuccessEvent -= getPurchaseIntentSuccessResult;
			Onestore_IapCallbackManager.getPurchaseIntentErrorEvent -= getPurchaseIntentErrorResult;

			Onestore_IapCallbackManager.consumeSuccessEvent -= consumeSuccessResult;
			Onestore_IapCallbackManager.consumeErrorEvent -= consumeErrorResult;

			Onestore_IapCallbackManager.manageRecurringSuccessEvent -= manageRecurringSuccessResult;
			Onestore_IapCallbackManager.manageRecurringErrorEvent -= manageRecurringErrorResult;

			Onestore_IapCallbackManager.getLoginIntentEvent -= getLoginIntentEvent;
		}

		void serviceConnectionResult (string result)
		{
			AndroidNative.showMessage ("serivce connect", result, "ok");
		}

		void isBillingSupportedResult (string result)
		{
			AndroidNative.showMessage ("isBillingSupported", result, "ok");
		}

		//현재 signature는 IapCallbackFromAndroid에서 받아오고 있으나 여기로 넘겨주지는 않고 있다. 일단  SDK 내부에서
		//구매 데이터와 시그너처를 통한 검증을 디폴트로 수행하고 있기 때문이다. 만약 시그너처가 필요하면 IapCallbackManager에서 가져다 쓸수 있다.
		//PlayerPref값을 셋팅하는것은 간단히 테스트 앱을 위한 것이지 실제로 아이템에 대한 관리나 저장은 개발사에서 해야 한다.
		void getPurchaseSuccessResult (PurchaseData result)
		{
			AndroidNative.showMessage ("getPurchase success", result.ToString (), "ok");
			PlayerPrefs.SetString (result.productId, JsonUtility.ToJson (result));
		}

		void getPurchaseErrorResult (string result)
		{
			if (result.Contains ("inapp")) {
				PlayerPrefs.SetString ("p5000", "");
			} else {
				PlayerPrefs.SetString ("a100000", "");
			}
			AndroidNative.showMessage ("getPurchase error", result, "ok");
		}

		void queryProductsSuccessResult (ProductDetail result)
		{
			AndroidNative.showMessage ("queryProducts success", result.ToString (), "ok");
		}

		void queryProductsErrorResult (string result)
		{
			AndroidNative.showMessage ("queryProducts error", result, "ok");
		}

		void getPurchaseIntentSuccessResult (PurchaseData result)
		{
			AndroidNative.showMessage ("getPurchaseIntent sucess", result.ToString (), "ok");
			PlayerPrefs.SetString (result.productId, JsonUtility.ToJson (result));
		}

		void getPurchaseIntentErrorResult (string result)
		{
			AndroidNative.showMessage ("getPurchaseIntent error", result, "ok");
		}

		void consumeSuccessResult (PurchaseData result)
		{
			AndroidNative.showMessage ("consume sucess", result.ToString (), "ok");
			PlayerPrefs.SetString (result.productId, "");
		}

		void consumeErrorResult (string result)
		{
			AndroidNative.showMessage ("consume error", result.ToString (), "ok");
		}

		void manageRecurringSuccessResult (PurchaseData result)
		{
			AndroidNative.showMessage ("ManageRecurring sucess", result.ToString (), "ok");
			PlayerPrefs.SetString (result.productId, JsonUtility.ToJson (result));
		}


		void manageRecurringErrorResult (string result)
		{
			AndroidNative.showMessage ("ManageRecurring error", result.ToString (), "ok");
		}

		void getLoginIntentEvent (string result)
		{
			AndroidNative.showMessage ("getLoginIntent ", result.ToString (), "ok");
		}
	}
}