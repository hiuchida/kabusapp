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
 * RankingByCategoryResponseRanking
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-03-15T02:55:13.412Z[GMT]")
public class RankingByCategoryResponseRanking {
  @SerializedName("No")
  private Integer no = null;

  @SerializedName("Trend")
  private String trend = null;

  @SerializedName("AverageRanking")
  private Double averageRanking = null;

  @SerializedName("Category")
  private String category = null;

  @SerializedName("CategoryName")
  private String categoryName = null;

  @SerializedName("CurrentPrice")
  private Double currentPrice = null;

  @SerializedName("ChangeRatio")
  private Double changeRatio = null;

  @SerializedName("CurrentPriceTime")
  private String currentPriceTime = null;

  @SerializedName("ChangePercentage")
  private Double changePercentage = null;

  public RankingByCategoryResponseRanking no(Integer no) {
    this.no = no;
    return this;
  }

   /**
   * 順位&lt;br&gt;※ランキング内で同じ順位が返却される場合があります（10位が2件など）
   * @return no
  **/
  @Schema(description = "順位<br>※ランキング内で同じ順位が返却される場合があります（10位が2件など）")
  public Integer getNo() {
    return no;
  }

  public void setNo(Integer no) {
    this.no = no;
  }

  public RankingByCategoryResponseRanking trend(String trend) {
    this.trend = trend;
    return this;
  }

   /**
   * トレンド &lt;table&gt;   &lt;thead&gt;       &lt;tr&gt;           &lt;th&gt;定義値&lt;/th&gt;           &lt;th&gt;内容&lt;/th&gt;       &lt;/tr&gt;   &lt;/thead&gt;   &lt;tbody&gt;       &lt;tr&gt;           &lt;td&gt;0&lt;/td&gt;           &lt;td&gt;対象データ無し&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;1&lt;/td&gt;           &lt;td&gt;過去10営業日より20位以上上昇&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;2&lt;/td&gt;           &lt;td&gt;過去10営業日より1～19位上昇&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;3&lt;/td&gt;           &lt;td&gt;過去10営業日と変わらず&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;4&lt;/td&gt;           &lt;td&gt;過去10営業日より1～19位下落&lt;/td&gt;       &lt;/tr&gt;       &lt;tr&gt;           &lt;td&gt;5&lt;/td&gt;           &lt;td&gt;過去10営業日より20位以上下落&lt;/td&gt;       &lt;/tr&gt;   &lt;/tbody&gt; &lt;/table&gt;
   * @return trend
  **/
  @Schema(description = "トレンド <table>   <thead>       <tr>           <th>定義値</th>           <th>内容</th>       </tr>   </thead>   <tbody>       <tr>           <td>0</td>           <td>対象データ無し</td>       </tr>       <tr>           <td>1</td>           <td>過去10営業日より20位以上上昇</td>       </tr>       <tr>           <td>2</td>           <td>過去10営業日より1～19位上昇</td>       </tr>       <tr>           <td>3</td>           <td>過去10営業日と変わらず</td>       </tr>       <tr>           <td>4</td>           <td>過去10営業日より1～19位下落</td>       </tr>       <tr>           <td>5</td>           <td>過去10営業日より20位以上下落</td>       </tr>   </tbody> </table>")
  public String getTrend() {
    return trend;
  }

  public void setTrend(String trend) {
    this.trend = trend;
  }

  public RankingByCategoryResponseRanking averageRanking(Double averageRanking) {
    this.averageRanking = averageRanking;
    return this;
  }

   /**
   * 平均順位&lt;br&gt;※100位以下は「999」となります
   * @return averageRanking
  **/
  @Schema(description = "平均順位<br>※100位以下は「999」となります")
  public Double getAverageRanking() {
    return averageRanking;
  }

  public void setAverageRanking(Double averageRanking) {
    this.averageRanking = averageRanking;
  }

  public RankingByCategoryResponseRanking category(String category) {
    this.category = category;
    return this;
  }

