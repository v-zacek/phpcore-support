package LogicClassReference;

import org.jetbrains.annotations.NotNull;
import com.intellij.patterns.XmlPatterns;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceRegistrar;

public class FormClassReferenceContributor extends PsiReferenceContributor {

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(
            XmlPatterns.xmlTag().withName("logicclass"),
            new FormClassReferenceProvider()
        );

        registrar.registerReferenceProvider(
            XmlPatterns.xmlTag().withName("action"),
            new FormClassReferenceProvider()
        );
    }

}
