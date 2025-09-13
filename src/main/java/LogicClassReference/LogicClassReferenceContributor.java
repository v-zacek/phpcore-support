package LogicClassReference;

import org.jetbrains.annotations.NotNull;
import com.intellij.patterns.XmlPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.PsiReferenceRegistrar;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.ProcessingContext;

public class LogicClassReferenceContributor extends PsiReferenceContributor {

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(
            XmlPatterns.xmlTag().withName("logicclass"),
            new PsiReferenceProvider() {
                @Override
                public PsiReference @NotNull [] getReferencesByElement(
                    @NotNull PsiElement element,
                    @NotNull ProcessingContext context
                ) {
                    if (!(element instanceof XmlTag tag)) return PsiReference.EMPTY_ARRAY;
                    String className = tag.getValue().getTrimmedText();
                    if (className.isEmpty()) return PsiReference.EMPTY_ARRAY;

                    return new PsiReference[]{
                        new LogicClassReference(tag, className)
                    };
                }
            }
        );
    }

}
