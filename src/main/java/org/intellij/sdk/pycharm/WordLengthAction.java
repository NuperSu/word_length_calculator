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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class WordLengthAction extends AnAction {

  private String getPythonScriptPath() throws IOException {
    InputStream resourceStream = getClass().getClassLoader()
        .getResourceAsStream("python/word_length.py");
    if (resourceStream == null) {
      throw new FileNotFoundException("Python script not found in resources.");
    }

    File tempScript = File.createTempFile("word_length", ".py");
    tempScript.deleteOnExit();

    Files.copy(resourceStream, tempScript.toPath(), StandardCopyOption.REPLACE_EXISTING);

    return tempScript.getAbsolutePath();
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent event) {
    Project project = event.getProject();
    String selectedText = Objects.requireNonNull(event.getData(CommonDataKeys.EDITOR)).getSelectionModel()
        .getSelectedText();
    if (selectedText == null || selectedText.isEmpty()) {
      Messages.showMessageDialog(project,
          "No text selected.",
          "Error",
          Messages.getErrorIcon());
      return;
    }

    // Get Python SDK
    assert project != null;
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
      String scriptPath = getPythonScriptPath();
      ProcessBuilder processBuilder = new ProcessBuilder(
          pythonSdk.getHomePath(),
          scriptPath,
          selectedText
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
            "Error executing Python script:\n" + outputBuilder,
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


  @Override
  public void update(@NotNull AnActionEvent e) {
    boolean isTextSelected = false;
    if (e.getData(CommonDataKeys.EDITOR) != null) {
      isTextSelected = Objects.requireNonNull(e.getData(CommonDataKeys.EDITOR)).getSelectionModel().hasSelection();
    }
    e.getPresentation().setEnabledAndVisible(isTextSelected);
  }
}
