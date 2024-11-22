# Task 1: Analysis of the Java Code

The original code has a few problems:

1. **Using `String` as a Lock Object**:  
   Strings in Java can be shared between different parts of the application. Using a
   dedicated `Object` instance for locking is better.

2. **Exception Handling for `wait()`**:  
   The `wait()` method can throw an `InterruptedException`, which is not handled in the code.
   Ignoring exceptions can lead to unexpected behavior.

3. **Thread Safety Concerns**:  
   The `shouldWait` variable is accessed without proper synchronization, which could lead to
   visibility issues in a multithreaded environment.

4. **Potential Deadlock**:  
   If `wait()` is called when no other thread is available to call `notifyAll()`, the program might
   get stuck indefinitely.

---

### Corrected Java Code

Corrected version of the Java code:

```java
public class MyClass {

  private final Object myLock = new Object();
  private boolean shouldWait = true;

  public void foo() {
    synchronized (myLock) {
      try {
        if (shouldWait) {
          myLock.wait();
        }
        // Perform some work
        myLock.notifyAll();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        System.err.println("Thread was interrupted: " + e.getMessage());
      }
    }
  }
}
```

---

### Kotlin Version of the Code

The Kotlin version of the code makes the same corrections

```kotlin
class MyClass {
    private val myLock = Any()

    @Volatile
    var shouldWait = true

    fun foo() {
        synchronized(myLock) {
            try {
                if (shouldWait) {
                    myLock.wait()
                }
                // Perform some work
                myLock.notifyAll()
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
                println("Thread was interrupted: ${e.message}")
            }
        }
    }
}
```

# Task 2: Word Length Calculator Plugin for PyCharm

The **Word Length Calculator** is a simple plugin for PyCharm that calculates and displays the
length of each word in the selected text. This plugin is designed to integrate seamlessly with the
PyCharm IDE and requires the Python plugin.

---

## Requirements

- **PyCharm 2024.1.7 or later**
- **Python Plugin** (com.intellij.modules.python)
- Java 17 or higher

---

## Installation

1. Clone or download this repository:
   ```bash
   git clone https://github.com/NuperSu/word-length-calculator.git
   cd word-length-calculator
   ```

2. Open the project in IntelliJ IDEA or PyCharm.

3. Build the plugin:
   ```bash
   ./gradlew build
   ```

4. Install the plugin:
    - Go to **File > Settings > Plugins > ⚙️ > Install Plugin from Disk**.
    - Select the generated `.zip` file from the `build/distributions/` directory.

5. Restart PyCharm.

---

## How to Use

1. Open any text file in PyCharm.
2. Select some text in the editor.
3. Open the **Word Length Tools** menu from the main menu bar.
4. Click **Print Word Lengths** (or use the keyboard shortcut `Ctrl+Alt+W`).
5. A dialog will display the length of each word in the selected text.

---
