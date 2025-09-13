package LogicClassReference;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlText;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class LogicClassReference extends PsiReferenceBase<XmlTag> {
    private final String className;

    public LogicClassReference(@NotNull XmlTag element, String className) {
        super(element, true);
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
