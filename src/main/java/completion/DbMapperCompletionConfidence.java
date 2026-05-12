package completion;

import com.intellij.codeInsight.completion.CompletionConfidence;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ThreeState;
import com.jetbrains.php.lang.psi.elements.ArrayCreationExpression;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class DbMapperCompletionConfidence extends CompletionConfidence {

    private static final Set<String> TARGET_METHODS = Set.of("save", "setValues", "fetchArray");

    @Override
    public @NotNull ThreeState shouldSkipAutopopup(
            @NotNull PsiElement contextElement,
            @NotNull PsiFile psiFile,
            int offset
    ) {
        StringLiteralExpression stringLiteral = PsiTreeUtil.getParentOfType(
            contextElement, StringLiteralExpression.class, false
        );

        PsiElement searchFrom = stringLiteral != null ? (PsiElement) stringLiteral : contextElement;

        ArrayCreationExpression array = PsiTreeUtil.getParentOfType(
            searchFrom, ArrayCreationExpression.class, true, MethodReference.class
        );
        if (array == null) return ThreeState.UNSURE;

        MethodReference methodRef = PsiTreeUtil.getParentOfType(array, MethodReference.class, true);
        if (methodRef == null) return ThreeState.UNSURE;

        String methodName = methodRef.getName();
        if (methodName == null || !TARGET_METHODS.contains(methodName)) return ThreeState.UNSURE;

        return ThreeState.NO;
    }
}
