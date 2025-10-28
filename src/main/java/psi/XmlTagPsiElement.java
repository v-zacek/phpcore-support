package psi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.ide.util.PsiNavigationSupport;
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
        return xmlTag.getName() + " - " + getFieldName();
    }

    @Override
    public String getPresentableText() {
        return getName();
    }

    @Override
    public String getLocationString() {
        return xmlTag.getContainingFile().getName() + ":" + getLineNumber();
    }

    private int getLineNumber() {
        Document doc = PsiDocumentManager.getInstance(xmlTag.getProject())
            .getDocument(xmlTag.getContainingFile());
        if (doc != null) {
            return doc.getLineNumber(xmlTag.getTextOffset()) + 1;
        }
        return -1;
    }

    private @Nullable String getFieldName() {
        PsiElement[] children = xmlTag.getParent().getChildren();
        for (PsiElement child : children) {
            if (!(child instanceof XmlTag xmlChild)) {
                continue;
            }

            if (xmlChild.getName().equals("fieldname")) {
                return xmlChild.getValue().getTrimmedText();
            }
        }

        return null;
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
