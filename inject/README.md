# Java Commons (Inject)
That library is allowing to inject parameters into constructor like dependency injection.

## Usage
To use this library, you need to add following to your project build file.

Maven:
```xml
<dependency>
    <groupId>pl.mrstudios.commons</groupId>
    <artifactId>commons-inject</artifactId>
    <version>1.0.0</version>
</dependency>
```

Gradle: (Groovy)
```groovy
implementation "pl.mrstudios.commons:commons-inject:1.0.0"
```

Gradle (Kotlin)
```kotlin
implementation("pl.mrstudios.commons:commons-inject:1.0.0")
```

## Example

```java
package pl.mrstudios.example;

import pl.mrstudios.commons.inject.Injector;
import pl.mrstudios.commons.inject.annotation.Inject;

public class Example {

    public static void main(String[] args) {

        Injector injector = new Injector()
                .register(args); // String[].class -> args

        injector.inject(AnClass.class);

    }

    public static class AnClass {

        @Inject
        public AnClass(String[] args) {
            System.out.println(String.join(" ", args));
        }

    }

}
```

