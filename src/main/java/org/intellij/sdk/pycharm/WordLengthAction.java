package org.intellij.sdk.pycharm;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.ui.Messages;
import com.jetbrains.python.sdk.PythonSdkUtil;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class WordLengthAction extends AnAction {

  @Override
  public void actionPerformed(@NotNull AnActionEvent event) {
    Project project = event.getProject();
    String selectedText = event.getData(CommonDataKeys.EDITOR).getSelectionModel().getSelectedText();

    if (selectedText == null || selectedText.isEmpty()) {
      Messages.showMessageDialog(project,
          "No text selected.",
          "Error",
          Messages.getErrorIcon());
      return;
    }

    // Get Python SDK
    Module[] modules = ModuleManager.getInstance(project).getModules();
    if (modules.length == 0) {
      Messages.showErrorDialog(project, "No modules found", "Error");
      return;
    }

    Sdk pythonSdk = PythonSdkUtil.findPythonSdk(modules[0]);
    if (pythonSdk == null || pythonSdk.getHomePath() == null) {
      Messages.showErrorDialog(project, "Python interpreter not found", "Error");
      return;
    }

    try {
      // Execute the Python script using the project's Python interpreter
      ProcessBuilder processBuilder = new ProcessBuilder(
          pythonSdk.getHomePath(),
          "-c",
          getPythonScript(selectedText)
      );
      processBuilder.redirectErrorStream(true);
      Process process = processBuilder.start();

      // Capture the output
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      StringBuilder outputBuilder = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        outputBuilder.append(line).append("\n");
      }

      int exitCode = process.waitFor();
      if (exitCode == 0) {
        Messages.showMessageDialog(project,
            outputBuilder.toString(),
            "Word Lengths",
            Messages.getInformationIcon());
      } else {
        Messages.showMessageDialog(project,
            "Error executing Python script:\n" + outputBuilder.toString(),
            "Error",
            Messages.getErrorIcon());
      }

    } catch (Exception e) {
      Messages.showMessageDialog(project,
          "Exception: " + e.getMessage(),
          "Error",
          Messages.getErrorIcon());
    }
  }

  private String getPythonScript(String text) {
    // Safely escape the text input
    String escapedText = text.replace("'", "\\'").replace("\n", "\\n").replace("\r", "\\r");

    // Return properly formatted Python code
    return String.format(
        "text = '''%s'''\n" +
            "words = text.split()\n" +
            "for word in words:\n" +
            "    # Strip punctuation for more accurate length\n" +
            "    clean_word = word.strip('.,!?()[]{}\":;')\n" +
            "    print(f\"'{word}': {len(clean_word)} characters\")",
        escapedText
    );
  }

  @Override
  public void update(@NotNull AnActionEvent e) {
    boolean isTextSelected = false;
    if (e.getData(CommonDataKeys.EDITOR) != null) {
      isTextSelected = e.getData(CommonDataKeys.EDITOR).getSelectionModel().hasSelection();
    }
    e.getPresentation().setEnabledAndVisible(isTextSelected);
  }
}
