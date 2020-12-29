package com.ansorgit.plugins.bash.editor.codecompletion;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.SystemInfoRt;
import java.io.File;
import java.io.UncheckedIOException;
import java.nio.file.FileSystemException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.stream.Stream;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.jvm.internal.PropertyReference1;
import kotlin.jvm.internal.PropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KDeclarationContainer;
import kotlin.reflect.KProperty;
import kotlin.text.StringsKt;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;








@Metadata(mv = {1, 1, 1}, bv = {1, 0, 0}, k = 1, d1 = {"\000$\n\002\030\002\n\002\020\000\n\002\b\002\n\002\030\002\n\002\020\016\n\002\030\002\n\002\b\005\n\002\020\036\n\002\b\007\030\000 \0212\0020\001:\002\021\022B\005¢\006\002\020\002J\f\020\013\032\b\022\004\022\0020\0060\fJ\024\020\r\032\b\022\004\022\0020\0060\f2\006\020\016\032\0020\005J\020\020\017\032\0020\0052\006\020\020\032\0020\005H\004R&\020\003\032\016\022\004\022\0020\005\022\004\022\0020\0060\0048BX\004¢\006\f\n\004\b\t\020\n\032\004\b\007\020\b¨\006\023"}, d2 = {"Lcom/ansorgit/plugins/bash/editor/codecompletion/BashPathCompletionService;", "", "()V", "commands", "Ljava/util/NavigableMap;", "", "Lcom/ansorgit/plugins/bash/editor/codecompletion/BashPathCompletionService$CompletionItem;", "getCommands", "()Ljava/util/NavigableMap;", "commands$delegate", "Lkotlin/Lazy;", "allCommands", "", "findCommands", "commandPrefix", "findUpperLimit", "prefix", "Companion", "CompletionItem", "BashSupport1_main"})
public final class BashPathCompletionService
{
  @Metadata(mv = {1, 1, 1}, bv = {1, 0, 0}, k = 3, d1 = {"\000\020\n\000\n\002\030\002\n\002\020\016\n\002\030\002\n\000\020\000\032\016\022\004\022\0020\002\022\004\022\0020\0030\001H\n¢\006\002\b\004"}, d2 = {"<anonymous>", "Ljava/util/TreeMap;", "", "Lcom/ansorgit/plugins/bash/editor/codecompletion/BashPathCompletionService$CompletionItem;", "invoke"})
  static final class BashPathCompletionService$commands$2
    extends Lambda
    implements Function0<TreeMap<String, CompletionItem>>
  {
    public static final BashPathCompletionService$commands$2 INSTANCE = new BashPathCompletionService$commands$2();
    
    @Metadata(mv = {1, 1, 1}, bv = {1, 0, 0}, k = 3, d1 = {"\000\026\n\000\n\002\020\013\n\000\n\002\030\002\n\002\b\002\n\002\030\002\n\000\020\000\032\0020\0012\016\020\002\032\n \004*\004\030\0010\0030\0032\016\020\005\032\n \004*\004\030\0010\0060\006H\n¢\006\002\b\007"}, d2 = {"<anonymous>", "", "f", "Ljava/nio/file/Path;", "kotlin.jvm.PlatformType", "attr", "Ljava/nio/file/attribute/BasicFileAttributes;", "test"})
    static final class BashPathCompletionService$commands$2$files$1<T, U>
      implements BiPredicate<Path, BasicFileAttributes>
    {
      public static final BashPathCompletionService$commands$2$files$1 INSTANCE = new BashPathCompletionService$commands$2$files$1();
      
      public final boolean test(Path f, BasicFileAttributes attr) {
        return (attr.isRegularFile() && Files.isExecutable(f)); } } @NotNull public final TreeMap<String, BashPathCompletionService.CompletionItem> invoke() { TreeMap<Object, Object> treeMap1; long start = System.currentTimeMillis(); TreeMap<Object, Object> result = new TreeMap<Object, Object>(); try { String paths = System.getenv("PATH"); if (paths != null) { byte b; String[] arrayOfString; for (arrayOfString = StringUtils.split(paths, File.pathSeparatorChar), b = 0; b < arrayOfString.length; ) { String e = arrayOfString[b]; String trimmed = StringsKt.trim(e, new char[] { '"', File.pathSeparatorChar }); CharSequence charSequence = trimmed; if (!((charSequence.length() == 0) ? 1 : 0)) try { Path path = Paths.get(trimmed, new String[0]); if (Files.isDirectory(path, new java.nio.file.LinkOption[0])) { BashPathCompletionService$commands$2$files$1<Path, BasicFileAttributes> bashPathCompletionService$commands$2$files$1 = BashPathCompletionService$commands$2$files$1.INSTANCE; boolean bool = true; Path path1 = path;















































































                  
                  Object[] arrayOfObject = (Object[])new FileVisitOption[0]; Stream<Path> files = Files.find(path1, bool, bashPathCompletionService$commands$2$files$1, (FileVisitOption[])arrayOfObject); }
                 }
              catch (Exception ex)
              { Exception exception1 = ex;
                if (exception1 instanceof java.nio.file.InvalidPathException || exception1 instanceof java.io.IOException || exception1 instanceof UncheckedIOException || exception1 instanceof SecurityException) {
                  BashPathCompletionService.Companion.getLOG().debug("Invalid path detected in " + "$" + "PATH element " + e, ex);
                } else if (exception1 instanceof FileSystemException) {
                  BashPathCompletionService.Companion.getLOG().debug("Ignoring filesystem exception in " + "$" + "PATH element " + e, ex);
                } else {
                  BashPathCompletionService.Companion.getLOG().error("Exception while scanning $PATH for command names", ex);
                }  }
               
            b++; }
           }
        
        treeMap1 = result; }
      finally
      { long duration = System.currentTimeMillis() - start;
        int size = result.size();
        BashPathCompletionService.Companion.getLOG().debug("bash commands loaded " + size + " commands in " + duration + " ms"); }
      
      return (TreeMap)treeMap1; }

    
    BashPathCompletionService$commands$2() {
      super(0);
    }
  }
  public static final Companion Companion = new Companion(null);
  private static final Logger LOG;
  
