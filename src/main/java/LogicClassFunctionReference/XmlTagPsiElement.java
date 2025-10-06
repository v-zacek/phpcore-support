package LogicClassFunctionReference;

import javax.swing.*;
import org.jetbrains.annotations.NotNull;
import com.intellij.ide.util.PsiNavigationSupport;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.FakePsiElement;
import com.intellij.psi.xml.XmlTag;

public class XmlTagPsiElement extends FakePsiElement {

    private final XmlTag xmlTag;

    public XmlTagPsiElement(XmlTag xmlTag) {
        this.xmlTag = xmlTag;
    }

    @Override
    public PsiElement getParent() {
        return xmlTag.getParent();
    }

    @Override
    public String getName() {
        return xmlTag.getName() + " (" + getContainingFile().getName() + ":" + getLineNumber() + ")";
    }

    @Override
    public String getPresentableText() {
        return getName();
    }

    @Override
    public String getLocationString() {
        return xmlTag.getContainingFile().getName() + ":" + getLineNumber();
    }

    @Override
    public ItemPresentation getPresentation() {
        return new ItemPresentation() {
            @Override
            public String getPresentableText() {
                return xmlTag.getName();
            }

            @Override
            public String getLocationString() {
                return xmlTag.getContainingFile().getName() + ":" + getLineNumber();
            }

            @Override
            public Icon getIcon(boolean unused) {
                return xmlTag.getIcon(0);
            }
        };
    }

    private int getLineNumber() {
        Document doc = PsiDocumentManager.getInstance(xmlTag.getProject())
            .getDocument(xmlTag.getContainingFile());
        if (doc != null) {
            return doc.getLineNumber(xmlTag.getTextOffset()) + 1;
        }
        return -1;
    }

    @Override
    public PsiFile getContainingFile() {
        return xmlTag.getContainingFile();
    }

    @Override
    public @NotNull PsiElement getNavigationElement() {
        return xmlTag;
    }

    @Override
    public int getTextOffset() {
        return xmlTag.getTextOffset();
    }

    @Override
    public boolean canNavigate() {
        return true;
    }

    @Override
    public void navigate(boolean requestFocus) {
        var descriptor = PsiNavigationSupport.getInstance()
            .createNavigatable(
                xmlTag.getProject(),
                xmlTag.getContainingFile().getVirtualFile(),
                xmlTag.getTextOffset()
            );
        descriptor.navigate(requestFocus);
    }

}
