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
 * WalletFutureSuccess
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-03-15T02:55:13.412Z[GMT]")
public class WalletFutureSuccess {
  @SerializedName("FutureTradeLimit")
  private Double futureTradeLimit = null;

  @SerializedName("MarginRequirement")
  private Double marginRequirement = null;

  public WalletFutureSuccess futureTradeLimit(Double futureTradeLimit) {
    this.futureTradeLimit = futureTradeLimit;
    return this;
  }

   /**
   * 新規建玉可能額
   * @return futureTradeLimit
  **/
  @Schema(description = "新規建玉可能額")
  public Double getFutureTradeLimit() {
    return futureTradeLimit;
  }

  public void setFutureTradeLimit(Double futureTradeLimit) {
    this.futureTradeLimit = futureTradeLimit;
  }

  public WalletFutureSuccess marginRequirement(Double marginRequirement) {
    this.marginRequirement = marginRequirement;
    return this;
  }

   /**
   * 必要証拠金額&lt;br&gt;※銘柄指定の場合のみ。&lt;br&gt;※銘柄が指定されなかった場合、空を返す。
   * @return marginRequirement
  **/
  @Schema(description = "必要証拠金額<br>※銘柄指定の場合のみ。<br>※銘柄が指定されなかった場合、空を返す。")
  public Double getMarginRequirement() {
    return marginRequirement;
  }

  public void setMarginRequirement(Double marginRequirement) {
    this.marginRequirement = marginRequirement;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WalletFutureSuccess walletFutureSuccess = (WalletFutureSuccess) o;
    return Objects.equals(this.futureTradeLimit, walletFutureSuccess.futureTradeLimit) &&
        Objects.equals(this.marginRequirement, walletFutureSuccess.marginRequirement);
  }

  @Override
  public int hashCode() {
    return Objects.hash(futureTradeLimit, marginRequirement);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WalletFutureSuccess {\n");
    
    sb.append("    futureTradeLimit: ").append(toIndentedString(futureTradeLimit)).append("\n");
    sb.append("    marginRequirement: ").append(toIndentedString(marginRequirement)).append("\n");
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
