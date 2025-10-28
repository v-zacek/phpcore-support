package reference.form.function;

import org.jetbrains.annotations.NotNull;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.ProcessingContext;

public class FormLogicFunctionReferenceProvider extends PsiReferenceProvider {

    final private @NotNull String logicMethodName;

    public FormLogicFunctionReferenceProvider(
        @NotNull String logicMethodName
    ) {
        this.logicMethodName = logicMethodName;
    }

    @Override
    public PsiReference @NotNull [] getReferencesByElement(
        @NotNull PsiElement element,
        @NotNull ProcessingContext context
    ) {
        if (!(element instanceof XmlTag tag)) return PsiReference.EMPTY_ARRAY;

        String functionName = tag.getValue().getTrimmedText();
        if (functionName.isEmpty()) return PsiReference.EMPTY_ARRAY;

        return new PsiReference[]{
            new FormLogicFunctionReference(tag, this.logicMethodName)
        };
    }

}
