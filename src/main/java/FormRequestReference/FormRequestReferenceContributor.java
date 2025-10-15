package FormRequestReference;

import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.PsiReferenceRegistrar;
import com.intellij.psi.impl.source.xml.XmlTokenImpl;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlText;
import com.intellij.psi.xml.XmlToken;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

public class FormRequestReferenceContributor extends PsiReferenceContributor {

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(XmlToken.class)
                .withParent(XmlText.class)
                .inside(PlatformPatterns.psiElement(XmlTag.class)
                            .withName("request")
                            .withParent(PlatformPatterns.psiElement(XmlTag.class).withName("ajaxForm"))
                ),
                new PsiReferenceProvider() {
                    @Override
                    public PsiReference @NotNull [] getReferencesByElement(
                        @NotNull PsiElement psiElement,
                        @NotNull ProcessingContext processingContext
                    ) {
                        if (!(psiElement instanceof XmlTokenImpl token)) return PsiReference.EMPTY_ARRAY;

                        String content = token.getText();
                        if (content.isEmpty()) return PsiReference.EMPTY_ARRAY;

                        if (!content.contains("::")) return PsiReference.EMPTY_ARRAY;

                        String[] parts = content.split("::");
                        if (parts.length != 2) return PsiReference.EMPTY_ARRAY;

                        String className = parts[0];
                        String method = parts[1];

                        return new PsiReference[] {
                            new FormRequestMethodReference(token, className, method)
                        };
                    }
                }
            );

    }

}
