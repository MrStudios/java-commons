# Java Commons (Reflection)
That library is allowing to scan classes to filter classes with annotation and/or interface implementation.

## Usage
To use this library, you need to add following to your project build file.

Maven:
```xml
<dependency>
    <groupId>pl.mrstudios.commons</groupId>
    <artifactId>commons-reflection</artifactId>
    <version>1.1.0</version>
</dependency>
```

Gradle: (Groovy)
```groovy
implementation "pl.mrstudios.commons:commons-reflection:1.1.0"
```

Gradle (Kotlin)
```kotlin
implementation("pl.mrstudios.commons:commons-reflection:1.1.0")
```

## Example

```java
package pl.mrstudios.example;

import pl.mrstudios.commons.reflection.Reflections;

public class Example {

    public static void main(String[] args) {
        
        /* Filter by Annotation */
        new Reflections<IPortal>("pl.mrstudios.example")
                .getClassesAnnotatedWith(Portal.class)
                .forEach((portal) -> {
                    try {
                        portal.getDeclaredConstructor().newInstance();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                });
        
        /* Filter by Interface */
        new Reflections<IPortal>("pl.mrstudios.example")
                .getClassesImplementing(IPortal.class)
                .forEach((portal) -> {
                    try {
                        portal.getDeclaredConstructor().newInstance();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                });
        
    }
    
}
```

