# WalletApi

All URIs are relative to *http://localhost:18080/kabusapi*

Method | HTTP request | Description
------------- | ------------- | -------------
[**walletCashGet**](WalletApi.md#walletCashGet) | **GET** /wallet/cash | 取引余力（現物）
[**walletCashSymbolGet**](WalletApi.md#walletCashSymbolGet) | **GET** /wallet/cash/{symbol} | 取引余力（現物）（銘柄指定）
[**walletFutureGet**](WalletApi.md#walletFutureGet) | **GET** /wallet/future | 取引余力（先物）
[**walletFutureSymbolGet**](WalletApi.md#walletFutureSymbolGet) | **GET** /wallet/future/{symbol} | 取引余力（先物）（銘柄指定）
[**walletMarginGet**](WalletApi.md#walletMarginGet) | **GET** /wallet/margin | 取引余力（信用）
[**walletMarginSymbolGet**](WalletApi.md#walletMarginSymbolGet) | **GET** /wallet/margin/{symbol} | 取引余力（信用）（銘柄指定）
[**walletOptionGet**](WalletApi.md#walletOptionGet) | **GET** /wallet/option | 取引余力（オプション）
[**walletOptionSymbolGet**](WalletApi.md#walletOptionSymbolGet) | **GET** /wallet/option/{symbol} | 取引余力（オプション）（銘柄指定）

<a name="walletCashGet"></a>
# **walletCashGet**
> WalletCashSuccess walletCashGet(X_API_KEY)

取引余力（現物）

口座の取引余力（現物）を取得します

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.WalletApi;


WalletApi apiInstance = new WalletApi();
String X_API_KEY = "X_API_KEY_example"; // String | トークン発行メソッドで取得した文字列
try {
    WalletCashSuccess result = apiInstance.walletCashGet(X_API_KEY);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling WalletApi#walletCashGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **X_API_KEY** | **String**| トークン発行メソッドで取得した文字列 |

### Return type

[**WalletCashSuccess**](WalletCashSuccess.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="walletCashSymbolGet"></a>
# **walletCashSymbolGet**
> WalletCashSuccess walletCashSymbolGet(X_API_KEY, symbol)

取引余力（現物）（銘柄指定）

指定した銘柄の取引余力（現物）を取得します

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.WalletApi;


WalletApi apiInstance = new WalletApi();
String X_API_KEY = "X_API_KEY_example"; // String | トークン発行メソッドで取得した文字列
String symbol = "symbol_example"; // String | 銘柄コード <br> ※次の形式で入力してください。<br> [銘柄コード]@[市場コード]<br> ※市場コードは下記の定義値から選択してください。 <b>市場コード</b> <table>   <thead>     <tr>       <th>定義値</th>       <th>説明</th>     </tr>   </thead>   <tbody>     <tr>       <td>1</td>       <td>東証</td>     </tr>     <tr>       <td>3</td>       <td>名証</td>     </tr>     <tr>           <td>5</td>           <td>福証</td>       </tr>       <tr>           <td>6</td>           <td>札証</td>       </tr>   </tbody> </table>
try {
    WalletCashSuccess result = apiInstance.walletCashSymbolGet(X_API_KEY, symbol);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling WalletApi#walletCashSymbolGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **X_API_KEY** | **String**| トークン発行メソッドで取得した文字列 |
 **symbol** | **String**| 銘柄コード &lt;br&gt; ※次の形式で入力してください。&lt;br&gt; [銘柄コード]@[市場コード]&lt;br&gt; ※市場コードは下記の定義値から選択してください。 &lt;b&gt;市場コード&lt;/b&gt; &lt;table&gt;   &lt;thead&gt;     &lt;tr&gt;       &lt;th&gt;定義値&lt;/th&gt;       &lt;th&gt;説明&lt;/th&gt;     &lt;/tr&gt;   &lt;/thead&gt;   &lt;tbody&gt;     &lt;tr&gt;       &lt;td&gt;1&lt;/td&gt;       &lt;td&gt;東証&lt;/td&gt;     &lt;/tr&gt;     &lt;tr&gt;       &lt;td&gt;3&lt;/td&gt;       &lt;td&gt;名証&lt;/td&gt;     &lt;/tr&gt;     &lt;tr&gt;           &lt;td&gt;5&lt;/td&gt;           &lt;td&gt;福証&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;6&lt;/td&gt;           &lt;td&gt;札証&lt;/td&gt;       &lt;/tr&gt;   &lt;/tbody&gt; &lt;/table&gt; |

### Return type

[**WalletCashSuccess**](WalletCashSuccess.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="walletFutureGet"></a>
# **walletFutureGet**
> WalletFutureSuccess walletFutureGet(X_API_KEY)

取引余力（先物）

口座の取引余力（先物）を取得します

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.WalletApi;


WalletApi apiInstance = new WalletApi();
String X_API_KEY = "X_API_KEY_example"; // String | トークン発行メソッドで取得した文字列
try {
    WalletFutureSuccess result = apiInstance.walletFutureGet(X_API_KEY);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling WalletApi#walletFutureGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **X_API_KEY** | **String**| トークン発行メソッドで取得した文字列 |

### Return type

[**WalletFutureSuccess**](WalletFutureSuccess.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="walletFutureSymbolGet"></a>
# **walletFutureSymbolGet**
> WalletFutureSuccess walletFutureSymbolGet(X_API_KEY, symbol)

取引余力（先物）（銘柄指定）

指定した銘柄の取引余力（先物）を取得します

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.WalletApi;


WalletApi apiInstance = new WalletApi();
String X_API_KEY = "X_API_KEY_example"; // String | トークン発行メソッドで取得した文字列
String symbol = "symbol_example"; // String | 銘柄コード <br> ※次の形式で入力してください。<br> [銘柄コード]@[市場コード]<br> ※市場コードは下記の定義値から選択してください。 <b>市場コード</b> <table>   <thead>       <tr>           <th>定義値</th>           <th>説明</th>       </tr>   </thead>   <tbody>       <tr>           <td>2</td>           <td>日通し</td>       </tr>       <tr>           <td>23</td>           <td>日中</td>       </tr>       <tr>           <td>24</td>           <td>夜間</td>       </tr>   </tbody> </table>
try {
    WalletFutureSuccess result = apiInstance.walletFutureSymbolGet(X_API_KEY, symbol);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling WalletApi#walletFutureSymbolGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **X_API_KEY** | **String**| トークン発行メソッドで取得した文字列 |
 **symbol** | **String**| 銘柄コード &lt;br&gt; ※次の形式で入力してください。&lt;br&gt; [銘柄コード]@[市場コード]&lt;br&gt; ※市場コードは下記の定義値から選択してください。 &lt;b&gt;市場コード&lt;/b&gt; &lt;table&gt;   &lt;thead&gt;       &lt;tr&gt;           &lt;th&gt;定義値&lt;/th&gt;           &lt;th&gt;説明&lt;/th&gt;       &lt;/tr&gt;   &lt;/thead&gt;   &lt;tbody&gt;       &lt;tr&gt;           &lt;td&gt;2&lt;/td&gt;           &lt;td&gt;日通し&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;23&lt;/td&gt;           &lt;td&gt;日中&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;24&lt;/td&gt;           &lt;td&gt;夜間&lt;/td&gt;       &lt;/tr&gt;   &lt;/tbody&gt; &lt;/table&gt; |

### Return type

[**WalletFutureSuccess**](WalletFutureSuccess.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="walletMarginGet"></a>
# **walletMarginGet**
> WalletMarginSuccess walletMarginGet(X_API_KEY)

取引余力（信用）

口座の取引余力（信用）を取得します

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.WalletApi;


WalletApi apiInstance = new WalletApi();
String X_API_KEY = "X_API_KEY_example"; // String | トークン発行メソッドで取得した文字列
try {
    WalletMarginSuccess result = apiInstance.walletMarginGet(X_API_KEY);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling WalletApi#walletMarginGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **X_API_KEY** | **String**| トークン発行メソッドで取得した文字列 |

### Return type

[**WalletMarginSuccess**](WalletMarginSuccess.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="walletMarginSymbolGet"></a>
# **walletMarginSymbolGet**
> WalletMarginSuccess walletMarginSymbolGet(X_API_KEY, symbol)

取引余力（信用）（銘柄指定）

指定した銘柄の取引余力（信用）を取得します

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.WalletApi;


WalletApi apiInstance = new WalletApi();
String X_API_KEY = "X_API_KEY_example"; // String | トークン発行メソッドで取得した文字列
String symbol = "symbol_example"; // String | 銘柄コード <br> ※次の形式で入力してください。<br> [銘柄コード]@[市場コード]<br> ※市場コードは下記の定義値から選択してください。 <b>市場コード</b> <table>   <thead>     <tr>       <th>定義値</th>       <th>説明</th>     </tr>   </thead>   <tbody>     <tr>       <td>1</td>       <td>東証</td>     </tr>     <tr>       <td>3</td>       <td>名証</td>     </tr>   </tbody> </table>
try {
    WalletMarginSuccess result = apiInstance.walletMarginSymbolGet(X_API_KEY, symbol);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling WalletApi#walletMarginSymbolGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **X_API_KEY** | **String**| トークン発行メソッドで取得した文字列 |
 **symbol** | **String**| 銘柄コード &lt;br&gt; ※次の形式で入力してください。&lt;br&gt; [銘柄コード]@[市場コード]&lt;br&gt; ※市場コードは下記の定義値から選択してください。 &lt;b&gt;市場コード&lt;/b&gt; &lt;table&gt;   &lt;thead&gt;     &lt;tr&gt;       &lt;th&gt;定義値&lt;/th&gt;       &lt;th&gt;説明&lt;/th&gt;     &lt;/tr&gt;   &lt;/thead&gt;   &lt;tbody&gt;     &lt;tr&gt;       &lt;td&gt;1&lt;/td&gt;       &lt;td&gt;東証&lt;/td&gt;     &lt;/tr&gt;     &lt;tr&gt;       &lt;td&gt;3&lt;/td&gt;       &lt;td&gt;名証&lt;/td&gt;     &lt;/tr&gt;   &lt;/tbody&gt; &lt;/table&gt; |

### Return type

[**WalletMarginSuccess**](WalletMarginSuccess.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="walletOptionGet"></a>
# **walletOptionGet**
> WalletOptionSuccess walletOptionGet(X_API_KEY)

取引余力（オプション）

口座の取引余力（オプション）を取得します

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.WalletApi;


WalletApi apiInstance = new WalletApi();
String X_API_KEY = "X_API_KEY_example"; // String | トークン発行メソッドで取得した文字列
try {
    WalletOptionSuccess result = apiInstance.walletOptionGet(X_API_KEY);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling WalletApi#walletOptionGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **X_API_KEY** | **String**| トークン発行メソッドで取得した文字列 |

### Return type

[**WalletOptionSuccess**](WalletOptionSuccess.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="walletOptionSymbolGet"></a>
# **walletOptionSymbolGet**
> WalletOptionSuccess walletOptionSymbolGet(X_API_KEY, symbol)

取引余力（オプション）（銘柄指定）

指定した銘柄の取引余力（オプション）を取得します

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.WalletApi;


WalletApi apiInstance = new WalletApi();
String X_API_KEY = "X_API_KEY_example"; // String | トークン発行メソッドで取得した文字列
String symbol = "symbol_example"; // String | 銘柄コード <br> ※次の形式で入力してください。<br> [銘柄コード]@[市場コード]<br> ※市場コードは下記の定義値から選択してください。 <b>市場コード</b> <table>   <thead>       <tr>           <th>定義値</th>           <th>説明</th>       </tr>   </thead>   <tbody>       <tr>           <td>2</td>           <td>日通し</td>       </tr>       <tr>           <td>23</td>           <td>日中</td>       </tr>       <tr>           <td>24</td>           <td>夜間</td>       </tr>   </tbody> </table>
try {
    WalletOptionSuccess result = apiInstance.walletOptionSymbolGet(X_API_KEY, symbol);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling WalletApi#walletOptionSymbolGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **X_API_KEY** | **String**| トークン発行メソッドで取得した文字列 |
 **symbol** | **String**| 銘柄コード &lt;br&gt; ※次の形式で入力してください。&lt;br&gt; [銘柄コード]@[市場コード]&lt;br&gt; ※市場コードは下記の定義値から選択してください。 &lt;b&gt;市場コード&lt;/b&gt; &lt;table&gt;   &lt;thead&gt;       &lt;tr&gt;           &lt;th&gt;定義値&lt;/th&gt;           &lt;th&gt;説明&lt;/th&gt;       &lt;/tr&gt;   &lt;/thead&gt;   &lt;tbody&gt;       &lt;tr&gt;           &lt;td&gt;2&lt;/td&gt;           &lt;td&gt;日通し&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;23&lt;/td&gt;           &lt;td&gt;日中&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;24&lt;/td&gt;           &lt;td&gt;夜間&lt;/td&gt;       &lt;/tr&gt;   &lt;/tbody&gt; &lt;/table&gt; |

### Return type

[**WalletOptionSuccess**](WalletOptionSuccess.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

