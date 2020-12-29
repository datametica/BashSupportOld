package com.ansorgit.plugins.bash.actions;

import com.ansorgit.plugins.bash.runner.repl.BashConsoleRunner;
import com.ansorgit.plugins.bash.util.BashIcons;
import com.intellij.execution.ExecutionException;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;














public class AddReplAction
  extends AnAction
{
  private static final Logger log = Logger.getInstance("AddReplAction");
  private BashConsoleRunner consoleRunner;
  
  public AddReplAction() {
    getTemplatePresentation().setIcon(BashIcons.BASH_FILE_ICON);
  }

  
  public void update(AnActionEvent e) {
    super.update(e);
    
    Presentation presentation = e.getPresentation();
    presentation.setEnabled((getModule(e) != null));
  }

  
  BashConsoleRunner getConsoleRunner() {
    return this.consoleRunner;
  }
  
  public void actionPerformed(AnActionEvent e) {
    Module module = getModule(e);
    
    if (module != null) {
      try {
        Project project = module.getProject();
        VirtualFile baseDir = project.getBaseDir();
        
        if (baseDir != null) {
          this.consoleRunner = new BashConsoleRunner(project, baseDir.getPath());
          this.consoleRunner.initAndRun();
        } 
      } catch (ExecutionException ex) {
        log.warn("Error running bash repl", (Throwable)ex);
      } 
    }
  }
  
  @Nullable
  private static Module getModule(AnActionEvent e) {
    Module module = (Module)e.getData(LangDataKeys.MODULE);
    if (module == null) {
      Project project = (Project)e.getData(LangDataKeys.PROJECT);
      if (project == null) {
        return null;
      }
      
      Module[] modules = ModuleManager.getInstance(project).getModules();
      if (modules.length == 1) {
        module = modules[0];
      }
    } 
    
    return module;
  }
}
