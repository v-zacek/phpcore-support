package FormFunctionReference;

import org.jetbrains.annotations.NotNull;
import com.intellij.patterns.XmlPatterns;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceRegistrar;

public class FormFunctionReferenceContributor extends PsiReferenceContributor {

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(
            XmlPatterns.xmlTag().withName("prepareValueFunction"),
            new FormFunctionReferenceProvider("prepareValue")
        );

        registrar.registerReferenceProvider(
            XmlPatterns.xmlTag().withName("serverValidateFunction"),
            new FormFunctionReferenceProvider("validateData")
        );

        registrar.registerReferenceProvider(
            XmlPatterns.xmlTag().withName("editValueFunction"),
            new FormFunctionReferenceProvider("editValue")
        );
    }

}
