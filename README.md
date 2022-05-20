# Mosaik plugin library

See [current draft](https://docs.google.com/document/d/1Pc4DLChaKyGbMnY4gu92mcBEDfeNiBCQ5hq9lJUssGg/edit?usp=sharing)

## Modules

### common-model
Java. Base models Mosaik library works on.

No dependencies. For use in Mosaik executors and dApps.

### serialization-gson
Java. Json schema definition and reference implementation of serialization of common model classes.

Depends on common-model, Gson. For use in Mosaik executors and dApps.

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

### backend-demo
Java. Backend demo serving a demo application using Spring Boot as HTTP server framework.

Run it by cloning this repo and call

    gradlew backend-demo:bootRun

### desktop-demo
Kotlin. Demo and debug application showcasing how a view tree is shown on screen, displaying 
current values and view tree serialization. Use this to debug and play with your Mosaik apps.

Run it by cloning this repo and call

    gradlew desktop-demo:run

If this is launched while a backend is serving its content at localhost:8080, the demo will 
launch the backend demo. Otherwise it will show a static content from its resource directory.