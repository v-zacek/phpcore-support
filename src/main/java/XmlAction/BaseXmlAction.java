package XmlAction;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlFile;

abstract class BaseXmlAction extends AnAction {

    private boolean isXmlFile(PsiFile psiFile) {
        VirtualFile virtualFile = psiFile.getVirtualFile();

        return virtualFile != null && "xml".equalsIgnoreCase(virtualFile.getExtension());
    }

    public void update(AnActionEvent e) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);

        boolean showAction = editor != null && psiFile instanceof XmlFile && isXmlFile(psiFile);
        e.getPresentation().setEnabledAndVisible(showAction);
    }

}
