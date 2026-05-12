package completion;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.patterns.PlatformPatterns;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;

public class DbMapperCompletionContributor extends CompletionContributor {
    public DbMapperCompletionContributor() {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement().withParent(StringLiteralExpression.class),
            new DbMapperArrayKeyCompletionProvider()
        );
    }
}
