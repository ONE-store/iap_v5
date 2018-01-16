# 원스토어 인앱결제란?

ONE 스토어 인앱결제(이하 IAP)는 ONE스토어 이용자들이 앱 내에서 구입할 수 있는 별도 인앱상품을 구매하기 위한 인증 및 결제 시스템이다. 개발사 앱에 IAP 모듈을 적용하면 인앱상품 이용 권한 및 결제 요청 시 ONEstore service 앱에서 해당 상품을 확인하여 결제를 수행한다. 또한 ONE스토어 앱를 통해 인앱상품을 관리하고 결제 내역을 확인할 수 있다.

다음은 원스토어 인앱결제의 서비스 구조를 나타낸 것이다.

![enter image description here]
(https://i.imgur.com/eNCe6ZY.png)

IAP 모듈은 IAP SDK(In-App Purchase Software Development Kit)라는 java 개발 라이브러리 형태로 제공된다. 개발사 앱에 SDK를 적용한 후 인앱결제 관련 함수를 호출하면 IAP서버로 요청이 전달된다. IAP 서버는 구매 요청에 대한 결과를 JSON 형태의 응답 데이터를 생성하여 개발자 앱으로 전송한다. 


# 권장 개발 환경

안드로이드용 애플리케이션에 IAP SDK를 적용하기 위해 다음과 같은 개발 환경이 필요합니다.

* Android 4.0 이상 버전(API 버전 14 이상)
* Java SDK 1.6 버전
* Android studio 2.0 이상 버전

Eclipse 에서는 아래 개발환경을 권장합니다.

* Eclipse IDE for Java Developers
* ADT Plug-in for Eclipse 15 버전 이상

# Terminology

* 원스토어 서비스 : ONE store service(OSS)
* IAP : 인앱결제, In-App Purchase
* AIDL : Android Interface Definition Language
