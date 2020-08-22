package com.piggest.minecraft.bukkit.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class Http_get {
	public static String get_content(String url) throws IOException, InterruptedException, URISyntaxException {
		HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(1)).build();
		URL url_obj = new URL(url);
		URI uri = url_obj.toURI();

		HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		return response.body();
	}
}
