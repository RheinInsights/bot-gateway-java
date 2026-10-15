// Copyright (c) Microsoft Corporation. All rights reserved.
// Modifications Copyright (c) 2025-2026 RheinInsights GmbH.
// Licensed under the MIT License.

package com.rheininsights.bot;

import java.util.concurrent.CompletableFuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import com.microsoft.bot.builder.ConversationState;
import com.microsoft.bot.builder.TurnContext;
import com.microsoft.bot.builder.UserState;
import com.microsoft.bot.dialogs.Dialog;
import com.microsoft.bot.schema.ChannelAccount;



// This bot is derived (view DialogBot<T>) from the TeamsActivityHandler class
// currently included as part of this sample.
public class TeamsBot<T extends Dialog> extends DialogBot<T> {
  private static Logger log = LogManager.getLogger();

    public TeamsBot(ConversationState conversationState, UserState userState, T dialog) {
        super(conversationState, userState, dialog);
    }

    @Override
    protected CompletableFuture<Void> onMembersAdded(
        List<ChannelAccount> membersAdded, TurnContext turnContext) {
      
      
      return new CompletableFuture<Void>();
        /*return turnContext.getActivity().getMembersAdded().stream()
            .filter(member -> !StringUtils
                .equals(member.getId(), turnContext.getActivity().getRecipient().getId()))
            .map(channel -> {
                Activity reply = MessageFactory.text("Welcome to AuthenticationBot."
                    + " Type anything to get logged in. Type 'logout' to sign-out.");

                return turnContext.sendActivity(reply);
            })
            .collect(CompletableFutures.toFutureList())
            .thenApply(resourceResponse -> null);
            */
    }

    @Override
    protected CompletableFuture<Void> onTeamsSigninVerifyState(TurnContext turnContext) {
        log.info("Running dialog with signin/verifystate from an Invoke Activity.");

        // The OAuth Prompt needs to see the Invoke Activity in order to complete the login process.

        // Run the Dialog with the new Token Response Event Activity.
         return Dialog.run(dialog, turnContext, conversationState.createProperty("DialogState"));
    }

}
