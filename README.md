# Mosaik plugin library

See [current draft](https://docs.google.com/document/d/1Pc4DLChaKyGbMnY4gu92mcBEDfeNiBCQ5hq9lJUssGg/edit?usp=sharing)

## Modules

### common-model
Java. Base models Mosaik library works on.

No dependencies.

### serialization-gson
Java. Json schema definition and reference implementation of serialization of common model classes.

Depends on common-model, Gson.

### common-runtime
Kotlin. Platform-agnostic reference implementation of Mosaik runtime

Depends on common-model, Kotlin-std, Kotlin coroutines.

### common-compose
Kotlin. Jetpack Compose/Desktop Compose implementation to render ViewContent into Composables. 
Can be used on Android and Desktop.

Depends on common-runtime and Jetbrains Compose.

### desktop-demo
Kotlin. Demo and debug application showcasing how a view tree is shown on screen, displaying 
current values and view tree serialization. Use this to debug and play with your Mosaik apps.

Run it by cloning this repo and call

    gradlew desktop-demo:run