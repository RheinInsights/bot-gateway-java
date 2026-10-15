// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.microsoft.bot.integration;

import java.util.HashSet;
import java.util.Set;

/**
 * Provides read-only access to configuration properties.
 */
public class AzureBotConfiguration {

  public String channelService;
  public String botAppId;
  public String appPassword;
  public String botOpenIdMetadata;
  public String entraTenantId;
  public String connectionName;
  public long created;
  
  
  
  public AzureBotConfiguration() {
    created = System.currentTimeMillis();
  }


  public Set<String> getValidIssuers() {
    Set<String> issuers = new HashSet<>();
    issuers.add("https://login.microsoftonline.com/"+entraTenantId+"/");
    issuers.add("https://sts.windows.net/"+entraTenantId+"/");
    return issuers;
  }
   
}
