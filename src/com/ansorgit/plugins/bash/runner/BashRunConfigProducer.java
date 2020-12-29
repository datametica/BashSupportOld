package com.ansorgit.plugins.bash.runner;

import com.ansorgit.plugins.bash.lang.psi.api.BashFile;
import com.ansorgit.plugins.bash.lang.psi.api.BashShebang;
import com.ansorgit.plugins.bash.util.BashInterpreterDetection;
import com.intellij.execution.Location;
import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.actions.RunConfigurationProducer;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
















public class BashRunConfigProducer
  extends RunConfigurationProducer<BashRunConfiguration>
{
  public BashRunConfigProducer() {
    super((ConfigurationType)BashConfigurationType.getInstance());
  }

  
  protected boolean setupConfigurationFromContext(BashRunConfiguration configuration, ConfigurationContext context, Ref<PsiElement> sourceElement) {
    Location location = context.getLocation();
    if (location == null) {
      return false;
    }
    
    PsiElement psiElement = location.getPsiElement();
    if (!psiElement.isValid()) {
      return false;
    }
    
    PsiFile psiFile = psiElement.getContainingFile();
    if (!(psiFile instanceof BashFile)) {
      return false;
    }
    sourceElement.set(psiFile);
    
    VirtualFile file = location.getVirtualFile();
    if (file == null) {
      return false;
    }
    
    configuration.setName(file.getPresentableName());
    configuration.setScriptName(VfsUtilCore.virtualToIoFile(file).getAbsolutePath());
    
    if (file.getParent() != null) {
      configuration.setWorkingDirectory(VfsUtilCore.virtualToIoFile(file.getParent()).getAbsolutePath());
    }
    
    Module module = context.getModule();
    if (module != null) {
      configuration.setModule(module);
    }





    
    if (!configuration.isUseProjectInterpreter() && configuration.getInterpreterPath().isEmpty()) {
      BashFile bashFile = (BashFile)psiFile;
      BashShebang shebang = bashFile.findShebang();
      if (shebang != null) {
        String shebandShell = shebang.shellCommand(false);
        
        if (BashInterpreterDetection.instance().isSuitable(shebandShell)) {
          configuration.setInterpreterPath(shebandShell);
          configuration.setInterpreterOptions(shebang.shellCommandParams());
        } 
      } 
    } 
    
    return true;
  }

  
  public boolean isConfigurationFromContext(BashRunConfiguration configuration, ConfigurationContext context) {
    Location location = context.getLocation();
    if (location == null) {
      return false;
    }


    
    VirtualFile file = location.getVirtualFile();
    return (file != null && FileUtil.pathsEqual(file.getPath(), configuration.getScriptName()));
  }
}
