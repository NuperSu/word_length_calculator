Here's a `README.md` file that summarizes and documents your plugin project, providing details on
how to build, run, and use it.

---

### README.md

# Word Length Calculator Plugin for PyCharm

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
   git clone https://github.com/your-repository/word-length-calculator.git
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
4. Click **Print Word Lengths**.
5. A dialog will display the length of each word in the selected text.

---
