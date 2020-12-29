package com.ansorgit.plugins.bash.documentation;

import com.ansorgit.plugins.bash.lang.psi.api.command.BashCommand;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.ansorgit.plugins.bash.util.OSUtil;
import com.intellij.execution.process.CapturingProcessHandler;
import com.intellij.execution.process.ProcessOutput;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NonNls;





















class SystemInfopageDocSource
  implements DocumentationSource, CachableDocumentationSource
{
  @NonNls
  private static final String CHARSET_NAME = "utf-8";
  private static final int TIMEOUT_IN_MILLISECONDS = (int)TimeUnit.SECONDS.toMillis(4L);
  private final String infoExecutable;
  private final String txt2htmlExecutable;
  private final Logger log = Logger.getInstance("#SystemInfopageDocSource");
  
  SystemInfopageDocSource() {
    this.infoExecutable = OSUtil.findBestExecutable("info");
    this.txt2htmlExecutable = OSUtil.findBestExecutable("txt2html");
  }
  
  public String documentation(PsiElement element, PsiElement originalElement) {
    if (this.infoExecutable == null) {
      return null;
    }
    
    if (!(element instanceof BashCommand) && !(element instanceof com.ansorgit.plugins.bash.lang.psi.api.command.BashGenericCommand)) {
      return null;
    }
    
    BashCommand command = (BashCommand)BashPsiUtils.findParent(element, BashCommand.class);
    if (command == null || !command.isExternalCommand()) {
      return null;
    }
    
    try {
      String commandName = command.getReferencedCommandName();
      
      boolean hasInfoPage = infoFileExists(commandName);
      if (!hasInfoPage) {
        return null;
      }
      
      String infoPageData = loadPlainTextInfoPage(commandName);
      
      if (this.txt2htmlExecutable != null) {
        return callTextToHtml(infoPageData);
      }
      
      return simpleTextToHtml(infoPageData);
    } catch (IOException e) {
      this.log.info("Failed to retrieve info page: ", e);

      
      return null;
    } 
  }
  
  boolean infoFileExists(String commandName) throws IOException {
    ProcessBuilder processBuilder = new ProcessBuilder(new String[] { this.infoExecutable, "-w", commandName });
    
    CapturingProcessHandler processHandler = new CapturingProcessHandler(processBuilder.start(), Charset.forName("utf-8"), this.infoExecutable + " -w " + commandName);
    ProcessOutput output = processHandler.runProcess(TIMEOUT_IN_MILLISECONDS);
    
    return (output.getExitCode() == 0 && !output.getStdout().isEmpty());
  }
  
  String loadPlainTextInfoPage(String commandName) throws IOException {
    ProcessBuilder processBuilder = new ProcessBuilder(new String[] { this.infoExecutable, "-o", "-", commandName });
    
    CapturingProcessHandler processHandler = new CapturingProcessHandler(processBuilder.start(), Charset.forName("utf-8"), this.infoExecutable + " -o - " + commandName);
    ProcessOutput output = processHandler.runProcess(TIMEOUT_IN_MILLISECONDS);
    
    if (output.getExitCode() != 0) {
      return null;
    }
    
    return output.getStdout();
  }
  
  String callTextToHtml(String infoPageData) throws IOException {
    if (this.txt2htmlExecutable == null)
    {
      return "<html><body><pre>" + infoPageData + "</pre></body></html>";
    }
    
    ProcessBuilder processBuilder = new ProcessBuilder(new String[] { this.txt2htmlExecutable, "--infile", "-" });
    
    CapturingProcessHandler processHandler = new MyCapturingProcessHandler(processBuilder.start(), infoPageData, this.txt2htmlExecutable + " --inifile -");
    
    ProcessOutput output = processHandler.runProcess(TIMEOUT_IN_MILLISECONDS);
    if (output.getExitCode() != 0) {
      return null;
    }
    
    return output.getStdout();
  }

  
  public String documentationUrl(PsiElement element, PsiElement originalElement) {
    return null;
  }
  
  private String simpleTextToHtml(String infoPageData) {
    return "<html><bead></head><body><pre>" + infoPageData + "</pre></body>";
  }


  
  public String findCacheKey(PsiElement element, PsiElement originalElement) {
    if (element instanceof BashCommand && ((BashCommand)element).isExternalCommand()) {
      return ((BashCommand)element).getReferencedCommandName();
    }
    
    return null;
  }
  
  private class MyCapturingProcessHandler extends CapturingProcessHandler {
    private final String stdinData;
    
    MyCapturingProcessHandler(Process process, String stdinData, String commandLine) {
      super(process, Charset.forName("utf-8"), commandLine);
      this.stdinData = stdinData;
    }

    
    public void startNotify() {
      super.startNotify();


      
      try {
        Writer stdinWriter = new OutputStreamWriter(getProcessInput(), "utf-8");
        stdinWriter.write(this.stdinData);
        stdinWriter.close();
      } catch (IOException e) {
        SystemInfopageDocSource.this.log.info("Exception passing data to txt2html", e);
      } 
    }
  }
}
