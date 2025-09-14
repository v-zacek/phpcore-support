package LogicClassFunctionReference;

import org.jetbrains.annotations.NotNull;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceRegistrar;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;

public class LogicClassFunctionReferenceContributor extends PsiReferenceContributor {

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(StringLiteralExpression.class),
            new LogicClassFunctionReferenceProvider("editValue", "editValueFunction")
        );

        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(StringLiteralExpression.class),
            new LogicClassFunctionReferenceProvider("validateData", "serverValidateFunction")
        );

        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(StringLiteralExpression.class),
            new LogicClassFunctionReferenceProvider("prepareValue", "prepareValueFunction")
        );
    }

}
