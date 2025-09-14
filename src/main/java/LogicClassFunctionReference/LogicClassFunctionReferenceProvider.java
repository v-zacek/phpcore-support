package LogicClassFunctionReference;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;

public class LogicClassFunctionReferenceProvider extends PsiReferenceProvider {

    private final @NotNull String logicMethodName;
    private final @NotNull String xmlFunctionTag;

    public LogicClassFunctionReferenceProvider(
        @NotNull String logicMethodName,
        @NotNull String xmlFunctionTag
    ) {
        this.logicMethodName = logicMethodName;
        this.xmlFunctionTag = xmlFunctionTag;
    }

    @Override
    public PsiReference @NotNull [] getReferencesByElement(
        @NotNull PsiElement psiElement,
        @NotNull ProcessingContext processingContext
    ) {

        StringLiteralExpression stringLiteralExpression = (StringLiteralExpression) psiElement;
        String content = stringLiteralExpression.getContents();
        if (content.isEmpty()) return PsiReference.EMPTY_ARRAY;

        Method method = PsiTreeUtil.getParentOfType(stringLiteralExpression, Method.class);

        if (method == null) return PsiReference.EMPTY_ARRAY;
        if (!method.getName().equals(this.logicMethodName)) return PsiReference.EMPTY_ARRAY;

        PhpClass containingClass = method.getContainingClass();
        if (containingClass == null) return PsiReference.EMPTY_ARRAY;

        if (!extendsLogicClass(containingClass)) return PsiReference.EMPTY_ARRAY;

        return new PsiReference[]{
            new LogicClassFunctionReference(stringLiteralExpression, containingClass, this.xmlFunctionTag)
        };
    }

    private boolean extendsLogicClass(PhpClass phpClass) {
        Set<String> visited = new HashSet<>();
        Deque<PhpClass> queue = new ArrayDeque<>();
        queue.add(phpClass);

        while (!queue.isEmpty()) {
            PhpClass current = queue.removeFirst();
            if (!visited.add(current.getFQN())) {
                continue;
            }

            for (PhpClass superClass : current.getSupers()) {
                if ("\\logic".equals(superClass.getFQN())) {
                    return true;
                }
                queue.add(superClass);
            }
        }

        return false;
    }

}
