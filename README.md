# The Nifli.com Feature Toggle Client for Java

[Nifli.com](https://nifli.com) offers Feature Flags (aka Feature Toggles) for B2B SaaS companies. This is the java client for [Nifli.com](https://nifli.com) Feature Toggles (herein called Toggles).

## Getting started

Add a dependency to the pom.xml file:
```xml
<dependency>
	<groupId>com.nifli</groupId>
	<artifactId>toggles-client-java</artifactId>
 	<version>1.0.0-SNAPSHOT</version>
</dependency>
```

### Create a new Toggles instance

It is easy to get a new instance of the TogglesClient. In any given application there will likely be a *single instance of Toggles*, injecting if/then/else tests where needed. 

To create a new instance of TogglesClient, pass in your application client ID and secret. This will create a TogglesClient instance using defaults, including the stage of 'development':
```java
TogglesClient toggles = new TogglesClient("qT4b805OQpyg-e0CifyD8c", "njI2_AsBpHTiNYVL-KaBOoHLoiFsoU-1");
```
or create a TogglesConfiguration instance, which allows tweaking settings within the client.
```java
TogglesConfiguration config = new TogglesConfiguration(
		"qT4b805OQpyg-e0CifyD8c",           // Client ID, required.
		"njI2_AsBpHTiNYVL-KaBOoHLoiFsoU-1") // Client Secret, required.
	.setStage("production")                 // Default is 'development'
	.setCacheTtlMillis(600000l)             // Default is 10 minutes (600,000ms).
	.setMaxRetries(5)                       // Default is 5.
	.setRetryDelayMillis(30l)               // Default is 30ms.
	.setShouldFetchOnStartup(true);         // Default is true.

TogglesClient toggles = new TogglesClient(config);
// or...
TogglesClient toggles = config.newClient();
```

### Feature toggle API

It is really simple to use toggles.

```java
if(toggles.isEnabled("Fantastic Feature"))
{
	// new stuff happens here
}
else
{
	// old stuff happens here
}
```

Calling `toggles.isEnabled("Fantastic Feature")` is the equivalent of calling `toggles.isEnabled("Fantastic Feature", false)`. 
Toggles will return `false` if it cannot find the named feature.

To default to `true` instead, simply pass `true` as the second argument:

```java
if (toggles.isEnabled("Fantastic Feature", true))
{
	// new stuff happens here
}
else
{
	// old stuff happens here
}
```
