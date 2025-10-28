package reference.form.function;

import org.jetbrains.annotations.NotNull;
import com.intellij.patterns.XmlPatterns;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceRegistrar;

/**
 * Form to logic class php literal
 */
public class FormLogicFunctionReferenceContributor extends PsiReferenceContributor {

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(
            XmlPatterns.xmlTag().withName("prepareValueFunction"),
            new FormLogicFunctionReferenceProvider("prepareValue")
        );

        registrar.registerReferenceProvider(
            XmlPatterns.xmlTag().withName("serverValidateFunction"),
            new FormLogicFunctionReferenceProvider("validateData")
        );

        registrar.registerReferenceProvider(
            XmlPatterns.xmlTag().withName("editValueFunction"),
            new FormLogicFunctionReferenceProvider("editValue")
        );
    }

}
