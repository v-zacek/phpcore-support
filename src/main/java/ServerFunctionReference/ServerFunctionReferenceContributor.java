package ServerFunctionReference;

import org.jetbrains.annotations.NotNull;
import com.intellij.patterns.XmlPatterns;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceRegistrar;

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
