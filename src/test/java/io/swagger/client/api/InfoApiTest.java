/*
 * kabuステーションAPI
 * # 定義情報   REST APIのコード一覧、エンドポイントは下記リンク参照     - [REST APIコード一覧](../ptal/error.html)
 *
 * OpenAPI spec version: 1.5
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package io.swagger.client.api;

import java.util.List;

import org.junit.Test;

import io.swagger.client.model.ApiSoftLimitResponse;
import io.swagger.client.model.BoardSuccess;
import io.swagger.client.model.ExchangeResponse;
import io.swagger.client.model.InlineResponse200;
import io.swagger.client.model.MarginPremiumResponse;
import io.swagger.client.model.OrdersSuccess;
import io.swagger.client.model.PositionsSuccess;
import io.swagger.client.model.PrimaryExchangeResponse;
import io.swagger.client.model.RegulationsResponse;
import io.swagger.client.model.SymbolNameSuccess;
import io.swagger.client.model.SymbolSuccess;
import v2.AuthorizedToken;


/**
 * API tests for InfoApi
 */
// @Ignore
public class InfoApiTest {

    private final InfoApi api = new InfoApi();

    /**
     * ソフトリミット
     *
     * kabuステーションAPIのソフトリミット値を取得する
     *
     * @throws Exception
     *          if the Api call fails
     */
    @Test
    public void apisoftlimitGetTest() throws Exception {
        String X_API_KEY = AuthorizedToken.getToken();
        ApiSoftLimitResponse response = api.apisoftlimitGet(X_API_KEY);

        // TODO: test validations
        System.out.println(response);        
    }
    /**
     * 時価情報・板情報
     *
     * 指定した銘柄の時価情報・板情報を取得します&lt;br&gt; レスポンスの一部にnullが発生した場合、該当銘柄を銘柄登録をしてから、 &lt;br&gt;再度時価情報・板情報APIを実行してください。
     *
     * @throws Exception
     *          if the Api call fails
     */
    @Test
    public void boardGetTest() throws Exception {
        String X_API_KEY = AuthorizedToken.getToken();
        String symbol = "9433@1"; // ＫＤＤＩ
        BoardSuccess response = api.boardGet(X_API_KEY, symbol);

        // TODO: test validations
        System.out.println(response);        
    }
    /**
     * 為替情報
     *
     * マネービューの情報を取得する
     *
     * @throws Exception
     *          if the Api call fails
     */
    @Test
    public void exchangeGetTest() throws Exception {
        String X_API_KEY = AuthorizedToken.getToken();
        String symbol = "usdjpy";
        ExchangeResponse response = api.exchangeGet(X_API_KEY, symbol);

        // TODO: test validations
        System.out.println(response);        
    }
    /**
     * プレミアム料取得
     *
     * 指定した銘柄のプレミアム料を取得するAPI
     *
     * @throws Exception
     *          if the Api call fails
     */
    @Test
    public void marginpremiumGetTest() throws Exception {
        String X_API_KEY = AuthorizedToken.getToken();
        String symbol = "9433"; // ＫＤＤＩ
        MarginPremiumResponse response = api.marginpremiumGet(X_API_KEY, symbol);

        // TODO: test validations
        System.out.println(response);        
    }
    /**
     * 注文約定照会
     *
     * 注文一覧を取得します。&lt;br&gt; ※下記Queryパラメータは任意設定となります。
     *
     * @throws Exception
     *          if the Api call fails
     */
    @Test
    public void ordersGetTest() throws Exception {
        String X_API_KEY = AuthorizedToken.getToken();
        String product = null;
        String id = null;
        String updtime = null;
        String details = null;
        String symbol = null;
        String state = null;
        String side = null;
        String cashmargin = null;
        List<OrdersSuccess> response = api.ordersGet(X_API_KEY, product, id, updtime, details, symbol, state, side, cashmargin);

        // TODO: test validations
//        System.out.println(response);
        System.out.println("List<OrdersSuccess>.size=" + response.size());
        for (int i = 0; i < response.size(); i++) {
        	OrdersSuccess order = response.get(i);
        	if (order.getState() == 5) {
        		continue;
        	}
        	System.out.println((i + 1) + ": " + order);
        }
    }
    /**
     * 残高照会
     *
     * 残高一覧を取得します。&lt;br&gt;※下記Queryパラメータは任意設定となります。
     *
     * @throws Exception
     *          if the Api call fails
     */
    @Test
    public void positionsGetTest() throws Exception {
        String X_API_KEY = AuthorizedToken.getToken();
        String product = "3";
        String symbol = null;
        String side = null;
        String addinfo = null;
        List<PositionsSuccess> response = api.positionsGet(X_API_KEY, product, symbol, side, addinfo);

        // TODO: test validations
        System.out.println(response);        
    }
    /**
     * 優先市場
     *
     * 株式の優先市場を取得する
     *
     * @throws Exception
     *          if the Api call fails
     */
    @Test
    public void primaryExchangeGetTest() throws Exception {
        String X_API_KEY = AuthorizedToken.getToken();
        String symbol = "9433"; // ＫＤＤＩ
        PrimaryExchangeResponse response = api.primaryExchangeGet(X_API_KEY, symbol);

        // TODO: test validations
        System.out.println(response);        
    }
    /**
     * 詳細ランキング
     *
     * 詳細ランキング画面と同様の各種ランキングを返します。 &lt;br&gt;ランキングの対象日はkabuステーションが保持している当日のデータとなります。 &lt;br&gt;※株価情報ランキング、業種別指数ランキングは、下記の時間帯でデータがクリアされるため、 &lt;br&gt;その間の詳細ランキングAPIは空レスポンスとなります。 &lt;br&gt;データクリア：平日7:53頃-9:00過ぎ頃 &lt;br&gt;※信用情報ランキングは毎週第３営業日の7:55頃にデータが更新されます。
     *
     * @throws Exception
     *          if the Api call fails
     */
    @Test
    public void rankingGetTest() throws Exception {
        String X_API_KEY = AuthorizedToken.getToken();
        String type = "1"; // 値上がり率
        String exchangeDivision = "ALL"; // 全市場
        InlineResponse200 response = api.rankingGet(X_API_KEY, type, exchangeDivision);

        // TODO: test validations
        System.out.println(response);        
    }
    /**
     * 規制情報
     *
     * 規制情報＋空売り規制情報を取得する
     *
     * @throws Exception
     *          if the Api call fails
     */
    @Test
    public void regulationsGetTest() throws Exception {
        String X_API_KEY = AuthorizedToken.getToken();
        String symbol = "9433@1"; // ＫＤＤＩ
        RegulationsResponse response = api.regulationsGet(X_API_KEY, symbol);

        // TODO: test validations
        System.out.println(response);        
    }
    /**
     * 銘柄情報
     *
     * 指定した銘柄情報を取得します
     *
     * @throws Exception
     *          if the Api call fails
     */
    @Test
    public void symbolGetTest() throws Exception {
        String X_API_KEY = AuthorizedToken.getToken();
        String symbol = "9433@1"; // ＫＤＤＩ
        String addinfo = null;
        SymbolSuccess response = api.symbolGet(X_API_KEY, symbol, addinfo);

        // TODO: test validations
        System.out.println(response);        
    }
    /**
     * 先物銘柄コード取得
     *
     * 先物銘柄コード取得
     *
     * @throws Exception
     *          if the Api call fails
     */
    @Test
    public void symbolnameFutureGetTest() throws Exception {
        String X_API_KEY = AuthorizedToken.getToken();
        Integer derivMonth = 202206;
        String futureCode = "NK225mini";
        SymbolNameSuccess response = api.symbolnameFutureGet(X_API_KEY, derivMonth, futureCode);

        // TODO: test validations
        System.out.println(response);        
    }
    /**
     * オプション銘柄コード取得
     *
     * オプション銘柄コード取得
     *
     * @throws Exception
     *          if the Api call fails
     */
    @Test
    public void symbolnameOptionGetTest() throws Exception {
        String X_API_KEY = AuthorizedToken.getToken();
        Integer derivMonth = 202204;
        String putOrCall = "C";
        Integer strikePrice = 26000;
        SymbolNameSuccess response = api.symbolnameOptionGet(X_API_KEY, derivMonth, putOrCall, strikePrice);

        // TODO: test validations
        System.out.println(response);        
    }
}
