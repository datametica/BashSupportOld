package com.ansorgit.plugins.bash.lang.psi.impl;

import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashCharSequence;
import com.ansorgit.plugins.bash.lang.psi.api.BashFileReference;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiFileUtils;
import com.ansorgit.plugins.bash.lang.psi.util.BashPsiUtils;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulator;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.impl.source.resolve.reference.impl.CachingReference;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.PsiFileReference;
import com.intellij.refactoring.rename.BindablePsiReference;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;



public class BashFileReferenceImpl
  extends BashBaseElement
  implements BashFileReference
{
  private final PsiReference fileReference;
  
  public BashFileReferenceImpl(ASTNode astNode) {
    super(astNode, "Bash File reference");

    
    this.fileReference = new CachingFileReference(this);
  }
  
  @Nullable
  public PsiFile findReferencedFile() {
    PsiReference reference = getReference();
    if (reference == null) {
      return null;
    }
    
    return (PsiFile)reference.resolve();
  }
  
  @NotNull
  public String getFilename() {
    PsiElement firstParam = getFirstChild();
    if (firstParam instanceof BashCharSequence) {
      if (((BashCharSequence)firstParam).getUnwrappedCharSequence() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/BashFileReferenceImpl", "getFilename" }));  return ((BashCharSequence)firstParam).getUnwrappedCharSequence();
    } 
    
    if (getText() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/BashFileReferenceImpl", "getFilename" }));  return getText();
  }
  
  public boolean isStatic() {
    PsiElement firstChild = getFirstChild();
    return (firstChild instanceof BashCharSequence && ((BashCharSequence)firstChild).isStatic());
  }

  
  public PsiReference getReference() {
    return this.fileReference;
  }

  
  public boolean canNavigate() {
    return (this.fileReference.resolve() != null);
  }

  
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "visitor", "com/ansorgit/plugins/bash/lang/psi/impl/BashFileReferenceImpl", "accept" }));  if (visitor instanceof BashVisitor) {
      ((BashVisitor)visitor).visitFileReference(this);
    } else {
      visitor.visitElement((PsiElement)this);
    } 
  }
  
  private static class CachingFileReference extends CachingReference implements PsiReference, BindablePsiReference, PsiFileReference {
    private final BashFileReferenceImpl element;
    
    public CachingFileReference(BashFileReferenceImpl myElement) {
      this.element = myElement;
    }

    
    public BashFileReferenceImpl getElement() {
      return this.element;
    }
    
    public TextRange getRangeInElement() {
      return getManipulator().getRangeInElement((PsiElement)this.element);
    }
    
    @NotNull
    private ElementManipulator<BashFileReferenceImpl> getManipulator() {
      ElementManipulator<BashFileReferenceImpl> manipulator = ElementManipulators.getManipulator((PsiElement)this.element);
      if (manipulator == null) {
        throw new IncorrectOperationException("no implementation found to rename " + this.element);
      }
      if (manipulator == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/BashFileReferenceImpl$CachingFileReference", "getManipulator" }));  return manipulator;
    }
    
    @NotNull
    public String getCanonicalText() {
      if (this.element.getText() == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/BashFileReferenceImpl$CachingFileReference", "getCanonicalText" }));  return this.element.getText();
    }
    
    public PsiElement handleElementRename(String newName) throws IncorrectOperationException {
      ElementManipulator<BashFileReferenceImpl> manipulator = getManipulator();
      
      return manipulator.handleContentChange((PsiElement)this.element, newName);
    }
    
    public PsiElement bindToElement(@NotNull PsiElement targetElement) throws IncorrectOperationException {
      if (targetElement == null) throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[] { "targetElement", "com/ansorgit/plugins/bash/lang/psi/impl/BashFileReferenceImpl$CachingFileReference", "bindToElement" }));  if (targetElement instanceof PsiFile) {
        
        PsiFile currentFile = BashPsiUtils.findFileContext((PsiElement)this.element);
        String relativeFilePath = BashPsiFileUtils.findRelativeFilePath(currentFile, (PsiFile)targetElement);
        
        return handleElementRename(relativeFilePath);
      } 
      
      throw new IncorrectOperationException("Unsupported for element type " + targetElement);
    }
    
    public boolean isReferenceTo(PsiElement element) {
      return PsiManager.getInstance(element.getProject()).areElementsEquivalent(element, resolve());
    }
    
    @NotNull
    public Object[] getVariants() {
      if (PsiElement.EMPTY_ARRAY == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/BashFileReferenceImpl$CachingFileReference", "getVariants" }));  return (Object[])PsiElement.EMPTY_ARRAY;
    }

    
    @NotNull
    public ResolveResult[] multiResolve(boolean incompleteCode) {
      PsiElement resolve = resolve();
      if (resolve == null) {
        if (ResolveResult.EMPTY_ARRAY == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/BashFileReferenceImpl$CachingFileReference", "multiResolve" }));  return ResolveResult.EMPTY_ARRAY;
      } 
      
      (new ResolveResult[1])[0] = (ResolveResult)new PsiElementResolveResult(resolve, true); if (new ResolveResult[1] == null) throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[] { "com/ansorgit/plugins/bash/lang/psi/impl/BashFileReferenceImpl$CachingFileReference", "multiResolve" }));  return new ResolveResult[1];
    }
    
    @Nullable
    public PsiElement resolveInner() {
      PsiFile containingFile = BashPsiUtils.findFileContext((PsiElement)getElement());
      if (!containingFile.isPhysical()) {
        return null;
      }
      
      return (PsiElement)BashPsiFileUtils.findRelativeFile(containingFile, this.element.getFilename());
    }
  }
}
