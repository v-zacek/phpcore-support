package action.xmlForm;

import org.jetbrains.annotations.NotNull;
import com.intellij.codeInsight.template.Template;
import com.intellij.codeInsight.template.TemplateManager;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;

public class InsertXmlItemAction extends BaseXmlAction implements DumbAware {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);

        if (project == null) return;
        if (editor == null) return;

        WriteCommandAction.runWriteCommandAction(project, () -> {
            TemplateManager templateManager = TemplateManager.getInstance(project);
            Template template = templateManager.createTemplate("", "");
            template.setToReformat(true);

            template.addTextSegment("<item html=\"\" type=\"\">\n");
            template.addTextSegment("    <fieldname></fieldname>\n");
            template.addTextSegment("    <description></description>\n");
            template.addTextSegment("    <title></title>\n");
            template.addTextSegment("    <datatype></datatype>\n");
            template.addTextSegment("</item>");

            templateManager.startTemplate(editor, template);
        });
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

}
