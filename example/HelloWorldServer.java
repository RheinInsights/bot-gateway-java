// Copyright (c) 2025-2026 RheinInsights GmbH.
// Licensed under the MIT License. See LICENSE in the project root.

package com.rheininsights.bot.example;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletionException;

import com.microsoft.bot.builder.Bot;
import com.microsoft.bot.builder.InvokeResponse;
import com.microsoft.bot.connector.authentication.AuthenticationException;
import com.microsoft.bot.integration.AzureBotConfiguration;
import com.microsoft.bot.integration.BotFrameworkHttpAdapter;
import com.microsoft.bot.schema.Activity;
import com.microsoft.bot.schema.Serialization;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

/**
 * Hosts {@link HelloWorldBot} on http://localhost:3978/api/messages using the
 * JDK's built-in HTTP server.
 *
 * The bot credentials are read from the environment variables
 * MICROSOFT_APP_ID, MICROSOFT_APP_PASSWORD and MICROSOFT_APP_TENANT_ID.
 */
public final class HelloWorldServer {

  private HelloWorldServer() {
  }

  public static void main(String[] args) throws IOException {
    AzureBotConfiguration config = new AzureBotConfiguration();
    config.botAppId = System.getenv("MICROSOFT_APP_ID");
    config.appPassword = System.getenv("MICROSOFT_APP_PASSWORD");
    config.entraTenantId = System.getenv("MICROSOFT_APP_TENANT_ID");

    BotFrameworkHttpAdapter adapter = new BotFrameworkHttpAdapter(config);
    Bot bot = new HelloWorldBot();

    HttpServer server = HttpServer.create(new InetSocketAddress(3978), 0);
    server.createContext("/api/messages", exchange -> handle(exchange, adapter, bot));
    server.start();
    System.out.println("Bot listening on http://localhost:3978/api/messages");
  }

  private static void handle(HttpExchange exchange, BotFrameworkHttpAdapter adapter, Bot bot)
      throws IOException {
    try (exchange) {
      if (!"POST".equals(exchange.getRequestMethod())) {
        exchange.sendResponseHeaders(405, -1);
        return;
      }

      String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
      Activity activity = Serialization.getAs(Serialization.jsonToTree(body), Activity.class);
      String authHeader = exchange.getRequestHeaders().getFirst("Authorization");

      try {
        InvokeResponse invokeResponse =
            adapter.processActivity(authHeader, activity, bot::onTurn).join();
        if (invokeResponse == null || invokeResponse.getBody() == null) {
          int status = invokeResponse == null ? 202 : invokeResponse.getStatus();
          exchange.sendResponseHeaders(status, -1);
          return;
        }
        byte[] out = Serialization.toString(invokeResponse.getBody())
            .getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(invokeResponse.getStatus(), out.length);
        try (OutputStream os = exchange.getResponseBody()) {
          os.write(out);
        }
      } catch (CompletionException e) {
        int status = e.getCause() instanceof AuthenticationException ? 401 : 500;
        exchange.sendResponseHeaders(status, -1);
      }
    }
  }
}
