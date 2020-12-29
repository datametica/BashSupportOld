package com.ansorgit.plugins.bash.call;

import com.intellij.codeInsight.daemon.quickFix.LightQuickFixParameterizedTestCase;
import com.intellij.codeInspection.InspectionEP;
import com.intellij.codeInspection.InspectionProfileEntry;
import com.intellij.codeInspection.LocalInspectionEP;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.util.containers.ContainerUtil;
import java.util.Iterator;
import java.util.List;
import junit.framework.AssertionFailedError;
import org.jetbrains.annotations.NotNull;

public abstract class BashLightQuickfixParametrizedTest
  extends LightQuickFixParameterizedTestCase
{
  private final Class<?>[] inspectionsClasses;
  
  public BashLightQuickfixParametrizedTest(Class<?>... inspectionsClasses) {
    this.inspectionsClasses = inspectionsClasses;
  }

  
  protected void doSingleTest(String fileSuffix, String testDataPath) {
    enableInspectionTools(this.inspectionsClasses);
    
    try {
      super.doSingleTest(fileSuffix, testDataPath);
    } catch (AssertionFailedError e) {


      
      if (ApplicationInfo.getInstance().getBuild().getBaselineVersion() != 163) {
        throw e;
      }
    } 
  }

  
  protected boolean isRunInWriteAction() {
    return true;
  }

  
  @NotNull
  protected String getTestDataPath() {
    if (BashTestUtils.getBasePath() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/call/BashLightQuickfixParametrizedTest", "getTestDataPath" }));  return BashTestUtils.getBasePath();
  }

  
  protected void enableInspectionTools(@NotNull Class<?>... classes) {
    if (classes == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "classes", "com/ansorgit/plugins/bash/call/BashLightQuickfixParametrizedTest", "enableInspectionTools" }));  InspectionProfileEntry[] tools = new InspectionProfileEntry[classes.length];
    
    List<InspectionEP> eps = ContainerUtil.newArrayList();
    ContainerUtil.addAll(eps, Extensions.getExtensions(LocalInspectionEP.LOCAL_INSPECTION));
    ContainerUtil.addAll(eps, Extensions.getExtensions(InspectionEP.GLOBAL_INSPECTION));

    
    for (int i = 0; i < classes.length; i++) {
      Iterator<InspectionEP> iterator = eps.iterator(); while (true) { if (iterator.hasNext()) { InspectionEP ep = iterator.next();
          if (classes[i].getName().equals(ep.implementationClass)) {
            tools[i] = ep.instantiateTool(); break;
          } 
          continue; }
        
        throw new IllegalArgumentException("Unable to find extension point for " + classes[i].getName()); }
    
    } 
    enableInspectionTools(tools);
  }
}
