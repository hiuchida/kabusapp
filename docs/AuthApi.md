# AuthApi

All URIs are relative to *http://localhost:18080/kabusapi*

Method | HTTP request | Description
------------- | ------------- | -------------
[**tokenPost**](AuthApi.md#tokenPost) | **POST** /token | トークン発行

<a name="tokenPost"></a>
# **tokenPost**
> TokenSuccess tokenPost(body)

トークン発行

APIトークンを発行します。&lt;br&gt; 発行したトークンは有効である限り使用することができ、リクエストごとに発行する必要はありません。&lt;br&gt; 発行されたAPIトークンは以下のタイミングで無効となります。&lt;br&gt; ・kabuステーションを終了した時&lt;br&gt; ・kabuステーションからログアウトした時&lt;br&gt; ・別のトークンが新たに発行された時&lt;br&gt; ※kabuステーションは早朝、強制的にログアウトいたしますのでご留意ください。&lt;br&gt;

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.AuthApi;


AuthApi apiInstance = new AuthApi();
RequestToken body = new RequestToken(); // RequestToken | 
try {
    TokenSuccess result = apiInstance.tokenPost(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AuthApi#tokenPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**RequestToken**](RequestToken.md)|  |

### Return type

[**TokenSuccess**](TokenSuccess.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

