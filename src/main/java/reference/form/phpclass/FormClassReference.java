package reference.form.phpclass;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlTag;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.PhpClass;

public class FormClassReference extends PsiReferenceBase<XmlTag> {

    private final String literal;

    public FormClassReference(@NotNull XmlTag element, String literal) {
        super(element, false);
        this.literal = literal;
    }

    @Override
    public @Nullable PsiElement resolve() {
        if (this.literal.isEmpty()) return null;
        Project project = getElement().getProject();

        if (this.literal.endsWith(".php")) {
            return resolveFileName(project);
        }

        return resolveClassName(project);
    }

    @Override
    public Object @NotNull [] getVariants() {
        return new Object[0];
    }

    private @Nullable PsiElement resolveClassName(Project project) {
        Collection<? extends PhpClass> classes = PhpIndex.getInstance(project).getAnyByFQN("\\" + literal);
        if (classes.isEmpty()) return null;

        return (PsiElement) classes.toArray()[0];
    }

    private @Nullable PsiElement resolveFileName(Project project) {
        String path = this.literal;

        String[] parts = {};
        if (path.contains("/")) {
            parts = path.split("/");
        } else if (path.contains("\\")) {
            parts = path.split("\\\\");
        }

        List<String> list = Arrays.stream(parts).toList();
        if (list.isEmpty()) return null;

        String className = list.getLast();
        if (className.isEmpty()) return null;

        Collection<VirtualFile> files = FilenameIndex.getVirtualFilesByName(
            className,
            GlobalSearchScope.allScope(project)
        );

        for (VirtualFile file : files) {
            String filePath = file.getPath();

            if (filePath.contains(path)) {
                return PsiManager.getInstance(project).findFile(file);
            }
        }

        return null;
    }

}
