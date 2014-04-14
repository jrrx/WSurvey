package com.voyage.http;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voyage.util.Consts;

public class HttpTest {

	public static void main(String[] args) throws ClientProtocolException,
			IOException {

		CloseableHttpClient httpclient = HttpClients.createMinimal();
		httpclient = HttpClients.createDefault();

		try {
			// http://192.168.1.103:8080/Voyage/survey
			HttpPost hpost = new HttpPost("http://localhost:8080/Voyage/survey");
			HttpEntity entity = new StringEntity("<xml><a>dd</a></xml>",
					"UTF-8");
			hpost.setEntity(entity);
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

			String response = httpclient.execute(hpost, responseHandler);
			System.out.println(response);

			ObjectMapper jsonMapper = new ObjectMapper();

			@SuppressWarnings("unchecked")
			Map<String, Object> res = jsonMapper.readValue(response, Map.class);
			System.out.println("access_token:" + res.get("access_token"));
			System.out.println("expires_in:" + res.get("expires_in"));

		} finally {
			httpclient.close();
		}

	}

	public void fetchAccessToken() throws ClientProtocolException, IOException {
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
		url = String.format(url, Consts.AppId, Consts.AppSecret);

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

			String response = httpclient.execute(httpGet, responseHandler);
			System.out.println(response);
		} finally {
			httpclient.close();
		}

	}

}
