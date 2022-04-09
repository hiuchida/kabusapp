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

package io.swagger.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.IOException;
/**
 * PrimaryExchangeResponse
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-04-09T08:07:59.802Z[GMT]")
public class PrimaryExchangeResponse {
  @SerializedName("Symbol")
  private String symbol = null;

  @SerializedName("PrimaryExchange")
  private Integer primaryExchange = null;

  public PrimaryExchangeResponse symbol(String symbol) {
    this.symbol = symbol;
    return this;
  }

   /**
   * 銘柄コード&lt;br&gt;※対象商品は、株式のみ
   * @return symbol
  **/
  @Schema(description = "銘柄コード<br>※対象商品は、株式のみ")
  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public PrimaryExchangeResponse primaryExchange(Integer primaryExchange) {
    this.primaryExchange = primaryExchange;
    return this;
  }

   /**
   * 優先市場 &lt;table&gt;   &lt;thead&gt;       &lt;tr&gt;           &lt;th&gt;定義値&lt;/th&gt;           &lt;th&gt;説明&lt;/th&gt;       &lt;/tr&gt;   &lt;/thead&gt;   &lt;tbody&gt;       &lt;tr&gt;           &lt;td&gt;1&lt;/td&gt;           &lt;td&gt;東証&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;3&lt;/td&gt;           &lt;td&gt;名証&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;5&lt;/td&gt;           &lt;td&gt;福証&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;6&lt;/td&gt;           &lt;td&gt;札証&lt;/td&gt;       &lt;/tr&gt;   &lt;/tbody&gt; &lt;/table&gt;
   * @return primaryExchange
  **/
  @Schema(description = "優先市場 <table>   <thead>       <tr>           <th>定義値</th>           <th>説明</th>       </tr>   </thead>   <tbody>       <tr>           <td>1</td>           <td>東証</td>       </tr>       <tr>           <td>3</td>           <td>名証</td>       </tr>       <tr>           <td>5</td>           <td>福証</td>       </tr>       <tr>           <td>6</td>           <td>札証</td>       </tr>   </tbody> </table>")
  public Integer getPrimaryExchange() {
    return primaryExchange;
  }

  public void setPrimaryExchange(Integer primaryExchange) {
    this.primaryExchange = primaryExchange;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PrimaryExchangeResponse primaryExchangeResponse = (PrimaryExchangeResponse) o;
    return Objects.equals(this.symbol, primaryExchangeResponse.symbol) &&
        Objects.equals(this.primaryExchange, primaryExchangeResponse.primaryExchange);
  }

  @Override
  public int hashCode() {
    return Objects.hash(symbol, primaryExchange);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PrimaryExchangeResponse {\n");
    
    sb.append("    symbol: ").append(toIndentedString(symbol)).append("\n");
    sb.append("    primaryExchange: ").append(toIndentedString(primaryExchange)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
