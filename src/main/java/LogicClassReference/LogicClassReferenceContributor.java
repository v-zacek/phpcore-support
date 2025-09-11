package LogicClassReference;

import LogicClassReference.LogicClassReference;
import com.intellij.patterns.XmlPatterns;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlText;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

public class LogicClassReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(
                XmlPatterns.xmlTag().withName("logicclass"),
                new PsiReferenceProvider() {
                    @Override
                    public PsiReference @NotNull [] getReferencesByElement(
                            @NotNull PsiElement element,
                            @NotNull ProcessingContext context) {
                        if (!(element instanceof XmlTag)) return PsiReference.EMPTY_ARRAY;
                        XmlTag tag = (XmlTag) element;
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