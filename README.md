# Mosaik plugin library

Hands on Tutorial: Developing for Mosaik
* [Part 1 - Concept](https://docs.ergoplatform.com/dev/stack/mosaik/intro/)
* [Part 2 - A simple UI](https://docs.ergoplatform.com/dev/stack/mosaik/tutorial2/)
* [Part 3 - Processing data](https://docs.ergoplatform.com/dev/stack/mosaik/tutorial3/)
* [Part 4 - A complete app](https://docs.google.com/document/d/1HjSIwI3cS70qyEv7zWtv5Rrc24JQtOh1H48uGquqZ3U/edit?usp=sharing)
* [Part 5 - Deploying on the web](https://docs.google.com/document/d/16PDmtHiMk28sMjHLf3PVeWmzb6C_gvj2ThEF6iCp6bY/edit?usp=sharing)

[Overview doc](https://docs.google.com/document/d/1Pc4DLChaKyGbMnY4gu92mcBEDfeNiBCQ5hq9lJUssGg/edit?usp=sharing)

## Library modules

The library modules are released on Jitpack. To add it to your environment, make sure Jitpack 
is in your repositories list:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

Add each library module like follows:

    implementation 'com.github.MrStahlfelge.mosaik:common-model:develop-SNAPSHOT'

### common-model
Java 7. Base models Mosaik library works on.

No dependencies. For use in Mosaik executors and dApps.

### common-model-ktx
Kotlin. Kotlin-friendly extensions for Mosaik common models, for example a Kotlin DSL to
build App View Contents.

Depends on common-model, Kotlin-std.

### serialization-gson
Java 7. Json schema definition and reference implementation of serialization and deserialization of 
common-model classes.

Depends on common-model, Gson. For use in Mosaik executors and dApps.

### serialization-jackson
Java 8. Json serialization of common-model classes.

Depends on common-model, jackson. For use in Mosaik dApps, mostly intended to be used with Spring.

### common-runtime
Kotlin. Platform-agnostic reference implementation of Mosaik client runtime

Depends on common-model, Kotlin-std, Kotlin coroutines. For use in Mosaik executors.

#### clientconnector-okhttp
Kotlin. HTTP Client/Server implementation for common-runtime's MosaikBackendConnector interface using
OkHttp3 and serialization-gson.

Depends on common-runtime, serialization-gson, OkHttp3. For use in Mosaik executors.

### common-compose
Kotlin. Jetpack Compose/Desktop Compose implementation to render ViewContent into Composables. 
Can be used on Android and Desktop.

Depends on common-runtime and Jetbrains Compose. For use in Mosaik executors building up on Compose.

### common-abstractui
Kotlin. A set of helper methods to manage rendering the Mosaik UI on non-declarative UI frameworks.

Depends on common-runtime. For use in Mosaik executors.

## Demo modules

### desktop-demo
Kotlin. Demo and debug application showcasing how a view tree is shown on screen, displaying 
current values and view tree serialization. Use this to debug and play with your Mosaik apps.

Run it by cloning this repo and call

    gradlew desktop-demo:run

Built in offline app shows some view elements and demonstrates capabilities of Mosaik when no
backend connection is available. Use the address bar to navigate to an existing Mosaik backend
(for example, one of the backend demos below).

### backend-demo
Java 11. Backend demo serving a demo application using Spring Boot as HTTP server framework.

Demonstrates to use FreeMarker generating Json or using Mosaik Model classes serialized with 
Spring's mechanisms.

Run it by cloning this repo and call

    gradlew backend-demo:bootRun

Available demo apps:

* visitor list: demonstrates data interchange between client and backend
* lazybox: demonstrates data loading without affecting user interaction
* alignments: demonstrating different horizontal alignment types in a Column

### backend-demo-kotlin
Kotlin. Backend demo serving a demo application using spring Boot. Demonstrating usage of the 
Kotlin DSL to serve the Mosaik app.

Run it by cloning this repo and call

    gradlew backend-demo-kotlin:bootRun

Available demo apps: 
* View elements demo: Showcasing available Mosaik view elements
* Actions demo: Demonstrating actions that Mosaik apps can launch
* Typical errors: A malicious Mosaik app to test executor graceful error handling