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
 * RequestCancelOrder
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-03-15T02:55:13.412Z[GMT]")
public class RequestCancelOrder {
  @SerializedName("OrderId")
  private String orderId = null;

  @SerializedName("Password")
  private String password = null;

  public RequestCancelOrder orderId(String orderId) {
    this.orderId = orderId;
    return this;
  }

   /**
   * 注文番号&lt;br&gt;sendorderのレスポンスで受け取るOrderID。
   * @return orderId
  **/
  @Schema(example = "20200529A01N06848002", required = true, description = "注文番号<br>sendorderのレスポンスで受け取るOrderID。")
  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public RequestCancelOrder password(String password) {
    this.password = password;
    return this;
  }

   /**
   * 注文パスワード
   * @return password
  **/
  @Schema(example = "xxxxxx", required = true, description = "注文パスワード")
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RequestCancelOrder requestCancelOrder = (RequestCancelOrder) o;
    return Objects.equals(this.orderId, requestCancelOrder.orderId) &&
        Objects.equals(this.password, requestCancelOrder.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, password);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RequestCancelOrder {\n");
    
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
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
