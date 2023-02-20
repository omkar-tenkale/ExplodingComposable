<div align="center"><img width="300px" height="300px"  src="https://raw.githubusercontent.com/omkar-tenkale/ExplodingComposable/master/art/exploding-composable.webp"/></div>
<h1 align="center">💢 ExplodingComposable</h1>
<h4 align="center">A Jetpack Compose utility library to add explosive dust effect animation to any composable</h4>

<div align="center">
  <img src="https://jitpack.io/v/omkar-tenkale/ExplodingComposable.svg" />
  <img src="https://img.shields.io/badge/license-Apache%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0.html" />
  <img src="https://img.shields.io/badge/PRs-welcome-brightgreen.svg" />
</div>

<div align="center">
  <sub>Based on 
  <a href="https://github.com/tyrantgit/ExplosionField">ExplosionField</a> by
  <a href="https://github.com/tyrantgit">@tyrantgit</a>
</div>

<br/>

## 💻 Installation
  
1. Add this to `build.gradle` of project gradle dependency

```groovy
allprojects {
	repositories {
		...
 		maven { url 'https://jitpack.io' }
	}
}
```
  
2. In `build.gradle` of `app` module, include this dependency
        
```groovy
implementation "com.github.omkar-tenkale:ExplodingComposable:1.0.0"
```

You can find latest version and changelogs in the [releases](https://github.com/omkar-tenkale/ExplodingComposable/releases).

<br/>
        
## ❓ Usage

Wrap the content to explode in [`Explodable`](https://github.com/omkar-tenkale/ExplodingComposable/blob/master/explodable/src/main/java/dev/omkartenkale/explodable/Explodable.kt) and trigger the explosion with `explosionController.explode()`

```kotlin
val explosionController = rememberExplosionController()

Explodable(controller = explosionController) {
    //Content to explode
    Box(modifier = Modifier
        .size(100.dp)
        .background(Color.Black)
        .clickable { explosionController.explode() })
}
```

<br/>
    
## 🎨 Customization
ExplodingComposable offers a variety of customizations
    
```kotlin
val explosionController = rememberExplosionController()

Explodable(
    //Standard compose modifier
    modifier = Modifier,

    // Control the explosion state with an instance of ExplosionController
    // You can access it using rememberExplosionController() method
    // Provides methods controller.explode() and controller.reset()
    controller = explosionController,

    // Control the animation with these params
    animationSpec = ExplosionAnimationSpec(
        // The higher the number, the bigger the explosion
        explosionPower = 2f,
        // Duration for the particle explosion
        explosionDurationMs = 750,
        // Duration for the shake effect before explosion
        shakeDurationMs = 250
    ),

    // Callback to fire when explosion is finished
    onExplode = {
                
    },

    // To control the explosion manually, use this param [0f-1f]
    currentProgress = progress,

    // The composable to explode
    content = {
        
    }
)
```

## 📱 Demo

Download the [sample app](https://github.com/omkar-tenkale/ExplodingComposable/releases/download/1.0.0/ExplodingComposableDemo.apk)
or explore the [sample project](https://github.com/omkar-tenkale/ExplodingComposable/tree/master/app/src/main/java/dev/omkartenkale/explodable/sample)

## 📃 License
Licensed under Apache license 2.0

This work is derived from [ExplosionField](https://github.com/tyrantgit/ExplosionField)
 
