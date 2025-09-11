package ServerFunctionReference;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ServerFunctionReference extends PsiReferenceBase<XmlTag> {

    private @NotNull String logicMethodName;

    public ServerFunctionReference(
            @NotNull XmlTag element,
            @NotNull String logicMethodName
    ) {
        super(element, true);

        this.logicMethodName = logicMethodName;
    }

    @Override
    public @Nullable PsiElement resolve() {
        Project project = getElement().getProject();

        // find logic class
        XmlFile xmlFile = (XmlFile) getElement().getContainingFile();

        XmlTag root = xmlFile.getRootTag();
        if (root == null) return null;

        XmlTag logicClassTag = root.findFirstSubTag("logicclass");
        if (logicClassTag == null) return null;

        String className = logicClassTag.getValue().getTrimmedText();
        if (className.isEmpty()) return null;

        final String target = getElement().getValue().getTrimmedText();

        // find logicclass
        Collection<? extends PhpClass> classes = PhpIndex.getInstance(project).getAnyByFQN("\\" + className);
        if (classes.isEmpty()) return null;

        Deque<PhpClass> queue = new ArrayDeque<>(classes);
        Set<String> visited = new HashSet<>();

        while (!queue.isEmpty()) {
            PhpClass phpClass = queue.removeFirst();
            if (!visited.add(phpClass.getFQN())) continue;

            // hledej metodu v této třídě
            Method method = phpClass.findMethodByName(this.logicMethodName);
            if (method != null) {
                PsiElement[] found = PsiTreeUtil.collectElements(method, el ->
                        el instanceof StringLiteralExpression &&
                                target.equals(((StringLiteralExpression) el).getContents())
                );
                if (found.length > 0) {
                    return found[0];  // ✅ našli jsme literál, konec
                }
            }

            // přidej všechny rodiče (extends + implements)
            queue.addAll(Arrays.stream(phpClass.getSupers()).toList());
        }

        return null;
    }

    @Override
    public Object @NotNull [] getVariants() {
        return super.getVariants();
    }
}
