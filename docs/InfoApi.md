# InfoApi

All URIs are relative to *http://localhost:18080/kabusapi*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apisoftlimitGet**](InfoApi.md#apisoftlimitGet) | **GET** /apisoftlimit | ソフトリミット
[**boardGet**](InfoApi.md#boardGet) | **GET** /board/{symbol} | 時価情報・板情報
[**exchangeGet**](InfoApi.md#exchangeGet) | **GET** /exchange/{symbol} | 為替情報
[**marginpremiumGet**](InfoApi.md#marginpremiumGet) | **GET** /margin/marginpremium/{symbol} | プレミアム料取得
[**ordersGet**](InfoApi.md#ordersGet) | **GET** /orders | 注文約定照会
[**positionsGet**](InfoApi.md#positionsGet) | **GET** /positions | 残高照会
[**primaryExchangeGet**](InfoApi.md#primaryExchangeGet) | **GET** /primaryexchange/{symbol} | 優先市場
[**rankingGet**](InfoApi.md#rankingGet) | **GET** /ranking | 詳細ランキング
[**regulationsGet**](InfoApi.md#regulationsGet) | **GET** /regulations/{symbol} | 規制情報
[**symbolGet**](InfoApi.md#symbolGet) | **GET** /symbol/{symbol} | 銘柄情報
[**symbolnameFutureGet**](InfoApi.md#symbolnameFutureGet) | **GET** /symbolname/future | 先物銘柄コード取得
[**symbolnameOptionGet**](InfoApi.md#symbolnameOptionGet) | **GET** /symbolname/option | オプション銘柄コード取得

<a name="apisoftlimitGet"></a>
# **apisoftlimitGet**
> ApiSoftLimitResponse apisoftlimitGet(X_API_KEY)

ソフトリミット

kabuステーションAPIのソフトリミット値を取得する

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.InfoApi;


InfoApi apiInstance = new InfoApi();
String X_API_KEY = "X_API_KEY_example"; // String | トークン発行メソッドで取得した文字列
try {
    ApiSoftLimitResponse result = apiInstance.apisoftlimitGet(X_API_KEY);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling InfoApi#apisoftlimitGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **X_API_KEY** | **String**| トークン発行メソッドで取得した文字列 |

### Return type

[**ApiSoftLimitResponse**](ApiSoftLimitResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="boardGet"></a>
# **boardGet**
> BoardSuccess boardGet(X_API_KEY, symbol)

時価情報・板情報

指定した銘柄の時価情報・板情報を取得します&lt;br&gt; レスポンスの一部にnullが発生した場合、該当銘柄を銘柄登録をしてから、 &lt;br&gt;再度時価情報・板情報APIを実行してください。

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.InfoApi;


InfoApi apiInstance = new InfoApi();
String X_API_KEY = "X_API_KEY_example"; // String | トークン発行メソッドで取得した文字列
String symbol = "symbol_example"; // String | 銘柄コード <br> ※次の形式で入力してください。<br> [銘柄コード]@[市場コード]<br> ※市場コードは下記の定義値から選択してください。 <b>市場コード</b> <table>   <thead>       <tr>           <th>定義値</th>           <th>説明</th>       </tr>   </thead>   <tbody>       <tr>           <td>1</td>           <td>東証</td>       </tr>       <tr>           <td>3</td>           <td>名証</td>       </tr>       <tr>           <td>5</td>           <td>福証</td>       </tr>       <tr>           <td>6</td>           <td>札証</td>       </tr>       <tr>           <td>2</td>           <td>日通し</td>       </tr>       <tr>           <td>23</td>           <td>日中</td>       </tr>       <tr>           <td>24</td>           <td>夜間</td>       </tr>   </tbody> </table>
try {
    BoardSuccess result = apiInstance.boardGet(X_API_KEY, symbol);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling InfoApi#boardGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **X_API_KEY** | **String**| トークン発行メソッドで取得した文字列 |
 **symbol** | **String**| 銘柄コード &lt;br&gt; ※次の形式で入力してください。&lt;br&gt; [銘柄コード]@[市場コード]&lt;br&gt; ※市場コードは下記の定義値から選択してください。 &lt;b&gt;市場コード&lt;/b&gt; &lt;table&gt;   &lt;thead&gt;       &lt;tr&gt;           &lt;th&gt;定義値&lt;/th&gt;           &lt;th&gt;説明&lt;/th&gt;       &lt;/tr&gt;   &lt;/thead&gt;   &lt;tbody&gt;       &lt;tr&gt;           &lt;td&gt;1&lt;/td&gt;           &lt;td&gt;東証&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;3&lt;/td&gt;           &lt;td&gt;名証&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;5&lt;/td&gt;           &lt;td&gt;福証&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;6&lt;/td&gt;           &lt;td&gt;札証&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;2&lt;/td&gt;           &lt;td&gt;日通し&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;23&lt;/td&gt;           &lt;td&gt;日中&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;24&lt;/td&gt;           &lt;td&gt;夜間&lt;/td&gt;       &lt;/tr&gt;   &lt;/tbody&gt; &lt;/table&gt; |

### Return type

[**BoardSuccess**](BoardSuccess.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="exchangeGet"></a>
# **exchangeGet**
> ExchangeResponse exchangeGet(X_API_KEY, symbol)

為替情報

マネービューの情報を取得する

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.InfoApi;


InfoApi apiInstance = new InfoApi();
String X_API_KEY = "X_API_KEY_example"; // String | トークン発行メソッドで取得した文字列
String symbol = "symbol_example"; // String | 通貨 <table>   <thead>     <tr>       <th>定義値</th>       <th>内容</th>     </tr>   </thead>   <tbody>     <tr>       <td>usdjpy</td>       <td>USD/JPY</td>     </tr>     <tr>       <td>eurjpy</td>       <td>EUR/JPY</td>     </tr>     <tr>       <td>gbpjpy</td>       <td>GBP/JPY</td>     </tr>     <tr>       <td>audjpy</td>       <td>AUD/JPY</td>     </tr>     <tr>       <td>chfjpy</td>       <td>CHF/JPY</td>     </tr>     <tr>       <td>cadjpy</td>       <td>CAD/JPY</td>     </tr>     <tr>       <td>nzdjpy</td>       <td>NZD/JPY</td>     </tr>     <tr>       <td>zarjpy</td>       <td>ZAR/JPY</td>     </tr>     <tr>       <td>eurusd</td>       <td>EUR/USD</td>     </tr>     <tr>       <td>gbpusd</td>       <td>GBP/USD</td>     </tr>     <tr>       <td>audusd</td>       <td>AUD/USD</td>     </tr>   </tbody> </table>
try {
    ExchangeResponse result = apiInstance.exchangeGet(X_API_KEY, symbol);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling InfoApi#exchangeGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **X_API_KEY** | **String**| トークン発行メソッドで取得した文字列 |
 **symbol** | **String**| 通貨 &lt;table&gt;   &lt;thead&gt;     &lt;tr&gt;       &lt;th&gt;定義値&lt;/th&gt;       &lt;th&gt;内容&lt;/th&gt;     &lt;/tr&gt;   &lt;/thead&gt;   &lt;tbody&gt;     &lt;tr&gt;       &lt;td&gt;usdjpy&lt;/td&gt;       &lt;td&gt;USD/JPY&lt;/td&gt;     &lt;/tr&gt;     &lt;tr&gt;       &lt;td&gt;eurjpy&lt;/td&gt;       &lt;td&gt;EUR/JPY&lt;/td&gt;     &lt;/tr&gt;     &lt;tr&gt;       &lt;td&gt;gbpjpy&lt;/td&gt;       &lt;td&gt;GBP/JPY&lt;/td&gt;     &lt;/tr&gt;     &lt;tr&gt;       &lt;td&gt;audjpy&lt;/td&gt;       &lt;td&gt;AUD/JPY&lt;/td&gt;     &lt;/tr&gt;     &lt;tr&gt;       &lt;td&gt;chfjpy&lt;/td&gt;       &lt;td&gt;CHF/JPY&lt;/td&gt;     &lt;/tr&gt;     &lt;tr&gt;       &lt;td&gt;cadjpy&lt;/td&gt;       &lt;td&gt;CAD/JPY&lt;/td&gt;     &lt;/tr&gt;     &lt;tr&gt;       &lt;td&gt;nzdjpy&lt;/td&gt;       &lt;td&gt;NZD/JPY&lt;/td&gt;     &lt;/tr&gt;     &lt;tr&gt;       &lt;td&gt;zarjpy&lt;/td&gt;       &lt;td&gt;ZAR/JPY&lt;/td&gt;     &lt;/tr&gt;     &lt;tr&gt;       &lt;td&gt;eurusd&lt;/td&gt;       &lt;td&gt;EUR/USD&lt;/td&gt;     &lt;/tr&gt;     &lt;tr&gt;       &lt;td&gt;gbpusd&lt;/td&gt;       &lt;td&gt;GBP/USD&lt;/td&gt;     &lt;/tr&gt;     &lt;tr&gt;       &lt;td&gt;audusd&lt;/td&gt;       &lt;td&gt;AUD/USD&lt;/td&gt;     &lt;/tr&gt;   &lt;/tbody&gt; &lt;/table&gt; | [enum: usdjpy, eurjpy, gbpjpy, audjpy, chfjpy, cadjpy, nzdjpy, zarjpy, eurusd, gbpusd, audusd]

### Return type

[**ExchangeResponse**](ExchangeResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="marginpremiumGet"></a>
# **marginpremiumGet**
> MarginPremiumResponse marginpremiumGet(X_API_KEY, symbol)

プレミアム料取得

指定した銘柄のプレミアム料を取得するAPI

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.InfoApi;


InfoApi apiInstance = new InfoApi();
String X_API_KEY = "X_API_KEY_example"; // String | トークン発行メソッドで取得した文字列
String symbol = "symbol_example"; // String | 銘柄コード
try {
    MarginPremiumResponse result = apiInstance.marginpremiumGet(X_API_KEY, symbol);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling InfoApi#marginpremiumGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **X_API_KEY** | **String**| トークン発行メソッドで取得した文字列 |
 **symbol** | **String**| 銘柄コード |

### Return type

[**MarginPremiumResponse**](MarginPremiumResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ordersGet"></a>
# **ordersGet**
> List&lt;OrdersSuccess&gt; ordersGet(X_API_KEY, product, id, updtime, details, symbol, state, side, cashmargin)

注文約定照会

注文一覧を取得します。&lt;br&gt; ※下記Queryパラメータは任意設定となります。

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.InfoApi;


InfoApi apiInstance = new InfoApi();
String X_API_KEY = "X_API_KEY_example"; // String | トークン発行メソッドで取得した文字列
String product = "product_example"; // String | 取得する商品 <table>   <thead>       <tr>           <th>定義値</th>           <th>説明</th>       </tr>   </thead>   <tbody>       <tr>           <td>0</td>           <td>すべて </td>       </tr>       <tr>           <td>1</td>           <td>現物</td>       </tr>       <tr>           <td>2</td>           <td>信用</td>       </tr>       <tr>           <td>3</td>           <td>先物</td>       </tr>       <tr>           <td>4</td>           <td>OP</td>       </tr>   </tbody> </table>
String id = "id_example"; // String | 注文番号<br> ※指定された注文番号と一致する注文のみレスポンスします。<br> ※指定された注文番号との比較では大文字小文字を区別しません。<br> ※複数の注文番号を指定することはできません。
String updtime = "updtime_example"; // String | 更新日時<br> ※形式：yyyyMMddHHmmss （例：20201201123456）<br> ※指定された更新日時以降（指定日時含む）に更新された注文のみレスポンスします。<br> ※複数の更新日時を指定することはできません。
String details = "details_example"; // String | 注文詳細抑止 <table>   <thead>       <tr>           <th>定義値</th>           <th>説明</th>       </tr>   </thead>   <tbody>       <tr>           <td>true</td>           <td>注文詳細を出力する（デフォルト）</td>       </tr>       <tr>           <td>false</td>           <td>注文詳細の出力しない</td>       </tr>   </tbody> </table>
String symbol = "symbol_example"; // String | 銘柄コード<br>※指定された銘柄コードと一致する注文のみレスポンスします。<br>※複数の銘柄コードを指定することができません。
String state = "state_example"; // String | 状態<br> ※指定された状態と一致する注文のみレスポンスします。<br> ※フィルタには数字の入力のみ受け付けます。<br> ※複数の状態を指定することはできません。 <table>   <thead>       <tr>           <th>定義値</th>           <th>説明</th>       </tr>   </thead>   <tbody>       <tr>           <td>1</td>           <td>待機（発注待機）</td>       </tr>       <tr>           <td>2</td>           <td>処理中（発注送信中）</td>       </tr>       <tr>           <td>3</td>           <td>処理済（発注済・訂正済）</td>       </tr>       <tr>           <td>4</td>           <td>訂正取消送信中</td>       </tr>       <tr>           <td>5</td>           <td>終了（発注エラー・取消済・全約定・失効・期限切れ）</td>       </tr>   </tbody> </table>
String side = "side_example"; // String | 売買区分<br> ※指定された売買区分と一致する注文のみレスポンスします。<br> ※フィルタには数字の入力のみ受け付けます。<br> ※複数の売買区分を指定することができません。 <table>   <thead>       <tr>           <th>定義値</th>           <th>説明</th>       </tr>   </thead>   <tbody>       <tr>           <td>1</td>           <td>売</td>       </tr>       <tr>           <td>2</td>           <td>買</td>       </tr>   </tbody> </table>
String cashmargin = "cashmargin_example"; // String | 取引区分<br> ※指定された取引区分と一致する注文のみレスポンスします。<br> ※フィルタには数字の入力のみ受け付けます。<br> ※複数の取引区分を指定することができません。 <table>   <thead>       <tr>           <th>定義値</th>           <th>説明</th>       </tr>   </thead>   <tbody>       <tr>           <td>2</td>           <td>新規</td>       </tr>       <tr>           <td>3</td>           <td>返済</td>       </tr>   </tbody> </table>
try {
    List<OrdersSuccess> result = apiInstance.ordersGet(X_API_KEY, product, id, updtime, details, symbol, state, side, cashmargin);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling InfoApi#ordersGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **X_API_KEY** | **String**| トークン発行メソッドで取得した文字列 |
 **product** | **String**| 取得する商品 &lt;table&gt;   &lt;thead&gt;       &lt;tr&gt;           &lt;th&gt;定義値&lt;/th&gt;           &lt;th&gt;説明&lt;/th&gt;       &lt;/tr&gt;   &lt;/thead&gt;   &lt;tbody&gt;       &lt;tr&gt;           &lt;td&gt;0&lt;/td&gt;           &lt;td&gt;すべて &lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;1&lt;/td&gt;           &lt;td&gt;現物&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;2&lt;/td&gt;           &lt;td&gt;信用&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;3&lt;/td&gt;           &lt;td&gt;先物&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;4&lt;/td&gt;           &lt;td&gt;OP&lt;/td&gt;       &lt;/tr&gt;   &lt;/tbody&gt; &lt;/table&gt; | [optional] [enum: 0, 1, 2, 3, 4]
 **id** | **String**| 注文番号&lt;br&gt; ※指定された注文番号と一致する注文のみレスポンスします。&lt;br&gt; ※指定された注文番号との比較では大文字小文字を区別しません。&lt;br&gt; ※複数の注文番号を指定することはできません。 | [optional]
 **updtime** | **String**| 更新日時&lt;br&gt; ※形式：yyyyMMddHHmmss （例：20201201123456）&lt;br&gt; ※指定された更新日時以降（指定日時含む）に更新された注文のみレスポンスします。&lt;br&gt; ※複数の更新日時を指定することはできません。 | [optional]
 **details** | **String**| 注文詳細抑止 &lt;table&gt;   &lt;thead&gt;       &lt;tr&gt;           &lt;th&gt;定義値&lt;/th&gt;           &lt;th&gt;説明&lt;/th&gt;       &lt;/tr&gt;   &lt;/thead&gt;   &lt;tbody&gt;       &lt;tr&gt;           &lt;td&gt;true&lt;/td&gt;           &lt;td&gt;注文詳細を出力する（デフォルト）&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;false&lt;/td&gt;           &lt;td&gt;注文詳細の出力しない&lt;/td&gt;       &lt;/tr&gt;   &lt;/tbody&gt; &lt;/table&gt; | [optional]
 **symbol** | **String**| 銘柄コード&lt;br&gt;※指定された銘柄コードと一致する注文のみレスポンスします。&lt;br&gt;※複数の銘柄コードを指定することができません。 | [optional]
 **state** | **String**| 状態&lt;br&gt; ※指定された状態と一致する注文のみレスポンスします。&lt;br&gt; ※フィルタには数字の入力のみ受け付けます。&lt;br&gt; ※複数の状態を指定することはできません。 &lt;table&gt;   &lt;thead&gt;       &lt;tr&gt;           &lt;th&gt;定義値&lt;/th&gt;           &lt;th&gt;説明&lt;/th&gt;       &lt;/tr&gt;   &lt;/thead&gt;   &lt;tbody&gt;       &lt;tr&gt;           &lt;td&gt;1&lt;/td&gt;           &lt;td&gt;待機（発注待機）&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;2&lt;/td&gt;           &lt;td&gt;処理中（発注送信中）&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;3&lt;/td&gt;           &lt;td&gt;処理済（発注済・訂正済）&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;4&lt;/td&gt;           &lt;td&gt;訂正取消送信中&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;5&lt;/td&gt;           &lt;td&gt;終了（発注エラー・取消済・全約定・失効・期限切れ）&lt;/td&gt;       &lt;/tr&gt;   &lt;/tbody&gt; &lt;/table&gt; | [optional] [enum: 1, 2, 3, 4, 5]
 **side** | **String**| 売買区分&lt;br&gt; ※指定された売買区分と一致する注文のみレスポンスします。&lt;br&gt; ※フィルタには数字の入力のみ受け付けます。&lt;br&gt; ※複数の売買区分を指定することができません。 &lt;table&gt;   &lt;thead&gt;       &lt;tr&gt;           &lt;th&gt;定義値&lt;/th&gt;           &lt;th&gt;説明&lt;/th&gt;       &lt;/tr&gt;   &lt;/thead&gt;   &lt;tbody&gt;       &lt;tr&gt;           &lt;td&gt;1&lt;/td&gt;           &lt;td&gt;売&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;2&lt;/td&gt;           &lt;td&gt;買&lt;/td&gt;       &lt;/tr&gt;   &lt;/tbody&gt; &lt;/table&gt; | [optional] [enum: 1, 2]
 **cashmargin** | **String**| 取引区分&lt;br&gt; ※指定された取引区分と一致する注文のみレスポンスします。&lt;br&gt; ※フィルタには数字の入力のみ受け付けます。&lt;br&gt; ※複数の取引区分を指定することができません。 &lt;table&gt;   &lt;thead&gt;       &lt;tr&gt;           &lt;th&gt;定義値&lt;/th&gt;           &lt;th&gt;説明&lt;/th&gt;       &lt;/tr&gt;   &lt;/thead&gt;   &lt;tbody&gt;       &lt;tr&gt;           &lt;td&gt;2&lt;/td&gt;           &lt;td&gt;新規&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;3&lt;/td&gt;           &lt;td&gt;返済&lt;/td&gt;       &lt;/tr&gt;   &lt;/tbody&gt; &lt;/table&gt; | [optional] [enum: 2, 3]

### Return type

[**List&lt;OrdersSuccess&gt;**](OrdersSuccess.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="positionsGet"></a>
# **positionsGet**
> List&lt;PositionsSuccess&gt; positionsGet(X_API_KEY, product, symbol, side, addinfo)

残高照会

残高一覧を取得します。&lt;br&gt;※下記Queryパラメータは任意設定となります。

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.InfoApi;


InfoApi apiInstance = new InfoApi();
String X_API_KEY = "X_API_KEY_example"; // String | トークン発行メソッドで取得した文字列
String product = "product_example"; // String | 取得する商品 <table>   <thead>       <tr>           <th>定義値</th>           <th>説明</th>       </tr>   </thead>   <tbody>       <tr>           <td>0</td>           <td>すべて</td>       </tr>       <tr>           <td>1</td>           <td>現物</td>       </tr>       <tr>           <td>2</td>           <td>信用</td>       </tr>       <tr>           <td>3</td>           <td>先物</td>       </tr>       <tr>           <td>4</td>           <td>OP</td>       </tr>   </tbody> </table>
String symbol = "symbol_example"; // String | 銘柄コード<br>※指定された銘柄コードと一致するポジションのみレスポンスします。<br>※複数の銘柄コードを指定することはできません。
String side = "side_example"; // String | 売買区分フィルタ<br> 指定された売買区分と一致する注文を返す <table>   <thead>       <tr>           <th>定義値</th>           <th>説明</th>       </tr>   </thead>   <tbody>       <tr>           <td>1</td>           <td>売</td>       </tr>       <tr>           <td>2</td>           <td>買</td>       </tr>   </tbody> </table>
String addinfo = "addinfo_example"; // String | 追加情報出力フラグ（未指定時：true）<br> ※追加情報は、「現在値」、「評価金額」、「評価損益額」、「評価損益率」を意味します。 <table>   <thead>       <tr>           <th>定義値</th>           <th>説明</th>       </tr>   </thead>   <tbody>       <tr>           <td>true</td>           <td>追加情報を出力する</td>       </tr>       <tr>           <td>false</td>           <td>追加情報を出力しない</td>       </tr>   </tbody> </table>
try {
    List<PositionsSuccess> result = apiInstance.positionsGet(X_API_KEY, product, symbol, side, addinfo);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling InfoApi#positionsGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **X_API_KEY** | **String**| トークン発行メソッドで取得した文字列 |
 **product** | **String**| 取得する商品 &lt;table&gt;   &lt;thead&gt;       &lt;tr&gt;           &lt;th&gt;定義値&lt;/th&gt;           &lt;th&gt;説明&lt;/th&gt;       &lt;/tr&gt;   &lt;/thead&gt;   &lt;tbody&gt;       &lt;tr&gt;           &lt;td&gt;0&lt;/td&gt;           &lt;td&gt;すべて&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;1&lt;/td&gt;           &lt;td&gt;現物&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;2&lt;/td&gt;           &lt;td&gt;信用&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;3&lt;/td&gt;           &lt;td&gt;先物&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;4&lt;/td&gt;           &lt;td&gt;OP&lt;/td&gt;       &lt;/tr&gt;   &lt;/tbody&gt; &lt;/table&gt; | [optional] [enum: 0, 1, 2, 3, 4]
 **symbol** | **String**| 銘柄コード&lt;br&gt;※指定された銘柄コードと一致するポジションのみレスポンスします。&lt;br&gt;※複数の銘柄コードを指定することはできません。 | [optional]
 **side** | **String**| 売買区分フィルタ&lt;br&gt; 指定された売買区分と一致する注文を返す &lt;table&gt;   &lt;thead&gt;       &lt;tr&gt;           &lt;th&gt;定義値&lt;/th&gt;           &lt;th&gt;説明&lt;/th&gt;       &lt;/tr&gt;   &lt;/thead&gt;   &lt;tbody&gt;       &lt;tr&gt;           &lt;td&gt;1&lt;/td&gt;           &lt;td&gt;売&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;2&lt;/td&gt;           &lt;td&gt;買&lt;/td&gt;       &lt;/tr&gt;   &lt;/tbody&gt; &lt;/table&gt; | [optional] [enum: 1, 2]
 **addinfo** | **String**| 追加情報出力フラグ（未指定時：true）&lt;br&gt; ※追加情報は、「現在値」、「評価金額」、「評価損益額」、「評価損益率」を意味します。 &lt;table&gt;   &lt;thead&gt;       &lt;tr&gt;           &lt;th&gt;定義値&lt;/th&gt;           &lt;th&gt;説明&lt;/th&gt;       &lt;/tr&gt;   &lt;/thead&gt;   &lt;tbody&gt;       &lt;tr&gt;           &lt;td&gt;true&lt;/td&gt;           &lt;td&gt;追加情報を出力する&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;false&lt;/td&gt;           &lt;td&gt;追加情報を出力しない&lt;/td&gt;       &lt;/tr&gt;   &lt;/tbody&gt; &lt;/table&gt; | [optional]

### Return type

[**List&lt;PositionsSuccess&gt;**](PositionsSuccess.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="primaryExchangeGet"></a>
# **primaryExchangeGet**
> PrimaryExchangeResponse primaryExchangeGet(X_API_KEY, symbol)

優先市場

株式の優先市場を取得する

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.InfoApi;


InfoApi apiInstance = new InfoApi();
String X_API_KEY = "X_API_KEY_example"; // String | トークン発行メソッドで取得した文字列
String symbol = "symbol_example"; // String | 銘柄コード
try {
    PrimaryExchangeResponse result = apiInstance.primaryExchangeGet(X_API_KEY, symbol);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling InfoApi#primaryExchangeGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **X_API_KEY** | **String**| トークン発行メソッドで取得した文字列 |
 **symbol** | **String**| 銘柄コード |

### Return type

[**PrimaryExchangeResponse**](PrimaryExchangeResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="rankingGet"></a>
# **rankingGet**
> InlineResponse200 rankingGet(X_API_KEY, type, exchangeDivision)

詳細ランキング

詳細ランキング画面と同様の各種ランキングを返します。 &lt;br&gt;ランキングの対象日はkabuステーションが保持している当日のデータとなります。 &lt;br&gt;※株価情報ランキング、業種別指数ランキングは、下記の時間帯でデータがクリアされるため、 &lt;br&gt;その間の詳細ランキングAPIは空レスポンスとなります。 &lt;br&gt;データクリア：平日7:53頃-9:00過ぎ頃 &lt;br&gt;※信用情報ランキングは毎週第３営業日の7:55頃にデータが更新されます。

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.InfoApi;


InfoApi apiInstance = new InfoApi();
String X_API_KEY = "X_API_KEY_example"; // String | トークン発行メソッドで取得した文字列
String type = "type_example"; // String | 種別<br> ※信用情報ランキングに「福証」「札証」を指定した場合は、空レスポンスになります <table>   <thead>       <tr>           <th>定義値</th>           <th>説明</th>       </tr>   </thead>   <tbody>       <tr>           <td>1</td>           <td>値上がり率（デフォルト）</td>       </tr>       <tr>           <td>2</td>           <td>値下がり率</td>       </tr>       <tr>           <td>3</td>           <td>売買高上位</td>       </tr>       <tr>           <td>4</td>           <td>売買代金</td>       </tr>       <tr>           <td>5</td>           <td>TICK回数</td>       </tr>       <tr>           <td>6</td>           <td>売買高急増</td>       </tr>       <tr>           <td>7</td>           <td>売買代金急増</td>       </tr>       <tr>           <td>8</td>           <td>信用売残増</td>       </tr>       <tr>           <td>9</td>           <td>信用売残減</td>       </tr>       <tr>           <td>10</td>           <td>信用買残増</td>       </tr>       <tr>           <td>11</td>           <td>信用買残減</td>       </tr>       <tr>           <td>12</td>           <td>信用高倍率</td>       </tr>       <tr>           <td>13</td>           <td>信用低倍率</td>       </tr>       <tr>           <td>14</td>           <td>業種別値上がり率</td>       </tr>       <tr>           <td>15</td>           <td>業種別値下がり率</td>       </tr>   </tbody> </table>
String exchangeDivision = "exchangeDivision_example"; // String | 市場<br> ※業種別値上がり率・値下がり率に市場を指定しても無視されます <table>   <thead>       <tr>           <th>定義値</th>           <th>説明</th>       </tr>   </thead>   <tbody>       <tr>           <td>ALL</td>           <td>全市場（デフォルト）</td>       </tr>       <tr>           <td>T</td>           <td>東証全体</td>       </tr>       <tr>           <td>T1</td>           <td>東証一部</td>       </tr>       <tr>           <td>T2</td>           <td>東証二部</td>       </tr>       <tr>           <td>TM</td>           <td>マザーズ</td>       </tr>       <tr>           <td>JQ</td>           <td>JASDAQ</td>       </tr>       <tr>           <td>M</td>           <td>名証</td>       </tr>       <tr>           <td>FK</td>           <td>福証</td>       </tr>       <tr>           <td>S</td>           <td>札証</td>       </tr>   </tbody> </table>
try {
    InlineResponse200 result = apiInstance.rankingGet(X_API_KEY, type, exchangeDivision);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling InfoApi#rankingGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **X_API_KEY** | **String**| トークン発行メソッドで取得した文字列 |
 **type** | **String**| 種別&lt;br&gt; ※信用情報ランキングに「福証」「札証」を指定した場合は、空レスポンスになります &lt;table&gt;   &lt;thead&gt;       &lt;tr&gt;           &lt;th&gt;定義値&lt;/th&gt;           &lt;th&gt;説明&lt;/th&gt;       &lt;/tr&gt;   &lt;/thead&gt;   &lt;tbody&gt;       &lt;tr&gt;           &lt;td&gt;1&lt;/td&gt;           &lt;td&gt;値上がり率（デフォルト）&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;2&lt;/td&gt;           &lt;td&gt;値下がり率&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;3&lt;/td&gt;           &lt;td&gt;売買高上位&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;4&lt;/td&gt;           &lt;td&gt;売買代金&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;5&lt;/td&gt;           &lt;td&gt;TICK回数&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;6&lt;/td&gt;           &lt;td&gt;売買高急増&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;7&lt;/td&gt;           &lt;td&gt;売買代金急増&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;8&lt;/td&gt;           &lt;td&gt;信用売残増&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;9&lt;/td&gt;           &lt;td&gt;信用売残減&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;10&lt;/td&gt;           &lt;td&gt;信用買残増&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;11&lt;/td&gt;           &lt;td&gt;信用買残減&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;12&lt;/td&gt;           &lt;td&gt;信用高倍率&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;13&lt;/td&gt;           &lt;td&gt;信用低倍率&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;14&lt;/td&gt;           &lt;td&gt;業種別値上がり率&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;15&lt;/td&gt;           &lt;td&gt;業種別値下がり率&lt;/td&gt;       &lt;/tr&gt;   &lt;/tbody&gt; &lt;/table&gt; | [enum: 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]
 **exchangeDivision** | **String**| 市場&lt;br&gt; ※業種別値上がり率・値下がり率に市場を指定しても無視されます &lt;table&gt;   &lt;thead&gt;       &lt;tr&gt;           &lt;th&gt;定義値&lt;/th&gt;           &lt;th&gt;説明&lt;/th&gt;       &lt;/tr&gt;   &lt;/thead&gt;   &lt;tbody&gt;       &lt;tr&gt;           &lt;td&gt;ALL&lt;/td&gt;           &lt;td&gt;全市場（デフォルト）&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;T&lt;/td&gt;           &lt;td&gt;東証全体&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;T1&lt;/td&gt;           &lt;td&gt;東証一部&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;T2&lt;/td&gt;           &lt;td&gt;東証二部&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;TM&lt;/td&gt;           &lt;td&gt;マザーズ&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;JQ&lt;/td&gt;           &lt;td&gt;JASDAQ&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;M&lt;/td&gt;           &lt;td&gt;名証&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;FK&lt;/td&gt;           &lt;td&gt;福証&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;S&lt;/td&gt;           &lt;td&gt;札証&lt;/td&gt;       &lt;/tr&gt;   &lt;/tbody&gt; &lt;/table&gt; | [enum: ALL, T, T1, T2, TM, JQ, M, FK, S]

### Return type

[**InlineResponse200**](InlineResponse200.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="regulationsGet"></a>
# **regulationsGet**
> RegulationsResponse regulationsGet(X_API_KEY, symbol)

規制情報

規制情報＋空売り規制情報を取得する

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.InfoApi;


InfoApi apiInstance = new InfoApi();
String X_API_KEY = "X_API_KEY_example"; // String | トークン発行メソッドで取得した文字列
String symbol = "symbol_example"; // String | 銘柄コード <br> ※次の形式で入力してください。<br> [銘柄コード]@[市場コード]<br> ※市場コードは下記の定義値から選択してください。 <b>市場コード</b> <table>   <thead>       <tr>           <th>定義値</th>           <th>説明</th>       </tr>   </thead>   <tbody>       <tr>           <td>1</td>           <td>東証</td>       </tr>       <tr>           <td>3</td>           <td>名証</td>       </tr>       <tr>           <td>5</td>           <td>福証</td>       </tr>       <tr>           <td>6</td>           <td>札証</td>       </tr>   </tbody> </table>
try {
    RegulationsResponse result = apiInstance.regulationsGet(X_API_KEY, symbol);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling InfoApi#regulationsGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **X_API_KEY** | **String**| トークン発行メソッドで取得した文字列 |
 **symbol** | **String**| 銘柄コード &lt;br&gt; ※次の形式で入力してください。&lt;br&gt; [銘柄コード]@[市場コード]&lt;br&gt; ※市場コードは下記の定義値から選択してください。 &lt;b&gt;市場コード&lt;/b&gt; &lt;table&gt;   &lt;thead&gt;       &lt;tr&gt;           &lt;th&gt;定義値&lt;/th&gt;           &lt;th&gt;説明&lt;/th&gt;       &lt;/tr&gt;   &lt;/thead&gt;   &lt;tbody&gt;       &lt;tr&gt;           &lt;td&gt;1&lt;/td&gt;           &lt;td&gt;東証&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;3&lt;/td&gt;           &lt;td&gt;名証&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;5&lt;/td&gt;           &lt;td&gt;福証&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;6&lt;/td&gt;           &lt;td&gt;札証&lt;/td&gt;       &lt;/tr&gt;   &lt;/tbody&gt; &lt;/table&gt; |

### Return type

[**RegulationsResponse**](RegulationsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="symbolGet"></a>
# **symbolGet**
> SymbolSuccess symbolGet(X_API_KEY, symbol, addinfo)

銘柄情報

指定した銘柄情報を取得します

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.InfoApi;


InfoApi apiInstance = new InfoApi();
String X_API_KEY = "X_API_KEY_example"; // String | トークン発行メソッドで取得した文字列
String symbol = "symbol_example"; // String | 銘柄コード <br> ※次の形式で入力してください。<br> [銘柄コード]@[市場コード]<br> ※市場コードは下記の定義値から選択してください。 <b>市場コード</b> <table>   <thead>       <tr>           <th>定義値</th>           <th>説明</th>       </tr>   </thead>   <tbody>       <tr>           <td>1</td>           <td>東証</td>       </tr>       <tr>           <td>3</td>           <td>名証</td>       </tr>       <tr>           <td>5</td>           <td>福証</td>       </tr>       <tr>           <td>6</td>           <td>札証</td>       </tr>       <tr>           <td>2</td>           <td>日通し</td>       </tr>       <tr>           <td>23</td>           <td>日中</td>       </tr>       <tr>           <td>24</td>           <td>夜間</td>       </tr>   </tbody> </table>
String addinfo = "addinfo_example"; // String | 追加情報出力フラグ（未指定時：true）<br> ※追加情報は、「時価総額」、「発行済み株式数」、「決算期日」、「清算値」を意味します。 <table>   <thead>       <tr>           <th>定義値</th>           <th>説明</th>       </tr>   </thead>   <tbody>       <tr>           <td>true</td>           <td>追加情報を出力する</td>       </tr>       <tr>           <td>false</td>           <td>追加情報を出力しない</td>       </tr>   </tbody> </table>
try {
    SymbolSuccess result = apiInstance.symbolGet(X_API_KEY, symbol, addinfo);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling InfoApi#symbolGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **X_API_KEY** | **String**| トークン発行メソッドで取得した文字列 |
 **symbol** | **String**| 銘柄コード &lt;br&gt; ※次の形式で入力してください。&lt;br&gt; [銘柄コード]@[市場コード]&lt;br&gt; ※市場コードは下記の定義値から選択してください。 &lt;b&gt;市場コード&lt;/b&gt; &lt;table&gt;   &lt;thead&gt;       &lt;tr&gt;           &lt;th&gt;定義値&lt;/th&gt;           &lt;th&gt;説明&lt;/th&gt;       &lt;/tr&gt;   &lt;/thead&gt;   &lt;tbody&gt;       &lt;tr&gt;           &lt;td&gt;1&lt;/td&gt;           &lt;td&gt;東証&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;3&lt;/td&gt;           &lt;td&gt;名証&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;5&lt;/td&gt;           &lt;td&gt;福証&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;6&lt;/td&gt;           &lt;td&gt;札証&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;2&lt;/td&gt;           &lt;td&gt;日通し&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;23&lt;/td&gt;           &lt;td&gt;日中&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;24&lt;/td&gt;           &lt;td&gt;夜間&lt;/td&gt;       &lt;/tr&gt;   &lt;/tbody&gt; &lt;/table&gt; |
 **addinfo** | **String**| 追加情報出力フラグ（未指定時：true）&lt;br&gt; ※追加情報は、「時価総額」、「発行済み株式数」、「決算期日」、「清算値」を意味します。 &lt;table&gt;   &lt;thead&gt;       &lt;tr&gt;           &lt;th&gt;定義値&lt;/th&gt;           &lt;th&gt;説明&lt;/th&gt;       &lt;/tr&gt;   &lt;/thead&gt;   &lt;tbody&gt;       &lt;tr&gt;           &lt;td&gt;true&lt;/td&gt;           &lt;td&gt;追加情報を出力する&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;false&lt;/td&gt;           &lt;td&gt;追加情報を出力しない&lt;/td&gt;       &lt;/tr&gt;   &lt;/tbody&gt; &lt;/table&gt; | [optional]

### Return type

[**SymbolSuccess**](SymbolSuccess.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="symbolnameFutureGet"></a>
# **symbolnameFutureGet**
> SymbolNameSuccess symbolnameFutureGet(X_API_KEY, derivMonth, futureCode)

先物銘柄コード取得

先物銘柄コード取得

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.InfoApi;


InfoApi apiInstance = new InfoApi();
String X_API_KEY = "X_API_KEY_example"; // String | トークン発行メソッドで取得した文字列
Integer derivMonth = 56; // Integer | 限月<br> ※限月はyyyyMM形式で指定します。0を指定した場合、直近限月となります。<br> ※取引最終日に「0」（直近限月）を指定した場合、日中・夜間の時間帯に関わらず、 取引最終日を迎える限月の銘柄コードを返します。取引最終日を迎える銘柄の取引は日中取引をもって終了となりますので、ご注意ください。
String futureCode = "futureCode_example"; // String | 先物コード<br> ※大文字小文字は区別しません。 <table>   <thead>       <tr>           <th>定義値</th>           <th>説明</th>       </tr>   </thead>   <tbody>       <tr>           <td>NK225</td>           <td>日経平均先物</td>       </tr>       <tr>           <td>NK225mini</td>           <td>日経225mini先物</td>       </tr>       <tr>           <td>TOPIX</td>           <td>TOPIX先物</td>       </tr>       <tr>           <td>TOPIXmini</td>           <td>ミニTOPIX先物</td>       </tr>       <tr>           <td>MOTHERS</td>           <td>東証マザーズ先物</td>       </tr>       <tr>           <td>JPX400</td>           <td>JPX日経400先物</td>       </tr>       <tr>           <td>DOW</td>           <td>NYダウ先物</td>       </tr>       <tr>           <td>VI</td>           <td>日経平均VI先物</td>       </tr>       <tr>           <td>Core30</td>           <td>TOPIX Core30先物</td>       </tr>       <tr>           <td>REIT</td>           <td>東証REIT指数先物</td>       </tr>   </tbody> </table>
try {
    SymbolNameSuccess result = apiInstance.symbolnameFutureGet(X_API_KEY, derivMonth, futureCode);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling InfoApi#symbolnameFutureGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **X_API_KEY** | **String**| トークン発行メソッドで取得した文字列 |
 **derivMonth** | **Integer**| 限月&lt;br&gt; ※限月はyyyyMM形式で指定します。0を指定した場合、直近限月となります。&lt;br&gt; ※取引最終日に「0」（直近限月）を指定した場合、日中・夜間の時間帯に関わらず、 取引最終日を迎える限月の銘柄コードを返します。取引最終日を迎える銘柄の取引は日中取引をもって終了となりますので、ご注意ください。 |
 **futureCode** | **String**| 先物コード&lt;br&gt; ※大文字小文字は区別しません。 &lt;table&gt;   &lt;thead&gt;       &lt;tr&gt;           &lt;th&gt;定義値&lt;/th&gt;           &lt;th&gt;説明&lt;/th&gt;       &lt;/tr&gt;   &lt;/thead&gt;   &lt;tbody&gt;       &lt;tr&gt;           &lt;td&gt;NK225&lt;/td&gt;           &lt;td&gt;日経平均先物&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;NK225mini&lt;/td&gt;           &lt;td&gt;日経225mini先物&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;TOPIX&lt;/td&gt;           &lt;td&gt;TOPIX先物&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;TOPIXmini&lt;/td&gt;           &lt;td&gt;ミニTOPIX先物&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;MOTHERS&lt;/td&gt;           &lt;td&gt;東証マザーズ先物&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;JPX400&lt;/td&gt;           &lt;td&gt;JPX日経400先物&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;DOW&lt;/td&gt;           &lt;td&gt;NYダウ先物&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;VI&lt;/td&gt;           &lt;td&gt;日経平均VI先物&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;Core30&lt;/td&gt;           &lt;td&gt;TOPIX Core30先物&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;REIT&lt;/td&gt;           &lt;td&gt;東証REIT指数先物&lt;/td&gt;       &lt;/tr&gt;   &lt;/tbody&gt; &lt;/table&gt; | [optional]

### Return type

[**SymbolNameSuccess**](SymbolNameSuccess.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="symbolnameOptionGet"></a>
# **symbolnameOptionGet**
> SymbolNameSuccess symbolnameOptionGet(X_API_KEY, derivMonth, putOrCall, strikePrice)

オプション銘柄コード取得

オプション銘柄コード取得

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.InfoApi;


InfoApi apiInstance = new InfoApi();
String X_API_KEY = "X_API_KEY_example"; // String | トークン発行メソッドで取得した文字列
Integer derivMonth = 56; // Integer | 限月<br>※限月はyyyyMM形式で指定します。0を指定した場合、直近限月となります。<br>※取引最終日に「0」（直近限月）を指定した場合、日中・夜間の時間帯に関わらず、取引最終日を迎える限月の銘柄コードを返します。取引最終日を迎える銘柄の取引は日中取引をもって終了となりますので、ご注意ください。
String putOrCall = "putOrCall_example"; // String | コール or プット<br> ※大文字小文字は区別しません。 <table>   <thead>       <tr>           <th>定義値</th>           <th>説明</th>       </tr>   </thead>   <tbody>       <tr>           <td>P</td>           <td>PUT</td>       </tr>       <tr>           <td>C</td>           <td>CALL</td>       </tr>   </tbody> </table>
Integer strikePrice = 56; // Integer | 権利行使価格<br>※0を指定した場合、APIを実行した時点でのATMとなります。
try {
    SymbolNameSuccess result = apiInstance.symbolnameOptionGet(X_API_KEY, derivMonth, putOrCall, strikePrice);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling InfoApi#symbolnameOptionGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **X_API_KEY** | **String**| トークン発行メソッドで取得した文字列 |
 **derivMonth** | **Integer**| 限月&lt;br&gt;※限月はyyyyMM形式で指定します。0を指定した場合、直近限月となります。&lt;br&gt;※取引最終日に「0」（直近限月）を指定した場合、日中・夜間の時間帯に関わらず、取引最終日を迎える限月の銘柄コードを返します。取引最終日を迎える銘柄の取引は日中取引をもって終了となりますので、ご注意ください。 |
 **putOrCall** | **String**| コール or プット&lt;br&gt; ※大文字小文字は区別しません。 &lt;table&gt;   &lt;thead&gt;       &lt;tr&gt;           &lt;th&gt;定義値&lt;/th&gt;           &lt;th&gt;説明&lt;/th&gt;       &lt;/tr&gt;   &lt;/thead&gt;   &lt;tbody&gt;       &lt;tr&gt;           &lt;td&gt;P&lt;/td&gt;           &lt;td&gt;PUT&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;C&lt;/td&gt;           &lt;td&gt;CALL&lt;/td&gt;       &lt;/tr&gt;   &lt;/tbody&gt; &lt;/table&gt; |
 **strikePrice** | **Integer**| 権利行使価格&lt;br&gt;※0を指定した場合、APIを実行した時点でのATMとなります。 |

### Return type

[**SymbolNameSuccess**](SymbolNameSuccess.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

