package FormRequestReference;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.impl.source.xml.XmlTokenImpl;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collection;

public class FormRequestMethodReference extends PsiReferenceBase<XmlTokenImpl> {

    private final @NotNull String className;
    private final @NotNull String methodName;

    public FormRequestMethodReference(
        @NotNull XmlTokenImpl element,
        @NotNull String className,
        @NotNull String methodName
    ) {
        super(element, false);
        this.className = className;
        this.methodName = methodName;
    }

    @Override
    public @Nullable PsiElement resolve() {
        Project project = getElement().getProject();
        if (this.className.isEmpty()) return null;

        PhpIndex phpIndex = PhpIndex.getInstance(project);
        String classFQN = this.className;
        if (!classFQN.startsWith("\\")) {
            classFQN = "\\" + classFQN;
        }

        Collection<PhpClass> classes = phpIndex.getAnyByFQN(classFQN);
        for (var phpClass : classes) {
            Method method = phpClass.findMethodByName(this.methodName);
            if (method != null) {
                return (PsiElement) method;
            }
        }

        return null;
    }

    @Override
    public Object @NotNull [] getVariants() { return new Object[0]; }

}
