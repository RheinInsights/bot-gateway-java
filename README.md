# RheinInsights Bot Gateway for Java

A Java library for building bots for Microsoft Teams and other Azure Bot
Service channels. Forked from Microsoft's Bot Framework SDK for Java
([microsoft/botbuilder-java](https://github.com/microsoft/botbuilder-java)),
which Microsoft no longer maintains.

> **Not affiliated with Microsoft.** Microsoft doesn't endorse, support or
> maintain this project. "Microsoft", "Microsoft Teams", "Azure" and "Bot
> Framework" are trademarks of Microsoft Corporation and appear here only to
> describe origin and compatibility.

## Differences from the upstream SDK

- Requires **Java 21** and uses current versions of its third-party
  dependencies.
- **No Spring.** The Spring Boot integration and Spring bean configuration were
  removed. You build a plain configuration object
  (`com.microsoft.bot.integration.AzureBotConfiguration`) and inject it, so it
  can come from whatever configuration source your application already uses.
- Fewer dependencies, with unused modules and build plugins removed.
- The SDK modules (builder, connector, dialogs, schema, integration) and
  Recognizers-Text are merged into a single artifact.

The Java package names (`com.microsoft.bot.*`, `com.microsoft.recognizers.*`)
are unchanged, so existing Bot Framework code can move over with minimal
changes.

## Build

Requirements: JDK 21 and Maven 3.9+.

```bash
mvn clean install
```

```xml
<dependency>
  <groupId>com.rheininsights</groupId>
  <artifactId>bot-gateway-java</artifactId>
  <version>1.0.0</version>
</dependency>
```

## Hello World

[`example/`](example) contains a complete, dependency-free bot:

- `HelloWorldBot` answers every message and greets new members.
- `HelloWorldServer` hosts it on `http://localhost:3978/api/messages` using
  the JDK's built-in HTTP server.

The essential part:

```java
AzureBotConfiguration config = new AzureBotConfiguration();
config.botAppId      = System.getenv("MICROSOFT_APP_ID");
config.appPassword   = System.getenv("MICROSOFT_APP_PASSWORD");
config.entraTenantId = System.getenv("MICROSOFT_APP_TENANT_ID");

BotFrameworkHttpAdapter adapter = new BotFrameworkHttpAdapter(config);
Bot bot = new HelloWorldBot();

// For each POST to /api/messages:
adapter.processActivity(authHeader, activity, bot::onTurn);
```

To run it, create an Azure Bot resource with a single-tenant Entra ID app
registration, set its messaging endpoint to `https://<your-host>/api/messages`,
set the three environment variables above and start `HelloWorldServer`.

Never commit real credentials. Load them from environment variables or a secret
store.

## License

MIT, see [LICENSE](LICENSE). Original code © Microsoft Corporation;
modifications © RheinInsights GmbH. See [NOTICE](NOTICE) for the upstream
projects this repository contains.

## Contributing

Issues and pull requests are welcome. Report security issues privately as
described in [SECURITY.md](SECURITY.md).
