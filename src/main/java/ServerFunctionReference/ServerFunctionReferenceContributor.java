package ServerFunctionReference;

import com.intellij.patterns.XmlPatterns;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

public class ServerFunctionReferenceContributor extends PsiReferenceContributor {

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(
                XmlPatterns.xmlTag().withName("prepareValueFunction"),
                new ServerFunctionReferenceProvider("prepareValue")
        );

        registrar.registerReferenceProvider(
                XmlPatterns.xmlTag().withName("serverValidateFunction"),
                new ServerFunctionReferenceProvider("validateData")
        );

        registrar.registerReferenceProvider(
                XmlPatterns.xmlTag().withName("editValueFunction"),
                new ServerFunctionReferenceProvider("editValue")
        );
    }

}
