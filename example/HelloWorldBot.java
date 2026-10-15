// Copyright (c) 2025-2026 RheinInsights GmbH.
// Licensed under the MIT License. See LICENSE in the project root.

package com.rheininsights.bot.example;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.microsoft.bot.builder.ActivityHandler;
import com.microsoft.bot.builder.MessageFactory;
import com.microsoft.bot.builder.TurnContext;
import com.microsoft.bot.schema.ChannelAccount;

/**
 * Minimal bot that greets new members and answers every message.
 */
public class HelloWorldBot extends ActivityHandler {

  @Override
  protected CompletableFuture<Void> onMessageActivity(TurnContext turnContext) {
    String text = turnContext.getActivity().getText();
    return turnContext
        .sendActivity(MessageFactory.text("Hello World! You said: " + text))
        .thenApply(response -> null);
  }

  @Override
  protected CompletableFuture<Void> onMembersAdded(
      List<ChannelAccount> membersAdded,
      TurnContext turnContext
  ) {
    String botId = turnContext.getActivity().getRecipient().getId();
    CompletableFuture<Void> result = CompletableFuture.completedFuture(null);
    for (ChannelAccount member : membersAdded) {
      if (!member.getId().equals(botId)) {
        result = result.thenCompose(v -> turnContext
            .sendActivity(MessageFactory.text("Hello World!"))
            .thenApply(response -> null));
      }
    }
    return result;
  }
}
