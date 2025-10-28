package action.xmlForm;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.codeInsight.template.Template;
import com.intellij.codeInsight.template.TemplateManager;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;

public class InsertXmlFormAction extends BaseXmlAction implements DumbAware {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);

        if (project == null) return;
        if (editor == null) return;

        ListPopup popup = JBPopupFactory.getInstance().createListPopup(
            new BaseListPopupStep<String>("Select form type", "Singleform", "Multiform") {
                @Override
                public @Nullable PopupStep<?> onChosen(String selectedValue, boolean finalChoice) {
                    if ("Singleform".equals(selectedValue)) {
                        insertSingleForm(project, editor);
                    } else if ("Multiform".equals(selectedValue)) {
                        insertMultiForm(project, editor);
                    }
                    return FINAL_CHOICE;
                }
            }
        );

        popup.showInBestPositionFor(editor);
    }

    private void insertSingleForm(Project project, Editor editor) {
        WriteCommandAction.runWriteCommandAction(project, () -> {
            TemplateManager templateManager = TemplateManager.getInstance(project);
            Template template = templateManager.createTemplate("", "");
            template.setToReformat(true);

            template.addTextSegment("<form xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
            template.addTextSegment("xsi:noNamespaceSchemaLocation=\"https://schema.olc.cz/singleForm.xsd\">");
            template.addTextSegment("<right></right>");
            template.addTextSegment("<header></header>");
            template.addTextSegment("<formname></formname>");
            template.addTextSegment("<action alias=\"1\"></action>");
            template.addTextSegment("<enctype>multipart/form-data</enctype>");
            template.addTextSegment("<method>post</method>");
            template.addTextSegment("<mode>singleform</mode>");
            template.addTextSegment("<logicclass></logicclass>");
            template.addTextSegment("\n\n");
            template.addTextSegment("</form>");


            templateManager.startTemplate(editor, template);
        });
    }

    private void insertMultiForm(Project project, Editor editor) {
        WriteCommandAction.runWriteCommandAction(project, () -> {
            TemplateManager templateManager = TemplateManager.getInstance(project);
            Template template = templateManager.createTemplate("", "");
            template.setToReformat(true);

            template.addTextSegment("<form xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
            template.addTextSegment("xsi:noNamespaceSchemaLocation=\"https://schema.olc.cz/singleForm.xsd\">");
            template.addTextSegment("<right></right>");
            template.addTextSegment("<header></header>");
            template.addTextSegment("<formname></formname>");
            template.addTextSegment("<action alias=\"1\"></action>");
            template.addTextSegment("<enctype>multipart/form-data</enctype>");
            template.addTextSegment("<method>post</method>");
            template.addTextSegment("<mode>multiform</mode>");
            template.addTextSegment("<logicclass></logicclass>");
            template.addTextSegment("\n\n");
            template.addTextSegment("</form>");

            templateManager.startTemplate(editor, template);
        });
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

}
