# Java Commons
This repository contains libraries that is used by MrStudios Industries in their open source and closed source projects.

## Usage
To use our libraries, you need to add our repository to your project, when you do that, you can add our libraries to your project.

Maven:
```xml
<repository>
    <id>mrstudios-repository</id>
    <url>https://repository.mrstudios.pl/public/</url>
</repository>
```

Gradle: (Groovy)
```groovy
maven {
    url "https://repository.mrstudios.pl/public/"
}
```

Gradle (Kotlin)
```kotlin
maven {
    url = uri("https://repository.mrstudios.pl/public/")
}
```

