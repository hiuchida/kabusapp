# OrderApi

All URIs are relative to *http://localhost:18080/kabusapi*

Method | HTTP request | Description
------------- | ------------- | -------------
[**cancelorderPut**](OrderApi.md#cancelorderPut) | **PUT** /cancelorder | 注文取消
[**sendoderFuturePost**](OrderApi.md#sendoderFuturePost) | **POST** /sendorder/future | 注文発注（先物）
[**sendorderOptionPost**](OrderApi.md#sendorderOptionPost) | **POST** /sendorder/option | 注文発注（オプション）
[**sendorderPost**](OrderApi.md#sendorderPost) | **POST** /sendorder | 注文発注（現物・信用）

<a name="cancelorderPut"></a>
# **cancelorderPut**
> OrderSuccess cancelorderPut(body, X_API_KEY)

注文取消

注文を取消します

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.OrderApi;


OrderApi apiInstance = new OrderApi();
RequestCancelOrder body = new RequestCancelOrder(); // RequestCancelOrder | 
String X_API_KEY = "X_API_KEY_example"; // String | トークン発行メソッドで取得した文字列
try {
    OrderSuccess result = apiInstance.cancelorderPut(body, X_API_KEY);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OrderApi#cancelorderPut");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**RequestCancelOrder**](RequestCancelOrder.md)|  |
 **X_API_KEY** | **String**| トークン発行メソッドで取得した文字列 |

### Return type

[**OrderSuccess**](OrderSuccess.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="sendoderFuturePost"></a>
# **sendoderFuturePost**
> OrderSuccess sendoderFuturePost(body, X_API_KEY)

注文発注（先物）

先物銘柄の注文を発注します。&lt;br&gt; 同一の銘柄に対しての注文は同時に5件ほどを上限としてご利用ください。

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.OrderApi;


OrderApi apiInstance = new OrderApi();
RequestSendOrderDerivFuture body = new RequestSendOrderDerivFuture(); // RequestSendOrderDerivFuture | 
String X_API_KEY = "X_API_KEY_example"; // String | トークン発行メソッドで取得した文字列
try {
    OrderSuccess result = apiInstance.sendoderFuturePost(body, X_API_KEY);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OrderApi#sendoderFuturePost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**RequestSendOrderDerivFuture**](RequestSendOrderDerivFuture.md)|  |
 **X_API_KEY** | **String**| トークン発行メソッドで取得した文字列 |

### Return type

[**OrderSuccess**](OrderSuccess.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="sendorderOptionPost"></a>
# **sendorderOptionPost**
> OrderSuccess sendorderOptionPost(body, X_API_KEY)

注文発注（オプション）

オプション銘柄の注文を発注します。&lt;br&gt; 同一の銘柄に対しての注文は同時に5件ほどを上限としてご利用ください。

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.OrderApi;


OrderApi apiInstance = new OrderApi();
RequestSendOrderDerivOption body = new RequestSendOrderDerivOption(); // RequestSendOrderDerivOption | 
String X_API_KEY = "X_API_KEY_example"; // String | トークン発行メソッドで取得した文字列
try {
    OrderSuccess result = apiInstance.sendorderOptionPost(body, X_API_KEY);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OrderApi#sendorderOptionPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**RequestSendOrderDerivOption**](RequestSendOrderDerivOption.md)|  |
 **X_API_KEY** | **String**| トークン発行メソッドで取得した文字列 |

### Return type

[**OrderSuccess**](OrderSuccess.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="sendorderPost"></a>
# **sendorderPost**
> OrderSuccess sendorderPost(body, X_API_KEY)

注文発注（現物・信用）

注文を発注します。&lt;br&gt; 同一の銘柄に対しての注文は同時に5件ほどを上限としてご利用ください。

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.OrderApi;


OrderApi apiInstance = new OrderApi();
RequestSendOrder body = new RequestSendOrder(); // RequestSendOrder | 
String X_API_KEY = "X_API_KEY_example"; // String | トークン発行メソッドで取得した文字列
try {
    OrderSuccess result = apiInstance.sendorderPost(body, X_API_KEY);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OrderApi#sendorderPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**RequestSendOrder**](RequestSendOrder.md)|  |
 **X_API_KEY** | **String**| トークン発行メソッドで取得した文字列 |

### Return type

[**OrderSuccess**](OrderSuccess.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

