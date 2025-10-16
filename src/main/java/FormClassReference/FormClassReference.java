package FormClassReference;

import java.util.Collection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.xml.XmlTag;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.PhpClass;

public class FormClassReference extends PsiReferenceBase<XmlTag> {

    private final String className;

    public FormClassReference(@NotNull XmlTag element, String className) {
        super(element, false);
        this.className = className;
    }

    @Override
    public @Nullable PsiElement resolve() {
        Project project = getElement().getProject();
        if (this.className.isEmpty()) return null;

        Collection<? extends PhpClass> classes = PhpIndex.getInstance(project).getAnyByFQN("\\" + className);
        if (classes.isEmpty()) return null;

        return (PsiElement) classes.toArray()[0];
    }

    @Override
    public Object @NotNull [] getVariants() {
        return new Object[0];
    }

}
