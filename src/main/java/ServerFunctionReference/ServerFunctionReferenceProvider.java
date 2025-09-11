package ServerFunctionReference;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

public class ServerFunctionReferenceProvider extends PsiReferenceProvider {

    private @NotNull String logicMethodName;

    public ServerFunctionReferenceProvider(
            @NotNull String logicMethodName
    )
    {
        this.logicMethodName = logicMethodName;
    }

    @Override
    public PsiReference @NotNull [] getReferencesByElement(
            @NotNull PsiElement element,
            @NotNull ProcessingContext context) {
        if (!(element instanceof XmlTag)) return PsiReference.EMPTY_ARRAY;
        XmlTag tag = (XmlTag) element;
        String functionName = tag.getValue().getTrimmedText();
        if (functionName.isEmpty()) return PsiReference.EMPTY_ARRAY;

        return new PsiReference[]{
                new ServerFunctionReference(tag, this.logicMethodName)
        };
    }
}
