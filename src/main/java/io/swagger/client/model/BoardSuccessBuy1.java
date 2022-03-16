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
import org.threeten.bp.OffsetDateTime;
/**
 * 買気配数量1本目
 */
@Schema(description = "買気配数量1本目")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-03-15T02:55:13.412Z[GMT]")
public class BoardSuccessBuy1 {
  @SerializedName("Time")
  private OffsetDateTime time = null;

  @SerializedName("Sign")
  private String sign = null;

  @SerializedName("Price")
  private Double price = null;

  @SerializedName("Qty")
  private Double qty = null;

  public BoardSuccessBuy1 time(OffsetDateTime time) {
    this.time = time;
    return this;
  }

   /**
   * 時刻&lt;br&gt;※株式銘柄の場合のみ
   * @return time
  **/
  @Schema(description = "時刻<br>※株式銘柄の場合のみ")
  public OffsetDateTime getTime() {
    return time;
  }

  public void setTime(OffsetDateTime time) {
    this.time = time;
  }

  public BoardSuccessBuy1 sign(String sign) {
    this.sign = sign;
    return this;
  }

   /**
   * 気配フラグ&lt;br&gt;※株式・先物・オプション銘柄の場合のみ &lt;table&gt;   &lt;thead&gt;       &lt;tr&gt;           &lt;th&gt;定義値&lt;/th&gt;           &lt;th&gt;説明&lt;/th&gt;       &lt;/tr&gt;   &lt;/thead&gt;   &lt;tbody&gt;       &lt;tr&gt;           &lt;td&gt;0000&lt;/td&gt;           &lt;td&gt;事象なし&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;0101&lt;/td&gt;           &lt;td&gt;一般気配&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;0102&lt;/td&gt;           &lt;td&gt;特別気配&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;0103&lt;/td&gt;           &lt;td&gt;注意気配&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;0107&lt;/td&gt;           &lt;td&gt;寄前気配&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;0108&lt;/td&gt;           &lt;td&gt;停止前特別気配&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;0109&lt;/td&gt;           &lt;td&gt;引け後気配&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;0116&lt;/td&gt;           &lt;td&gt;寄前気配約定成立ポイントなし&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;0117&lt;/td&gt;           &lt;td&gt;寄前気配約定成立ポイントあり&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;0118&lt;/td&gt;           &lt;td&gt;連続約定気配&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;0119&lt;/td&gt;           &lt;td&gt;停止前の連続約定気配&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;0120&lt;/td&gt;           &lt;td&gt;買い上がり売り下がり中&lt;/td&gt;       &lt;/tr&gt;   &lt;/tbody&gt; &lt;/table&gt;
   * @return sign
  **/
  @Schema(description = "気配フラグ<br>※株式・先物・オプション銘柄の場合のみ <table>   <thead>       <tr>           <th>定義値</th>           <th>説明</th>       </tr>   </thead>   <tbody>       <tr>           <td>0000</td>           <td>事象なし</td>       </tr>       <tr>           <td>0101</td>           <td>一般気配</td>       </tr>       <tr>           <td>0102</td>           <td>特別気配</td>       </tr>       <tr>           <td>0103</td>           <td>注意気配</td>       </tr>       <tr>           <td>0107</td>           <td>寄前気配</td>       </tr>       <tr>           <td>0108</td>           <td>停止前特別気配</td>       </tr>       <tr>           <td>0109</td>           <td>引け後気配</td>       </tr>       <tr>           <td>0116</td>           <td>寄前気配約定成立ポイントなし</td>       </tr>       <tr>           <td>0117</td>           <td>寄前気配約定成立ポイントあり</td>       </tr>       <tr>           <td>0118</td>           <td>連続約定気配</td>       </tr>       <tr>           <td>0119</td>           <td>停止前の連続約定気配</td>       </tr>       <tr>           <td>0120</td>           <td>買い上がり売り下がり中</td>       </tr>   </tbody> </table>")
  public String getSign() {
    return sign;
  }

  public void setSign(String sign) {
    this.sign = sign;
  }

  public BoardSuccessBuy1 price(Double price) {
    this.price = price;
    return this;
  }

   /**
   * 値段&lt;br&gt;※株式・先物・オプション銘柄の場合のみ
   * @return price
  **/
  @Schema(description = "値段<br>※株式・先物・オプション銘柄の場合のみ")
  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public BoardSuccessBuy1 qty(Double qty) {
    this.qty = qty;
    return this;
  }

   /**
   * 数量&lt;br&gt;※株式・先物・オプション銘柄の場合のみ
   * @return qty
  **/
  @Schema(description = "数量<br>※株式・先物・オプション銘柄の場合のみ")
  public Double getQty() {
    return qty;
  }

  public void setQty(Double qty) {
    this.qty = qty;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BoardSuccessBuy1 boardSuccessBuy1 = (BoardSuccessBuy1) o;
    return Objects.equals(this.time, boardSuccessBuy1.time) &&
        Objects.equals(this.sign, boardSuccessBuy1.sign) &&
        Objects.equals(this.price, boardSuccessBuy1.price) &&
        Objects.equals(this.qty, boardSuccessBuy1.qty);
  }

  @Override
  public int hashCode() {
    return Objects.hash(time, sign, price, qty);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BoardSuccessBuy1 {\n");
    
    sb.append("    time: ").append(toIndentedString(time)).append("\n");
    sb.append("    sign: ").append(toIndentedString(sign)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
    sb.append("    qty: ").append(toIndentedString(qty)).append("\n");
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
