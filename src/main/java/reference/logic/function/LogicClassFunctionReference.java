package reference.logic.function;

import psi.XmlTagPsiElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiPolyVariantReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;

public class LogicClassFunctionReference extends PsiPolyVariantReferenceBase<StringLiteralExpression> {

    private final @NotNull PhpClass containingClass;
    private final @NotNull String xmlFunctionTag;

    public LogicClassFunctionReference(
        @NotNull StringLiteralExpression element,
        @NotNull PhpClass containingClass,
        @NotNull String xmlFunctionTag
    ) {
        super(element, false);

        this.containingClass = containingClass;
        this.xmlFunctionTag = xmlFunctionTag;
    }

    @Override
    public Object @NotNull [] getVariants() {
        return super.getVariants();
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean b) {
        Project project = getElement().getProject();
        Collection<VirtualFile> files = FilenameIndex.getAllFilesByExt(
            project,
            "xml",
            GlobalSearchScope.projectScope(project)
        );

        String literalValue = getElement().getContents();

        List<ResolveResult> results = new ArrayList<>();

        for (VirtualFile file : files) {
            PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
            if (!(psiFile instanceof XmlFile xmlFile)) continue;

            XmlTag root = xmlFile.getRootTag();
            if (root == null) continue;

            var logicClassTag = root.findFirstSubTag("logicclass");
            if (logicClassTag == null) continue;

            Collection<PhpClass> logicClasses = PhpIndex.getInstance(project)
                .getAnyByFQN("\\" + logicClassTag.getValue().getTrimmedText());

            if (!isAnyDescender(containingClass, logicClasses)) continue;

            for (XmlTag tag : PsiTreeUtil.findChildrenOfType(xmlFile, XmlTag.class)) {
                if (this.xmlFunctionTag.equals(tag.getName()) &&
                    literalValue.equals(tag.getValue().getTrimmedText())) {
                    results.add(new PsiElementResolveResult(new XmlTagPsiElement(tag)));
                }
            }
        }

        return results.toArray(new ResolveResult[0]);
    }

    private boolean isAnyDescender(PhpClass containingClass, Collection<PhpClass> logicClasses) {
        for (PhpClass logicClass : logicClasses) {
            if (logicClass.getFQN().equals(containingClass.getFQN())) {
                return true;
            }

            Collection<PhpClass> superClasses = logicClass.getSuperClasses();

            for (PhpClass superClass : superClasses) {
                if (superClass.getFQN().equals(containingClass.getFQN())) {
                    return true;
                }
            }
        }

        return false;
    }
}
