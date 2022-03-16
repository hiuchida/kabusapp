# PositionsSuccess

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**executionID** | **String** | 約定番号&lt;br&gt;※現物取引では、nullが返ります。 |  [optional]
**accountType** | **Integer** | 口座種別 &lt;table&gt;   &lt;thead&gt;       &lt;tr&gt;           &lt;th&gt;定義値&lt;/th&gt;           &lt;th&gt;説明&lt;/th&gt;       &lt;/tr&gt;   &lt;/thead&gt;   &lt;tbody&gt;       &lt;tr&gt;           &lt;td&gt;2&lt;/td&gt;           &lt;td&gt;一般&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;4&lt;/td&gt;           &lt;td&gt;特定&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;12&lt;/td&gt;           &lt;td&gt;法人&lt;/td&gt;       &lt;/tr&gt;   &lt;/tbody&gt; &lt;/table&gt; |  [optional]
**symbol** | **String** | 銘柄コード |  [optional]
**symbolName** | **String** | 銘柄名 |  [optional]
**exchange** | **Integer** | 市場コード &lt;table&gt;   &lt;thead&gt;       &lt;tr&gt;           &lt;th&gt;定義値&lt;/th&gt;           &lt;th&gt;説明&lt;/th&gt;       &lt;/tr&gt;   &lt;/thead&gt;   &lt;tbody&gt;       &lt;tr&gt;           &lt;td&gt;1&lt;/td&gt;           &lt;td&gt;東証&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;3&lt;/td&gt;           &lt;td&gt;名証&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;5&lt;/td&gt;           &lt;td&gt;福証&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;6&lt;/td&gt;           &lt;td&gt;札証&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;2&lt;/td&gt;           &lt;td&gt;日通し&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;23&lt;/td&gt;           &lt;td&gt;日中&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;24&lt;/td&gt;           &lt;td&gt;夜間&lt;/td&gt;       &lt;/tr&gt;   &lt;/tbody&gt; &lt;/table&gt; |  [optional]
**exchangeName** | **String** | 市場名 |  [optional]
**securityType** | **Integer** | 銘柄種別&lt;br&gt;※先物・オプション銘柄の場合のみ |  [optional]
**executionDay** | **Integer** | 約定日（建玉日）&lt;br&gt;※信用・先物・オプションの場合のみ&lt;br&gt;※現物取引では、nullが返ります。 |  [optional]
**price** | **Double** | 値段 |  [optional]
**leavesQty** | **Double** | 残数量（保有数量） |  [optional]
**holdQty** | **Double** | 拘束数量（返済のために拘束されている数量） |  [optional]
**side** | **String** | 売買区分 &lt;table&gt;   &lt;thead&gt;       &lt;tr&gt;           &lt;th&gt;定義値&lt;/th&gt;           &lt;th&gt;説明&lt;/th&gt;       &lt;/tr&gt;   &lt;/thead&gt;   &lt;tbody&gt;       &lt;tr&gt;           &lt;td&gt;1&lt;/td&gt;           &lt;td&gt;売&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;2&lt;/td&gt;           &lt;td&gt;買&lt;/td&gt;       &lt;/tr&gt;   &lt;/tbody&gt; &lt;/table&gt; |  [optional]
**expenses** | **Double** | 諸経費&lt;br&gt;※信用・先物・オプションの場合のみ |  [optional]
**commission** | **Double** | 手数料&lt;br&gt;※信用・先物・オプションの場合のみ |  [optional]
**commissionTax** | **Double** | 手数料消費税&lt;br&gt;※信用・先物・オプションの場合のみ |  [optional]
**expireDay** | **Integer** | 返済期日&lt;br&gt;※信用・先物・オプションの場合のみ |  [optional]
**marginTradeType** | **Integer** | 信用取引区分&lt;br&gt;※信用の場合のみ &lt;table&gt;   &lt;thead&gt;       &lt;tr&gt;           &lt;th&gt;定義値&lt;/th&gt;           &lt;th&gt;説明&lt;/th&gt;       &lt;/tr&gt;   &lt;/thead&gt;   &lt;tbody&gt;       &lt;tr&gt;           &lt;td&gt;1&lt;/td&gt;           &lt;td&gt;制度信用&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;2&lt;/td&gt;           &lt;td&gt;一般信用（長期）&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;3&lt;/td&gt;           &lt;td&gt;一般信用（デイトレ）&lt;/td&gt;       &lt;/tr&gt;   &lt;/tbody&gt; &lt;/table&gt; |  [optional]
**currentPrice** | **Double** | 現在値&lt;br&gt;追加情報出力フラグ：falseの場合、null |  [optional]
**valuation** | **Double** | 評価金額&lt;br&gt;追加情報出力フラグ：falseの場合、null |  [optional]
**profitLoss** | **Double** | 評価損益額&lt;br&gt;追加情報出力フラグ：falseの場合、null |  [optional]
**profitLossRate** | **Double** | 評価損益率&lt;br&gt;追加情報出力フラグ：falseの場合、null |  [optional]