  static {
    LOG = Logger.getInstance("#bash.completion");
    $$delegatedProperties = new KProperty[] { (KProperty)Reflection.property1((PropertyReference1)new PropertyReference1Impl((KDeclarationContainer)Reflection.getOrCreateKotlinClass(BashPathCompletionService.class), "commands", "getCommands()Ljava/util/NavigableMap;")) };
  }
  
  @Metadata(mv = {1, 1, 1}, bv = {1, 0, 0}, k = 1, d1 = {"\000\032\n\002\030\002\n\002\020\000\n\002\b\002\n\002\030\002\n\002\b\004\n\002\030\002\n\000\b\003\030\0002\0020\001B\007\b\002¢\006\002\020\002J\b\020\b\032\0020\tH\007R\034\020\003\032\n \005*\004\030\0010\0040\004X\004¢\006\b\n\000\032\004\b\006\020\007¨\006\n"}, d2 = {"Lcom/ansorgit/plugins/bash/editor/codecompletion/BashPathCompletionService$Companion;", "", "()V", "LOG", "Lcom/intellij/openapi/diagnostic/Logger;", "kotlin.jvm.PlatformType", "getLOG", "()Lcom/intellij/openapi/diagnostic/Logger;", "getInstance", "Lcom/ansorgit/plugins/bash/editor/codecompletion/BashPathCompletionService;", "BashSupport1_main"})
  public static final class Companion {
    private Companion() {}
    
    private final Logger getLOG() {
      return BashPathCompletionService.LOG;
    }
    
    @JvmStatic
    @NotNull
    public final BashPathCompletionService getInstance() {
      if (ServiceManager.getService(BashPathCompletionService.class) == null)
        Intrinsics.throwNpe(); 
      return (BashPathCompletionService)ServiceManager.getService(BashPathCompletionService.class);
    }
  }
  
  @Metadata(mv = {1, 1, 1}, bv = {1, 0, 0}, k = 1, d1 = {"\000\022\n\002\030\002\n\002\020\000\n\000\n\002\020\016\n\002\b\t\b\b\030\0002\0020\001B\025\022\006\020\002\032\0020\003\022\006\020\004\032\0020\003¢\006\002\020\005J\t\020\t\032\0020\003HÆ\003J\t\020\n\032\0020\003HÆ\003J\035\020\013\032\0020\0002\b\b\002\020\002\032\0020\0032\b\b\002\020\004\032\0020\003HÆ\001R\021\020\002\032\0020\003¢\006\b\n\000\032\004\b\006\020\007R\021\020\004\032\0020\003¢\006\b\n\000\032\004\b\b\020\007¨\006\f"}, d2 = {"Lcom/ansorgit/plugins/bash/editor/codecompletion/BashPathCompletionService$CompletionItem;", "", "filename", "", "path", "(Ljava/lang/String;Ljava/lang/String;)V", "getFilename", "()Ljava/lang/String;", "getPath", "component1", "component2", "copy", "BashSupport1_main"})
  public static final class CompletionItem {
    @NotNull
    private final String filename;
    @NotNull
    private final String path;
    
