package reference.dbmapper;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.ArrayCreationExpression;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpExpression;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DbMapperPropertyReferenceProvider extends PsiReferenceProvider {

    private static final Set<String> TARGET_METHODS = Set.of("save", "setValues", "fetchArray");
    private static final String DB_MAPPER_TRAIT_NAME = "DbMapperTrait";

    @Override
    public PsiReference @NotNull [] getReferencesByElement(
        @NotNull PsiElement element,
        @NotNull ProcessingContext context
    ) {
        StringLiteralExpression stringLiteral = (StringLiteralExpression) element;

        ArrayCreationExpression arrayCreation = PsiTreeUtil.getParentOfType(
            stringLiteral, ArrayCreationExpression.class, true, MethodReference.class
        );
        if (arrayCreation == null) return PsiReference.EMPTY_ARRAY;

        if (PsiTreeUtil.getParentOfType(arrayCreation, ArrayCreationExpression.class, true, MethodReference.class) != null) {
            return PsiReference.EMPTY_ARRAY;
        }

        MethodReference methodRef = PsiTreeUtil.getParentOfType(arrayCreation, MethodReference.class, true);
        if (methodRef == null) return PsiReference.EMPTY_ARRAY;

        String methodName = methodRef.getName();
        if (methodName == null || !TARGET_METHODS.contains(methodName)) return PsiReference.EMPTY_ARRAY;

        PhpExpression classRef = methodRef.getClassReference();
        if (classRef == null) return PsiReference.EMPTY_ARRAY;

        PhpIndex phpIndex = PhpIndex.getInstance(element.getProject());
        Set<String> visited = new HashSet<>();
        List<PhpClass> matchingClasses = new ArrayList<>();

        for (String typeSig : classRef.getType().getTypes()) {
            for (PhpNamedElement phpElement : phpIndex.getBySignature(typeSig, visited, 0)) {
                if (phpElement instanceof PhpClass phpClass) {
                    if (usesDbMapperTrait(phpClass)) {
                        matchingClasses.add(phpClass);
                    }
                } else if (phpElement instanceof Method method) {
                    for (String retSig : method.getType().getTypes()) {
                        Collection<PhpClass> retClasses = retSig.startsWith("\\")
                            ? phpIndex.getAnyByFQN(retSig)
                            : resolveSignatureToClasses(retSig, phpIndex, visited);
                        for (PhpClass retClass : retClasses) {
                            if (usesDbMapperTrait(retClass)) {
                                matchingClasses.add(retClass);
                            }
                        }
                    }
                }
            }
        }

        if (matchingClasses.isEmpty()) return PsiReference.EMPTY_ARRAY;

        return new PsiReference[]{new DbMapperPropertyReference(stringLiteral, matchingClasses)};
    }

    private Collection<PhpClass> resolveSignatureToClasses(String sig, PhpIndex phpIndex, Set<String> visited) {
        List<PhpClass> classes = new ArrayList<>();
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
}