   /**
   * 業種コード
   * @return category
  **/
  @Schema(description = "業種コード")
  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public RankingByCategoryResponseRanking categoryName(String categoryName) {
    this.categoryName = categoryName;
    return this;
  }

   /**
   * 業種名
   * @return categoryName
  **/
  @Schema(description = "業種名")
  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public RankingByCategoryResponseRanking currentPrice(Double currentPrice) {
    this.currentPrice = currentPrice;
    return this;
  }

   /**
   * 現在値
   * @return currentPrice
  **/
  @Schema(description = "現在値")
  public Double getCurrentPrice() {
    return currentPrice;
  }

  public void setCurrentPrice(Double currentPrice) {
    this.currentPrice = currentPrice;
  }

  public RankingByCategoryResponseRanking changeRatio(Double changeRatio) {
    this.changeRatio = changeRatio;
    return this;
  }

   /**
   * 前日比
   * @return changeRatio
  **/
  @Schema(description = "前日比")
  public Double getChangeRatio() {
    return changeRatio;
  }

  public void setChangeRatio(Double changeRatio) {
    this.changeRatio = changeRatio;
  }

  public RankingByCategoryResponseRanking currentPriceTime(String currentPriceTime) {
    this.currentPriceTime = currentPriceTime;
    return this;
  }

   /**
   * 時刻&lt;br&gt;HH:mm&lt;br&gt;※日付は返しません
   * @return currentPriceTime
  **/
  @Schema(description = "時刻<br>HH:mm<br>※日付は返しません")
  public String getCurrentPriceTime() {
    return currentPriceTime;
  }

  public void setCurrentPriceTime(String currentPriceTime) {
    this.currentPriceTime = currentPriceTime;
  }

  public RankingByCategoryResponseRanking changePercentage(Double changePercentage) {
    this.changePercentage = changePercentage;
    return this;
  }

   /**
   * 騰落率（%）
   * @return changePercentage
  **/
  @Schema(description = "騰落率（%）")
  public Double getChangePercentage() {
    return changePercentage;
  }

  public void setChangePercentage(Double changePercentage) {
    this.changePercentage = changePercentage;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RankingByCategoryResponseRanking rankingByCategoryResponseRanking = (RankingByCategoryResponseRanking) o;
    return Objects.equals(this.no, rankingByCategoryResponseRanking.no) &&
        Objects.equals(this.trend, rankingByCategoryResponseRanking.trend) &&
        Objects.equals(this.averageRanking, rankingByCategoryResponseRanking.averageRanking) &&
        Objects.equals(this.category, rankingByCategoryResponseRanking.category) &&
        Objects.equals(this.categoryName, rankingByCategoryResponseRanking.categoryName) &&
        Objects.equals(this.currentPrice, rankingByCategoryResponseRanking.currentPrice) &&
        Objects.equals(this.changeRatio, rankingByCategoryResponseRanking.changeRatio) &&
        Objects.equals(this.currentPriceTime, rankingByCategoryResponseRanking.currentPriceTime) &&
        Objects.equals(this.changePercentage, rankingByCategoryResponseRanking.changePercentage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(no, trend, averageRanking, category, categoryName, currentPrice, changeRatio, currentPriceTime, changePercentage);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RankingByCategoryResponseRanking {\n");
    
    sb.append("    no: ").append(toIndentedString(no)).append("\n");
    sb.append("    trend: ").append(toIndentedString(trend)).append("\n");
    sb.append("    averageRanking: ").append(toIndentedString(averageRanking)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    categoryName: ").append(toIndentedString(categoryName)).append("\n");
    sb.append("    currentPrice: ").append(toIndentedString(currentPrice)).append("\n");
    sb.append("    changeRatio: ").append(toIndentedString(changeRatio)).append("\n");
    sb.append("    currentPriceTime: ").append(toIndentedString(currentPriceTime)).append("\n");
    sb.append("    changePercentage: ").append(toIndentedString(changePercentage)).append("\n");
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
