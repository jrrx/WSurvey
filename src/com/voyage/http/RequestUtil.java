package com.voyage.http;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voyage.meta.TokenResult;
import com.voyage.util.Consts;
import com.voyage.util.LogUtil;

/**
 * 将要废弃，用WxService代替，如果后续测试没问题，直接删除
 * 
 * @author Houyangyang
 */
@Deprecated
public class RequestUtil {

	/**
	 * @return 微信的access_token
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static TokenResult fetchAccessToken() {
		TokenResult tr = new TokenResult();
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
		url = String.format(url, Consts.AppId, Consts.AppSecret);
		try {
			String responseStr = weixinGet(url);
			if (responseStr != null) {
				ObjectMapper jsonMapper = new ObjectMapper();
				@SuppressWarnings("unchecked")
				Map<String, Object> res = jsonMapper.readValue(responseStr,
						Map.class);
				// {"access_token":"ACCESS_TOKEN","expires_in":7200}
				Object re = res.get("access_token");
				if (re != null) {
					tr.setAccess_token((String) re);
					tr.setExpires_in((Integer) res.get("expires_in"));
				} else {
					// like {"errcode":40013,"errmsg":"invalid appid"}
					re = res.get("errcode");
					tr.setErrcode((Integer) re);
					tr.setErrmsg((String) res.get("errmsg"));
					LogUtil.GlobalLog.error(String.format(
							"Get access_token error, errorcode:%d, errmsg:%s",
							tr.getErrcode(), tr.getErrmsg()));
				}
			}
		} catch (Throwable t) {
			LogUtil.GlobalLog.error("Error occured when get access_token:\n", t);
		}
		return tr;
	}

	private static String weixinGet(String url) throws ClientProtocolException,
			IOException {
		String responseStr = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpGet = new HttpGet(url);
			// Create a custom response handler
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
				public String handleResponse(final HttpResponse response)
						throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity)
								: null;
					} else {
						throw new ClientProtocolException(
								"Unexpected response status: " + status);
					}
				}
			};
			responseStr = httpclient.execute(httpGet, responseHandler);
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return responseStr;
	}

}
