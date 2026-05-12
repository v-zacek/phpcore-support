package completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.ArrayCreationExpression;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpExpression;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DbMapperArrayKeyCompletionProvider extends CompletionProvider<CompletionParameters> {

    private static final Set<String> TARGET_METHODS = Set.of("save", "setValues", "fetchArray");
    private static final String DB_MAPPER_TRAIT_NAME = "DbMapperTrait";

    @Override
    protected void addCompletions(
            @NotNull CompletionParameters parameters,
            @NotNull ProcessingContext context,
            @NotNull CompletionResultSet result
    ) {
        PsiElement position = parameters.getPosition();

        PsiElement stringLiteralEl = position.getParent();
        if (!(stringLiteralEl instanceof StringLiteralExpression)) return;

        ArrayCreationExpression arrayCreation = PsiTreeUtil.getParentOfType(
            stringLiteralEl, ArrayCreationExpression.class, true, MethodReference.class
        );
        if (arrayCreation == null) return;

        if (PsiTreeUtil.getParentOfType(arrayCreation, ArrayCreationExpression.class, true, MethodReference.class) != null) {
            return;
        }

        MethodReference methodRef = PsiTreeUtil.getParentOfType(arrayCreation, MethodReference.class, true);
        if (methodRef == null) return;

        String methodName = methodRef.getName();
        if (methodName == null || !TARGET_METHODS.contains(methodName)) return;

        PhpExpression classRef = methodRef.getClassReference();
        if (classRef == null) return;

        PhpIndex phpIndex = PhpIndex.getInstance(position.getProject());
        Set<String> visited = new HashSet<>();

        for (String typeSig : classRef.getType().getTypes()) {
            for (PhpNamedElement element : phpIndex.getBySignature(typeSig, visited, 0)) {
                if (element instanceof PhpClass phpClass) {
                    if (usesDbMapperTrait(phpClass)) {
                        addPropertyCompletions(phpClass, result);
                    }
                } else if (element instanceof Method method) {
                    for (String retSig : method.getType().getTypes()) {
                        Collection<PhpClass> retClasses = retSig.startsWith("\\")
                            ? phpIndex.getAnyByFQN(retSig)
                            : resolveSignatureToClasses(retSig, phpIndex, visited);
                        for (PhpClass retClass : retClasses) {
                            if (usesDbMapperTrait(retClass)) {
                                addPropertyCompletions(retClass, result);
                            }
                        }
                    }
                }
            }
        }
    }

    private Collection<PhpClass> resolveSignatureToClasses(String sig, PhpIndex phpIndex, Set<String> visited) {
        Set<PhpClass> classes = new HashSet<>();
        for (PhpNamedElement el : phpIndex.getBySignature(sig, visited, 0)) {
            if (el instanceof PhpClass phpClass) {
                classes.add(phpClass);
            }
        }
        return classes;
    }

    private boolean usesDbMapperTrait(PhpClass phpClass) {
        for (PhpClass trait : phpClass.getTraits()) {
            if (DB_MAPPER_TRAIT_NAME.equals(trait.getName())) {
                return true;
            }
        }
        return false;
    }

    private void addPropertyCompletions(PhpClass phpClass, CompletionResultSet result) {
        for (Field field : phpClass.getFields()) {
            if (field.isConstant()) continue;
            if (!field.getModifier().isPublic()) continue;
            result.addElement(LookupElementBuilder.create(field.getName()));
        }
    }
}
