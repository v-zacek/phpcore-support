package reference.dbmapper;

import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReferenceBase;
import com.intellij.psi.ResolveResult;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DbMapperPropertyReference extends PsiPolyVariantReferenceBase<StringLiteralExpression> {

    private final Collection<PhpClass> classes;

    public DbMapperPropertyReference(
        @NotNull StringLiteralExpression element,
        @NotNull Collection<PhpClass> classes
    ) {
        super(element, true);
        this.classes = classes;
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        String propertyName = getElement().getContents();
        List<ResolveResult> results = new ArrayList<>();

        for (PhpClass phpClass : classes) {
            for (Field field : phpClass.getFields()) {
                if (field.isConstant()) continue;
                if (!field.getModifier().isPublic()) continue;
                if (propertyName.equals(field.getName())) {
                    results.add(new PsiElementResolveResult(field));
                }
            }
        }

        return results.toArray(new ResolveResult[0]);
    }

    @Override
    public Object @NotNull [] getVariants() {
        return new Object[0];
    }
}
