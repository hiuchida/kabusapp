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
 * UnregisterAllSuccess
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-04-09T08:07:59.802Z[GMT]")
public class UnregisterAllSuccess {
  @SerializedName("RegistList")
  private Object registList = null;

  public UnregisterAllSuccess registList(Object registList) {
    this.registList = registList;
    return this;
  }

   /**
   * 現在登録されている銘柄のリスト&lt;br&gt;※銘柄登録解除が正常に行われれば、空リストを返します。&lt;br&gt;　登録解除でエラー等が発生した場合、現在登録されている銘柄のリストを返します
   * @return registList
  **/
  @Schema(example = "[]", description = "現在登録されている銘柄のリスト<br>※銘柄登録解除が正常に行われれば、空リストを返します。<br>　登録解除でエラー等が発生した場合、現在登録されている銘柄のリストを返します")
  public Object getRegistList() {
    return registList;
  }

  public void setRegistList(Object registList) {
    this.registList = registList;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UnregisterAllSuccess unregisterAllSuccess = (UnregisterAllSuccess) o;
    return Objects.equals(this.registList, unregisterAllSuccess.registList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(registList);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UnregisterAllSuccess {\n");
    
    sb.append("    registList: ").append(toIndentedString(registList)).append("\n");
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