    @NotNull
    public final String getFilename() {
      return this.filename;
    }
    
    @NotNull
    public final String getPath() {
      return this.path;
    }
    
    public CompletionItem(@NotNull String filename, @NotNull String path) {
      this.filename = filename;
      this.path = path;
    }
    
    @NotNull
    public final String component1() {
      return this.filename;
    }
    
    @NotNull
    public final String component2() {
      return this.path;
    }
    
    @NotNull
    public final CompletionItem copy(@NotNull String filename, @NotNull String path) {
      Intrinsics.checkParameterIsNotNull(filename, "filename");
      Intrinsics.checkParameterIsNotNull(path, "path");
      return new CompletionItem(filename, path);
    }
    
    public String toString() {
      return "CompletionItem(filename=" + this.filename + ", path=" + this.path + ")";
    }
    
    public int hashCode() {
      return ((this.filename != null) ? this.filename.hashCode() : 0) * 31 + ((this.path != null) ? this.path.hashCode() : 0);
    }
    
    public boolean equals(Object param1Object) {
      if (this != param1Object) {
        if (param1Object instanceof CompletionItem) {
          CompletionItem completionItem = (CompletionItem)param1Object;
          if (Intrinsics.areEqual(this.filename, completionItem.filename) && Intrinsics.areEqual(this.path, completionItem.path))
            return true; 
        } 
      } else {
        return true;
      } 
      return false;
    }
  }
  private final Lazy commands$delegate = LazyKt.lazy(BashPathCompletionService$commands$2.INSTANCE);
  
  @NotNull
  public final Collection<CompletionItem> findCommands(@NotNull String commandPrefix) {
    Intrinsics.checkParameterIsNotNull(commandPrefix, "commandPrefix");
    NavigableMap<String, CompletionItem> subMap = getCommands().subMap(commandPrefix, true, findUpperLimit(commandPrefix), true);
    Intrinsics.checkExpressionValueIsNotNull(subMap.values(), "subMap.values");
    return subMap.values();
  }
  
  @NotNull
  public final Collection<CompletionItem> allCommands() {
    Intrinsics.checkExpressionValueIsNotNull(getCommands().values(), "commands.values");
    return getCommands().values();
  }
  
  @NotNull
  protected final String findUpperLimit(@NotNull String prefix) {
    Intrinsics.checkParameterIsNotNull(prefix, "prefix");
    CharSequence charSequence = prefix;
    if (prefix.length() == 1) {
      char c = prefix.charAt(0);
      Intrinsics.checkExpressionValueIsNotNull((c < 'z') ? Character.toString((char)(c + 1)) : "z", "if (c < 'z') Character.t…) + 1).toChar()) else \"z\"");
    } else {
      char lastChar = prefix.charAt(prefix.length() - 1);
      String str1 = prefix;
      boolean bool = false;
      int i = prefix.length() - 1;
      StringBuilder stringBuilder = new StringBuilder();
      if (str1 == null)
        throw new TypeCastException("null cannot be cast to non-null type java.lang.String"); 
      Intrinsics.checkExpressionValueIsNotNull(str1.substring(bool, i), "(this as java.lang.Strin…ing(startIndex, endIndex)");
      String str2 = str1.substring(bool, i);
      str1 = prefix;
      bool = false;
      i = prefix.length() - 1;
      BashPathCompletionService bashPathCompletionService = this;
      if (str1 == null)
        throw new TypeCastException("null cannot be cast to non-null type java.lang.String"); 
      Intrinsics.checkExpressionValueIsNotNull(str1.substring(bool, i), "(this as java.lang.Strin…ing(startIndex, endIndex)");
      str2 = str1.substring(bool, i);
    } 
    return ((charSequence.length() == 0)) ? "z" : ((lastChar < 'z') ? stringBuilder.append(str2).append(Character.toString((char)(lastChar + 1))).toString() : bashPathCompletionService.findUpperLimit(str2));
  }
  
  private final NavigableMap<String, CompletionItem> getCommands() {
    Lazy lazy = this.commands$delegate;
    BashPathCompletionService bashPathCompletionService = this;
    KProperty kProperty = $$delegatedProperties[0];
    return (NavigableMap<String, CompletionItem>)lazy.getValue();
  }
  
  @JvmStatic
  @NotNull
  public static final BashPathCompletionService getInstance() {
    return Companion.getInstance();
  }
}
