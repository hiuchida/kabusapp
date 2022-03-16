# RegisterApi

All URIs are relative to *http://localhost:18080/kabusapi*

Method | HTTP request | Description
------------- | ------------- | -------------
[**registerPut**](RegisterApi.md#registerPut) | **PUT** /register | 銘柄登録
[**unregisterAllPut**](RegisterApi.md#unregisterAllPut) | **PUT** /unregister/all | 銘柄登録全解除
[**unregisterPut**](RegisterApi.md#unregisterPut) | **PUT** /unregister | 銘柄登録解除

<a name="registerPut"></a>
# **registerPut**
> RegistSuccess registerPut(body, X_API_KEY)

銘柄登録

PUSH配信する銘柄を登録します。&lt;br&gt; API登録銘柄リストを開くには、kabuステーションAPIインジケーターを右クリックし「API登録銘柄リスト」を選択してください。

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.RegisterApi;


RegisterApi apiInstance = new RegisterApi();
RequestRegister body = new RequestRegister(); // RequestRegister | 登録する銘柄のリスト
String X_API_KEY = "X_API_KEY_example"; // String | トークン発行メソッドで取得した文字列
try {
    RegistSuccess result = apiInstance.registerPut(body, X_API_KEY);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling RegisterApi#registerPut");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**RequestRegister**](RequestRegister.md)| 登録する銘柄のリスト |
 **X_API_KEY** | **String**| トークン発行メソッドで取得した文字列 |

### Return type

[**RegistSuccess**](RegistSuccess.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="unregisterAllPut"></a>
# **unregisterAllPut**
> UnregisterAllSuccess unregisterAllPut(X_API_KEY)

銘柄登録全解除

API登録銘柄リストに登録されている銘柄をすべて解除します

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.RegisterApi;


RegisterApi apiInstance = new RegisterApi();
String X_API_KEY = "X_API_KEY_example"; // String | トークン発行メソッドで取得した文字列
try {
    UnregisterAllSuccess result = apiInstance.unregisterAllPut(X_API_KEY);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling RegisterApi#unregisterAllPut");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **X_API_KEY** | **String**| トークン発行メソッドで取得した文字列 |

### Return type

[**UnregisterAllSuccess**](UnregisterAllSuccess.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="unregisterPut"></a>
# **unregisterPut**
> RegistSuccess unregisterPut(body, X_API_KEY)

銘柄登録解除

API登録銘柄リストに登録されている銘柄を解除します

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.RegisterApi;


RegisterApi apiInstance = new RegisterApi();
RequestUnregister body = new RequestUnregister(); // RequestUnregister | 登録解除する銘柄のリスト
String X_API_KEY = "X_API_KEY_example"; // String | トークン発行メソッドで取得した文字列
try {
    RegistSuccess result = apiInstance.unregisterPut(body, X_API_KEY);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling RegisterApi#unregisterPut");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**RequestUnregister**](RequestUnregister.md)| 登録解除する銘柄のリスト |
 **X_API_KEY** | **String**| トークン発行メソッドで取得した文字列 |

### Return type

[**RegistSuccess**](RegistSuccess.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

