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

import io.swagger.client.ApiCallback;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.Configuration;
import io.swagger.client.Pair;
import io.swagger.client.ProgressRequestBody;
import io.swagger.client.ProgressResponseBody;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;


import io.swagger.client.model.ErrorResponse;
import io.swagger.client.model.RequestToken;
import io.swagger.client.model.TokenSuccess;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthApi {
    private ApiClient apiClient;

    public AuthApi() {
        this(Configuration.getDefaultApiClient());
    }

    public AuthApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for tokenPost
     * @param body  (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call tokenPostCall(RequestToken body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // create path and map variables
        String localVarPath = "/token";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }
    
    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call tokenPostValidateBeforeCall(RequestToken body, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling tokenPost(Async)");
        }
        
        com.squareup.okhttp.Call call = tokenPostCall(body, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * トークン発行
     * APIトークンを発行します。&lt;br&gt; 発行したトークンは有効である限り使用することができ、リクエストごとに発行する必要はありません。&lt;br&gt; 発行されたAPIトークンは以下のタイミングで無効となります。&lt;br&gt; ・kabuステーションを終了した時&lt;br&gt; ・kabuステーションからログアウトした時&lt;br&gt; ・別のトークンが新たに発行された時&lt;br&gt; ※kabuステーションは早朝、強制的にログアウトいたしますのでご留意ください。&lt;br&gt;
     * @param body  (required)
     * @return TokenSuccess
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public TokenSuccess tokenPost(RequestToken body) throws ApiException {
        ApiResponse<TokenSuccess> resp = tokenPostWithHttpInfo(body);
        return resp.getData();
    }

    /**
     * トークン発行
     * APIトークンを発行します。&lt;br&gt; 発行したトークンは有効である限り使用することができ、リクエストごとに発行する必要はありません。&lt;br&gt; 発行されたAPIトークンは以下のタイミングで無効となります。&lt;br&gt; ・kabuステーションを終了した時&lt;br&gt; ・kabuステーションからログアウトした時&lt;br&gt; ・別のトークンが新たに発行された時&lt;br&gt; ※kabuステーションは早朝、強制的にログアウトいたしますのでご留意ください。&lt;br&gt;
     * @param body  (required)
     * @return ApiResponse&lt;TokenSuccess&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<TokenSuccess> tokenPostWithHttpInfo(RequestToken body) throws ApiException {
        com.squareup.okhttp.Call call = tokenPostValidateBeforeCall(body, null, null);
        Type localVarReturnType = new TypeToken<TokenSuccess>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * トークン発行 (asynchronously)
     * APIトークンを発行します。&lt;br&gt; 発行したトークンは有効である限り使用することができ、リクエストごとに発行する必要はありません。&lt;br&gt; 発行されたAPIトークンは以下のタイミングで無効となります。&lt;br&gt; ・kabuステーションを終了した時&lt;br&gt; ・kabuステーションからログアウトした時&lt;br&gt; ・別のトークンが新たに発行された時&lt;br&gt; ※kabuステーションは早朝、強制的にログアウトいたしますのでご留意ください。&lt;br&gt;
     * @param body  (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call tokenPostAsync(RequestToken body, final ApiCallback<TokenSuccess> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = tokenPostValidateBeforeCall(body, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<TokenSuccess>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
