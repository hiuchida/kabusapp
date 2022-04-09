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
import io.swagger.client.model.RankingByTradeVolumeResponseRanking;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * RankingByTradeVolumeResponse
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-04-09T08:07:59.802Z[GMT]")
public class RankingByTradeVolumeResponse implements OneOfinlineResponse200 {
  @SerializedName("Type")
  private String type = null;

  @SerializedName("ExchangeDivision")
  private String exchangeDivision = null;

  @SerializedName("Ranking")
  private List<RankingByTradeVolumeResponseRanking> ranking = null;

  public RankingByTradeVolumeResponse type(String type) {
    this.type = type;
    return this;
  }

   /**
   * 種別
   * @return type
  **/
  @Schema(description = "種別")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public RankingByTradeVolumeResponse exchangeDivision(String exchangeDivision) {
    this.exchangeDivision = exchangeDivision;
    return this;
  }

   /**
   * 市場
   * @return exchangeDivision
  **/
  @Schema(description = "市場")
  public String getExchangeDivision() {
    return exchangeDivision;
  }

  public void setExchangeDivision(String exchangeDivision) {
    this.exchangeDivision = exchangeDivision;
  }

  public RankingByTradeVolumeResponse ranking(List<RankingByTradeVolumeResponseRanking> ranking) {
    this.ranking = ranking;
    return this;
  }

  public RankingByTradeVolumeResponse addRankingItem(RankingByTradeVolumeResponseRanking rankingItem) {
    if (this.ranking == null) {
      this.ranking = new ArrayList<RankingByTradeVolumeResponseRanking>();
    }
    this.ranking.add(rankingItem);
    return this;
  }

   /**
   * ランキング
   * @return ranking
  **/
  @Schema(description = "ランキング")
  public List<RankingByTradeVolumeResponseRanking> getRanking() {
    return ranking;
  }

  public void setRanking(List<RankingByTradeVolumeResponseRanking> ranking) {
    this.ranking = ranking;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RankingByTradeVolumeResponse rankingByTradeVolumeResponse = (RankingByTradeVolumeResponse) o;
    return Objects.equals(this.type, rankingByTradeVolumeResponse.type) &&
        Objects.equals(this.exchangeDivision, rankingByTradeVolumeResponse.exchangeDivision) &&
        Objects.equals(this.ranking, rankingByTradeVolumeResponse.ranking);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, exchangeDivision, ranking);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RankingByTradeVolumeResponse {\n");
    
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    exchangeDivision: ").append(toIndentedString(exchangeDivision)).append("\n");
    sb.append("    ranking: ").append(toIndentedString(ranking)).append("\n");
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
